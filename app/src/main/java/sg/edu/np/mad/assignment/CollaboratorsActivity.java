package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorsActivity extends AppCompatActivity {
    private Spinner spinner;
    private RecyclerView recyclerView;
    private ImageView backBtn;
    private CollaboratorAdapter collabAdapter;
    private TextView title;

    FirebaseFirestore db;

    List<User> dataHolder = new ArrayList<>();
    List<String> permissionList = new ArrayList<>();

    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborators);

        trip = (Trip)getIntent().getSerializableExtra("tripDetails");

        backBtn = findViewById(R.id.collabBackBtn);
        spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.collabRV);
        title=findViewById(R.id.textView15);


        // Go back to view itinerary
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollaboratorsActivity.this, AddActivityMain.class);
                startActivity(intent);
            }
        });

        showData();

    }

    private void showData()
    {
        // Parse spinner list
        permissionList.add("Can Edit");
        permissionList.add("View Only");


        // User data
        db = FirebaseFirestore.getInstance();
        // Add user from firebase to dataholder
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                collabAdapter = new CollaboratorAdapter(CollaboratorsActivity.this, dataHolder, permissionList);

                // Call when data is retrieved
                for (DocumentSnapshot doc: task.getResult()) {
                    User user = new User(
                            doc.getString("username"),
                            doc.getString("email"),
                            doc.getString("userId"),
                            doc.getString("profilePic")
                    );


                    dataHolder.add(user);
                    LinearLayoutManager lmg = new LinearLayoutManager(CollaboratorsActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(lmg);
                    recyclerView.setAdapter(collabAdapter);
                }

            }
        });
    }

}