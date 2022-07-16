package sg.edu.np.mad.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.collabviewholder>{
    DALTrip dal = new DALTrip();
    List<TripAdmin> dataHolder;
    private CollaboratorsActivity collabActivity;
    private String thisTripUserId, thisTripId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Boolean editPermBtnClickable = false;
    Boolean spinnerBinded = false;
    RadioButton radioButton, editRB, viewRB;


    public CollaboratorAdapter(CollaboratorsActivity collaboratorsActivity, List<TripAdmin> dataHolder, String uid, Trip trip) {
        this.dataHolder = dataHolder;
        collabActivity = collaboratorsActivity;
        this.thisTripUserId = uid;
        if (trip.getUserId().equals(uid)){
            editPermBtnClickable = true;
        }

    }


    @NonNull
    @Override
    public CollaboratorAdapter.collabviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collaborator_view_holder, parent, false);
        return new collabviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollaboratorAdapter.collabviewholder holder, int position) {
        TripAdmin ta = dataHolder.get(position);
        thisTripUserId = collabActivity.trip.getUserId();
        thisTripId = collabActivity.trip.getId();
        holder.name.setText(ta.getUserName());
        holder.permTextView.setText(ta.permission);

        View customLayout = collabActivity.getLayoutInflater().inflate(R.layout.collab_edit_alert_dialog, null);

        editRB = (RadioButton) customLayout.findViewById(R.id.editRB);
        viewRB = (RadioButton) customLayout.findViewById(R.id.viewRB);

        Log.v("ythisnotworking", ta.permission);

        if (ta.permission.equals("Can Edit")){
            holder.editPernissionBtn.setImageDrawable(collabActivity.getDrawable(R.drawable.ic_edit_black_24dp));
            editRB.setChecked(true);
        }
        else{
            holder.editPernissionBtn.setImageDrawable(collabActivity.getDrawable(R.drawable.ic_visibility_black_24dp));
            viewRB.setChecked(true);
        }


        // Alert dialog 💢💢
        holder.editPernissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Modify permission for "+ta.userName);
                builder.setView(customLayout);

                // Delete user
                customLayout.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dal.removeTripSerializedTA(thisTripId, ta);
                        Toast.makeText(v.getContext(), "User " + ta.userName + " was deleted.", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(position);
                    }
                });


                // Save changes
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  UPDATE PERM CODES! 💦💦😋😋😋
                        final RadioGroup rg = customLayout.findViewById(R.id.collabEditAlertRG);
                        int selectedId = rg.getCheckedRadioButtonId();
                        radioButton = (RadioButton) customLayout.findViewById(selectedId);

                        TripAdmin newTA = new TripAdmin(ta.userId, ta.userName, radioButton.getText().toString());
                        Gson gson = new Gson();
                        String taJsonString = gson.toJson(newTA);

                        dal.updateTripSerializedTAL(thisTripId, ta, taJsonString);
                        notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("Cancel", null);
                //  Initiating the alert dialog
                AlertDialog select_task_confirm_dialog = builder.create();
                select_task_confirm_dialog.show();
            }
        });

        // 💢💢💢💢

//        holder.row.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setTitle("Delete Collaborator");
//                builder.setMessage("Remove "+ta.userName+ " from the collaborator list?");
//                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //  Remove COllab! 💦💦😋😋😋
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                //  Initiating the alert dialog
//                AlertDialog select_task_confirm_dialog = builder.create();
//                select_task_confirm_dialog.show();
//                return false;
//            }
//        });

        //  Bind Profile Picture
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + ta.userId + "/profilePic");


        //Loading image into profile picture with picasso api
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.profilePic);
            }
        });

    }


    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class collabviewholder extends RecyclerView.ViewHolder
    {
        ConstraintLayout row;
        ShapeableImageView profilePic;
        TextView name, ownerTxt;
        ImageView editPernissionBtn;
        TextView permTextView;

        public collabviewholder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.collabName);
            editPernissionBtn = itemView.findViewById(R.id.editPernissionBtn);
            permTextView = itemView.findViewById(R.id.permissionTxt);
            row = itemView.findViewById(R.id.row);
        }
    }

    public void update(CollaboratorsActivity activity, Trip trip) {
        db.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (trip.getId().equals(doc.getString("id"))) {
                        dataHolder.clear();
                        ArrayList<TripAdmin> data = new ArrayList<TripAdmin>();
                        ArrayList<String> stalist = new ArrayList<String>();
                        stalist = (ArrayList<String>) doc.get("serializedTAL");
                        for (int i=0; i<stalist.size(); i++){
                            Gson gson = new Gson();
                            TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
                            dataHolder.add(tempTa);
                        }
                        activity.collaboratorAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
//        db.collection("Trip").document(trip.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    dataHolder.clear();
//                    ArrayList<TripAdmin> data = new ArrayList<TripAdmin>();
//                    ArrayList<String> stalist = new ArrayList<String>();
//                    stalist = (ArrayList<String>) task.getResult().get("serializedTAL");
//                    for (int i=0; i<stalist.size(); i++){
//                        Gson gson = new Gson();
//                        TripAdmin tempTa = gson.fromJson(stalist.get(i), TripAdmin.class);
//                        dataHolder.add(tempTa);
//                    }
//                    activity.collaboratorAdapter.notifyDataSetChanged();
//                }
//
//            }
//        });
    }
}

