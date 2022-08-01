package sg.edu.np.mad.assignment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewActivity extends BottomSheetDialogFragment {
    DALTrip dal = new DALTrip();

    public static final String TAG = "AddNewActivity";

    private TextView setDueDate;
    private TextView setDueTime;
    private TextView textView2Edit;
    private EditText mActivityEdit;
    private EditText mVenueEdit;
    private EditText mAddressEdit;
    private Button mSaveBtn;
    private FirebaseFirestore firestore;
    private Context context;
    private String dueDate = "";
    private String dueTime = "";
    private String id = "";
    private String dueDateUpdate = "";
    private String TripId, currTripName, currTripDest, currTripsDate, currTripeDate;
    public ArrayList<EditHistory> currentEHList;
    String editByUserName = "";
    private String editLog = "";
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaydate;
    private int tHour,tMinute;
    private String dueTimeUpdate = "";
    private String location;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public static AddNewActivity newInstance(){
        return new AddNewActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AddActivityMain parentClass = (AddActivityMain)getActivity();
        TripId = parentClass.TripId;
        currTripName = parentClass.currTripName;
        currTripDest = parentClass.currTripDest;
        currTripsDate = parentClass.currTripsDate;
        currTripeDate = parentClass.currTripeDate;
        currentEHList = parentClass.currentEditHistoryList;
        return inflater.inflate(R.layout.add_new_activity, container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDueDate = view.findViewById(R.id.set_due_tv);
        setDueTime = view.findViewById(R.id.time_picker);
        mActivityEdit = view.findViewById(R.id.task_edittextactivity);
        textView2Edit = view.findViewById(R.id.textView2);
        mVenueEdit = view.findViewById(R.id.venue_edittext);
        mAddressEdit = view.findViewById(R.id.Address_editText);
        mSaveBtn = view.findViewById(R.id.save_btn);

        //Initialize places
        Places.initialize(context.getApplicationContext(),"AIzaSyDKV4d5j6mwRwKiZzeF7L1Cw45qL8P5_Qs");
        mVenueEdit.setFocusable(false);
        mVenueEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,Place.Field.NAME);

                //create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN
                ,fieldList).build(view.getContext());

                //Start activity result
                startActivityForResult(intent,100);

            }
        });

        firestore = FirebaseFirestore.getInstance();

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String activity = bundle.getString("Activity");
            String Venue = bundle.getString("Venue");
            String Address = bundle.getString("Address");
            id = bundle.getString("id");
            dueDateUpdate = bundle.getString("due");
            dueTimeUpdate = bundle.getString("time");

            mActivityEdit.setText(activity);
            mVenueEdit.setText(Venue);
            mAddressEdit.setText(Address);
            setDueDate.setText(dueDateUpdate);
            setDueTime.setText(dueTimeUpdate);

            if (Venue.isEmpty()|| activity.isEmpty()|| Address.isEmpty()){
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }
        }

        mActivityEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.toString().equals("")){
                   mSaveBtn.setEnabled(false);
                   mSaveBtn.setBackgroundColor(Color.GRAY);
               }else{
                   mSaveBtn.setEnabled(true);
                   mSaveBtn.setBackgroundColor(getResources().getColor(R.color.dark_blue));
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        setDueDate.setText(dayOfMonth + "/" + month + "/" + year);
                        dueDate = dayOfMonth + "/" + month +"/"+year;

                    }
                } , YEAR , MONTH , DAY);

                datePickerDialog.show();
            }
        });

        setDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        tHour = hourOfDay;
                        tMinute = minute;
                        String time = tHour + ":" + tMinute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24Hours.parse(time);
                            // Initialize 12 hours time format
                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                            // Set selected time on textview
                            setDueTime.setText(f12Hours.format(date));
                            String timeadd = tHour + ":" + tMinute;
                            dueTime = timeadd;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },12,0,false
                );
                // set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Displayed previous selected time
                timePickerDialog.updateTime(tHour,tMinute);
                //show dialog
                timePickerDialog.show();
            }
        });

        // Get user data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                editByUserName = task.getResult().getString("username");    // Find and set username of current user
            }
        });

        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String activity = mActivityEdit.getText().toString();
                String Venue = mVenueEdit.getText().toString();
                String Address = mAddressEdit.getText().toString();

                if (finalIsUpdate){
                    // Update itinerary item
                    firestore.collection("Activity").document(id).update("Activity" , activity , "due" , dueDate, "Venue", Venue, "Address", Address, "Location", location,"time",dueTime);

                    if (!bundle.getString("Activity").equals(activity)){
                        editLog += "Activity name updated from '" + bundle.getString("Activity") + "' to '" + activity + "'.";
                    }
                    if (!bundle.getString("Venue").equals(Venue)){
                        editLog += "Activity venue updated from '" + bundle.getString("Venue") + "' to '" + Venue + "'.";
                    }
                    if (!bundle.getString("Address").equals(Address)){
                        editLog += "Activity Address updated from '" + bundle.getString("Address") + "' to '" + Address + "'.";
                    }


                    // Get date and time of when trip is created
                    calendar = Calendar.getInstance();
                    dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    todaydate = dateFormat.format(calendar.getTime());

                    EditHistory newEH = new EditHistory(uid, editByUserName, todaydate, editLog);

                    Gson gson = new Gson();
                    String ehJsonString = gson.toJson(newEH);

                    currentEHList.add(newEH);
                    dal.updateTrip(TripId, TripId, currTripDest, currTripsDate, currTripeDate, currentEHList, ehJsonString);

                    dal.updateTripEditHistory(newEH, TripId);

                    Toast.makeText(context, "Activity Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (activity.isEmpty()) {
                        Toast.makeText(context, "Empty Activity not Allowed !", Toast.LENGTH_SHORT).show();
                    } else {

                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("TripId", TripId);
                        taskMap.put("Activity", activity);
                        taskMap.put("Venue", Venue);
                        taskMap.put("Address", Address);
                        taskMap.put("due", dueDate);
                        taskMap.put("Location", location);
                        taskMap.put("time", dueTime);
                        taskMap.put("status", 0);

                        firestore.collection("Activity").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> activity) {
                                if (activity.isSuccessful()) {
                                    Toast.makeText(context, "Activity Saved", Toast.LENGTH_SHORT).show();

                                    EditHistory newEH = new EditHistory(uid, editByUserName, todaydate, String.format("‣ Activity Created"));
                                    dal.updateTripEditHistory(newEH, TripId);
                                } else {
                                    Toast.makeText(context, activity.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK){
            //When success
            //Initialize Place
            Place place = Autocomplete.getPlaceFromIntent(data);
            mAddressEdit.setText(place.getAddress());
            mVenueEdit.setText(String.format(place.getName()));
            location = String.valueOf(place.getLatLng());

            try{
                firestore.collection("Activity").document(id).update("Location" , location);
            }catch(Exception e){
                location = String.valueOf(place.getLatLng());
            }

        }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            // When error
            // Initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(context.getApplicationContext(), status.getStatusMessage()
            , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
