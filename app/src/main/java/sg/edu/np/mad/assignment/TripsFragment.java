package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripsFragment extends Fragment {
    DALTrip dal = new DALTrip();
    List<Trip> thisTripList = new ArrayList<Trip>();

    private View mview;
    RecyclerView ongoingRV, upcomingRV;
    List<Trip> dataHolder = new ArrayList<>();
    List<Trip> dataHolder2 = new ArrayList<>();
    List<Trip> dataHolder3 = new ArrayList<>();

    TextView pastTxt, upcomingTxt;
    ImageView addBtn;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaydate;
    private boolean hasTrips = false;


    String uid;

    // Firestore instance
    FirebaseFirestore db;

    public TripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_trips, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        pastTxt = mview.findViewById(R.id.pastTxt);
        upcomingTxt = mview.findViewById(R.id.upcomingTxt);
        addBtn = mview.findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mview.getContext(), AddTrip.class);
                startActivity(i);
            }
        });

        // init firestore
        db = FirebaseFirestore.getInstance();

        showData();

        return mview;
    }

    private void showData()
    {
        // get current date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("d MMM yyyy");
        todaydate = dateFormat.format(calendar.getTime());

        // get & write firestore data
        db.collection("Trip").orderBy("startDate", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                Gson gson = new Gson();
                TripAdapter adapter = new TripAdapter(getContext(), dataHolder);
                TripAdapter adapter2 = new TripAdapter(getContext(), dataHolder2);
                TripAdapter adapter3 = new TripAdapter(getContext(), dataHolder3);

                // Call when data is retrieved
                for (DocumentSnapshot doc: task.getResult()) {
                    ArrayList<String> sharedTripLists = new ArrayList<String>();
                    ArrayList<TripAdmin> tripAdminArrayList = new ArrayList<TripAdmin>();

                    ArrayList<String> stalist = (ArrayList<String>) doc.get("serializedTAL");
                    if (stalist != null) {
                        for (int i = 0; i < stalist.size(); i++) {
                            TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                            sharedTripLists.add(tempTa.userId);
                        }
                    }

                    if (uid.equals(doc.getString("userId")) || sharedTripLists.contains(uid)){
                        hasTrips = true;
                        Trip trip = new Trip(
                                doc.getString("destination"),
                                doc.getString("startDate"),
                                doc.getString("endDate"),
                                doc.getString("tripName"),
                                doc.getString("id"),
                                doc.getString("userId"),
                                (ArrayList<String>) doc.get("serializedTAL"),
                                (ArrayList<EditHistory>) doc.get("EditHistoryList"),
                                (ArrayList<String>) doc.get("serializedEHL")
                        );

                        try {
                            Date today = dateFormat.parse(todaydate);
                            Date startdate = dateFormat.parse(doc.getString("startDate"));
                            Date enddate = dateFormat.parse(doc.getString("endDate"));

                            // Display ongoing trips - today's date AFTER start date & BEFORE end date
                            if (today.after(startdate) && today.before(enddate) || today.equals(startdate)) {
                                dataHolder.add(trip);
                                ongoingRV = mview.findViewById(R.id.ongoingRV);

                                LinearLayoutManager firstManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                ongoingRV.setLayoutManager(firstManager);
                                ongoingRV.setAdapter(adapter);

                                addBtn.setVisibility(View.GONE);
                            }
                            //Display upcoming trips - today's date BEFORE start date & BEFORE end date
                            else if (today.before(startdate) && today.before(enddate)) {
                                dataHolder2.add(trip);
                                upcomingRV = mview.findViewById(R.id.upcomingRV);

                                LinearLayoutManager secondManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                upcomingRV.setLayoutManager(secondManager);
                                upcomingRV.setAdapter(adapter2);
                            }
                            // Display past trips - after end date
                            else if (today.after(enddate)) {
                                dataHolder3.add(trip);
                                upcomingRV = mview.findViewById(R.id.upcomingRV);

                            }

                            pastTxt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    pastTxt.setTextColor(Color.parseColor("#112D4E"));
                                    upcomingTxt.setTextColor(Color.parseColor("#808080"));

                                    if (dataHolder3.isEmpty() == false) {
                                        LinearLayoutManager secondManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                        upcomingRV.setLayoutManager(secondManager);
                                        upcomingRV.setAdapter(adapter3);
                                        upcomingRV.setVisibility(View.VISIBLE);
                                    }
                                    else if (dataHolder2.isEmpty() == false)
                                    {
                                        upcomingRV.setVisibility(View.GONE);
                                    }
                                }
                            });

                            upcomingTxt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pastTxt.setTextColor(Color.parseColor("#808080"));
                                    upcomingTxt.setTextColor(Color.parseColor("#112D4E"));

                                    if (dataHolder2.isEmpty() == false) {
                                        LinearLayoutManager secondManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                        upcomingRV.setLayoutManager(secondManager);
                                        upcomingRV.setAdapter(adapter2);
                                        upcomingRV.setVisibility(View.VISIBLE);
                                    }
                                    else if (dataHolder3.isEmpty() == false)
                                    {
                                        upcomingRV.setVisibility(View.GONE);
                                    }

                                }
                            });

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }
}