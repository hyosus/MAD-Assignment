package sg.edu.np.mad.assignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.myviewholder>
{
    TripsFragment tripsFragment;
    List<Trip> dataHolder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaydate;

    public TripAdapter(Context context, List<Trip> dataHolder) {
        this.tripsFragment = tripsFragment;
        this.dataHolder = dataHolder;
    }

    @Override
    public TripAdapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_view_holder, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(final TripAdapter.myviewholder holder, int position) {
        // Get position of trip
        Trip trip = dataHolder.get(position);

        // Set title
        holder.title.setText(trip.getTripName());

        // Set country/destination
        holder.country.setText(trip.getDestination());

        // Set date
        // get start and end duration in a single string
        String durationStr = dataHolder.get(position).getStartDate() + " - " + dataHolder.get(position).getEndDate();
        holder.duration.setText(durationStr);


        // Set time left till start of trip
        // get current date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("d MMM yyyy");
        todaydate = dateFormat.format(calendar.getTime());

        try {
            Date today = dateFormat.parse(todaydate);
            Date startdate = dateFormat.parse(dataHolder.get(position).getStartDate());
            Date enddate = dateFormat.parse(dataHolder.get(position).getEndDate());

            // Calculate difference between current date to start date
            long difference = Math.abs(startdate.getTime() - today.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);

            if (today.after(startdate) && today.before(enddate) || today.equals(startdate) || today.after(enddate))
            {
                holder.daysLeft.setVisibility(View.GONE);
                holder.timeIcon.setVisibility(View.GONE);
            }

            holder.daysLeft.setText(dayDifference + " day(s)");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //  Intent to trip details/itinerary
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddActivityMain.class);
                intent.putExtra("tripDetails",trip);
                view.getContext().startActivity(intent);
            }
        });

        // Menu popup
        holder.menu.setOnClickListener(new View.OnClickListener() {
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


                // When selecting item menu
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

                                break;

                            case R.id.collabMenu:
                                Intent cIntent=new Intent(view.getContext(),CollaboratorsActivity.class);
                                cIntent.putExtra("tripDetails", trip);
                                view.getContext().startActivity(cIntent);
                                break;

                            case R.id.verHistMenu:
                                Intent vIntent=new Intent(view.getContext(),VersionHistoryActivity.class);
                                vIntent.putExtra("tripId", trip.getId());
                                view.getContext().startActivity(vIntent);

                                break;

                            case R.id.delMenu:
                                new AlertDialog.Builder(view.getContext())
                                    .setMessage("Delete this trip?")
                                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (DocumentSnapshot doc : task.getResult()) {
                                                        if (trip.getId().equals(doc.getString("id"))) {
                                                            // delete trip
                                                            db.collection("Trip").document(doc.getId())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        dataHolder.remove(position);
                                                                        notifyItemRemoved(position);
                                                                        Log.d("DeleteTrip", "DocumentSnapshot successfully deleted!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure( Exception e) {
                                                                        Log.w("DeleteTrip", "Error deleting document", e);
                                                                    }
                                                                });

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
                                                        }
                                                    }
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

//        holder.menuinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(),BudgetInfoActivity.class);
//                intent.putExtra("name", trip.getTripName());
//                view.getContext().startActivity(intent);
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img, timeIcon, menu,menuinfo;
        TextView title, duration, daysLeft, country;
        ConstraintLayout container;

        public myviewholder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bgVH);
            timeIcon = itemView.findViewById(R.id.timeIconVH);
            title = itemView.findViewById(R.id.titleVH);
            country = itemView.findViewById(R.id.countryVH);
            duration = itemView.findViewById(R.id.dateVH);
            daysLeft = itemView.findViewById(R.id.daysLeftVH);
            menu = itemView.findViewById(R.id.menuVH);
            container = itemView.findViewById(R.id.container);
            menuinfo = itemView.findViewById(R.id.menuinfo);

        }
    }

}
