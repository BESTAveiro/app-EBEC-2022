package beest.Aveiroo.EBEC.ui.admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class AdminViewModel extends ViewModel {
    private MutableLiveData<HashMap<String, Integer>> message_count = new MutableLiveData<HashMap<String, Integer>>();
    public void setMessageList(HashMap<String, Integer> message_list) {
        this.message_count.setValue(message_list);
    }

    public MutableLiveData<HashMap<String, Integer>> getMessageCount() {
        return this.message_count;
    }
    // TODO: Implement the ViewModel
}