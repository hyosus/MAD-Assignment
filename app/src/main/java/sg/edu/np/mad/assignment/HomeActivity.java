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
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
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
    DALTrip dal = new DALTrip();

    ShapeableImageView profileImg;
    StorageReference storageReference;

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid;
    String queryTripId = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Handle deep link
        try{
            FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                @Override
                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                    }

                    if (deepLink != null){
                        // get tripId from link query
                        queryTripId = deepLink.getQueryParameter("tripid");

                        FirebaseFirestore.getInstance().collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    if (queryTripId.equals(doc.getString("id"))) {
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

                                        // add user to collaborator
                                        ArrayList<String> stalist = new ArrayList<String>();
                                        stalist = (ArrayList<String>) doc.get("serializedTAL");     // get data from firebase and add to stalist
                                        ArrayList<String> sharedTripLists = new ArrayList<String>();
                                        Gson gson = new Gson();

                                        for (int i=0; i<stalist.size(); i++) {
                                            TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);      // get trip admin
                                            sharedTripLists.add(tempTa.userId);
                                        }

                                        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                // Check if user is the owner or already part of collaborator
                                                if (!doc.getString("userId").equals(uid) && !sharedTripLists.contains(uid)){
                                                    TripAdmin newTA = new TripAdmin(uid, task.getResult().getString("username"), "View Only");
                                                    String taJsonString = gson.toJson(newTA);
                                                    dal.addTripSerializedTA(trip.getId(), taJsonString);
                                                    finish();
                                                }
                                            }
                                        });

                                        // view trip
                                        Intent intent = new Intent(HomeActivity.this, AddActivityMain.class);
                                        intent.putExtra("tripDetails", trip);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
        catch (Error e){
            Toast.makeText(this, "Error getting trip", Toast.LENGTH_SHORT).show();
        }

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
                                if (stalist != null){
                                    for (int i=0; i<stalist.size(); i++){
                                        TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                                        sharedTripLists.add(tempTa.userId);
                                    }

                                    if (uid.equals(document.getString("userId")) || sharedTripLists.contains(uid)) {
                                        count++;
                                    }
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