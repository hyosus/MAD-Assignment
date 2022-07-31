package sg.edu.np.mad.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.rpc.ErrorInfoOrBuilder;

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
    public String TripId, currTripName, currTripDest, currTripsDate, currTripeDate;
    ArrayList<EditHistory> currentEditHistoryList;
    private ImageView menu;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Trip> dataHolder;

    public Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity_main);


        ImageView backBtn = findViewById(R.id.backBtn2);
        menu = findViewById(R.id.addActivityMenu);
        mFab = findViewById(R.id.collabAddBtn);
        mFab.setVisibility(View.GONE);
        firestore = FirebaseFirestore.getInstance();

        TextView header = findViewById(R.id.tripNameTxt);
        TextView date = findViewById(R.id.datesTxt);


        Trip trip = (Trip)getIntent().getSerializableExtra("tripDetails");


        if (trip != null) {
            currentEditHistoryList = trip.EditHistoryList;
            currTripName = trip.getTripName();
            currTripDest = trip.getDestination();
            currTripsDate = trip.getStartDate();
            currTripeDate = trip.getEndDate();
            TripId = trip.getId();
            header.setText(trip.getTripName());
            date.setText(trip.getStartDate() + " - " + trip.getEndDate());
//            Toast.makeText(this, "is this working", Toast.LENGTH_SHORT).show();

            String dateNoSlash = date.getText().toString();

            // Omit slashes in date
            if (dateNoSlash.contains("/")){
                dateNoSlash = dateNoSlash.replace("/", " ");
                date.setText(dateNoSlash);
            }
        }

        // Menu popout
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

                MenuItem editItem = popupMenu.getMenu().findItem(R.id.editMenu);
                MenuItem deleteItem = popupMenu.getMenu().findItem(R.id.delMenu);

                // If is not owner of trip
                if (!uid.equals(trip.getUserId()))
                {
                    // user cant delete trip
                    deleteItem.setVisible(false);
                }

                db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      for (DocumentSnapshot doc : task.getResult()) {
                          if (trip.getId().equals(doc.getString("id"))) {
                              if (task.isSuccessful()) {
                                  ArrayList<String> sharedTripLists = new ArrayList<String>();
                                  ArrayList<String> stalist = new ArrayList<String>();
                                  stalist = (ArrayList<String>) doc.get("serializedTAL");

                                  if (uid.equals(doc.getString("userId"))){
                                      editItem.setVisible(true);
                                  }
                                  else{
                                      editItem.setVisible(false);
                                  }

                                  for (int i=0; i<stalist.size(); i++){
                                      Gson gson = new Gson();
                                      TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);


                                      if (tempTa.getUserId().equals(uid)){
                                          if (tempTa.getPermission().equals("Can Edit")){
                                              editItem.setVisible(true);
                                          }
                                          else {
                                              editItem.setVisible(false);
                                          }
                                      }
                                  }
                              }
                          }
                      }
                    }
                });


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.shareMenu:
                                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                        .setLink(Uri.parse("https://www.example.com/?tripid=" + trip.getTripName()))
                                        .setDomainUriPrefix("https://madtripify.page.link")
                                        // Open links with this app on Android
                                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("sg.edu.np.mad.assignment").setMinimumVersion(1).build())
                                        .buildDynamicLink();

                                Uri dynamicLinkUri = dynamicLink.getUri();

                                Intent myIntent = new Intent(Intent.ACTION_SEND);
                                myIntent.setType("text/plain");
                                String body = "Follow me on my trip! ";

                                myIntent.putExtra(Intent.EXTRA_TEXT,body + String.valueOf(dynamicLinkUri));
                                view.getContext().startActivity(Intent.createChooser(myIntent, "Share Using"));

                                break;

                            case R.id.editMenu:
                                Intent intent=new Intent(view.getContext(),AddTrip.class);
                                intent.putExtra("EDIT", trip);
                                view.getContext().startActivity(intent);
                                finish();
                                break;

                            case R.id.collabMenu:
                                Intent cIntent=new Intent(view.getContext(),CollaboratorsActivity.class);
                                cIntent.putExtra("tripDetails", trip);
                                cIntent.putExtra("tripId", uid);
                                view.getContext().startActivity(cIntent);
                                finish();
                                break;

                            case R.id.verHistMenu:
                                Intent vIntent=new Intent(view.getContext(),VersionHistoryActivity.class);
                                vIntent.putExtra("tripId", trip.getId());
                                startActivity(vIntent);
                                break;

                            case R.id.delMenu:
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Delete this trip?")
                                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // delete itinerary
                                                db.collection("Activity").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        for (DocumentSnapshot aDoc : task.getResult()) {
                                                            if (aDoc.getString("TripId").equals(trip.getId())){
                                                                db.collection("Activity").document(aDoc.getId()).delete();
                                                            }
                                                        }
                                                    }
                                                });

                                                db.collection("Trip").document(trip.getId()).delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            finish();
                                                            Log.d("DeleteTrip", "DocumentSnapshot successfully deleted!");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure( Exception e) {
                                                            Log.w("DeleteTrip", "Error deleting document", e);
                                                        }
                                                    });
                                            }

                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();

                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivityMain.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sg.edu.np.mad.assignment.AddNewActivity.newInstance().show(getSupportFragmentManager() , sg.edu.np.mad.assignment.AddNewActivity.TAG);
            }
        });

        mList = new ArrayList<>();
        adapter = new AddActivityAdapter(AddActivityMain.this , mList);

        recyclerView = findViewById(R.id.recycerlview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddActivityMain.this));

        db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()){
                    if (trip.getId().equals(doc.getString("id"))){
                        if (uid.equals(doc.getString("userId")))
                        {
                            mFab.setVisibility(View.VISIBLE);
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new sg.edu.np.mad.assignment.TouchHelper(adapter));
                            itemTouchHelper.attachToRecyclerView(AddActivityMain.this.recyclerView);
                            return;
                        }
                        else
                        {
                            // cant edit trip
                            mFab.setVisibility(View.GONE);
                        }
                        if (task.isSuccessful()) {
                            ArrayList<String> sharedTripLists = new ArrayList<String>();
                            ArrayList<String> stalist = new ArrayList<String>();
                            stalist = (ArrayList<String>) doc.get("serializedTAL");
                            mFab.setVisibility(View.GONE);
                            for (int i=0; i<stalist.size(); i++){
                                Gson gson = new Gson();
                                TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                                sharedTripLists.add(tempTa.userId);
                                if ((tempTa.getUserId().equals(uid) && tempTa.permission.equals("Can Edit")))
                                {
                                    mFab.setVisibility(View.VISIBLE);
                                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new sg.edu.np.mad.assignment.TouchHelper(adapter));
                                    itemTouchHelper.attachToRecyclerView(AddActivityMain.this.recyclerView);
                                }
                            }
                        }
                    }
                }
            }
        });


        recyclerView.setAdapter(adapter);
        showData();
    }
    private void showData(){
        try {
            query = firestore.collection("Activity").orderBy("time" , Query.Direction.DESCENDING);

            listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange documentChange : value.getDocumentChanges()){
                        if (documentChange.getType() == DocumentChange.Type.ADDED){
                            String id = documentChange.getDocument().getId();
                            ActivityModel activityModel = documentChange.getDocument().toObject(ActivityModel.class).withId(id);
                            try {
                                if (TripId.equals(activityModel.getTripId())){
                                    mList.add(activityModel);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            catch (Error e){}

                        }
                    }
                    listenerRegistration.remove();
                }
            });
        }
        catch (Error e){}
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}