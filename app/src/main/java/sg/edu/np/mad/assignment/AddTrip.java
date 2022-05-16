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
    private ImageView back;
    final Calendar startCalendar= Calendar.getInstance();
    final Calendar endCalendar= Calendar.getInstance();

    EditText sd,ed;

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
        ImageView save = findViewById(R.id.saveBtn);
        TextView name = findViewById(R.id.nameTxt);
        sd = findViewById(R.id.startTxt);
        ed = findViewById(R.id.endTxt);
        TextView dest = findViewById(R.id.destinationTxt);

        save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trips t = new Trips(dest.toString(), sd.toString(), ed.toString(), name.toString());
                Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                startActivity(Intent);

                Log.v("test", t.toString());
            }
        }));

        //  Initiate DatePicker
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH,month);
            startCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, date,startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, AlertDialog.THEME_HOLO_LIGHT, date,endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel(){
        String myFormat="MM/dd/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        sd.setText(dateFormat.format(startCalendar.getTime()));
        ed.setText(dateFormat.format(endCalendar.getTime()));
    }
}