package best.Aveiro.EBEC.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import best.Aveiro.EBEC.Objects.User;
import best.Aveiro.EBEC.R;

public class HomeFragment extends Fragment {
    private TextView ongoing, nextUp, nextTime;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ongoing = root.findViewById(R.id.ongoingName);
        nextUp = root.findViewById(R.id.nextName);
        nextTime = root.findViewById(R.id.nextTime);
        getEvents();

        return root;
    }
    private void getEvents(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = db.child("events");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ongoing.setText(dataSnapshot.child("ongoing").getValue().toString());;
                nextUp.setText(dataSnapshot.child("next").getValue().toString());
                nextTime.setText(dataSnapshot.child("next_time").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), "Failed to access database, check your internet connection", Toast.LENGTH_LONG).show();
            }
        };

        usersRef.addValueEventListener(eventListener);
    }
}