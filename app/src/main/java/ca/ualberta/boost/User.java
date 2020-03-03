package ca.ualberta.boost;

public class User {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String confirmpassword;
    private String email;
    private int phoneNumber;

    //constructor
    public User(String firstName, String lastName, String userName, String password, String confirmpassword, String email,int phoneNumber ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.confirmpassword = password;
        this.email = email;
        this.phoneNumber = phoneNumber;

    }

    // setters


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
