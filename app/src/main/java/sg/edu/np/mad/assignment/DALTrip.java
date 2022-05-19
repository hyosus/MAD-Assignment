package sg.edu.np.mad.assignment;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DALTrip {

    private DatabaseReference databaseReference;
    public DALTrip(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Trip.class.getSimpleName());
    }

    public Task<Void> add(Trip trip)
    {
        Log.v("fb","DAOTrip.add executed");
        return databaseReference.push().setValue(trip);
    }
}
