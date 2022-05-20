package sg.edu.np.mad.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.myviewholder>
{
    TripsFragment tripsFragment;
    List<Trip> dataHolder;
    Context context;

    public TripAdapter(TripsFragment tripsFragment, List<Trip> dataHolder) {
        this.tripsFragment = tripsFragment;
        this.dataHolder = dataHolder;
    }

    public TripAdapter(List<Trip> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_view_holder, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Trip t = dataHolder.get(position);
        holder.title.setText(dataHolder.get(position).getTripName());
        String dateStr = dataHolder.get(position).getStartDate() + dataHolder.get(position).getEndDate();
        holder.date.setText(dateStr);

    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView title, date;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bgVH);
            title = itemView.findViewById(R.id.titleVH);
            date = itemView.findViewById(R.id.dateVH);

        }
    }
}
