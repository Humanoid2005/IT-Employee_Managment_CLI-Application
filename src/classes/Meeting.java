package classes;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class Meeting {
    public int meeting_id;
    public String meeting_mode;
    public Time meeting_start_time;
    public Time meeting_end_time;
    public String meeting_location;
    public int meeting_organiser_id;
    public int department_id;
    public String purpose_of_meeting;
    public String minutes_of_meeting;
    public Date meeting_date;

    
    public Meeting() {
    }

    public Meeting(int meeting_id, String meeting_mode, Time meeting_start_time, Time meeting_end_time,
                    String meeting_location, int meeting_organiser_id, int department_id,
                    String purpose_of_meeting, String minutes_of_meeting,Date meeting_date) {
        this.meeting_id = meeting_id;
        this.meeting_mode = meeting_mode;
        this.meeting_start_time = meeting_start_time;
        this.meeting_end_time = meeting_end_time;
        this.meeting_location = meeting_location;
        this.meeting_organiser_id = meeting_organiser_id;
        this.department_id = department_id;
        this.purpose_of_meeting = purpose_of_meeting;
        this.minutes_of_meeting = minutes_of_meeting;
        this.meeting_date = meeting_date;
    }

    public Meeting(ResultSet rs) throws SQLException {
        this.meeting_id = rs.getInt("meeting_id");
        this.meeting_mode = rs.getString("meeting_mode");
        this.meeting_start_time = rs.getTime("meeting_start_time");
        this.meeting_end_time = rs.getTime("meeting_end_time");
        this.meeting_location = rs.getString("meeting_location");
        this.meeting_organiser_id = rs.getInt("meeting_organiser_id");
        this.department_id = rs.getInt("department_id");
        this.purpose_of_meeting = rs.getString("purpose_of_meeting");
        this.minutes_of_meeting = rs.getString("minutes_of_meeting");
        this.meeting_date = rs.getDate("meeting_date");
    }
}