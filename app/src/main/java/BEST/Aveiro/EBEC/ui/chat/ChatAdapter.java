package best.Aveiro.EBEC.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import best.Aveiro.EBEC.Objects.ChatMessage;
import best.Aveiro.EBEC.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder>  {
    private Context mContext;
    private ArrayList<ChatMessage> messages;
    private LayoutInflater mInflater;
    public ChatAdapter(Context context, ArrayList<ChatMessage> list_of_messages){
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.messages = list_of_messages;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView;
        mItemView = mInflater.inflate(R.layout.chat_message_item, parent, false);
        return new ChatAdapter.MessageViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage currentMessage = this.messages.get(position);
        holder.message.setText(currentMessage.getMessage());
        holder.sender.setText(currentMessage.getSender());
        holder.userImage.setImageResource(R.drawable.ic_user);
        if (currentMessage.getMessage().equals("EBEC Aveiro")){
            holder.userImage.setImageResource(R.drawable.ebec);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public ChatAdapter mAdapter;
        private TextView sender, message;
        private ImageView userImage;
        public MessageViewHolder(@NonNull View itemView, ChatAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            sender = itemView.findViewById(R.id.sender_username);
            message = itemView.findViewById(R.id.message_text);
            userImage = itemView.findViewById(R.id.sender_image);

        }
    }
}
