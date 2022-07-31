package sg.edu.np.mad.assignment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import sg.edu.np.mad.assignment.AddActivityMain;
import sg.edu.np.mad.assignment.AddNewActivity;
import sg.edu.np.mad.assignment.Model.ActivityModel;
import sg.edu.np.mad.assignment.Model.activity_mapview;
import sg.edu.np.mad.assignment.R;

public class AddActivityAdapter extends RecyclerView.Adapter<AddActivityAdapter.MyViewHolder> {

    private List<ActivityModel> todoList;
    private AddActivityMain activity;
    private FirebaseFirestore firestore;

    public AddActivityAdapter(AddActivityMain addActivityMain, List<ActivityModel> todoList){
        this.todoList = todoList;
        activity = addActivityMain;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_activity, parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    // Function to remove activity
    public void deleteActivity(int position){
        ActivityModel activityModel = todoList.get(position);
        firestore.collection("Activity").document(activityModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }

    // Function to edit activity
    public void editTask(int position){
        ActivityModel activityModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("Activity" , activityModel.getActivity());
        bundle.putString("Venue" , activityModel.getVenue());
        //bundle.putString("time" , activityModel.getTime().toString());
        bundle.putString("Address" , activityModel.getAddress());
        bundle.putString("due" , activityModel.getDue());
        bundle.putString("Location" , activityModel.getlocation());
        bundle.putString("id" , activityModel.TaskId);

        AddNewActivity addNewActivity = new AddNewActivity();
        addNewActivity.setArguments(bundle);
        addNewActivity.show(activity.getSupportFragmentManager() , addNewActivity.getTag());

    }
    // Add Checkbox to Viewholder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ActivityModel activityModel = todoList.get(position);
        holder.Activityname.setText(activityModel.getActivity());
        holder.Venue.setText(activityModel.getVenue());
        holder.mDueDateTv.setText("Date: " + activityModel.getDue());
        //holder.time.setText(activityModel.getTime().toString());

        holder.mCheckBox.setChecked(toBoolean(activityModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("Activity").document(activityModel.TaskId).update("status" , 1);
                    Toast.makeText(activity.getApplicationContext(),"Completed activity",Toast.LENGTH_SHORT).show();
                }else{
                    firestore.collection("Activity").document(activityModel.TaskId).update("status" , 0);
                }
            }
        });

        // When user click on activity, this will send over the information to activity_mapview
        holder.contain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                ActivityModel activityModel = todoList.get(position);
                Intent intent = new Intent(view.getContext(), activity_mapview.class);
                TextView textView = view.findViewById(R.id.Activity_name);

                String activity_name = textView.getText().toString();
                intent.putExtra("NAME", activity_name);
                intent.putExtra("DUE", activityModel.getDue());
                intent.putExtra("VENUE", activityModel.getVenue());
                intent.putExtra("ADDRESS", activityModel.getAddress());
                intent.putExtra("LOCATION", activityModel.getlocation());

                view.getContext().startActivity(intent);
            }
        });
    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv,Activityname,Venue;
        CheckBox mCheckBox;
        ConstraintLayout contain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contain = itemView.findViewById(R.id.contain);
            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            Activityname = itemView.findViewById(R.id.Activity_name);
            Venue = itemView.findViewById(R.id.Venue);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);

        }
    }
}
