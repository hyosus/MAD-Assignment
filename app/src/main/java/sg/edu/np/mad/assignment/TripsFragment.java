package sg.edu.np.mad.assignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripsFragment extends Fragment {
    private View mview;
    RecyclerView ongoingRV, upcomingRV;
    List<Trip> dataHolder = new ArrayList<>();
    List<Trip> dataHolder2 = new ArrayList<>();

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaydate;

    // Firestore instance
    FirebaseFirestore db;

    public TripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_trips, container, false);

        // init firestore
        db = FirebaseFirestore.getInstance();

        showData();

        return mview;
    }

    private void showData()
    {
        // get current date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/M/yyyy");
        todaydate = dateFormat.format(calendar.getTime());

        // get & write firestore data
        db.collection("Trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        // Call when data is retrieved
                        for (DocumentSnapshot doc: task.getResult()) {

                            Trip trip = new Trip(
                                    doc.getString("destination"),
                                    doc.getString("startDate"),
                                    doc.getString("endDate"),
                                    doc.getString("tripName"),
                                    doc.getString("id"));

                            try {
                                Date today = dateFormat.parse(todaydate);
                                Date startdate = dateFormat.parse(doc.getString("startDate"));
                                Date enddate = dateFormat.parse(doc.getString("endDate"));

                                // Display ongoing trips - today's date AFTER start date & BEFORE end date
                                if (today.after(startdate) && today.before(enddate) || today.equals(startdate)) {
                                    dataHolder.add(trip);

                                    TripAdapter adapter = new TripAdapter(getContext(), dataHolder);
                                    ongoingRV= mview.findViewById(R.id.ongoingRV);
                                    LinearLayoutManager firstManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                    ongoingRV.setLayoutManager(firstManager);
                                    ongoingRV.setAdapter(adapter);
                                }
                                //Display upcoming trips - today's date BEFORE start date & BEFORE end date
                                else if (today.before(startdate) && today.before(enddate))
                                {
                                    dataHolder2.add(trip);
                                    TripAdapter adapter2 = new TripAdapter(getContext(), dataHolder2);
                                    upcomingRV= mview.findViewById(R.id.upcomingRV);
                                    LinearLayoutManager secondManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                    upcomingRV.setLayoutManager(secondManager);
                                    upcomingRV.setAdapter(adapter2);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

}