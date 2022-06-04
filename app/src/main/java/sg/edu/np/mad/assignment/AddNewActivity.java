package sg.edu.np.mad.assignment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewActivity extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewActivity";

    private TextView setDueDate;
    private EditText mActivityEdit;
    private EditText mVenueEdit;
    private EditText mAddressEdit;
    private Button mSaveBtn;
    private FirebaseFirestore firestore;
    private Context context;
    private String dueDate = "";
    private String id = "";
    private String dueDateUpdate = "";

    public static AddNewActivity newInstance(){
        return new AddNewActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_activity, container , false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate = view.findViewById(R.id.set_due_tv);
        mActivityEdit = view.findViewById(R.id.activity_edittext);
        mVenueEdit = view.findViewById(R.id.venue_edittext);
        mAddressEdit = view.findViewById(R.id.Address_editText);
        mSaveBtn = view.findViewById(R.id.save_btn);

        firestore = FirebaseFirestore.getInstance();

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String activity = bundle.getString("activity");
            String venue = bundle.getString("venue");
            String address = bundle.getString("address");
            id = bundle.getString("id");
            dueDateUpdate = bundle.getString("due");

            mActivityEdit.setText(activity);
            mVenueEdit.setText(venue);
            mAddressEdit.setText(address);
            setDueDate.setText(dueDateUpdate);

            if (activity.length() > 0){
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }
        }
        // Edit changes listener
        mActivityEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            // Users will be able to see the changes on edit
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
        // We are creating an on click listener for our Date time picker
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
        // On save button on click listener
        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Activity = mActivityEdit.getText().toString();
                String Venue = mVenueEdit.getText().toString();
                String Address = mAddressEdit.getText().toString();

                // saving activity name, date, venue and address to firebase
                if (finalIsUpdate){
                    firestore.collection("Activity").document(id).update(
                            "Activity" , Activity ,
                            "due" , dueDate,
                            "Venue", Venue,
                            "Address", Address
                    );
                    Toast.makeText(context, "Activity Updated", Toast.LENGTH_SHORT).show();

                }
                else {
                    // Error toast message if no input in Activity name
                    if (Activity.isEmpty()) {
                        Toast.makeText(context, "Empty Activity not Allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        // Adding data to firebase
                        Map<String, Object> ActivityMap = new HashMap<>();

                        ActivityMap.put("Activity", Activity);
                        ActivityMap.put("Venue", Venue);
                        ActivityMap.put("Address", Address);
                        ActivityMap.put("due", dueDate);
                        ActivityMap.put("status", 0);
                        ActivityMap.put("time", FieldValue.serverTimestamp());

                        firestore.collection("Activity").add(ActivityMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> Activity) {
                                if (Activity.isSuccessful()) {
                                    Toast.makeText(context, "Activity Saved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, Activity.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
