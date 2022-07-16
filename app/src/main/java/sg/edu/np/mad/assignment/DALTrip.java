package sg.edu.np.mad.assignment;


import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

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
//        firestoreDatabase.collection("Trip").document(tripId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    ArrayList<String> temp = new ArrayList<String>();
//                    temp = (ArrayList<String>) task.getResult().get("serializedTAL");
//                    temp.add(newdata);
//                    firestoreDatabase.collection("Trip").document(tripId).update("serializedTAL", temp);
//                }
//            }
//        });
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
//        firestoreDatabase.collection("Trip").document(tripId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    Gson gson = new Gson();
//                    ArrayList<String> temp = new ArrayList<String>();
//                    temp = (ArrayList<String>) task.getResult().get("serializedTAL");
//
//                    for (int i=0; i<temp.size(); i++){
//                        TripAdmin currentta = gson.fromJson(temp.get(i), TripAdmin.class);
//                        if (currentta.userId.equals(ta.userId)){
//                            temp.set(i, newData);
//                            break;
//                        }
//                    }
//                    firestoreDatabase.collection("Trip").document(tripId).update("serializedTAL", temp);
//                }
//            }
//        });
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

//        firestoreDatabase.collection("Trip").document(tripId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    Gson gson = new Gson();
//                    ArrayList<String> temp = new ArrayList<String>();
//                    temp = (ArrayList<String>) task.getResult().get("serializedTAL");
//
//                    for (int i=0; i<temp.size(); i++){
//                        TripAdmin currentta = gson.fromJson(temp.get(i), TripAdmin.class);
//                        if (currentta.userId.equals(ta.userId)){
//                            temp.remove(i);
//                            break;
//                        }
//                    }
//                    firestoreDatabase.collection("Trip").document(tripId).update("serializedTAL", temp);
//                }
//            }
//        });
    }

    public void updateTripEditHistory(EditHistory eh, String tripId){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String todaydate = dateFormat.format(calendar.getTime());
        eh.editTime = todaydate;

        //  Prepare Collab List
        ArrayList<String> sharedTripLists = new ArrayList<String>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                        Log.v("help12345", eh.editByUserName);
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
                        return;
                    }
                }
            }
        });
    }

    public void updateTrip(String oldTripId, String newTitle, String newDestin, String sd, String ed){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (oldTripId.equals(doc.getString("id"))) {
                        db.collection("Trip").document(doc.getId()).update("tripName",newTitle, "destination", newDestin,
                                "startDate", sd, "endDate", ed, "id", newTitle);
                    }
                }
            }
        });

    }

    public ArrayList<Trip> getAllTripArrayList(){
        firestoreDatabase.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Trip> temp = new ArrayList<Trip>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Trip trip = new Trip(
                                doc.getString("destination"),
                                doc.getString("startDate"),
                                doc.getString("endDate"),
                                doc.getString("tripName"),
                                doc.getString("id"),
                                doc.getString("userId"),
                                (ArrayList<String>) doc.get("serializedTAL"),
                                (ArrayList<EditHistory>) doc.get("EditHistoryList")
                                );
                        temp.add(trip);
                        tempTripArrayList = temp;
                    }
                }
                else {
                    Log.w("getAllTripArrayList", "Error getting documents.", task.getException());
                }

            }
        });
        return tempTripArrayList;
    }

}
