package best.Aveiro.EBEC;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import best.Aveiro.EBEC.Objects.Team;
import best.Aveiro.EBEC.Objects.User;

public class MainActivityViewModel extends ViewModel {

    private HashMap<String, String> email_username = new HashMap<String, String>();
    private User current_user;
    private Team current_team;
    private MutableLiveData<Integer> fab_view = new MutableLiveData<Integer>();
    private MutableLiveData<String> mode = new MutableLiveData<String>();
    private boolean isAdmin = false;
    private String topic;

    public User getCurrentUser() {
        return current_user;
    }

    public void setCurrentUser(User current_user) {
        this.current_user = current_user;
    }

    public void setAdmin(){
        this.isAdmin = true;
    }
    public void setIsFABVisible(int newView){
        this.fab_view.setValue(newView);
    };
    public MutableLiveData<Integer> getFabView(){
        return this.fab_view;
    }
    public Team getCurrentTeam() {

        return current_team;
    }

    public void setCurrentTeam(Team current_team) {
        this.current_team = current_team;
    }

    public void setCurrentTopic(String _topic) {
        this.topic = _topic;
    }
    public String getTopic(){
            return this.topic;
    }

    public boolean isAdmin() {
        return isAdmin;
    }


     public String getUsernameByEmail(String username){
        return email_username.get(username);

    }
    void setEmailUsername(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference teamsRef = db.child("teams");
        teamsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot teamsSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot team_member : teamsSnapshot.child("members").getChildren()) {

                        System.out.println("TEAM Members " + team_member.toString());

                        DatabaseReference usersRef = db.child("users");
                        usersRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot userSnapshot) {
                                for (DataSnapshot usersSnapshot : userSnapshot.getChildren()) {


                                        String name_1 = usersSnapshot.child("first_name").getValue().toString();
                                        String name_2 = usersSnapshot.child("last_name").getValue().toString();
                                        String final_name = name_1 + " " + name_2;
                                        email_username.put(team_member.getValue().toString(), final_name);
                                        System.out.println(email_username.toString());
                                        break;

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setMode(String dark) {
        mode.setValue(dark);
    }

    public MutableLiveData<String> getMode(){
        return this.mode;
    }
}