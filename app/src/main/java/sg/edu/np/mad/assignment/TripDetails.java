package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TripDetails extends AppCompatActivity {
    List<Trip> dataHolder = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        TextView header = findViewById(R.id.tripNameTxt);
        TextView date = findViewById(R.id.datesTxt);

        DALTrip dalTrip = new DALTrip();
        Trip trip = (Trip)getIntent().getSerializableExtra("tripDetails");

        if (trip != null) {
            Log.v("cb", trip.getTripName());
            header.setText(trip.getTripName());
            date.setText(trip.getStartDate() + " - " + trip.getEndDate());
        }
    }
}