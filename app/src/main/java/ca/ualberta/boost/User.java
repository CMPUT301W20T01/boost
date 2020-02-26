package ca.ualberta.boost;

public class User {

    private String firstName;
    private String lastName;
    private String userName;
    //private String password;
    private String email;
    private String phoneNumber;

    public User(){}

    //constructor
    public User(String firstName, String lastName, String userName,  String email, String phoneNumber ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        //this.password = password;
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

   // public void setPassword(String password) {
       // this.password = password;
    //}

  /*  public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }*/

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
