package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TripViewHolder extends AppCompatActivity {
    ImageView bg;
    public static final String EXTRA_TEXT = "sg.edu.np.mad.assignment.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg.setOnClickListener(v -> OpenActivity());
    }
    public void OpenActivity() {
        TextView textView1 = (TextView) findViewById(R.id.titleVH);
        String text = textView1.getText().toString();

        Intent intent = new Intent(this, AddActivityMain.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }
}



