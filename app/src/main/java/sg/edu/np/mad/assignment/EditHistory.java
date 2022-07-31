package sg.edu.np.mad.assignment;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditHistory {
    private String editedByUserId;
    public String editByUserName;
    public String editTime;
    public String editLog;


    public EditHistory(String editedByUserId, String editByUserName, String editTime, String editLog) {
        this.editedByUserId = editedByUserId;
        this.editByUserName = editByUserName;
        this.editTime = editTime;
        this.editLog = editLog;
    }

    public EditHistory(){}

    public String getEditedByUserId() {
        return editedByUserId;
    }

    public void setEditedByUserId(String editedByUserId) {
        this.editedByUserId = editedByUserId;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditLog() {
        return editLog;
    }

    public void setEditLog(String editLog) {
        this.editLog = editLog;
    }
}


