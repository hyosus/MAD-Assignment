package sg.edu.np.mad.assignment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.myviewholder>
{
    TripsFragment tripsFragment;
    List<Trip> dataHolder;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        holder.title.setText(dataHolder.get(position).getTripName());

        // get start and end duration in a single string
        String durationStr = dataHolder.get(position).getStartDate() + " - " + dataHolder.get(position).getEndDate();
        holder.duration.setText(durationStr);

        // get current date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/M/yyyy");
        todaydate = dateFormat.format(calendar.getTime());

        try {
            Date today = dateFormat.parse(todaydate);
            Date startdate = dateFormat.parse(dataHolder.get(position).getStartDate());
            Date enddate = dateFormat.parse(dataHolder.get(position).getEndDate());

            long difference = Math.abs(startdate.getTime() - today.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);

            if (today.after(startdate) && today.before(enddate) || today.equals(startdate))
            {
                holder.daysLeft.setVisibility(View.GONE);
                holder.timeIcon.setVisibility(View.GONE);
            }

            holder.daysLeft.setText(dayDifference + " day(s)");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //  Intent to trip details/itinerary
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TripDetails.class);
                intent.putExtra("tripKey", dataHolder.get(holder.getAdapterPosition()).getTripName());
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

                // When selecting item menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.editMenu:
                                Toast.makeText(view.getContext(), "Edit selected", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.delMenu:
                                Toast.makeText(view.getContext(), "Delete selected", Toast.LENGTH_SHORT).show();
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
        TextView title, duration, daysLeft;

        public myviewholder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bgVH);
            timeIcon = itemView.findViewById(R.id.timeIconVH);
            title = itemView.findViewById(R.id.titleVH);
            duration = itemView.findViewById(R.id.dateVH);
            daysLeft = itemView.findViewById(R.id.daysLeftVH);
            menu = itemView.findViewById(R.id.menuVH);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // after clicking of the item of recycler view.
//                    // we are passing our course object to the new activity.
//                    Trip trips = dataHolder.get(getAdapterPosition());
//
//                    Intent i = new Intent(context, UpdateTrip.class);
//
//                    i.putExtra("trip", trips);
//
//                    context.startActivity(i);
//                }
//            });

        }
    }

    public void deleteTrip(){
        db.collection("Trip").document("DC")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }


    public void getID(){
        db.collection("Trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG2", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG2", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
