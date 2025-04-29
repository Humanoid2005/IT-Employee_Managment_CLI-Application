package management_systems;

public class Credentials {
    static String USER = "root";
    static String PASSWORD = "rcop";

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
