package sg.edu.np.mad.assignment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class VersionHistoryActivity extends AppCompatActivity {

    private ImageView backBtn;
    VersionHistoryAdapter vhAdapter;
    private ArrayList<EditHistory> dataholder = new ArrayList<EditHistory>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_history);

        //  Get Bundle
        String thisTripId = getIntent().getStringExtra("tripId");

        //  Bind Views
        RecyclerView recyclerView = findViewById(R.id.vhRV);
        findViewById(R.id.verHistBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  Get Data
        FirebaseFirestore.getInstance().collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Gson gson = new Gson();
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.getString("id").equals(thisTripId)){
                        for(String json : (ArrayList<String>) doc.get("serializedEHL")){
                            dataholder.add(gson.fromJson(json, EditHistory.class));
                        }
                        Collections.reverse(dataholder);
                        VersionHistoryAdapter vhAdapter = new VersionHistoryAdapter(dataholder);
                        LinearLayoutManager lmg = new LinearLayoutManager(VersionHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(lmg);
                        recyclerView.setAdapter(vhAdapter);
                    }
                }
            }
        });


    }
}