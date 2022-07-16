package sg.edu.np.mad.assignment;


import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.LongToDoubleFunction;


public class DALTrip {
    private ArrayList<Trip> tempTripArrayList = new ArrayList<Trip>();

    private FirebaseFirestore firestoreDatabase;

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

    public void addTripSerializedTA(String tripId, String newdata){
        firestoreDatabase.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (tripId.equals(doc.getString("id"))) {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp = (ArrayList<String>) doc.get("serializedTAL");
                        temp.add(newdata);
                        firestoreDatabase.collection("Trip").document(doc.getId()).update("serializedTAL", temp);
                    }
                }
            }
        });
    }

    public void updateTripSerializedTAL(String tripId, TripAdmin ta, String newData){
        firestoreDatabase.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (tripId.equals(doc.getString("id"))) {
                        Gson gson = new Gson();
                        ArrayList<String> temp = new ArrayList<String>();
                        temp = (ArrayList<String>) doc.get("serializedTAL");

                        for (int i=0; i<temp.size(); i++){
                            TripAdmin currentta = gson.fromJson(temp.get(i), TripAdmin.class);
                            if (currentta.userId.equals(ta.userId)){
                                temp.set(i, newData);
                                break;
                            }
                        }
                        firestoreDatabase.collection("Trip").document(doc.getId()).update("serializedTAL", temp);
                    }
                }
            }
        });
    }


    public void removeTripSerializedTA(String tripId, TripAdmin ta){
        firestoreDatabase.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (tripId.equals(doc.getString("id"))) {
                        Gson gson = new Gson();
                        ArrayList<String> temp = new ArrayList<String>();
                        temp = (ArrayList<String>) doc.get("serializedTAL");

                        for (int i=0; i<temp.size(); i++){
                            TripAdmin currentta = gson.fromJson(temp.get(i), TripAdmin.class);
                            if (currentta.userId.equals(ta.userId)){
                                temp.remove(i);
                                break;
                            }
                        }
                        firestoreDatabase.collection("Trip").document(doc.getId()).update("serializedTAL", temp);
                    }
                }
            }
        });

    }

    public void updateTripEditHistory(EditHistory eh, String tripId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(eh.getEditedByUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                eh.editByUserName = task.getResult().getString("username");
            }
        });

        //  Prepare Collab List
        ArrayList<String> sharedTripLists = new ArrayList<String>();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (tripId.equals(doc.getString("id"))) {

                                ArrayList<String> stalist = new ArrayList<String>();
                                stalist = (ArrayList<String>) doc.get("serializedTAL");
                                for (int i=0; i<stalist.size(); i++) {
                                    Gson gson = new Gson();
                                    TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                                    sharedTripLists.add(tempTa.userId);
                                }
                            }

                        }
                    }
                });

                for (DocumentSnapshot doc : task.getResult()) {
                    //  Check if owner or in user in trip collab list
                    if (eh.getEditedByUserId().equals(doc.getString("userId")) || sharedTripLists.contains(eh.getEditedByUserId())) {
                        eh.editByUserName = doc.getString("username");
                    }
                }
            }
        });


        db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (tripId.equals(doc.getString("id"))) {
                        ArrayList<EditHistory> temp = (ArrayList<EditHistory>) doc.get("EditHistoryList");
                        temp.add(eh);
                        firestoreDatabase.collection("Trip").document(doc.getId()).update("EditHistoryList", temp);

                        //  new
                        Gson gson = new Gson();
                        ArrayList<String> sEHL = new ArrayList<String>();
                        for(EditHistory eh: temp){
                            sEHL.add(gson.toJson(eh));
                        }
                        firestoreDatabase.collection("Trip").document(doc.getId()).update("serializedEHL", sEHL);
                        return;
                    }
                }
            }
        });
    }

    public void updateTrip(String oldTripId, String newTitle, String newDestin, String sd, String ed, ArrayList<EditHistory> EHL, String newData){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Gson gson = new Gson();
                for (DocumentSnapshot doc : task.getResult()) {

                    if (oldTripId.equals(doc.getString("id"))) {
                        ArrayList<String> temp = new ArrayList<String>();
                        temp = (ArrayList<String>) doc.get("serializedEHL");
                        temp.add(newData);

                        db.collection("Trip").document(doc.getId()).update("tripName",newTitle, "destination", newDestin,
                                "startDate", sd, "endDate", ed, "id", newTitle, "EditHistoryList", EHL, "serializedEHL", temp);
                    }
                }
            }
        });

    }

}
