package sg.edu.np.mad.assignment;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {
    private String destination;
    private String startDate;
    private String endDate;
    private String tripName;
    private String id;
    private String userId;
    public ArrayList<String> serializedTAL;
    public ArrayList<EditHistory> EditHistoryList;
    public ArrayList<String> serializedEHL;

    // Constructor
    public Trip(String destination, String startDate, String endDate, String tripName, String id, String userId, ArrayList<String> sTAL, ArrayList<EditHistory> editHistoryList, ArrayList<String> sEHL) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripName = tripName;
        this.id = id;
        this.userId = userId;
        this.serializedTAL = sTAL;
        this.EditHistoryList = editHistoryList;
        this.serializedEHL = sEHL;
    }

    // Public constructor for firebase
    public Trip(){}

    // Get set


    public ArrayList<String> getSerializedEHL() {
        return serializedEHL;
    }

    public void setSerializedEHL(ArrayList<String> serializedEHL) {
        this.serializedEHL = serializedEHL;
    }

    public ArrayList<String> getSerializedTAL() {
        return serializedTAL;
    }

    public void setSerializedTAL(ArrayList<String> serializedTAL) {
        this.serializedTAL = serializedTAL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
