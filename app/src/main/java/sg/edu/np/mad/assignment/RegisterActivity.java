package sg.edu.np.mad.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Declarations start
    private EditText editTextEmail, editTextPassword, editTextHomeCountry;

    private ProgressBar progressBar;
    private ImageView backspace;
    private Button registerUser;



    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HOMECOUNTRY = "homeCountry";
    //Declarations ends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        mAuth = FirebaseAuth.getInstance();

        //left arrow icon for backing to login page
        ImageView backspace = findViewById(R.id.backspace);
        backspace.setOnClickListener(this);

        //register user button
        Button registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);



        //Input text box
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextHomeCountry = (EditText) findViewById(R.id.homeCountry);



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backspace:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;

        }
    }


    private void  registerUser(){
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String homeCountry = editTextHomeCountry.getText().toString().trim();


        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("Minimum Length should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        if(homeCountry.isEmpty()){
            editTextHomeCountry.setError("Home Country is required");
            editTextHomeCountry.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            Map<String, Object> users = new HashMap<>();
                            users.put(KEY_EMAIL, email);
                            users.put(KEY_HOMECOUNTRY, homeCountry);


                            //firebase collection path to user and auto generate ID
                            db.collection("users").document().set(users)

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(RegisterActivity.this,"user has been registered successfully!",Toast.LENGTH_LONG).show();

                                            //direct to login layout
                                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this,"Failed to register! Try Again!",Toast.LENGTH_LONG).show();

                                        }
                                    });

                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Failed to register!",Toast.LENGTH_LONG).show();

                        }


                    }
                });








    }


}
