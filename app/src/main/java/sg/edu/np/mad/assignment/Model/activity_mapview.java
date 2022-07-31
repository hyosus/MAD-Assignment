package sg.edu.np.mad.assignment.Model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sg.edu.np.mad.assignment.AddActivityMain;
import sg.edu.np.mad.assignment.R;

public class activity_mapview extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    String name, venue_name,location,due_date,address;
    TextView title,venue,locationadd,Date,ADD;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);


        //intent
        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");

        // Retrieving information from activity page
        venue_name = intent.getStringExtra("VENUE");
        due_date = intent.getStringExtra("DUE");
        location = intent.getStringExtra("LOCATION");
        address = intent.getStringExtra("ADDRESS");

        title = findViewById(R.id.Map_title);
        venue = findViewById(R.id.venue);
        Date = findViewById(R.id.date);
        locationadd = findViewById(R.id.textView21);
        backBtn = findViewById(R.id.backActivity);

        // Inserting and display the passed over information
        title.setText(name);
        venue.setText(venue_name);
        Date.setText(due_date);

        // back button to return to Add Activity Main
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_mapview.this, AddActivityMain.class);
                startActivity(intent);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    // Calls google map api and view location on map
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;

        // Text formatting to get latitude and longitude and place a marker
        String location_filtered = location.replace("lat/lng","");
        location_filtered = location_filtered.replaceAll(":","");
        location_filtered = location_filtered.replaceAll("\\(","").replaceAll("\\)","");
        String[] latlong =  location_filtered.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(location).title(name).snippet(address));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));

        //locationadd.setText(location_filtered);
    }

}
