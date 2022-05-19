package sg.edu.np.mad.assignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {
    private View mview;
    RecyclerView recyclerView;
    ArrayList<Trip> dataHolder;
    ArrayList<Trip> tripList;

    public TripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_trips, container, false);
        recyclerView = mview.findViewById(R.id.ongoingRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataHolder = new ArrayList<>();

        Trip obj = new Trip("Singapore","12/05/2022", "15/05/2022", "Holiday");
        dataHolder.add(obj);


//        tripList = getArguments().getParcelableArrayList("tripList");
//
//        tripList.add(obj);


        recyclerView.setAdapter(new TripAdapter(dataHolder));

        return mview;
    }
}