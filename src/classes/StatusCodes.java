package classes;

public class StatusCodes {
    public final int SUCCESS = 0;
    public final int INVALID_USER = 1;
    public final int WRONG_PASSWORD = 2;
    public final int USER_NOT_LOGGED_IN = 3;
    public final int USER_LOGGED_IN = 4;
    public final int DB_INSERTION_ERROR = 5;
    public final int DB_UPDATION_ERROR = 6;
    public final int DB_DELETION_ERROR = 7;
    public final int DB_FETCH_ERROR = 8;
    public final int DB_ROLLBACK = 9;
    public final int DB_COMMIT = 10;
    public final int UNAUTHORISED_ACCESS = 11;
    public final int INVALID_VALUE = 12;

    public java.sql.Date getCurrentDate(){
        java.util.Date today = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(today.getTime());
        return sqlDate;
    }

    public java.sql.Time getCurrentTime(){
        long timeInMs = System.currentTimeMillis();
        java.sql.Time sqlTime = new java.sql.Time(timeInMs);
        return sqlTime;
    }
}
