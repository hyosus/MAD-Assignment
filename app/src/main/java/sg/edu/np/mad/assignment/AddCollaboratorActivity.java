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
                            for (DocumentSnapshot doc: task.getResult()) {
                                String email = doc.getString("email");
                                String userId = doc.getString("userId");

                                if(email.equals(collabEmail)){
                                    TripAdmin newTA = new TripAdmin(userId,doc.getString("username"), selectedRB.getText().toString());
                                    Gson gson = new Gson();
                                    String taJsonString = gson.toJson(newTA);
                                    dal.addTripSerializedTA(trip.getId(), taJsonString);
                                    finish();
                                }
                            }
                        }
                    });

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCollaboratorActivity.this, CollaboratorsActivity.class);
                startActivity(intent);
            }
        });

    }

    // Input validation
    private void showError(EditText input, String missing_information) {
        input.setError(missing_information);
        input.requestFocus();
    }


}