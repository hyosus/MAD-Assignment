package sg.edu.np.mad.assignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTrip extends AppCompatActivity {
    private ImageView back, save;
    private EditText name, dest, sd, ed;


    final Calendar startCalendar= Calendar.getInstance();
    final Calendar endCalendar= Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

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
        name = findViewById(R.id.nameTxt);
        sd = findViewById(R.id.startTxt);
        ed = findViewById(R.id.endTxt);
        dest = findViewById(R.id.destinationTxt);

        // Save user input
        save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
//                Trips t = new Trips(dest.toString(), sd.toString(), ed.toString(), name.toString());
//                Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
//                startActivity(Intent);
            }
        }));

        //  Initiate DatePicker
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH,month);
            startCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };

        DatePickerDialog.OnDateSetListener enddate = (view, year, month, day) -> {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH,month);
            endCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };

        // Start date
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, date,startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // End date
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, enddate,endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void validation() {
        String title = name.getText().toString();
        String destination = dest.getText().toString();
        String sDate = sd.getText().toString();
        String eDate = ed.getText().toString();

        if (title.isEmpty() ){
            showError(name, "Missing information");
        }
        else if (destination.isEmpty()) {
            showError(dest, "Missing information");
        }
        else if (sDate.isEmpty()){
            showError(sd, "Missing information");
        }
        else if (eDate.isEmpty() || eDate.compareTo(sDate)<0){
            showError(ed, "Missing information");
            Log.v("testdate", "end date before start date");
        }
        else {
            Log.v("testdate", sDate+eDate);
            Trips t = new Trips(destination,sDate, eDate,title);
            Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
            startActivity(Intent);
        }
    }

    private void showError(EditText input, String missing_information) {
        input.setError(missing_information);
        input.requestFocus();
    }

    private void updateLabel(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyy");
        sd.setText(dateFormat.format(startCalendar.getTime()));
        ed.setText(dateFormat.format(endCalendar.getTime()));
    }
}