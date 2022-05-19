package sg.edu.np.mad.assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.myviewholder>
{
    ArrayList<Trip> dataHolder;

    public TripAdapter(ArrayList<Trip> dataHolder) {
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

    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView title;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bgVH);
            title = itemView.findViewById(R.id.titleVH);

        }
    }
}
