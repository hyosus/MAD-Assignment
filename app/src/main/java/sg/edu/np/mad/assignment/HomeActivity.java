package sg.edu.np.mad.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Object Trips;
    BottomNavigationView bottomNavigationView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ArrayList<Trip> tripList = new ArrayList<>();
        tripList.add(new Trip("singapore", "12/05/2022", "17/05.2022", "june holi"));


        // Show NoTripsFragment if user has not created any trips
        if (tripList.isEmpty()){
            loadFragment(new NoTripsFragment());
        }
        else {
            loadFragment(new TripsFragment());
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragFrame,new TripsFragment()).commit();

        bottomNavigationView = findViewById(R.id.navBar);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        Intent Intent = new Intent(HomeActivity.this, AddTrip.class);
                        startActivity(Intent);
                        return true;

                }
                return false;
            }
        });
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragFrame, fragment);
        ft.commit();
    }
}