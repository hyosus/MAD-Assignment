package sg.edu.np.mad.assignment;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class DALTrip {
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestoreDatabase;
    private List<String> keyList = new ArrayList<>();
    private ArrayList<Trip> tripList = new ArrayList<>();
    public DALTrip(){
        //  Realtime DB
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Trip.class.getSimpleName());

        //  Firestore
        firestoreDatabase = FirebaseFirestore.getInstance();
    }

//    public Task<Void> add(Trip trip)
//    {
//        Log.v("fb","DAOTrip.add executed");
//        return databaseReference.push().setValue(trip);
//    }

    //  CRUD Functions
    //  CRUD:Create
    public void createTrip(Trip trip){
        try{
            firestoreDatabase.collection(Trip.class.getSimpleName()).document(trip.getTripName()).set(trip);
            //  firestoreDatabase.collection(Trip.class.getSimpleName()).add(trip);
        }
        catch(Error e){
            Log.v("DALTrip",String.format("Could not create Trip\n%s",e));
        }
    }

    //  CRUD:RETRIEVE
    public void retrieveKeyList(){

//        myDB.collection("tasks").document("user_preferred_id").set(data)myDB.collection("tasks").addSnapshotListener(new EventListener() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                if (e != null)
//                    toastResult(e.getMessage());
//                list.clear();
//                for (DocumentSnapshot doc : documentSnapshots) {
//                    list.add(doc.getString("task_name"));
//                }
//            }
//        });

        firestoreDatabase.collection(Trip.class.getSimpleName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Trip> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference docRef = firestoreDatabase.collection("Users").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Trip trip = new Trip(
                                                document.get("destination").toString(),
                                                document.get("startDate").toString(),
                                                document.get("endDate").toString(),
                                                document.get("tripName").toString()
                                        );
                                        Log.v("FUCK",trip.getDestination().toString());
                                        tripList.add(trip);
                                    } else {
                                        Log.v("DALTrip", "Error getting document for "+document.getId());
                                    }
                                }

                            }
                        });
                    }

                } else {
                    Log.v("DALTrip", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
