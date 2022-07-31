package sg.edu.np.mad.assignment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.assignment.Adapter.AddActivityAdapter;
import sg.edu.np.mad.assignment.Model.ActivityModel;

public class AddActivityMain extends AppCompatActivity implements sg.edu.np.mad.assignment.OnDialogCloseListner {

    private RecyclerView recyclerView;
    private FloatingActionButton mFab;
    private FirebaseFirestore firestore;
    private AddActivityAdapter adapter;
    private List<ActivityModel> mList;
    private Query query;
    private ListenerRegistration listenerRegistration;
    public String TripId;
    private ImageView helpBtn;
    private Dialog mdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity_main);

        //intent
        Intent intent = getIntent();

        recyclerView = findViewById(R.id.recycerlview);
        ImageView backBtn = findViewById(R.id.backBtn2);
        mFab = findViewById(R.id.floatingActionButton);
        firestore = FirebaseFirestore.getInstance();
        helpBtn = findViewById(R.id.helpBtn);
        mdialog = new Dialog(this);

        TextView header = findViewById(R.id.tripNameTxt);
        TextView date = findViewById(R.id.datesTxt);
        TextView datestart= findViewById(R.id.datestart);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddActivityMain.this));

        Trip trip = (Trip)getIntent().getSerializableExtra("tripDetails");
        TripId = trip.getId();

        if (trip != null) {
            header.setText(trip.getTripName());
            date.setText(trip.getStartDate() + " - " + trip.getEndDate());
            datestart.setText(trip.getStartDate());

            String dateNoSlash = date.getText().toString();
            String startdateNoSlash = datestart.getText().toString();

            // Omit slashes in date
            if (dateNoSlash.contains("/")){
                dateNoSlash = dateNoSlash.replace("/", " ");
                startdateNoSlash = startdateNoSlash.replace("/", " ");
                date.setText(dateNoSlash);
                datestart.setText(startdateNoSlash);
            }
        }
        // back button to return to Home
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivityMain.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        // When user click any part of the trip image, it will create/bring you to the activity page
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewActivity.newInstance().show(getSupportFragmentManager() , AddNewActivity.TAG);
            }
        });

        // When user click on ? icon, it will display a popup window on how to use the feature
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.setContentView(R.layout.popup_window);

                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.show();
            }
        });

        mList = new ArrayList<>();
        adapter = new AddActivityAdapter(AddActivityMain.this , mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
        recyclerView.setAdapter(adapter);
    }


    // Show data on page
    private void showData(){
        // Sorted by time ascending as earliest activity should be at the top
       query = firestore.collection("Activity").orderBy("time" , Query.Direction.ASCENDING);

       listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        ActivityModel activityModel = documentChange.getDocument().toObject(ActivityModel.class).withId(id);
                        if (TripId.equals(activityModel.getTripId())){
                            mList.add(activityModel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                listenerRegistration.remove();

            }
        });
    }
    // When complete, show data and clear list, and update adapter on data change (if any)
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}