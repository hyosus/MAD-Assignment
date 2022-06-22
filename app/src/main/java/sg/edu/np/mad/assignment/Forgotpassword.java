package sg.edu.np.mad.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText;
    private Button forgotPassBtn;
    FirebaseAuth auth;
    private ImageView backspace4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


        emailEditText = (EditText) findViewById(R.id.editTextTextEmailAddress);
        forgotPassBtn = (Button) findViewById((R.id.forgotPasswordBtn));
        forgotPassBtn.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

        backspace4 = (ImageView) findViewById(R.id.backspace4);
        backspace4.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backspace4:
                startActivity(new Intent(this, Signin.class));
                break;
            case R.id.forgotPasswordBtn:
                resetPassword();
                break;

        }
    }

    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Type in EMail!");
            emailEditText.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgotpassword.this, "Your email has been given a password reset link!", Toast.LENGTH_LONG).show();
                }

                else{
                    Toast.makeText(Forgotpassword.this, "Please try again!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}