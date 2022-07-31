package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AddCollaboratorActivity extends AppCompatActivity {

    private EditText collabEmailTxt;
    private ImageView backBtn;
    private Button sendBtn;
    private RadioGroup radioGroup;
    private RadioButton selectedRB;
    private String collabEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Trip trip;
    DALTrip dal = new DALTrip();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collaborator);

        // Get extra
        trip = (Trip)getIntent().getSerializableExtra("tripDetails");

        collabEmailTxt = findViewById(R.id.collabEmail);
        backBtn = findViewById(R.id.addCollabBackBtn);
        radioGroup = findViewById(R.id.collabEditAlertRG);
        sendBtn = findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collabEmail = collabEmailTxt.getText().toString();

                // Radio button
                int radioId = radioGroup.getCheckedRadioButtonId();
                selectedRB = findViewById(radioId);

                if (collabEmail.isEmpty()){
                    showError(collabEmailTxt, "Missing information");
                }
                else
                {
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<String> emailList = new ArrayList<>();
                            for (DocumentSnapshot doc: task.getResult()) {
                                String email = doc.getString("email");
                                if (doc.getId().equals(trip.getUserId()) && collabEmail.equals(email)){
                                    Toast.makeText(AddCollaboratorActivity.this, "You are the owner of this trip.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String userId = doc.getString("userId");
                                emailList.add(email);

                                ArrayList<String> sharedTripLists = new ArrayList<String>();

                                db.collection("Trip").document(trip.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        ArrayList<String> stalist = new ArrayList<String>();
                                        stalist = (ArrayList<String>) task.getResult().get("serializedTAL");     // get data from firebase and add to stalist

                                        if (stalist != null){
                                            for (int i=0; i<stalist.size(); i++) {
                                                Gson gson = new Gson();
                                                TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);      // get trip admin
                                                sharedTripLists.add(tempTa.userId);
                                            }
                                        }

                                        // check if email input is a valid email. Is the user already added. Is the user the owner
                                        if(email.equals(collabEmail)){
                                            if (!sharedTripLists.contains(userId) && !userId.equals(task.getResult().getString("userId"))){
                                                TripAdmin newTA = new TripAdmin(userId,doc.getString("username"), selectedRB.getText().toString());
                                                Gson gson = new Gson();
                                                String taJsonString = gson.toJson(newTA);
                                                dal.addTripSerializedTA(trip.getId(), taJsonString);
                                                Toast.makeText(AddCollaboratorActivity.this, "Added user " + doc.getString("username"), Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else if (sharedTripLists.contains(userId)){
                                                Toast.makeText(AddCollaboratorActivity.this, "User has already been added", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (userId.equals(task.getResult().getString("userId"))){
                                                Toast.makeText(AddCollaboratorActivity.this, "Cannot add yourself", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                            if (!emailList.contains(collabEmail)){
                                Toast.makeText(AddCollaboratorActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // Input validation
    private void showError(EditText input, String missing_information) {
        input.setError(missing_information);
        input.requestFocus();
    }

}