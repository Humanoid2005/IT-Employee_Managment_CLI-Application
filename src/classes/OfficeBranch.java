package classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficeBranch {
    public int branch_id;
    public String city;
    public String state;
    public String country;
    public String address;
    public int size_of_office;

    public OfficeBranch() {
    }

    public OfficeBranch(int branch_id, String city, String state, String country, String address, int size_of_office) {
        this.branch_id = branch_id;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.size_of_office = size_of_office;
    }

    public OfficeBranch(ResultSet rs) throws SQLException {
        this.branch_id = rs.getInt("branch_id");
        this.city = rs.getString("city");
        this.state = rs.getString("state");
        this.country = rs.getString("country");
        this.address = rs.getString("address");
        this.size_of_office = rs.getInt("size_of_office");
    } 
}
