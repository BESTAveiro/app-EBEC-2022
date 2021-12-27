package best.Aveiro.EBEC.ui.admin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.TeamChatHolder>  {
    private Activity mActivity;
    private Context mContext;
    private HashMap<String, Integer> topics;
    private LayoutInflater mInflater;
    public MainActivityViewModel mMainViewModel;

    public ChatListAdapter(Activity activity, HashMap<String, Integer> list_of_topics, MainActivityViewModel mViewModel){
        mMainViewModel =  mViewModel;
        this.mActivity = activity;
        mInflater = LayoutInflater.from(activity.getApplicationContext());
        this.mContext = activity.getApplicationContext();
        this.topics = list_of_topics;
    }
    @NonNull
    @Override
    public TeamChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView;
        mItemView = mInflater.inflate(R.layout.chat_team, parent, false);
        return new TeamChatHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamChatHolder holder, int position) {
        String topic = (String) this.topics.keySet().toArray()[position];
        holder.topic.setText(topic);

        NavController navController = Navigation.findNavController(mActivity, R.id.nav_host_fragment);
        holder.admin_topic_chat_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.nav_chat);
                mMainViewModel.setCurrentTopic(topic);
            }
        });

    }

    @Override
    public int getItemCount() {
        return topics.keySet().size();
    }

    public class TeamChatHolder extends RecyclerView.ViewHolder {
        public ChatListAdapter mAdapter;
        private TextView topic;
        private ConstraintLayout admin_topic_chat_card;
        public TeamChatHolder(@NonNull View itemView, ChatListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            topic = itemView.findViewById(R.id.chat_team_name);
            admin_topic_chat_card = itemView.findViewById(R.id.admin_topic_chat_card);
        }
    }
}
