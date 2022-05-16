package sg.edu.np.mad.assignment;

import java.util.Date;

public class Trips {
    String destination;
    String startDate;
    String endDate;
    String tripName;

    public Trips (String destination, String startDate, String endDate, String tripName) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripName = tripName;
    }
}
