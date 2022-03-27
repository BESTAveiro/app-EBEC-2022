package best.Aveiro.EBEC;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import best.Aveiro.EBEC.Objects.User;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        // Verify if an user is already logged in
        prefs = getApplicationContext().getSharedPreferences("LOGIN_PREFS", 0);

        if (prefs.contains("logged_in")) {
            startApp();
        }
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email_input);
        mPasswordView = (EditText) findViewById(R.id.password_input);



        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable email = mEmailView.getText();
                Editable password = mPasswordView.getText();
                if (email.toString().length() != 0 && password.toString().length() != 0) {
                    attemptLogin(email.toString().trim(), password.toString().trim());
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Values For Password or Email", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void startApp() {
        Intent it = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(it);
        finish();
    }

    private void attemptLogin(final String email, final String password) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = db.child("users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found_match = false;
                for (DataSnapshot userSnaphot : dataSnapshot.getChildren()) {
                    User user = new User();
                    user.setEmail(userSnaphot.child("email").getValue().toString());
                    user.setPassword(userSnaphot.child("password").getValue().toString());
                    boolean isAdmin = userSnaphot.child("admin").getValue(Boolean.class);

                    prefs.edit().putBoolean("admin", isAdmin).apply();
                    if (user.getEmail().equals(email) && user.getPassword().equals(password)) {

                        prefs.edit().putBoolean("logged_in", true).apply();
                        prefs.edit().putString("user_email", user.getEmail()).apply();
                        startApp();
                        found_match=true;
                        break;
                    }

                }
                if(!found_match)
                    Toast.makeText(getApplicationContext(), "Values Provided Do Not Seem To Match Any User In Our Database", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to access database, check your internet connection", Toast.LENGTH_LONG).show();
            }
        };

        usersRef.addValueEventListener(eventListener);
    }

}

