package sg.edu.np.mad.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {
    private View mview;
    RecyclerView recyclerView;
    List<Trip> dataHolder = new ArrayList<>();
    ArrayList<Trip> tripList;

    // Firestore instance
    FirebaseFirestore db;

    TripAdapter adapter;

    public TripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_trips, container, false);
        recyclerView = mview.findViewById(R.id.ongoingRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // init firestore
        db = FirebaseFirestore.getInstance();

        showData();

        return mview;
    }

    private void showData()
    {
        // eDate.compareTo(sDate)<0
        String todaydate = "21/05/2022";
        db.collection("Trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // Call when data is retrieved
                        for (DocumentSnapshot doc: task.getResult()) {
                            Trip trip = new Trip(
                                    doc.getString("destination"),
                                    doc.getString("startDate"),
                                    doc.getString("endDate"),
                                    doc.getString("tripName"));


                            if (todaydate.compareTo(doc.getString("startDate")) > 0 & todaydate.compareTo(doc.getString("endDate")) < 0){
                                dataHolder.add(trip);
                            }

                        }

                        adapter = new TripAdapter(TripsFragment.this, dataHolder);

                        recyclerView.setAdapter(adapter);

                    }
                });
    }
}