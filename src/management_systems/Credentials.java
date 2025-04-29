package management_systems;

public class Credentials {
    static String USER = "mysql_username";
    static String PASSWORD = "mysql_password";

    public String getUSER(){
        return USER;
    }

    public String getPASSWORD(){
        return PASSWORD;
    }

    public void setUSER(String user){
        Credentials.USER = user;
    }

    public void setPASSWORD(String password){
        Credentials.PASSWORD = password;
    }
}
