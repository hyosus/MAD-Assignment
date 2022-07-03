package sg.edu.np.mad.assignment;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.collabviewholder>{

    List<User> dataHolder;
    List<String> spinnerList;
    private CollaboratorsActivity collabActivity;
    private String userId;
    StorageReference storageReference;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public CollaboratorAdapter(CollaboratorsActivity collaboratorsActivity, List<User> dataHolder, List<String> spinnerList) {
        this.dataHolder = dataHolder;
        collabActivity = collaboratorsActivity;
        this.spinnerList = spinnerList;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    @Override
    public CollaboratorAdapter.collabviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collaborator_view_holder, parent, false);
        return new collabviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollaboratorAdapter.collabviewholder holder, int position) {
        User user = dataHolder.get(position);
        holder.name.setText(user.getUserName());
        userId = collabActivity.trip.getUserId();

        //Loading image into profile picture with picasso api
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child(user.getProfilePic());
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.profilePic);
            }
        });


        // Set spinner
        if (userId.equals(user.getUserId()))
        {
            holder.spinner.setVisibility(View.GONE);
        }
        else
        {
            holder.ownerTxt.setVisibility(View.GONE);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(collabActivity
        , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter((dataAdapter));

    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class collabviewholder extends RecyclerView.ViewHolder
    {
        ShapeableImageView profilePic;
        TextView name, ownerTxt;
        Spinner spinner;

        public collabviewholder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.collabName);
            spinner = itemView.findViewById(R.id.spinner);
            ownerTxt = itemView.findViewById(R.id.userPermTxt);
        }
    }
}

