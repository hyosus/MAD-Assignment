package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

//    final Calendar startCalendar= Calendar.getInstance();
//    final Calendar endCalendar= Calendar.getInstance();


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

        // Get user input
        save = findViewById(R.id.saveBtn);


        // Save user input
        save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validation();
                DAOTrip dao = new DAOTrip();
                Trip trip = new Trip(dest.getText().toString(), sd.getText().toString(), ed.getText().toString(), name.getText().toString());
                dao.add(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });


//                Trips t = new Trips(dest.toString(), sd.toString(), ed.toString(), name.toString());
//                Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
//                startActivity(Intent);
            }
        }));

        //  Initiate DatePicker
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal= Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                // Picker dialog
                DatePickerDialog picker = new DatePickerDialog(AddTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                        sd.setText(day+"/"+(month+1)+"/"+year);

                    }
                }, year, month, day);
                picker.show();
            }
        });

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal= Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                // Picker dialog
                DatePickerDialog picker = new DatePickerDialog(AddTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                        ed.setText(day+"/"+(month+1)+"/"+year);

                    }
                }, year, month, day);
                picker.show();
            }
        });

//        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
//            startCalendar.set(Calendar.YEAR, year);
//            startCalendar.set(Calendar.MONTH,month);
//            startCalendar.set(Calendar.DAY_OF_MONTH,day);
//            updateLabel();
//        };
//
//        DatePickerDialog.OnDateSetListener enddate = (view, year, month, day) -> {
//            endCalendar.set(Calendar.YEAR, year);
//            endCalendar.set(Calendar.MONTH,month);
//            endCalendar.set(Calendar.DAY_OF_MONTH,day);
//            updateLabel();
//        };

        // Start date
//        sd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, date,startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });

        // End date
//        ed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, enddate,endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });

    }

//    private void validation() {
//        String title = name.getText().toString();
//        String destination = dest.getText().toString();
//        String sDate = sd.getText().toString();
//        String eDate = ed.getText().toString();
//
//        if (title.isEmpty() ){
//            showError(name, "Missing information");
//        }
//        else if (destination.isEmpty()) {
//            showError(dest, "Missing information");
//        }
//        else if (sDate.isEmpty()){
//            showError(sd, "Missing information");
//        }
//        else if (eDate.isEmpty() || eDate.compareTo(sDate)<0){
//            showError(ed, "Missing information");
//            Log.v("testdate", "end date before start date");
//        }
//        else {
//
//            Intent intent = new Intent(AddTrip.this, HomeActivity.class);
//            startActivity(intent);
//
//        }
//    }

//    private void showError(EditText input, String missing_information) {
//        input.setError(missing_information);
//        input.requestFocus();
//    }

//    private void updateLabel(){
//        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyy");
//        sd.setText(dateFormat.format(startCalendar.getTime()));
//        ed.setText(dateFormat.format(endCalendar.getTime()));
//    }
}