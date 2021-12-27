package best.Aveiro.EBEC.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import best.Aveiro.EBEC.MainActivity;
import best.Aveiro.EBEC.MainActivityViewModel;
import best.Aveiro.EBEC.Objects.ChatMessage;
import best.Aveiro.EBEC.Objects.Day;
import best.Aveiro.EBEC.Objects.Prova;
import best.Aveiro.EBEC.Objects.Team;
import best.Aveiro.EBEC.Objects.User;
import best.Aveiro.EBEC.R;
import best.Aveiro.EBEC.ui.team_details.TeamProvasAdapter;

public class ChatFragment extends Fragment {

    private ChatViewModel mChatViewModel;
    private MainActivityViewModel mMainViewModel;
    private RecyclerView messages_recycler;
    private Button send_message;
    private EditText composed_message;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference msgRef = db.child("messages");
    SharedPreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.chat_fragment, container, false);
        mChatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        mMainViewModel =  new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        messages_recycler = root.findViewById(R.id.messages_recycler);


        composed_message = root.findViewById(R.id.message_compose_text);
        mChatViewModel.getMessageList().observe(getViewLifecycleOwner(),messagesList->{
            ChatAdapter provas_adapter = new ChatAdapter(getActivity().getApplicationContext(), messagesList);
            messages_recycler.setAdapter(provas_adapter);
            messages_recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        });
        getMessages();
        send_message = root.findViewById(R.id.btn_send_message);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = composed_message.getText().toString();
                if (message.trim().length()>1){
                    saveMessage(message);
                    composed_message.setText("");

                }

            }
        });
        mMainViewModel.setIsFABVisible(View.GONE);



        return root;
    }

    public void saveMessage(String message) {
        if (message != null) {
            DatabaseReference mCurrentTracked = msgRef.child(mMainViewModel.getCurrentTeam().getName()).push();
            mCurrentTracked.child("sender").setValue(String.format("%s %s", mMainViewModel.getCurrentUser().getFirstName(),mMainViewModel.getCurrentUser().getLastName()));
            mCurrentTracked.child("message").setValue(message);


        }else{
            Toast.makeText(getActivity(), "Not Saved - Not Enough Time or Distance", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mMainViewModel.setIsFABVisible(View.VISIBLE);
    }

    public void getMessages(){
        msgRef.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                        String topic = "_";
                        if (mMainViewModel.isAdmin())
                            topic = mMainViewModel.getTopic();
                        else
                            topic = mMainViewModel.getCurrentTeam().getName();

                        if (messagesSnapshot.getKey().equalsIgnoreCase(topic)){
                            ArrayList<ChatMessage> message_list = new ArrayList<ChatMessage>();
                            for (DataSnapshot chat_msgs: messagesSnapshot.getChildren()){
                                ChatMessage temp = new ChatMessage();
                                temp.setSender(chat_msgs.child("sender").getValue().toString());
                                temp.setMessage(chat_msgs.child("message").getValue().toString());
                                System.out.println(chat_msgs.toString()+"-------------");
                                message_list.add(temp);
                            }


                            mChatViewModel.setMessageList(message_list);
                            break;

                        }
                    }
                }catch(Exception e){

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


}