package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        addCollaboratorBtn = findViewById(R.id.collabAddBtn);
        profilePic = findViewById(R.id.profilePic2);

        storageReference = FirebaseStorage.getInstance().getReference();

        TextView ownerName = findViewById(R.id.ownerName);

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
                Intent intent = new Intent(CollaboratorsActivity.this, AddActivityMain.class);
                startActivity(intent);
                intent.putExtra("tripDetails", trip);
                finish();
            }
        });

        showData();

        //  Listener
        // FIX THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        DocumentReference docRef = db.collection("Trip").document(trip.getId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    collaboratorAdapter.notifyDataSetChanged();
                } else {
                    collaboratorAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void showData()
    {
        dataHolder = new ArrayList<>();

        // User data
        db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


        //  💚💚💚
        collaboratorAdapter = new CollaboratorAdapter(CollaboratorsActivity.this, dataHolder, uid, trip);
        LinearLayoutManager lmg = new LinearLayoutManager(CollaboratorsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lmg);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(collaboratorAdapter);

        db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (trip.getId().equals(doc.getString("id"))) {
                        ArrayList<String> sharedTripLists = new ArrayList<String>();

                        ArrayList<String> stalist = new ArrayList<String>();
                        stalist = (ArrayList<String>) doc.get("serializedTAL");
                        for (int i=0; i<stalist.size(); i++) {
                            Gson gson = new Gson();
                            TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                            dataHolder.add(tempTa);
                            sharedTripLists.add(tempTa.userId);
                            collaboratorAdapter.notifyDataSetChanged();

                            if (uid.equals(doc.getString("userId")) ||
                                    sharedTripLists.contains(uid) && sharedTripLists.get(sharedTripLists.indexOf(uid)).equals("Can Edit")) {
                                addCollaboratorBtn.setVisibility(View.VISIBLE);
                            } else {
                                addCollaboratorBtn.setVisibility(View.GONE);
                            }
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        collaboratorAdapter.update(CollaboratorsActivity.this, trip);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        collaboratorAdapter.update(CollaboratorsActivity.this, trip);
    }
}