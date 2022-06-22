package sg.edu.np.mad.assignment.Model;

public class ActivityModel extends ActivityId {

    private String Activity ,Venue, Address, due;
    private int status;

    public String getActivity() {
        return Activity;
    }
    public String getVenue() {
        return Venue;
    }
    public String getAddress() { return Address; }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
