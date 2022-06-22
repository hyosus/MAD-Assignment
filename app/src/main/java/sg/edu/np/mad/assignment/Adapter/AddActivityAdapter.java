package sg.edu.np.mad.assignment.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import sg.edu.np.mad.assignment.Model.ActivityModel;
import sg.edu.np.mad.assignment.AddActivityMain;
import sg.edu.np.mad.assignment.AddNewActivity;
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

    public void deleteActivity(int position){
        ActivityModel activityModel = todoList.get(position);
        firestore.collection("Activity").document(activityModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public void editTask(int position){
        ActivityModel activityModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("Activity" , activityModel.getActivity());
        bundle.putString("Venue" , activityModel.getVenue());
        bundle.putString("Address" , activityModel.getAddress());
        bundle.putString("due" , activityModel.getDue());
        bundle.putString("id" , activityModel.TaskId);

        AddNewActivity addNewActivity = new AddNewActivity();
        addNewActivity.setArguments(bundle);
        addNewActivity.show(activity.getSupportFragmentManager() , addNewActivity.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ActivityModel activityModel = todoList.get(position);
        holder.mCheckBox.setText(activityModel.getActivity());
        holder.Venue.setText(activityModel.getVenue());
        holder.mDueDateTv.setText("Due On " + activityModel.getDue());

        holder.mCheckBox.setChecked(toBoolean(activityModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("Activity").document(activityModel.TaskId).update("status" , 1);
                }else{
                    firestore.collection("Activity").document(activityModel.TaskId).update("status" , 0);
                }
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

        TextView mDueDateTv;
        TextView Venue;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mDueDateTv = itemView.findViewById(R.id.Venue);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);

        }
    }
}
