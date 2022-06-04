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

import sg.edu.np.mad.assignment.AddActivityMain;
import sg.edu.np.mad.assignment.AddNewActivity;
import sg.edu.np.mad.assignment.Model.ActivityModel;
import sg.edu.np.mad.assignment.R;

public class AddActivityAdapter extends RecyclerView.Adapter<AddActivityAdapter.MyViewHolder> {

    private List<ActivityModel> todoactivities;
    private AddActivityMain activity;
    private FirebaseFirestore firestore;

    public AddActivityAdapter(AddActivityMain addActivityMain, List<ActivityModel> todoList){
        this.todoactivities = todoList;
        activity = addActivityMain;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        ActivityModel activityModel = todoactivities.get(position);
        firestore.collection("task").document(activityModel.ActivityId).delete();
        todoactivities.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public void editTask(int position){
        ActivityModel activityModel = todoactivities.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("Activity" , activityModel.getTask());
        bundle.putString("due" , activityModel.getDue());
        bundle.putString("id" , activityModel.ActivityId);

        AddNewActivity addNewActivity = new AddNewActivity();
        addNewActivity.setArguments(bundle);
        addNewActivity.show(activity.getSupportFragmentManager() , addNewActivity.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ActivityModel activityModel = todoactivities.get(position);
        holder.mCheckBox.setText(activityModel.getTask());

        holder.mDueDateTv.setText("Date End " + activityModel.getDue());

        holder.mCheckBox.setChecked(toBoolean(activityModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("Activity").document(activityModel.ActivityId).update("status" , 1);
                }else{
                    firestore.collection("Activity").document(activityModel.ActivityId).update("status" , 0);
                }
            }
        });

    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoactivities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);

        }
    }
}
