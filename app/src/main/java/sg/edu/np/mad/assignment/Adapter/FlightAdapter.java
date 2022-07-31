package sg.edu.np.mad.assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.assignment.Model.FlightModel;
import sg.edu.np.mad.assignment.R;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.MyViewHolder> {
    Context context;
    ArrayList<FlightModel> flightModelArrayList;


    public FlightAdapter(Context context, ArrayList<FlightModel> flightModelArrayList) {
        this.context = context;
        this.flightModelArrayList = flightModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.item_show_flight,parent,false);
       return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FlightModel flightModel = flightModelArrayList.get(position);
        holder.flighttitle.setText(flightModel.getFlighttitle());
        holder.from.setText(flightModel.getFrom());
        holder.to.setText(flightModel.getTo());
        holder.deptime.setText(flightModel.getTimedep());
        holder.landtime.setText(flightModel.getTime());
        holder.tripname.setText(flightModel.getTripname());
    }

    @Override
    public int getItemCount() {
        return flightModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView flighttitle,from,to,deptime,landtime,tripname;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flighttitle = itemView.findViewById(R.id.flighttitle);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            deptime = itemView.findViewById(R.id.deptime);
            landtime = itemView.findViewById(R.id.landtime);
            tripname = itemView.findViewById(R.id.tripname);
        }
    }
}
