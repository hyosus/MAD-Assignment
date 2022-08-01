package sg.edu.np.mad.assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VersionHistoryAdapter extends RecyclerView.Adapter<VersionHistoryAdapter.verhistviewholder>{
    ArrayList<EditHistory> dataHolder;


    public VersionHistoryAdapter(ArrayList<EditHistory> dataHolder)
    {
        this.dataHolder = dataHolder;
    }
    @NonNull
    @Override
    public verhistviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.version_history_view_holder, parent, false);
        return new verhistviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull verhistviewholder holder, int position) {
        holder.userNameTxt.setText(dataHolder.get(position).editByUserName);
        holder.editDateTimeTxt.setText(dataHolder.get(position).editTime);
        holder.userNameTxt.setText(dataHolder.get(position).editByUserName);
        String changesMade = dataHolder.get(position).editLog.replace(";","\n");
        holder.editLogTxt.setText(changesMade);
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }


    public class verhistviewholder extends RecyclerView.ViewHolder
    {
        TextView editDateTimeTxt, userNameTxt, editLogTxt;

        public verhistviewholder(View itemView) {
            super(itemView);
            editDateTimeTxt = itemView.findViewById(R.id.editDateTimeTxt);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            editLogTxt = itemView.findViewById(R.id.editLogTxt);
        }
    }

}
