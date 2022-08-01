package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView backBtn;
    private ShapeableImageView profilePic;
    private FloatingActionButton addCollaboratorBtn;
    CollaboratorAdapter collaboratorAdapter;
    private SwipeRefreshLayout swipeContainer;

    FirebaseFirestore db;
    StorageReference storageReference;

    List<TripAdmin> dataHolder;

    Trip trip;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Boolean editPermBtnClickable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborators);
        trip = (Trip)getIntent().getSerializableExtra("tripDetails");

        new CollaboratorAdapter(CollaboratorsActivity.this, dataHolder, uid, trip);

        backBtn = findViewById(R.id.collabBackBtn);
        recyclerView = findViewById(R.id.collabRV);
        addCollaboratorBtn = findViewById(R.id.addActivityBtn);
        profilePic = findViewById(R.id.profilePic2);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataHolder.clear();
                showData();
                swipeContainer.setRefreshing(false);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();


        // Add collaborator
        addCollaboratorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollaboratorsActivity.this, AddCollaboratorActivity.class);
                intent.putExtra("tripDetails" , trip);
                startActivity(intent);
            }
        });


        // Go back to view itinerary
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void showData()
    {
        dataHolder = new ArrayList<>();

        // User data
        db = FirebaseFirestore.getInstance();
        db.collection("users").orderBy("username", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                TextView ownerName = findViewById(R.id.ownerName);
                for (DocumentSnapshot doc: task.getResult()) {
                    User user = new User(
                            doc.getString("username"),
                            doc.getString("email"),
                            doc.getString("userId"),
                            doc.getString("profilePic")
                    );

                    // Set name and profile pic of owner
                    if (trip.getUserId().equals(doc.getString("userId")))
                    {
                        ownerName.setText(doc.getString("username"));
                        StorageReference profileRef = storageReference.child(doc.getString("profilePic"));
                        //Loading image into profile picture with picasso api
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(profilePic);
                            }
                        });
                    }

                }
            }
        });


        //  Set adapter to recyclerview
        collaboratorAdapter = new CollaboratorAdapter(CollaboratorsActivity.this, dataHolder, uid, trip);
        LinearLayoutManager lmg = new LinearLayoutManager(CollaboratorsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lmg);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(collaboratorAdapter);

        // Get trip data
        db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    // If current trip id is found in firebase
                    if (trip.getId().equals(doc.getString("id"))) {
                        ArrayList<String> sharedTripLists = new ArrayList<String>();

                        ArrayList<String> stalist = new ArrayList<String>();
                        stalist = (ArrayList<String>) doc.get("serializedTAL");     // get data from firebase and add to stalist

                        if (uid.equals(doc.getString("userId"))){
                            addCollaboratorBtn.setVisibility(View.VISIBLE);
                        }
                        else{
                            addCollaboratorBtn.setVisibility(View.GONE);
                        }

                        for (int i=0; i<stalist.size(); i++){
                            Gson gson = new Gson();
                            TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                            dataHolder.add(tempTa);
                            collaboratorAdapter.notifyDataSetChanged();

                            if (tempTa.getUserId().equals(uid)){
                                if (tempTa.getPermission().equals("Can Edit")){
                                    addCollaboratorBtn.setVisibility(View.VISIBLE);
                                }
                                else {
                                    addCollaboratorBtn.setVisibility(View.GONE);                                }
                            }
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        showData();
    }
}