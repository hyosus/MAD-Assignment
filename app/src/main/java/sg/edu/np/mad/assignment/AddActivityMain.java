package sg.edu.np.mad.assignment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
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
    private ImageView menu;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Trip> dataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity_main);

        recyclerView = findViewById(R.id.recycerlview);
        ImageView backBtn = findViewById(R.id.backBtn2);
        menu = findViewById(R.id.addActivityMenu);
        mFab = findViewById(R.id.floatingActionButton);
        firestore = FirebaseFirestore.getInstance();

        TextView header = findViewById(R.id.tripNameTxt);
        TextView date = findViewById(R.id.datesTxt);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddActivityMain.this));

        Trip trip = (Trip)getIntent().getSerializableExtra("tripDetails");


        if (trip != null) {
            TripId = trip.getId();
            header.setText(trip.getTripName());
            date.setText(trip.getStartDate() + " - " + trip.getEndDate());

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

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.shareMenu:
                                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                        .setLink(Uri.parse("https://www.example.com"))
                                        .setDomainUriPrefix("https://madtripify.page.link")
                                        // Open links with this app on Android
                                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("sg.edu.np.mad.assignment")
                                                .setMinimumVersion(1).build()).buildDynamicLink();

                                Uri dynamicLinkUri = dynamicLink.getUri();

                                Log.v("CHIBAI", String.valueOf(dynamicLinkUri));

                                Intent myIntent = new Intent(Intent.ACTION_SEND);
                                myIntent.setType("text/plain");
                                String body = "Follow me on my trip! ";
//                                String sub = "Your Subject";
//                                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                                myIntent.putExtra(Intent.EXTRA_TEXT,body + String.valueOf(dynamicLinkUri));
                                view.getContext().startActivity(Intent.createChooser(myIntent, "Share Using"));


                                break;
                            case R.id.editMenu:
                                Intent intent=new Intent(view.getContext(),AddTrip.class);
                                intent.putExtra("EDIT", trip);
                                view.getContext().startActivity(intent);

                                break;

                            case R.id.collabMenu:
                                Intent cIntent=new Intent(view.getContext(),CollaboratorsActivity.class);
                                cIntent.putExtra("tripDetails", trip);
                                view.getContext().startActivity(cIntent);

                                break;

                            case R.id.delMenu:
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Delete this trip?")
                                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                db.collection("Trip").document(trip.getId())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Intent dIntent = new Intent(AddActivityMain.this, HomeActivity.class);
                                                                startActivity(dIntent);
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

        // Handle firebase deep link
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.w("linktest", "getDynamicLink:success");
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("linktest", "getDynamicLink:onFailure", e);
                    }
                });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivityMain.this,HomeActivity.class);
                startActivity(intent);
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new sg.edu.np.mad.assignment.TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
        recyclerView.setAdapter(adapter);
    }
    private void showData(){
       query = firestore.collection("Activity").orderBy("time" , Query.Direction.DESCENDING);

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

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}