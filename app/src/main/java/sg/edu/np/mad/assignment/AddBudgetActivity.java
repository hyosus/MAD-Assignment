package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;

public class AddBudgetActivity extends AppCompatActivity {

    AnyChartView any_chart_view;
    EditText editbudget,editexpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        any_chart_view = findViewById(R.id.any_chart_view);
        editexpense = findViewById(R.id.editexpense);
        editbudget = findViewById(R.id.editbudget);




        //setupChartView();
    }

    public void setupChartView() {
        Pie pie = AnyChart.pie();
        ArrayList<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.add(new DataEntry());
    }
}