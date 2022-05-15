package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NoTripsFragment extends Fragment implements View.OnClickListener {
    private View mview;
    private Button create;

    public NoTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview =  inflater.inflate(R.layout.fragment_no_trips, container, false);

        create = (Button) mview.findViewById(R.id.createBtn);
        create.setOnClickListener(this);
        return mview;
    }

    @Override
    // OnClick method for "Create a Trip" button to bring user to AddTrip activity
    public void onClick(View view) {
        Intent mainActivityIntent = new Intent(view.getContext(), AddTrip.class);
        startActivity(mainActivityIntent);
    }
}