package sg.edu.np.mad.assignment.Model;


import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class ActivityId {
    @Exclude
    public String TaskId;

    public  <T extends ActivityId> T withId(@NonNull final String id){
        this.TaskId = id;
        return  (T) this;
    }

}
