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
    public String editLog = "";

    public EditHistory(String editedByUserId, String editLog) {
        this.editedByUserId = editedByUserId;
        this.editTime = editTime;
        this.editLog = editLog;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(editedByUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                editByUserName = task.getResult().getString("username");
            }
        });
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


