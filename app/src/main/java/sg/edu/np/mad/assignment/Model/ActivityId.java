package sg.edu.np.mad.assignment.Model;


import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class ActivityId {
    @Exclude
    public String ActivityId;

    public  <T extends  ActivityId> T withId(@NonNull final String id){
        this.ActivityId = id;
        return  (T) this;
    }

}
