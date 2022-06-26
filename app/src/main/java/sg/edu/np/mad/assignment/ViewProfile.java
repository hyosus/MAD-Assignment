package sg.edu.np.mad.assignment;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class ViewProfile<maxNumPhotosAndVideos> extends AppCompatActivity {
    ImageView profileImgLarge;
    String username;
    String email;
    String phoneNo;
    String country;
    String dob;
    Uri imageUri;
    String profImageId;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    HashMap<String, Object> hashMap = new HashMap<>();
    EditText countryInput;
    Button changeCountry;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView backBtn;
    Button logoutBtn;
    ImageView changeProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        profileImgLarge = findViewById(R.id.profileImgLarge);
        backBtn = findViewById(R.id.backButton);
        changeProfilePicture = findViewById(R.id.uploadImgBtn);
        logoutBtn = findViewById(R.id.logoutButton);
        countryInput = findViewById(R.id.editTextCountry);
        changeCountry = findViewById(R.id.changeCountry);

        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference documentReference = db.collection("users").document(uid);

        EditText usernameInput = findViewById(R.id.editTextName);
        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText phoneNoInput = findViewById(R.id.editTextPhone);
        EditText dobInput = findViewById(R.id.editTextDate);
        EditText countryInput = findViewById(R.id.editTextCountry);

        //Setting text on fields from firestore
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                emailInput.setText(value.getString("email"));
                countryInput.setText(value.getString("homeCountry"));
                usernameInput.setText(value.getString("username"));
                dobInput.setText(value.getString("dob"));
                phoneNoInput.setText(value.getString("phoneNo"));
            }
        });

        StorageReference profileRef = storageReference.child("users/" + uid + "/profilePic");

        //Loading image into profile picture with picasso api
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImgLarge);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields(emailInput, usernameInput, phoneNoInput, dobInput, countryInput)){
                    username = usernameInput.getText().toString().trim();
                    email = emailInput.getText().toString().trim();
                    phoneNo = phoneNoInput.getText().toString().trim();
                    dob = dobInput.getText().toString().trim();
                    country = countryInput.getText().toString().trim();

                    hashMap.put("username", username);
                    hashMap.put("email", email);
                    hashMap.put("phoneNo", phoneNo);
                    hashMap.put("dob", dob);
                    hashMap.put("homeCountry", country);

                    db.collection("users").document(uid).set(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ViewProfile.this, "Profile saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ViewProfile.this, "Profile saving failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                    startActivity(new Intent(ViewProfile.this, HomeActivity.class));
                }
            }
        });

        changeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCountryList();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields(emailInput, usernameInput, phoneNoInput, dobInput, countryInput)){
                    username = usernameInput.getText().toString().trim();
                    email = emailInput.getText().toString().trim();
                    phoneNo = phoneNoInput.getText().toString().trim();
                    dob = dobInput.getText().toString().trim();
                    country = countryInput.getText().toString().trim();

                    hashMap.put("username", username);
                    hashMap.put("email", email);
                    hashMap.put("phoneNo", phoneNo);
                    hashMap.put("dob", dob);
                    hashMap.put("homeCountry", country);

                    db.collection("users").document(uid).set(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ViewProfile.this, "Profile saved and logged out", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ViewProfile.this, "Profile saving failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                    startActivity(new Intent(ViewProfile.this, MainActivity.class));
                }
            }
        });

        changeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 99 && resultCode==RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            profileImgLarge.setImageURI(imageUri);
            uploadPicture(imageUri, uid);
        }
    }

    private void uploadPicture(Uri imgUri, String uid) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.show();

        StorageReference image = storageReference.child("users/" + uid + "/" + "profilePic");

        imageUri = imgUri;

        image.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(ViewProfile.this, "Profile Photo Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ViewProfile.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploading: " + (int) progressPercent + "%");
                    }
                });

    }

    public boolean checkFields(EditText emailInput, EditText usernameInput, EditText phoneNoInput,
                               EditText dobInput, EditText countryInput){

        email = emailInput.getText().toString().trim();
        username = usernameInput.getText().toString().trim();
        phoneNo = phoneNoInput.getText().toString().trim();
        dob = dobInput.getText().toString().trim();
        country = countryInput.getText().toString().trim();
        if(username.isEmpty()){
            usernameInput.requestFocus();
            usernameInput.setError("Name is required");
            return false;
        }
        if(email.isEmpty()){
            emailInput.requestFocus();
            emailInput.setError("Email is required");
            return false;
        }
        if(phoneNo.isEmpty()){
            phoneNoInput.requestFocus();
            phoneNoInput.setError("Phone Number is required");
            return false;
        }
        if(dob.isEmpty()){
            dobInput.requestFocus();
            dobInput.setError("Date of birth is required");
            return false;
        }
        if(country.isEmpty()){
            countryInput.requestFocus();
            countryInput.setError("Home country is required");
            return false;
        }
        else{
            return true;
        }
    }

    public void getCountryList()
    {
        // Initialise dialog
        Dialog dialog = new Dialog(ViewProfile.this);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewProfile.this,
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
                countryInput.setText(adapter.getItem(i));

                // Dismiss dialog
                dialog.dismiss();
            }
        });
    }
}