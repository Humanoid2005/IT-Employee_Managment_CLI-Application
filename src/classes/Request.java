package classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Request {
    public int request_id;
    public int request_from;
    public int request_to;
    public String request_description;
    public boolean approved;

    public Request() {
    }

    public Request(int request_id,int request_from,int request_to, String request_description, boolean approved) {
        this.request_id = request_id;
        this.request_from = request_from;
        this.request_to = request_to;
        this.request_description = request_description;
        this.approved = approved;
    }

    public Request(ResultSet rs) throws SQLException {
        this.request_id = rs.getInt("request_id");
        this.request_from = rs.getInt("request_from");
        this.request_to = rs.getInt("request_to");
        this.request_description = rs.getString("request_description");
        this.approved = rs.getBoolean("approved");
    }
}
