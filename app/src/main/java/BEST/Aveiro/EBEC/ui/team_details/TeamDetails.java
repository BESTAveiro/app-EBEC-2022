package best.Aveiro.EBEC.ui.team_details;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import best.Aveiro.EBEC.MainActivityViewModel;
import best.Aveiro.EBEC.Objects.Prova;
import best.Aveiro.EBEC.Objects.Team;
import best.Aveiro.EBEC.Objects.User;
import best.Aveiro.EBEC.R;

public class TeamDetails extends Fragment {
    private MainActivityViewModel mainViewModel;
    private User currentUser;
    private Team currentTeam;
    private RecyclerView teamMembersList;
    private RecyclerView teamProvasList;
    private TextView mTeamName;
    private TextView mTeamCredits;
    private CardView mProvasCard;
    private String mode = "LIGHT";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class); //getActivity()

        View root = inflater.inflate(R.layout.team_details_fragment, container, false);
        currentUser = mainViewModel.getCurrentUser();
        currentTeam = updateTeam();
        mainViewModel.setCurrentTeam(currentTeam);


        mTeamName = root.findViewById(R.id.team_details_name);
        mTeamCredits = root.findViewById(R.id.team_details_credits);
        mTeamCredits.setVisibility(View.GONE);
        mProvasCard = root.findViewById(R.id.provas_card);
        teamMembersList = root.findViewById(R.id.team_members_recyclerView);
        teamProvasList = root.findViewById(R.id.team_provas_recyclerView);

        // Comparing to see which preference is selected and applying those theme settings

        return root;
    }



    public Team updateTeam() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference teamsRef = db.child("teams");
        Team temp_team = new Team();

        teamsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("TEAM Members ++++++++++" + mainViewModel.getCurrentUser().getTeamName());
                    DataSnapshot teamsSnapshot = dataSnapshot.child(mainViewModel.getCurrentUser().getTeamName());
                    ArrayList<String> members = new ArrayList<>();
                    for (DataSnapshot team_member : teamsSnapshot.child("members").getChildren()) {

                        members.add(team_member.getValue().toString());
                        System.out.println("TEAM Members ++++++++++" + members.toString() + team_member.getValue().toString());
                        temp_team.setName(teamsSnapshot.child("name").getValue().toString());
                        temp_team.setCredits(teamsSnapshot.child("credits").getValue(Integer.class));
                        temp_team.setModality(teamsSnapshot.child("md").getValue().toString());

                        temp_team.setMembers(members);

                        ArrayList<Prova> competitions = new ArrayList<Prova>();
                        for (DataSnapshot competition : teamsSnapshot.child("comps").getChildren()) {
                            Prova temp_prova = new Prova();
                            temp_prova.setName(competition.getKey());
                            temp_prova.setScore(competition.getValue().toString());
                            competitions.add(temp_prova);
                        }
                        temp_team.setProvas(competitions);
                        currentTeam = temp_team;
                        updateState();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                };
            });
        return temp_team;
        }

        private void updateState () {
            mTeamName.setText(currentTeam.getName());
            TeamMembersAdapter members_adapter = new TeamMembersAdapter(getActivity().getApplicationContext(), currentTeam.getMembers(), mode);
            teamMembersList.setAdapter(members_adapter);
            mTeamCredits.setText(currentTeam.getCredits() + " Credits");
            teamMembersList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            if (currentTeam.getModality().equalsIgnoreCase("TD")) {
                if (mProvasCard.getVisibility() == View.GONE)
                    mProvasCard.setVisibility(View.VISIBLE);
                TeamProvasAdapter provas_adapter = new TeamProvasAdapter(getActivity().getApplicationContext(), currentTeam.getProvas());
                teamProvasList.setAdapter(provas_adapter);
                teamProvasList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            } else {
                mProvasCard.setVisibility(View.GONE);
            }
        }


    }

