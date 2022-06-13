package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddTrip extends AppCompatActivity {
    private ImageView back;
    Button save;
    ArrayList<Trip> tripList = new ArrayList<Trip>();
    final Calendar startCalendar= Calendar.getInstance();
    final Calendar endCalendar= Calendar.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        final EditText name = findViewById(R.id.nameEdit);
        final EditText sd = findViewById(R.id.startEdit);
        final EditText ed = findViewById(R.id.endEdit);
        final EditText dest = findViewById(R.id.destEdit);
        TextView header = findViewById(R.id.headerTxt);
        save = findViewById(R.id.saveBtn);

        DALTrip dalTrip = new DALTrip();
        Trip trip_edit = (Trip)getIntent().getSerializableExtra("EDIT");

        // Edit existing trip
        if(trip_edit != null)
        {
            save.setText("UPDATE");
            header.setText("Update Trip");
            name.setText(trip_edit.getTripName());
            dest.setText(trip_edit.getDestination());
            sd.setText(trip_edit.getStartDate());
            ed.setText(trip_edit.getEndDate());
        }
        else
        {
            save.setText("SAVE");
            header.setText("Add Trip");
        }

        // Send user back to home screen
        back = findViewById(R.id.backBtn);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTrip.this);

        back.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = name.getText().toString();
                String destination = dest.getText().toString();
                String sDate = sd.getText().toString();
                String eDate = ed.getText().toString();

                if (title.isEmpty() == false || destination.isEmpty() == false || sDate.isEmpty() == false || eDate.isEmpty() == false){
                    builder.setMessage("Discard changes?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                            startActivity(Intent);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                    startActivity(Intent);
                }
            }
        }));
        
        // Country dropdown
        String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
                "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas",
                "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
                "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam",
                "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
                "Central African Republic", "Chad", "Chile", "China, People's republic of", "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
                "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire",
                "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
                "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
                "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana",
                "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar",
                "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
                "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
                "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
                "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kosovo", "Kuwait",
                "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya",
                "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar",
                "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius",
                "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat",
                "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
                "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands",
                "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Palestine", "Peru", "Philippines", "Pitcairn",
                "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda",
                "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
                "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore",
                "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
                "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon",
                "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
                "Taiwan", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Tibet", "Togo", "Tokelau", "Tonga",
                "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay",
                "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)",
                "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};

        dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialise dialog
                Dialog dialog = new Dialog(AddTrip.this);

                // Set customer dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // Set custom height and width
                dialog.getWindow().setLayout(800,1000);

                dialog.show();

                // Initialise and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView lv = dialog.findViewById(R.id.listView);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTrip.this,
                        android.R.layout.simple_list_item_1,countries);

                lv.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Set selected item on textview
                        dest.setText(adapter.getItem(i));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });


        // Save user input
        save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = name.getText().toString();
                String destination = dest.getText().toString();
                String sDate = sd.getText().toString();
                String eDate = ed.getText().toString();

                if (trip_edit == null)
                {
                    if (title.isEmpty()){
                        showError(name, "Missing information");
                    }
                    else if (destination.isEmpty()) {
                        showError(dest, "Missing information");
                    }
                    else if (sDate.isEmpty()){
                        Toast.makeText(AddTrip.this, "Missing Date", Toast.LENGTH_LONG).show();
                    }
                    else if (eDate.isEmpty()){
                        Toast.makeText(AddTrip.this, "Missing Date", Toast.LENGTH_LONG).show();
                    }
                    else if (eDate.compareTo(sDate)<0) {
                        Toast.makeText(AddTrip.this, "End Date cannot be before Start Date", Toast.LENGTH_LONG).show();
                    }
                    else {
                        DALTrip dalTrip = new DALTrip();
                        Trip trip = new Trip(dest.getText().toString(), sd.getText().toString(), ed.getText().toString(), name.getText().toString(), name.getText().toString());
                        dalTrip.createTrip(trip);
                        finish();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                        Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                        startActivity(Intent);
                    }
                }
                else
                {
                    if (title.isEmpty()){
                        showError(name, "Missing information");
                    }
                    else if (destination.isEmpty()) {
                        showError(dest, "Missing information");
                    }
                    else if (sDate.isEmpty()){
                        Toast.makeText(AddTrip.this, "Missing Date", Toast.LENGTH_LONG).show();
                    }
                    else if (eDate.isEmpty()){
                        Toast.makeText(AddTrip.this, "Missing Date", Toast.LENGTH_LONG).show();
                    }
                    else if (eDate.compareTo(sDate)<0) {
                        Toast.makeText(AddTrip.this, "End Date cannot be before Start Date", Toast.LENGTH_LONG).show();
                    }
                    else{
                        db.collection("Trip").document(trip_edit.getId()).update(
                                "tripName",title, "destination", destination,
                                "startDate", sDate, "endDate", eDate);

                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                        Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                        startActivity(Intent);
                    }

                }
            }
        }));

        // Initiate date picker
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH,month);
            startCalendar.set(Calendar.DAY_OF_MONTH,day);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/M/yyyy");
            sd.setText(dateFormat.format(startCalendar.getTime()));
        };

        DatePickerDialog.OnDateSetListener enddate = (view, year, month, day) -> {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH,month);
            endCalendar.set(Calendar.DAY_OF_MONTH,day);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/M/yyyy");
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