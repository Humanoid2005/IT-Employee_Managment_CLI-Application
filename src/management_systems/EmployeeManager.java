package management_systems;

import classes.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Features implemented:
 * - Role Based Control Access (RBAC)
 * - Basic authentication: login, logout, change password
 * - Admin operations: 1. Add employee
 *                     2. Fire employee
 *                     3. Reassign employee (can change salary, department)
 *                     4. approve leaves
 *                     5. view leave requests 
 *                     5. create meetings 
 *                     6. update MoM 
 *                     7. Get employee performance report
 *                     8. Get employee's dependents' data 
 * - Employee operations: 1. Request for leave
 *                        2. Get experience report
 *                        3. Join meeting
 *                        4. Leave meeting 
 *                        5. View my meetings
 *                        6. Add work log entry
 *                        7. Add Skill
 * - Common operations: 1. View list of employees working at your branch
 *                      2. View department details
 *                      3. View office branches
 *                      4. Send requests
 *                      5. View requests
 *                      5. Accept requests
 */

public class EmployeeManager {
    String role = null;
    Employee loggedInUser = null;
    private final int key = 3;
    private DB db;
    private final StatusCodes status;


    public EmployeeManager() {
        this.db = new DB();
        this.status = new StatusCodes();
    }

    public void openDB(){
        db.openDB();
    }

    public void closeDB(){
        db.closeDB();
    }

    public String encryptPassword(String Ipassword,int key){
        StringBuilder encrypted = new StringBuilder();
    
        for (int i = 0; i < Ipassword.length(); i++) {
            char c = Ipassword.charAt(i);
    
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + key) % 26 + base);
            }
            
            encrypted.append(c);
        }
        
        return encrypted.toString();
    }

    //BASIC AUTHENTICATION TO MAINTAIN ROLE BASED ACCESS CONTROL
    public int login(int ssn,String password) throws SQLException{
        String encrypted_password = encryptPassword(password, key);
        Employee e = db.getEmployee(ssn);
        int HR_Dept_no = db.getDepartmentNumber("HR");
        if(e!=null){
            if(e.employee_password.equals(encrypted_password)){
                if(e.department_id == HR_Dept_no){
                    this.role = "admin";
                }
                else{
                    this.role = "employee";
                }
                this.loggedInUser = e;
                return status.SUCCESS;
            }
            else{
                return status.WRONG_PASSWORD;
            }
        }
        return status.INVALID_USER;
    }

    public int logout(int ssn){
        if(this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }

        this.loggedInUser = null;
        this.role = null;
        return status.SUCCESS;
    }

    public int checkLoggedIn(){
        if (role == null || this.loggedInUser==null) {
            return status.USER_NOT_LOGGED_IN;
        }
        return status.USER_LOGGED_IN;
    }

    public int changePassword(String password) throws SQLException{
        if(role==null){
            return status.UNAUTHORISED_ACCESS;
        }

        String employee_password = encryptPassword(password, key);
        this.loggedInUser.employee_password = employee_password;
        int result = db.updateEmployeePassword(this.loggedInUser.employee_id, employee_password);

        return result;
    }

    //BASIC ADMIN ACTIONS
    public int register(Employee e) throws SQLException{
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }

        e.employee_password = encryptPassword(e.employee_password, key);

        if(db.addEmployee(e)==status.SUCCESS){
            return status.SUCCESS;
        }
        else{
            return status.DB_INSERTION_ERROR;
        }
    }

    public int removeEmployee(int ssn) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }
        
        return db.removeEmployee(ssn,this.loggedInUser);
    }

    public int printEmployeeDependents(int ssn) throws SQLException{
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }

        if(!role.equals("admin")){
            return status.UNAUTHORISED_ACCESS;
        }
        
        ArrayList<Dependent> dependents = db.getEmployeeDependents(ssn);

        if(dependents==null){
            return status.DB_FETCH_ERROR;
        }

        System.out.println("=======================================================================================");
        System.out.println("                                EMPLOYEE DEPENDENTS                                    ");
        System.out.println("=======================================================================================");
        System.out.printf("%-15s | %-20s | %-6s | %-12s | %-15s\n", 
                "Employee ID", "Dependent Name", "Sex", "DOB", "Relation");
        System.out.println("---------------------------------------------------------------------------------------");
        
        for (Dependent d : dependents) {
            System.out.printf("%-15s | %-20s | %-6s | %-12s | %-15s\n", 
                    d.employee_id, 
                    d.dependent_name, 
                    d.sex,
                    d.dob,
                    d.relation);
        }
        System.out.println("=======================================================================================");
        return status.SUCCESS;
    }

    public int displayPerformanceReport(int ssn) throws SQLException{
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }

        if(role.equals("admin")==false && ssn!=this.loggedInUser.employee_id){
            return status.UNAUTHORISED_ACCESS;
        }

        PerformanceReport PR = db.getPerformanceReport(ssn);

        if(PR==null){
            return status.DB_FETCH_ERROR;
        }

        PR.printPerformanceReport();
        return status.SUCCESS;
    }

    public int addSkill(String skill) throws SQLException{
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }

        db.addSkill(this.loggedInUser.employee_id, skill);
        return status.SUCCESS;
    }

    public int addWorkEntry(int project_id,String contribution,int hours,Date date_of_work) throws SQLException{
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }
        
        return db.addWorkLog(this.loggedInUser.employee_id, project_id, contribution, hours, date_of_work);
    }

    public int reassignEmployee(int ssn, int dept_no, int salary) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }
        
        return db.reassignEmployee(ssn, dept_no, salary);
    }

    public int getAllEmployees() throws SQLException {
        if (role == null || this.loggedInUser==null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }
        
        ArrayList<Employee> employees = db.getEmployeesAtBranch(-1, true);
        if (employees != null) {
            printEmployees(employees);
            return status.SUCCESS;
        }

        return status.DB_FETCH_ERROR;
    }

    //LEAVES

    public int requestLeave(Date startDate, Date endDate, int requestTo) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        return db.askForLeave(loggedInUser.employee_id, startDate, endDate, requestTo);
    }

    public int viewLeaveRequests() {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }
        
        ArrayList<Leave> leaveRequests = db.displayLeaveRequests(loggedInUser.employee_id);
        if (leaveRequests == null) {
            return status.DB_FETCH_ERROR;
        }
        
        System.out.println("=======================================================================================");
        System.out.println("                                 LEAVE REQUESTS                                      ");
        System.out.println("=======================================================================================");
        System.out.printf("%-12s | %-12s | %-20s | %-12s | %-12s | %-8s\n", 
                "Leave ID", "Employee ID", "Employee Name", "Start Date", "End Date", "Status");
        System.out.println("---------------------------------------------------------------------------------------");
        
        for (Leave l : leaveRequests) {
            System.out.printf("%-12s | %-12s | %-20s | %-12s | %-12s | %-8s\n", 
                    l.leave_id, 
                    l.employee.employee_id, 
                    l.employee.employee_name,
                    l.start_date,
                    l.end_date,
                    l.approved ? "Approved" : "Pending");
        }
        System.out.println("=======================================================================================");
        return status.SUCCESS;
    }

    public int displayMyLeaveRequests(){
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        ArrayList<Leave> leaveRequests = db.displayMyLeaveRequests(loggedInUser.employee_id);
        if (leaveRequests == null) {
            return status.DB_FETCH_ERROR;
        }
        
        System.out.println("=======================================================================================");
        System.out.println("                                 LEAVE REQUESTS                                      ");
        System.out.println("=======================================================================================");
        System.out.printf("%-12s | %-12s | %-20s | %-12s | %-12s | %-8s\n", 
                "Leave ID", "Employee ID", "Employee Name", "Start Date", "End Date", "Status");
        System.out.println("---------------------------------------------------------------------------------------");
        
        for (Leave l : leaveRequests) {
            System.out.printf("%-12s | %-12s | %-20s | %-12s | %-12s | %-8s\n", 
                    l.leave_id, 
                    l.employee.employee_id, 
                    l.employee.employee_name,
                    l.start_date,
                    l.end_date,
                    l.approved ? "Approved" : "Pending");
        }
        System.out.println("=======================================================================================");
        return status.SUCCESS;
    }

    public int approveLeave(int ssn,int leave_id) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }
        
        return db.approveLeave(ssn, leave_id);
    }

    // REQUESTS

    public int sendRequest(int request_to, String request_description) throws SQLException{
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }
        
        return db.sendRequest(this.loggedInUser.employee_id, request_to, request_description);
    }


    public int viewRequests(){
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }
        
        ArrayList<Request> requests = db.displayRequests(loggedInUser.employee_id);
        if (requests == null) {
            return status.DB_FETCH_ERROR;
        }
        
        System.out.println("=======================================================================================");
        System.out.println("                                   REQUESTS                                           ");
        System.out.println("=======================================================================================");
        System.out.printf("%-12s | %-12s | %-12s | %-30s | %-8s\n", 
                "Request ID", "From Employee", "To Employee", "Description", "Status");
        System.out.println("---------------------------------------------------------------------------------------");
        
        for (Request r : requests) {
            System.out.printf("%-12s | %-12s | %-12s | %-30s | %-8s\n", 
                    r.request_id, 
                    r.request_from, 
                    r.request_to, 
                    r.request_description,
                    r.approved ? "Approved" : "Pending");
        }
        System.out.println("=======================================================================================");
        return status.SUCCESS;
    }

    public int viewMyRequests(){
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }

        
        ArrayList<Request> requests = db.displayMyRequests(loggedInUser.employee_id);
        if (requests == null) {
            return status.DB_FETCH_ERROR;
        }
        
        System.out.println("=======================================================================================");
        System.out.println("                                   REQUESTS                                           ");
        System.out.println("=======================================================================================");
        System.out.printf("%-12s | %-12s | %-12s | %-30s | %-8s\n", 
                "Request ID", "From Employee", "To Employee", "Description", "Status");
        System.out.println("---------------------------------------------------------------------------------------");
        
        for (Request r : requests) {
            System.out.printf("%-12s | %-12s | %-12s | %-30s | %-8s\n", 
                    r.request_id, 
                    r.request_from, 
                    r.request_to, 
                    r.request_description,
                    r.approved ? "Approved" : "Pending");
        }
        System.out.println("=======================================================================================");
        return status.SUCCESS;
    }

    public int approveRequest(int request_id) throws SQLException{
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }
        
        return db.approveRequest(loggedInUser.employee_id, request_id);
    }

    //MEETINGS

    public int createMeeting(Meeting meeting) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }

        meeting.meeting_organiser_id = this.loggedInUser.employee_id;
        
        return db.createMeeting(meeting);
    }

    public int joinMeeting(int meeting_id) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        return db.joinMeeting(loggedInUser.employee_id, meeting_id);
    }

    public int leaveMeeting(int meeting_id) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        return db.leaveMeeting(loggedInUser.employee_id, meeting_id);
    }

    public int showMyMeetings(){
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }
        
        ArrayList<Meeting> meetings = db.getMeetings(loggedInUser.employee_id);
        if (meetings == null) {
            return status.DB_FETCH_ERROR;
        }

        System.out.println("===================================================================");
        System.out.println("                          MEETINGS                                 ");
        System.out.println("===================================================================");
        
        for (Meeting m : meetings) {
            System.out.println("-------------------------------------------------------------------");
            System.out.println("Meeting ID:       " + m.meeting_id);
            System.out.println("Date:             " + m.meeting_date);
            System.out.println("Time:             " + m.meeting_start_time + " - " + 
                              (m.meeting_end_time != null ? m.meeting_end_time : "TBD"));
            System.out.println("Location:         " + (m.meeting_location != null ? m.meeting_location : "N/A"));
            System.out.println("Mode:             " + m.meeting_mode);
            System.out.println("Department:       " + m.department_id);
            System.out.println("Organizer ID:     " + m.meeting_organiser_id);
            System.out.println("Purpose:          " + (m.purpose_of_meeting != null ? m.purpose_of_meeting : "N/A"));
            
            System.out.println("Minutes:          " + 
                              (m.minutes_of_meeting != null ? m.minutes_of_meeting: "Not Available"));
        }
        
        System.out.println("===================================================================");
        
        return status.SUCCESS;
    }

    public int updateMoM(int meeting_id, String mom) throws SQLException {
        if (role == null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        if (!role.equals("admin")) {
            return status.UNAUTHORISED_ACCESS;
        }
        
        return db.addMoM(meeting_id, mom);
    }

    public int getExperienceReport() throws SQLException{
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }

        return displayPerformanceReport(this.loggedInUser.employee_id);
    }

    public int getEmployeesAtYourBranch() throws SQLException {
        if (role == null || this.loggedInUser==null) {
            return status.USER_NOT_LOGGED_IN;
        }
        
        ArrayList<Employee> employees = db.getEmployeesAtBranch(loggedInUser.office_location_id, false);
        if (employees != null) {
            printEmployees(employees);
            return status.SUCCESS;
        }
        return  status.DB_FETCH_ERROR;
    }

    public int viewDepartments(){
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }
        
        ArrayList<Department> departments = db.getDepartments();
        if (departments == null) {
            return status.DB_FETCH_ERROR;
        }
        
        System.out.println("=======================================================================================");
        System.out.println("                                DEPARTMENT DETAILS                                    ");
        System.out.println("=======================================================================================");
        System.out.printf("%-15s | %-30s | %-30s\n", 
                "Department ID", "Department Name", "Manager ID");
        System.out.println("---------------------------------------------------------------------------------------");
        
        for (Department d : departments) {
            System.out.printf("%-15s | %-30s | %-30s\n", 
                    d.department_id, 
                    d.department_name, 
                    d.manager_id);
        }
        System.out.println("=======================================================================================");
        return status.SUCCESS;
    }

    public int viewOfficeBranches(){
        if(this.role==null || this.loggedInUser==null){
            return status.USER_NOT_LOGGED_IN;
        }
        
        ArrayList<OfficeBranch> branches = db.getOfficeBranchs();
        if (branches == null) {
            return status.DB_FETCH_ERROR;
        }
        
        System.out.println("=======================================================================================");
        System.out.println("                                OFFICE BRANCHES                                       ");
        System.out.println("=======================================================================================");
        System.out.printf("%-15s | %-30s | %-30s\n", 
                "Branch ID", "Branch Location", "Branch Area (in sqft)");
        System.out.println("---------------------------------------------------------------------------------------");
        
        for (OfficeBranch b : branches) {
            System.out.printf("%-15s | %-30s | %-30s\n", 
                    b.branch_id, 
                    b.address+" ,"+b.city+", "+b.state+", "+b.country,
                    b.size_of_office);
        }
        System.out.println("=======================================================================================");
        return status.SUCCESS;
    }

    private void printEmployees(ArrayList<Employee> employees) {
        System.out.println("====================================================================================================");
        System.out.println("                                      EMPLOYEE LIST                                                 ");
        System.out.println("====================================================================================================");
        System.out.printf("%-15s | %-20s | %-6s | %-12s | %-15s | %-15s |\n", 
                "Employee ID", "Name", "Salary", "Department", "Manager ID", "Office Location");
        System.out.println("----------------------------------------------------------------------------------------------------");
        
        for (Employee e : employees) {
            System.out.printf("%-15s | %-20s | %-6d | %-12s | %-15s | %-15s |\n", 
                    e.employee_id, 
                    e.employee_name, 
                    e.salary,
                    e.department_id,
                    e.manager_id >0 ? e.manager_id : "N/A",
                    e.office_location_id);
        }
        System.out.println("===================================================================================================");
    }
}
