package sg.edu.np.mad.assignment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Trip implements Serializable {
    private String destination;
    private String startDate;
    private String endDate;
    private String tripName;
    private String id;

    // Constructor
    public Trip(String destination, String startDate, String endDate, String tripName, String id) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripName = tripName;
        this.id = id;
    }

    // Public constructor for firebase
    public Trip(){}

    // Get set

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
}
