package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ShapeableImageView profileImg;
    StorageReference storageReference;

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Show different fragments
        showTripFragment();

        profileImg = findViewById(R.id.profileImg);

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + uid + "/profilePic");

        //Loading image into profile picture with picasso api
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImg);
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewProfile = new Intent(HomeActivity.this, ViewProfile.class);
                startActivity(viewProfile);
            }
        });

        bottomNavigationView = findViewById(R.id.navBar);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calculate:
                        Intent currencyIntent = new Intent(HomeActivity.this, CurrencyConvertor.class);
                        startActivity(currencyIntent);
                        return true;

                    case R.id.add:
                        Intent Intent = new Intent(HomeActivity.this, AddTrip.class);
                        startActivity(Intent);
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTripFragment();
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragFrame, fragment);
        ft.commit();
    }

    public void showTripFragment(){
        db.collection("Trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Gson gson = new Gson();
                        if (task.isSuccessful()) {

                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                ArrayList<String> sharedTripLists = new ArrayList<String>();
                                ArrayList<TripAdmin> tripAdminArrayList = new ArrayList<TripAdmin>();

                                ArrayList<String> stalist = (ArrayList<String>) document.get("serializedTAL");
                                for (int i=0; i<stalist.size(); i++){
                                    TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                                    sharedTripLists.add(tempTa.userId);
                                }

                                if (uid.equals(document.getString("userId")) || sharedTripLists.contains(uid)) {
                                    count++;
                                }
                            }
                            if (count == 0){
                                loadFragment(new NoTripsFragment());
                            }
                            else {
                                loadFragment(new TripsFragment());
                            }
                        }
                        else {
                            Log.d("checkTripListEmpty()", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}