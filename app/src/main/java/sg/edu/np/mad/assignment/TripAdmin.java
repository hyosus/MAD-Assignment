package sg.edu.np.mad.assignment;

public class TripAdmin {
    public String userId;
    public String userName;
    public String permission;

    public TripAdmin( String userId, String userName, String permission) {
        this.userId = userId;
        this.userName = userName;
        this.permission = permission;
    }

    public TripAdmin(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
