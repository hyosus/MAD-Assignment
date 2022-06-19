package sg.edu.np.mad.assignment;

import android.net.Uri;
import java.util.List;

public class User {

    public String homeCountry;
    public String username;
    public String email;
    public String phoneNo;
    public String dob;
    public Uri profPicUri;

//    public Integer tripCount;
    public List<Trip> trips;


    public User(){
    }


    public User(String email,String homeCountry, String username, String phoneNo, String dob,
                Uri profPicUri, List<Trip> trips){

        this.email = email;
        this.homeCountry = homeCountry;
        this.username = username;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.profPicUri = profPicUri;
        this.trips = trips;

    }
}
