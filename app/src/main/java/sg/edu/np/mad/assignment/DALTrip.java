package sg.edu.np.mad.assignment;


import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class DALTrip {
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestoreDatabase;
    private List<String> keyList = new ArrayList<>();
    private ArrayList<Trip> tripList = new ArrayList<>();
    public DALTrip(){

        //  Firestore
        firestoreDatabase = FirebaseFirestore.getInstance();
    }


    //  CRUD Functions
    //  CRUD:Create
    public void createTrip(Trip trip){
        try{
            firestoreDatabase.collection(Trip.class.getSimpleName()).document(trip.getTripName()).set(trip);
        }
        catch(Error e){
            Log.v("DALTrip",String.format("Could not create Trip\n%s",e));
        }
    }
}
