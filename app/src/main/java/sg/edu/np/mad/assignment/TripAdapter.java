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
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        //Omit slashes in date
        String dateNoSlash = holder.duration.getText().toString();

        if (dateNoSlash.contains("/")){
            dateNoSlash = dateNoSlash.replace("/", " ");
            holder.duration.setText(dateNoSlash);
        }

        // Set time left till start of trip
        // get current date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
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


                // Set collab menu item to invisible
                MenuItem collabItem = popupMenu.getMenu().findItem(R.id.collabMenu);
                collabItem.setVisible(false);

                // Set edit menu to invisible if user got no permission to edit
                MenuItem editItem = popupMenu.getMenu().findItem(R.id.editMenu);

                if (uid.equals(trip.getUserId()))
                {
                    editItem.setVisible(true);
                }
                else
                {
                    editItem.setVisible(false);

                }

                // When selecting item menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.shareMenu:
                                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                        .setLink(Uri.parse("https://www.example.com&d=1"))
                                        .setDomainUriPrefix("https://madtripify.page.link")
                                        // Open links with this app on Android
                                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("sg.edu.np.mad.assignment")
                                                .setMinimumVersion(1).build()).buildDynamicLink();

                                Uri dynamicLinkUri = dynamicLink.getUri();

//                                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                                        .setLink(Uri.parse("https://www.example.com/"))
//                                        .setDomainUriPrefix("https://example.page.link")
//                                        // Set parameters
//                                        // ...
//                                        .buildShortDynamicLink()
//                                        .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                                                if (task.isSuccessful()) {
//                                                    // Short link created
//                                                    Uri shortLink = task.getResult().getShortLink();
//                                                    Uri flowchartLink = task.getResult().getPreviewLink();
//                                                } else {
//                                                    // Error
//                                                    // ...
//                                                }
//                                            }
//                                        });

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

    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img, timeIcon, menu;
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

        }
    }

}
