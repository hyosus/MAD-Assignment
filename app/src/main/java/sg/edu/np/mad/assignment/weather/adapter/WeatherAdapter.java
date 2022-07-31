package sg.edu.np.mad.assignment.weather.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.assignment.R;
import sg.edu.np.mad.assignment.weather.activity.WeatherDetailActivity;
import sg.edu.np.mad.assignment.weather.model.WeatherDataModel;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    List<WeatherDataModel> modelList;
    String myCity;
    Activity context;

    public WeatherAdapter(List<WeatherDataModel> modelList, Activity context, String city) {
        this.modelList = modelList;
        this.context = context;
        this.myCity = city;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cities, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WeatherDataModel dataModel = modelList.get(position);
        holder.tvCityName.setText(dataModel.getCityName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WeatherDetailActivity.class);
            intent.putExtra("latitude", modelList.get(position).getLatitude());
            intent.putExtra("longitude", modelList.get(position).getLongitude());
            if (position == 0) {
                intent.putExtra("city", myCity);
            }else {
                intent.putExtra("city", "");
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<WeatherDataModel> filteredList) {
        modelList = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
        }
    }
}
