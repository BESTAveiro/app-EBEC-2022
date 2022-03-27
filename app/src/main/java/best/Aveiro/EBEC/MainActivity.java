package best.Aveiro.EBEC;

import android.app.MediaRouteButton;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import best.Aveiro.EBEC.Objects.Prova;
import best.Aveiro.EBEC.Objects.Team;
import best.Aveiro.EBEC.Objects.User;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private AppBarConfiguration mAppBarConfiguration;
    private Configuration configuration;
    private MainActivityViewModel mainViewModel;
    public FloatingActionButton message_fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        configuration = new Configuration();

        preferences = getApplicationContext().getSharedPreferences("LOGIN_PREFS", 0);

        mainViewModel =
                new ViewModelProvider(this).get(MainActivityViewModel.class); //getActivity()
        mainViewModel.setEmailUsername();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        getUser(preferences.getString("user_email","_"));
        boolean isAdmin = preferences.getBoolean("admin", false);
        Menu menu = navigationView.getMenu();

        if (isAdmin){
            mainViewModel.setAdmin();
            mAppBarConfiguration =   new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_teams, R.id.nav_schedule, R.id.nav_admin, R.id.nav_best, R.id.nav_sponsors)
                    .setDrawerLayout(drawer)
                    .build();
            MenuItem nav_admin = menu.findItem(R.id.nav_admin);
            nav_admin.setVisible(true);
        }else{
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_teams, R.id.nav_schedule, R.id.nav_best, R.id.nav_sponsors, R.id.nav_admin)
                    .setDrawerLayout(drawer)
                    .build();
            MenuItem menuOpen = menu.findItem(R.id.nav_admin);
            menuOpen.setVisible(false);
        }






        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        message_fab = findViewById(R.id.message_fab);
        message_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_chat);
            }
        });
        if (preferences.getBoolean("admin", false)){
            message_fab.setVisibility(View.GONE);
        }

        mainViewModel.getFabView().observe(this, view->{
            message_fab.setVisibility(view);
        });

        setupNotifications();
    }
    private void getUser(String email){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersRef = db.child("users");

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnaphot : dataSnapshot.getChildren()) {
                        User user = new User();
                        user.setEmail(userSnaphot.child("email").getValue().toString());
                        user.setPassword(userSnaphot.child("password").getValue().toString());
                        user.setFirstName(userSnaphot.child("first_name").getValue().toString());
                        user.setLastName(userSnaphot.child("last_name").getValue().toString());
                        user.setId(userSnaphot.child("id").getValue(Long.class));
                        user.setTeamName(userSnaphot.child("team").getValue().toString());
                        if (user.getEmail().equals(email)) {
                            mainViewModel.setCurrentUser(user);
                            getTeam(email);
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Failed to access database, check your internet connection", Toast.LENGTH_LONG).show();
                }
            };

            usersRef.addValueEventListener(eventListener);
    }
    private void getTeam(String useremail){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = db.child("teams");
        Team temp_team = new Team();
        usersRef.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("TEAM Members "+dataSnapshot.toString());
                for (DataSnapshot teamsSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> members = new ArrayList<>();

                    for (DataSnapshot team_member : teamsSnapshot.child("members").getChildren()){

                        System.out.println("TEAM Members "+team_member.toString());
                        members.add(team_member.getValue().toString());
                    }
                    System.out.println("TEAM Members "+members.toString());
                    if (members.contains(useremail)){
                        temp_team.setName(teamsSnapshot.child("name").getValue().toString());
                        temp_team.setCredits(teamsSnapshot.child("credits").getValue(Integer.class));
                        temp_team.setModality(teamsSnapshot.child("md").getValue().toString());

                        temp_team.setMembers(members);

                        ArrayList<Prova> competitions = new ArrayList<Prova>();
                        for (DataSnapshot competition : teamsSnapshot.child("comps").getChildren()){
                            Prova temp_prova = new Prova();
                            temp_prova.setName(competition.getKey());
                            temp_prova.setScore(competition.getValue().toString());
                            competitions.add(temp_prova);
                        }
                        temp_team.setProvas(competitions);
                        mainViewModel.setCurrentTeam(temp_team);

                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void setupNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);

            }
        }
        // [END handle_data_extras]


        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("ebec")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }

                    }
                });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "Token " + token;

                    }
                });
        // [END log_reg_token]


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String darkModeString = getString(R.string.dark_mode);
        switch (item.getItemId()) {
            case R.id.action_signOut:
                preferences.edit().remove("logged_in").apply();
                Intent log_in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(log_in);
                break;
            case R.id.action_darkmode:
                final String[] darkModeValues = getResources().getStringArray(R.array.dark_mode_values);
                // The apps theme is decided depending upon the saved preferences on app startup
                String pref = preferences
                        .getString(getString(R.string.dark_mode), getString(R.string.system_theme_preference_value));
                // Comparing to see which preference is selected and applying those theme settings

                if (pref.equals(darkModeValues[2])) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    preferences.edit().putString(getString(R.string.dark_mode), "MODE_NIGHT_NO").commit();
                    mainViewModel.setMode("LIGHT");
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferences.edit().putString(getString(R.string.dark_mode), "MODE_NIGHT_YES").commit();
                    mainViewModel.setMode("DARK");
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}