package sg.edu.np.mad.assignment.Model;

public class ActivityModel extends ActivityId {

    private String Activity ,Venue, Address, due, TripId,Location;
    private int status;

    public String getActivity() {
        return Activity;
    }
    public String getVenue() {
        return Venue;
    }
    public String getAddress() { return Address; }
    public String getlocation() { return Location; }

    public String getDue() {
        return due;
    }
    //public DateTime getTime() { return time; }

    public int getStatus() {
        return status;
    }
    public String getTripId() { return TripId; }

}
