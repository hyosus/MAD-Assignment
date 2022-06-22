package sg.edu.np.mad.assignment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmails, editTextPasswords;
    private Button signIn1;
    private TextView forgotpassword;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signIn1 = (Button) findViewById(R.id.SignIn1);
        signIn1.setOnClickListener(this);


        editTextEmails = (EditText) findViewById(R.id.email2);
        editTextPasswords = (EditText) findViewById(R.id.password4);

        //forgot password
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        forgotpassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        ImageView backspace = findViewById(R.id.backspace2);
        backspace.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SignIn1:
                userSignIn();
                break;
            case R.id.backspace2:
                startActivity(new Intent(this, MainActivity.class));
                break;
//            case R.id.forgotpassword:
//                startActivity(new Intent(this, Forgotpassword.class));
//                break;
        }

    }

    private void userSignIn(){
        String emails = editTextEmails.getText().toString().trim();
        String passwords = editTextPasswords.getText().toString().trim();


        if(emails.isEmpty()){
            editTextEmails.setError("Email is required");
            editTextEmails.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
            editTextEmails.setError("Please provide valid email");
            editTextEmails.requestFocus();
            return;
        }

        if(passwords.isEmpty()){
            editTextPasswords.setError("Password is required");
            editTextPasswords.requestFocus();
            return;
        }

        if(passwords.length()<6){
            editTextPasswords.setError("Minimum Length should be 6 characters");
            editTextPasswords.requestFocus();
            return;
        }



        mAuth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //redirect to Home Screen
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));


                } else {
                    Toast.makeText(SignInActivity.this, "Failed to sign in! Please check your credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }



}