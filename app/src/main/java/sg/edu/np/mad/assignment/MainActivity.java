package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);

            this.finish();
        }
        Log.w("linktest", "getDynamicLink:success");

    }

    @Override
    protected void onNewIntent ( Intent newIntent ) {
        super.onNewIntent ( newIntent );
        setIntent ( newIntent );
        checkDynamicLinkIntent ();
    }

    private void checkDynamicLinkIntent () {
        if (!Objects.equals ( null, getIntent ().getData () )) {
            Uri dynamicLink = Uri.parse ( getIntent ().getData ().toString () );
            String parameterValue = dynamicLink.getQueryParameter("tripId");
            //get any data from your URI if needed
            Intent intent = new Intent ( MainActivity.this, AddActivityMain.class );
            intent.putExtra ( "bookDetail", parameterValue );
            startActivity ( intent );
        }
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
