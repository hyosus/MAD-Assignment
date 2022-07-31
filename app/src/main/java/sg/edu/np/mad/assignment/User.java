package sg.edu.np.mad.assignment;

public class User {
    private String userName;
    private String email;
    private String userId;
    private String profilePic;
    public String userPermission;

    // Constructor
    public User(String userName, String email, String userId, String profilePic) {
        this.userName = userName;
        this.email = email;
        this.userId = userId;
        this.profilePic = profilePic;
    }

    public User() {
    }

    // Get set
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
