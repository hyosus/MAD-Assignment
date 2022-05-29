package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateTrip extends AppCompatActivity {
    private EditText tripNameEdit, destEdit, startDateEdit, endDateEdit;

    // creating a strings for storing our values from Edittext fields.
    private String name, dest, sd, ed;

    // creating a variable for firebasefirestore.
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);

        Trip trips = (Trip) getIntent().getSerializableExtra("trip");

        db = FirebaseFirestore.getInstance();

        tripNameEdit = findViewById(R.id.nameEdit);
        destEdit = findViewById(R.id.destEdit);
        startDateEdit = findViewById(R.id.startEdit);
        endDateEdit = findViewById(R.id.endEdit);

        Button update = findViewById(R.id.updateBtn);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = tripNameEdit.getText().toString();
                dest = destEdit.getText().toString();
                sd = startDateEdit.getText().toString();
                ed = endDateEdit.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(name)) {
                    tripNameEdit.setError("Please enter Trip Name");
//                } else if (TextUtils.isEmpty(courseDescription)) {
//                    courseDescriptionEdt.setError("Please enter Course Description");
//                } else if (TextUtils.isEmpty(courseDuration)) {
//                    courseDurationEdt.setError("Please enter Course Duration");
                } else {
                    // calling a method to update our course.
                    // we are passing our object class, course name,
                    // course description and course duration from our edittext field.
                    updateTrip(trips, name, dest, sd, ed);
                }
            }
        });

    }

    private void updateTrip(Trip courses, String name, String dest, String sDate, String eDate) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        Trip updatedCourse = new Trip(dest, sDate, eDate, name);

        // after passing data to object class we are
        // sending it to firebase with specific document id.
        // below line is use to get the collection of our Firebase Firestore.
        db.collection("Trip").
                // below line is use toset the id of
                // document where we have to perform
                // update operation.
                        document(courses.getId()).

                // after setting our document id we are
                // passing our whole object class to it.
                        set(updatedCourse).

                // after passing our object class we are
                // calling a method for on success listener.
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateTrip.this, "Trip has been updated..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(UpdateTrip.this, "Fail to update the data..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}