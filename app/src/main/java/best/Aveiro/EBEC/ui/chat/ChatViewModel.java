package best.Aveiro.EBEC.ui.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import best.Aveiro.EBEC.Objects.ChatMessage;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<ArrayList<ChatMessage>>();
    public void setMessageList(ArrayList<ChatMessage> message_list) {
        this.messages.setValue(message_list);
    }

    public MutableLiveData<ArrayList<ChatMessage>> getMessageList() {
        return this.messages;
    }
}