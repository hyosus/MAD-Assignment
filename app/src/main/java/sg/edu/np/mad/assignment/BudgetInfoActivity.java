package sg.edu.np.mad.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.assignment.Adapter.BudgetAdapter;
import sg.edu.np.mad.assignment.Adapter.FlightAdapter;
import sg.edu.np.mad.assignment.Model.Budget;
import sg.edu.np.mad.assignment.Model.FlightModel;

public class BudgetInfoActivity extends AppCompatActivity {


    RecyclerView recyclerview,recyclerviewbudget;
    FloatingActionButton floating,floatingbudget;
    FirebaseFirestore db;
    TimePickerDialog dialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String name;
    TextView textname;
    String ampm;
    ArrayList<FlightModel> flightModelArrayList;
    FlightModel tempModel;
    FlightAdapter adapter;
    BudgetAdapter budgetAdapter;
    ArrayList<Budget> budgetArrayList;
    Budget budgetmodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_info);
        floating = findViewById(R.id.floating);
        floatingbudget = findViewById(R.id.floatingbudget);
        recyclerview = findViewById(R.id.recyclerview);
        textname = findViewById(R.id.textname);
        recyclerviewbudget = findViewById(R.id.recyclerviewbudget);
        db = FirebaseFirestore.getInstance();
        name = getIntent().getStringExtra("name");
        textname.setText(name);
        recyclerview.setHasFixedSize(true);
        recyclerviewbudget.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewbudget.setLayoutManager(new LinearLayoutManager(this));
        flightModelArrayList = new ArrayList<>();
        budgetArrayList = new ArrayList<>();
        adapter = new FlightAdapter(BudgetInfoActivity.this,flightModelArrayList);
        budgetAdapter = new BudgetAdapter(BudgetInfoActivity.this,budgetArrayList);
        recyclerview.setAdapter(adapter);
        recyclerviewbudget.setAdapter(budgetAdapter);

        EventChangeListner();
        BudgetChangeListner();

        //back button
        ImageView backBtn = findViewById(R.id.bckbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetInfoActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        //swipe to delete
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        };



        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(BudgetInfoActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.dialogcontentwrittensermone))
                        .setExpanded(true, 1350)
                        .create();

                View myView = dialogPlus.getHolderView();
                EditText namee = myView.findViewById(R.id.editname);
                EditText From = myView.findViewById(R.id.editfrom);
                EditText To = myView.findViewById(R.id.editto);
                EditText Time = myView.findViewById(R.id.edittime);
                EditText Time1 = myView.findViewById(R.id.edittime1);
                EditText Trip = myView.findViewById(R.id.edittrip);
                CardView cardsave = myView.findViewById(R.id.cardsave);

                Time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);
                        dialog = new TimePickerDialog(BudgetInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                if (i>=12){
                                    ampm = "PM";
                                }
                                else {
                                    ampm = "AM";
                                }

                                Time.setText(i + ":" + i1 + ampm);
                            }
                        },currentHour,currentMinute,false);
                        dialog.show();
                    }
                });

                Time1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        currentMinute = calendar.get(Calendar.MINUTE);
                        dialog = new TimePickerDialog(BudgetInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                if (i>=12){
                                    ampm = "PM";
                                }
                                else {
                                    ampm = "AM";
                                }

                                Time1.setText(i + ":" + i1 + ampm);
                            }
                        },currentHour,currentMinute,false);
                        dialog.show();
                    }
                });

                dialogPlus.show();


                cardsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("flighttitle", namee.getText().toString());
                        map.put("from", From.getText().toString());
                        map.put("to", To.getText().toString());
                        map.put("time", Time.getText().toString());
                        map.put("timedep", Time1.getText().toString());
                        map.put("tripname", Trip.getText().toString());
//                        map.put("TripId", TripId); //just edited

                        db.collection("FlightDetails").document(namee.getText().toString()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(BudgetInfoActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NotNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        floatingbudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(BudgetInfoActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.dialog_budget))
                        .setExpanded(true, 1300)
                        .create();

                View myView = dialogPlus.getHolderView();
                EditText editbudget = myView.findViewById(R.id.editbudget);
                EditText editexpense = myView.findViewById(R.id.editexpense);
                EditText edittripname = myView.findViewById(R.id.edittripname);
                CardView cardsave = myView.findViewById(R.id.cardsave);

                dialogPlus.show();


                cardsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("budget", editbudget.getText().toString());
                        map.put("expense", editexpense.getText().toString());
                        map.put("tripname", edittripname.getText().toString());


                        db.collection("BudgetDetals").document(editbudget.getText().toString()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(BudgetInfoActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NotNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });
    }

    private void BudgetChangeListner() {
        db.collection("BudgetDetals").orderBy("budget", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                budgetmodel =dc.getDocument().toObject(Budget.class);
                                if (budgetmodel.getTripname().equals(name)){
                                    budgetArrayList.add(budgetmodel);
                                }
                            }
                            budgetAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void EventChangeListner() {
        db.collection("FlightDetails").orderBy("flighttitle", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){

                                tempModel = dc.getDocument().toObject(FlightModel.class);
                                if (tempModel.getTripname().equals(name)) {
                                    flightModelArrayList.add(tempModel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }



}