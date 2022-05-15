package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AddTrip extends AppCompatActivity {
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        back = findViewById(R.id.backBtn);

        // Send user back to home screen
        back.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(AddTrip.this, HomeActivity.class);
                startActivity(Intent);
            }
        }));
    }
}