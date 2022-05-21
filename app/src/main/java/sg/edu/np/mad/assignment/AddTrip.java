package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTrip extends AppCompatActivity {
    private ImageView back, save;
    ArrayList<Trip> tripList = new ArrayList<Trip>();

    final Calendar startCalendar= Calendar.getInstance();
    final Calendar endCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        final EditText name = findViewById(R.id.nameTxt);
        final EditText sd = findViewById(R.id.startTxt);
        final EditText ed = findViewById(R.id.endTxt);
        final EditText dest = findViewById(R.id.destinationTxt);

        back = findViewById(R.id.backBtn);

        // Send user back to home screen
        back.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                startActivity(Intent);
            }
        }));


        save = findViewById(R.id.saveBtn);

        DALTrip dalTrip = new DALTrip();

        // Save user input
        save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = name.getText().toString();
                String destination = dest.getText().toString();
                String sDate = sd.getText().toString();
                String eDate = ed.getText().toString();

                if (title.isEmpty()){
                    showError(name, "Missing information");
                }
                else if (destination.isEmpty()) {
                    showError(dest, "Missing information");
                }
                else if (sd == null){
                    showError(sd, "Missing information");
                }
                else if (eDate.isEmpty()){
                    showError(ed, "Missing information");
                }
                else if (eDate.compareTo(sDate)<0) {
                    showError(ed, "End date cannot be before Start date");
                }
                else {
                    DALTrip dalTrip = new DALTrip();
                    Trip trip = new Trip(dest.getText().toString(), sd.getText().toString(), ed.getText().toString(), name.getText().toString());
                    dalTrip.createTrip(trip);
                    finish();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                    Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                    startActivity(Intent);
                }


            }
        }));

        // Initiate date picker
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH,month);
            startCalendar.set(Calendar.DAY_OF_MONTH,day);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/M/yyy");
            sd.setText(dateFormat.format(startCalendar.getTime()));
        };

        DatePickerDialog.OnDateSetListener enddate = (view, year, month, day) -> {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH,month);
            endCalendar.set(Calendar.DAY_OF_MONTH,day);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/M/yyy");
            ed.setText(dateFormat.format(endCalendar.getTime()));
        };

//         Start date
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, date,startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//         End date
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, enddate,endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    private void showError(EditText input, String missing_information) {
        input.setError(missing_information);
        input.requestFocus();
    }

}