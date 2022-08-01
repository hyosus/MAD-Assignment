package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //On Click listener for buttons
        Button createAcc = findViewById(R.id.button);
        Button signIn = findViewById(R.id.button2);

        createAcc.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                break;
        }

    }



}
