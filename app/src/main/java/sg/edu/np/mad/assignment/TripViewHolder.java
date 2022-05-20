package sg.edu.np.mad.assignment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TripViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView bg;

    public TripViewHolder(View itemView){
        super(itemView);


        title = itemView.findViewById(R.id.titleVH);
        bg = itemView.findViewById(R.id.bgVH);

        // Set intent to itinerary
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}