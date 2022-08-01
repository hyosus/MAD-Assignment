package sg.edu.np.mad.assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.assignment.Model.Budget;
import sg.edu.np.mad.assignment.Model.FlightModel;
import sg.edu.np.mad.assignment.R;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.MyViewHolder> {
    Context context;
    ArrayList<Budget> budgetArrayList;

    public BudgetAdapter(Context context, ArrayList<Budget> budgetArrayList) {
        this.context = context;
        this.budgetArrayList = budgetArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.budget_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Budget budget = budgetArrayList.get(position);
        holder.from.setText(budget.getBudget());
        holder.to.setText(budget.getExpense());
        holder.tripname.setText(budget.getTripname());
    }

    @Override
    public int getItemCount() {
        return budgetArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView from,to,tripname;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            tripname = itemView.findViewById(R.id.tripname);

        }
    }
}
