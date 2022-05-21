package sg.edu.np.mad.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.myviewholder>
{
    TripsFragment tripsFragment;
    List<Trip> dataHolder;
    Context context;

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

        // get start and end date in a single string
        String dateStr = dataHolder.get(position).getStartDate() + " - " + dataHolder.get(position).getEndDate();

        holder.date.setText(dateStr);

        String startD = dataHolder.get(position).getStartDate();

    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView title, date, daysLeft;

        public myviewholder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bgVH);
            title = itemView.findViewById(R.id.titleVH);
            date = itemView.findViewById(R.id.dateVH);
            daysLeft = itemView.findViewById(R.id.daysLeftVH);

        }
    }
}
