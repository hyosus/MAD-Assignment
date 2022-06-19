package sg.edu.np.mad.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewProfile<maxNumPhotosAndVideos> extends AppCompatActivity {
TextView changeProfilePic;
ImageView profileImgLarge;
String username;
String email;
String phoneNo;
String country;
String dob;
User user;
Uri profImgUri;
String userId;

StorageReference storageReference;

    ImageView backBtn;

    Button logoutBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        changeProfilePic = findViewById(R.id.changeProfPic);
        profileImgLarge = findViewById(R.id.profileImgLarge);
        backBtn = findViewById(R.id.backButton);

        EditText usernameInput = findViewById(R.id.editTextName);
        EditText emailInput = findViewById(R.id.editTextEmail);
        EditText phoneNoInput = findViewById(R.id.editTextPhone);
        EditText dobInput = findViewById(R.id.editTextDate);
        EditText countryInput = findViewById(R.id.editTextCountry);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        DocumentReference documentReference = db.collection("users").document(uid);
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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields(emailInput, usernameInput, phoneNoInput, dobInput, countryInput)){
                    username = usernameInput.getText().toString().trim();
                    email = emailInput.getText().toString().trim();
                    phoneNo = phoneNoInput.getText().toString().trim();
                    dob = dobInput.getText().toString().trim();
                    country = countryInput.getText().toString().trim();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("username", username);
                    hashMap.put("email", email);
                    hashMap.put("phoneNo", phoneNo);
                    hashMap.put("dob", dob);
                    hashMap.put("homeCountry", country);

                    db.collection("users").document(uid).set(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ViewProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ViewProfile.this, "Profile Saving Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                    startActivity(new Intent(ViewProfile.this, HomeActivity.class));
                }
            }
        });



        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
//            changeProfilePic.launch(openGallery);
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


// Uploading image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                profileImgLarge.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }


    }


//    Uploading image
    private void uploadImageToFirebase(Uri imgUri) {
        // uploading to firebase strorage
        StorageReference fileReference = storageReference.child("profile.jpg");
        fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Picasso.get().load(uri).into();

                    }
                })
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewProfile.this, "Image Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //    ActivityResultLauncher<Intent> changeProfilePicResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Uri imageUri = data.getData();
//                    }
//                }
//            });
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void requestStoragePermission() {
//        requestPermissions(storagePermission, STORAGE_REQUEST);
//    }

//    private boolean checkStoragePermission() {
//        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//
//    private boolean checkCameraPermission() {
//        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
//        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//        return result && result1;
//    }



//    private void pickFromGallery() {
//
//        CropImage.activity()
//            .setGuidelines(CropImageView.Guidelines.ON)
//            .start(this);
//    }


//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void requestCameraPermission() {
//        requestPermissions(CameraPermission, CAMERA_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage)
//    }
}