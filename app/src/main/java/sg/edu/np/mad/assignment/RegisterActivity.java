package sg.edu.np.mad.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
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

        // Country dropdown/searchable spinner
        editTextHomeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCountryList();
            }
        });


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

    // get list of countries using Locale
    public void getCountryList()
    {
        // Initialise dialog
        Dialog dialog = new Dialog(RegisterActivity.this);

        // Set customer dialog
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        // Set custom height and width
        dialog.getWindow().setLayout(1000,1200);

        dialog.show();

        // Initialise and assign variable
        EditText editText = dialog.findViewById(R.id.edit_text);
        ListView lv = dialog.findViewById(R.id.listView);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        String country;

        for (Locale loc : locale)
        {
            country = loc.getDisplayCountry();

            if (country.length() > 0 && !countries.contains(country))
            {
                countries.add(country);
            }
        }

        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this,
                android.R.layout.simple_list_item_1,countries);

        lv.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Set selected item on textview
                editTextHomeCountry.setText(adapter.getItem(i));

                // Dismiss dialog
                dialog.dismiss();
            }
        });
    }


}
