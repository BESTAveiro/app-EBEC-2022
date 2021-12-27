package best.Aveiro.EBEC.ui.admin;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import best.Aveiro.EBEC.MainActivityViewModel;
import best.Aveiro.EBEC.Objects.ChatMessage;
import best.Aveiro.EBEC.R;
import best.Aveiro.EBEC.ui.chat.ChatAdapter;
import best.Aveiro.EBEC.ui.chat.ChatViewModel;

public class AdminFragment extends Fragment {

    private AdminViewModel mViewModel;
    private MainActivityViewModel mMainViewModel;
    private HashMap<String, Integer> topic_count = new HashMap<String, Integer>();
    private HashMap<String, String> email, team_name;
    private AdminViewModel mAdminViewModel;
    private RecyclerView chat_topic_recycler;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference msgRef = db.child("messages");


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.admin_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        chat_topic_recycler = root.findViewById(R.id.chat_topics);
        mMainViewModel =  new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        mAdminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        getMessagesToSee();
        mAdminViewModel.getMessageCount().observe(getViewLifecycleOwner(),m->{
            ChatListAdapter provas_adapter = new ChatListAdapter(getActivity(), topic_count, mMainViewModel);

            chat_topic_recycler.setAdapter(provas_adapter);
            chat_topic_recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        });

        return root;
    }

    public void getMessagesToSee(){
        msgRef.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                        String team_name = messagesSnapshot.getKey();
                        topic_count.put(team_name,0);
                        for (DataSnapshot chat_message: messagesSnapshot.getChildren()) {
                            boolean isIt = false;
                            for (DataSnapshot seen_by: chat_message.child("seen_by").getChildren()) {
                                if (seen_by.getKey().equalsIgnoreCase(mMainViewModel.getCurrentUser().getFirstName()+ " "+mMainViewModel.getCurrentUser().getLastName())){
                                    isIt=true;
                                    break;
                                }
                            }
                            if (!isIt)
                                topic_count.put(team_name, topic_count.get(team_name)+1 );
                        }

                        }

                }catch(Exception e){

                }
                mAdminViewModel.setMessageList(topic_count);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


}