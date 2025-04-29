package management_systems;

import classes.*;
import java.sql.*;
import java.util.ArrayList;

public class DB {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeedb";
    private final Credentials sql_credential;
    private final StatusCodes status;
    private Connection db;

    public DB(){
        sql_credential = new Credentials();
        this.db = null;
        status = new StatusCodes();
    }

    public void openDB(){
        try {
            Class.forName(DB.JDBC_DRIVER);
            this.db = DriverManager.getConnection(DB_URL,sql_credential.getUSER(),sql_credential.getPASSWORD());
            this.db.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDB(){
        try {
            db.commit();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Deals with basic CRUD operations on employee */
    public Employee getEmployee(int ssn) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("select * from employees as e where e.employee_id=?");
            pstmt.setInt(1,ssn);
            ResultSet rs = pstmt.executeQuery();

            Employee e = null;
            if(rs.next()){
                e = new Employee(rs);
            }
            

            rs.close();
            return e;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Employee> getEmployeesAtBranch(int branchID,boolean getAll) throws SQLException{
        try{
            PreparedStatement pstmt;
            if(getAll==false){
                pstmt = db.prepareStatement("select * from employees as e where e.office_location_id=?");
                pstmt.setInt(1, branchID);
            }
            else{
                pstmt = db.prepareStatement("select * from employees as e");
            }
            ArrayList<Employee>employees = new ArrayList<>();
            
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Employee employee = new Employee(rs);
                employees.add(employee);
            }

            rs.close();
            return employees;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addEmployee(Employee employee) throws SQLException{
        try {
            int employeeId = employee.employee_id;
            String employeePassword = employee.employee_password;
            String employeeName = employee.employee_name;
            int salary = employee.salary;
            int departmentId = employee.department_id;
            int managerId = employee.manager_id;
            int officeLocationId = employee.office_location_id;
            Date dob = employee.dob;
            Date doj = employee.doj;
            String houseAddress = employee.house_address;
            String sex = employee.sex;

            PreparedStatement pstmt = db.prepareStatement("insert into employees (employee_password, employee_name, salary, department_id,manager_id, office_location_id, dob, doj, house_address, sex) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setString(1, employeePassword);
            pstmt.setString(2, employeeName);
            pstmt.setInt(3, salary);
            pstmt.setInt(4, departmentId);
            pstmt.setInt(5, managerId);
            pstmt.setInt(6, officeLocationId);
            pstmt.setDate(7, dob);
            pstmt.setDate(8, doj);
            pstmt.setString(9, houseAddress);
            pstmt.setString(10, sex);
            int result = pstmt.executeUpdate();

            if(employeePassword==null || employeeName==null || salary==0 || departmentId<=0 || officeLocationId<=0 || dob==null || doj==null || houseAddress==null || sex==null){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result == 1){
                db.commit();
                return status.SUCCESS;
            }
            System.out.println(result);

            return status.DB_INSERTION_ERROR;
        } catch (Exception error) {
            db.rollback();
            error.printStackTrace();
            return status.DB_INSERTION_ERROR;
        }
    }

    public int removeEmployee(int ssn,Employee employee) throws SQLException{
        try {
            if(ssn==employee.manager_id){
                return status.INVALID_VALUE;
            }
            PreparedStatement pstmt = db.prepareStatement("delete from employees where employee_id=?");
            pstmt.setInt(1, ssn);
            int result = pstmt.executeUpdate();

            if(result>=0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_DELETION_ERROR;

        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            return status.DB_DELETION_ERROR;
        }
    }

    public int updateEmployeePassword(int employee_id,String employee_password) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("update employees set employee_password=? where employee_id=?");
            pstmt.setString(1,employee_password);
            pstmt.setInt(2,employee_id);
            
            int result = pstmt.executeUpdate();

            if(employee_password==null || employee_id<=0){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_UPDATION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return status.DB_UPDATION_ERROR;
        }
    }

    public int reassignEmployee(int ssn,int dept_no,int salary) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("update employees set department_id=?,salary=? where employee_id=?");
            pstmt.setInt(1,dept_no);
            pstmt.setInt(2,salary);
            pstmt.setInt(3,ssn);
            
            int result = pstmt.executeUpdate();

            if(ssn<=0 || dept_no<0 || salary<=0){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_UPDATION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return status.DB_UPDATION_ERROR;
        }
    }

    /*Deals with employee dependents */
    public ArrayList<Dependent> getEmployeeDependents(int ssn) throws SQLException{
        try{
            PreparedStatement pstmt = db.prepareStatement("select * from dependents as d where d.employee_id=?");
            pstmt.setInt(1,ssn);
            ResultSet result = pstmt.executeQuery();
            ArrayList<Dependent>dependents = new ArrayList<Dependent>();

            while(result.next()){
                int employee_id = result.getInt("employee_id");
                String dependent_name = result.getString("dependent_name");
                String sex = result.getString("sex");
                Date dob = result.getDate("dob");
                String relation = result.getString("relation");
                Dependent d = new Dependent(employee_id,dependent_name,sex,dob,relation);
                dependents.add(d);
            }

            result.close();
            return dependents;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*Deals with employee performance report */
    public PerformanceReport getPerformanceReport(int ssn) throws SQLException{
        try {
            Employee employee = getEmployee(ssn);
            ArrayList<String>skills = new ArrayList<>();
            WorkReport work_reports = new WorkReport();
            PreparedStatement pstmt = db.prepareStatement("select * from skills where employee_id = ?");
            pstmt.setInt(1,ssn);
            ResultSet result = pstmt.executeQuery();
            while(result.next()){
                skills.add(result.getString("skill_name"));
            }
            result.close();

            PreparedStatement pstmt2 = db.prepareStatement("select * from projects as p INNER JOIN work_log as w ON p.project_id=w.project_id where w.employee_id=?");
            pstmt2.setInt(1,ssn);
            ResultSet rs = pstmt2.executeQuery();

            while(rs.next()){
                Project p = new Project(rs);

                int hours = rs.getInt("hours_worked");
                String contribution = rs.getString("contribution");
                Date date_of_work =  rs.getDate("date_of_work");

                work_reports.addWorkEntry(p,hours,contribution,date_of_work);
            }

            rs.close();

            PerformanceReport report = new PerformanceReport(employee, skills, work_reports);
            return report;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*Deals with employee work logs */
    public int addWorkLog(int ssn,int projectID,String contribution,int hours,Date date_of_work) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("insert into work_log(employee_id,project_id,hours_worked,date_of_work,contribution) values (?,?,?,?,?)");
            pstmt.setInt(1, ssn);
            pstmt.setInt(2,projectID);
            pstmt.setInt(3,hours);
            pstmt.setDate(4,date_of_work);
            pstmt.setString(5,contribution);
            int result = pstmt.executeUpdate();

            if(hours<=0 || ssn<=0 || projectID<=0 || contribution==null || date_of_work==null){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_INSERTION_ERROR;
        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            return status.DB_INSERTION_ERROR;
        }       
    }

    /*Deals with employee skills */
    public int addSkill(int ssn,String skill) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("insert into skills(employee_id,skill_name) values (?,?)");
            pstmt.setInt(1, ssn);
            pstmt.setString(2,skill);
            int result = pstmt.executeUpdate();

            if(ssn<=0 || skill==null){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_INSERTION_ERROR;
        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            return status.DB_INSERTION_ERROR;
        }
    }

    /*Deals with meetings */
    public int joinMeeting(int ssn,int meeting_id) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("insert into attends_meeting(employee_id,meeting_id) values (?,?)");
            pstmt.setInt(1,ssn);
            pstmt.setInt(2,meeting_id);
            int result = pstmt.executeUpdate();
            Date nowD = status.getCurrentDate();
            Time nowT = status.getCurrentTime();

            PreparedStatement tester = db.prepareStatement("select meeting_date,meeting_start_time,meeting_end_time from meetings where meeting_id=?");
            tester.setInt(1, meeting_id);
            ResultSet test_set = tester.executeQuery();
            Date meeting_date = null;
            Time meeting_start_time = null;
            if(test_set.next()){
                meeting_date = test_set.getDate("meeting_date");
                meeting_start_time = test_set.getTime("meeting_start_time");
            }

            if(ssn <=0 || meeting_id <=0 || (nowD.after(meeting_date) || (nowD.equals(meeting_date) && nowT.after(meeting_start_time)))){
                db.rollback();
                return status.INVALID_VALUE;
            }
            
            if(result>0){
                db.commit();
                return status.SUCCESS;
            }
            return status.DB_INSERTION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return status.DB_INSERTION_ERROR;
        }
    }

    public int leaveMeeting(int ssn,int meeting_id) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("delete from attends_meeting where employee_id=? and meeting_id=?");
            pstmt.setInt(1,ssn);
            pstmt.setInt(2,meeting_id);
            int result = pstmt.executeUpdate();

            Date nowD = status.getCurrentDate();
            Time nowT = status.getCurrentTime();
            PreparedStatement tester = db.prepareStatement("select meeting_date,meeting_start_time,meeting_end_time from meetings where meeting_id=?");
            tester.setInt(1, meeting_id);
            ResultSet test_set = tester.executeQuery();
            Date meeting_date = null;
            Time meeting_start_time = null;
            if(test_set.next()){
                meeting_date = test_set.getDate("meeting_date");
                meeting_start_time = test_set.getTime("meeting_start_time");
            }

            if(ssn <=0 || meeting_id <=0 || (nowD.after(meeting_date) || (nowD.equals(meeting_date) && nowT.after(meeting_start_time)))){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_DELETION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return status.DB_DELETION_ERROR;
        }
    }

    public int createMeeting(Meeting meeting) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("insert into meetings (meeting_mode, meeting_start_time, meeting_end_time, meeting_location, meeting_organiser_id, department_id, purpose_of_meeting,meeting_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,meeting.meeting_mode);
            pstmt.setTime(2,meeting.meeting_start_time);
            pstmt.setTime(3,meeting.meeting_end_time);
            pstmt.setString(4,meeting.meeting_location);
            pstmt.setInt(5,meeting.meeting_organiser_id);
            pstmt.setInt(6,meeting.department_id);
            pstmt.setString(7,meeting.purpose_of_meeting);
            pstmt.setDate(8,meeting.meeting_date);
            int result = pstmt.executeUpdate();

            int meeting_id = -1;
            ResultSet generated_pk = pstmt.getGeneratedKeys();
            if(generated_pk.next()){
                meeting_id = generated_pk.getInt(1);
            }

        
            PreparedStatement sub_pstmt = db.prepareStatement("insert into attends_meeting(meeting_id,employee_id) values (?,?)");
            sub_pstmt.setInt(1,meeting_id);
            sub_pstmt.setInt(2,meeting.meeting_organiser_id);
            int sub_result = sub_pstmt.executeUpdate();


            Date nowD = status.getCurrentDate();
            Time nowT = status.getCurrentTime();

            if(meeting.meeting_mode == null || meeting.meeting_start_time == null ||
            meeting.meeting_end_time == null || meeting.meeting_location == null ||
            meeting.meeting_organiser_id <= 0 || meeting.department_id <= 0 ||
            meeting.purpose_of_meeting == null || meeting.meeting_date == null ||
            meeting.meeting_date.before(nowD) ||
            (meeting.meeting_date.equals(nowD) && meeting.meeting_start_time.before(nowT)) ||
            (meeting.meeting_date.equals(nowD) && meeting.meeting_end_time.before(nowT)) ||
            meeting.meeting_end_time.before(meeting.meeting_start_time)){
                db.rollback();
                System.out.println(meeting_id);
                System.out.println("CAME HERE");
                return status.INVALID_VALUE;
            }

            if(result>0 && sub_result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_INSERTION_ERROR;
        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            return status.DB_INSERTION_ERROR;
        }
    }

    public ArrayList<Meeting> getMeetings(int ssn){
        try {
            PreparedStatement pstmt = db.prepareStatement("select * from meetings as m INNER JOIN attends_meeting as am ON m.meeting_id=am.meeting_id where am.employee_id=?");
            pstmt.setInt(1,ssn);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Meeting> meetings = new ArrayList<>();

            while(rs.next()){
                Meeting meeting = new Meeting(rs);
                meetings.add(meeting);
            }

            rs.close();

            return meetings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addMoM(int meeting_id,String MoM) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("update meetings set minutes_of_meeting=? where meeting_id=?");
            pstmt.setString(1,MoM);
            pstmt.setInt(2,meeting_id);
            
            int result = pstmt.executeUpdate();

            if(meeting_id<=0 || MoM==null){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_UPDATION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return status.DB_UPDATION_ERROR;
        }
    }
    
    /*Deals with Leave Requests */
    public int askForLeave(int ssn,Date starDate,Date endDate,int requestTo) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("insert into employee_leaves(employee_id,request_to,start_date,end_date) values (?,?,?,?)");
            pstmt.setInt(1,ssn);
            pstmt.setInt(2,requestTo);
            pstmt.setDate(3,starDate);
            pstmt.setDate(4,endDate);

            int result = pstmt.executeUpdate();

            if(ssn<=0 || starDate==null || endDate==null || requestTo<=0 || (endDate.before(starDate) && endDate.equals(starDate)==false)){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_INSERTION_ERROR;
        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            return status.DB_INSERTION_ERROR;
        }
    }

    public ArrayList<Leave> displayLeaveRequests(int ssn){
        try{
            PreparedStatement pstmt = db.prepareStatement("select * from employee_leaves as el where el.request_to=?");
            pstmt.setInt(1,ssn);
            ResultSet result = pstmt.executeQuery();
            ArrayList<Leave>leaveRequests = new ArrayList<Leave>();

            while(result.next()){
                Leave leave = new Leave(result);
                PreparedStatement sub_pstmt = db.prepareStatement("select * from employees as e where e.employee_id=?");
                sub_pstmt.setString(1,result.getString("employee_id"));
                ResultSet rs = sub_pstmt.executeQuery();
                Employee employee = null;
                if(rs.next()){
                    employee = new Employee(rs);
                }
                leave.setEmployee(employee);
                rs.close();
                leaveRequests.add(leave);
            }

            result.close();
            return leaveRequests;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Leave> displayMyLeaveRequests(int ssn){
        try{
            PreparedStatement pstmt = db.prepareStatement("select * from employee_leaves as el where el.employee_id=?");
            pstmt.setInt(1,ssn);
            ResultSet result = pstmt.executeQuery();
            ArrayList<Leave>leaveRequests = new ArrayList<Leave>();

            while(result.next()){
                Leave leave = new Leave(result);
                PreparedStatement sub_pstmt = db.prepareStatement("select * from employees as e where e.employee_id=?");
                sub_pstmt.setInt(1,result.getInt("employee_id"));
                ResultSet rs = sub_pstmt.executeQuery();
                Employee employee = null;
                if(rs.next()){
                    employee = new Employee(rs);
                }
                leave.setEmployee(employee);
                rs.close();
                leaveRequests.add(leave);
            }

            result.close();
            return leaveRequests;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int approveLeave(int ssn,int leave_id) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("update employee_leaves set approved=? where leave_id=?");
            pstmt.setBoolean(1,true);
            pstmt.setInt(2,leave_id);
            
            int result = pstmt.executeUpdate();

            PreparedStatement tester  = db.prepareStatement("select request_to from employee_leaves where leave_id=?");
            tester.setInt(1,leave_id);
            ResultSet test_set = tester.executeQuery();
            int approver_ssn = -1;
            if(test_set.next()==true){
                approver_ssn = test_set.getInt("request_to");
            }
            

            if(result>0 && approver_ssn==ssn){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_UPDATION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return status.DB_UPDATION_ERROR;
        }
    }

    /* Deals with Requests */
    public int sendRequest(int request_from,int request_to,String request_description) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("insert into requests(request_from,request_to,request_description) values (?,?,?)");
            pstmt.setInt(1,request_from);
            pstmt.setInt(2,request_to);
            pstmt.setString(3,request_description);

            int result = pstmt.executeUpdate();

            if(request_from<=0 || request_to<=0 || request_description==null){
                db.rollback();
                return status.INVALID_VALUE;
            }

            if(result>0){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_INSERTION_ERROR;
        } catch (Exception e) {
            db.rollback();
            e.printStackTrace();
            return status.DB_INSERTION_ERROR;
        }
    }

    public ArrayList<Request> displayRequests(int ssn){
        try{
            PreparedStatement pstmt = db.prepareStatement("select * from requests as r where r.request_to=?");
            pstmt.setInt(1,ssn);
            ResultSet result = pstmt.executeQuery();
            ArrayList<Request>requests = new ArrayList<Request>();

            while(result.next()){
                Request request = new Request(result);
                requests.add(request);
            }

            result.close();
            return requests;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Request> displayMyRequests(int ssn){
        try{
            PreparedStatement pstmt = db.prepareStatement("select * from requests as r where r.request_from=?");
            pstmt.setInt(1,ssn);
            ResultSet result = pstmt.executeQuery();
            ArrayList<Request>requests = new ArrayList<Request>();

            while(result.next()){
                Request request = new Request(result);
                requests.add(request);
            }

            result.close();
            return requests;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int approveRequest(int ssn,int request_id) throws SQLException{
        try {
            PreparedStatement pstmt = db.prepareStatement("update requests set approved=? where request_id=?");
            pstmt.setBoolean(1,true);
            pstmt.setInt(2,request_id);
            
            int result = pstmt.executeUpdate();

            PreparedStatement tester  = db.prepareStatement("select request_to from requests where request_id=?");
            tester.setInt(1,request_id);
            ResultSet test_set = tester.executeQuery();
            int approver_ssn = -1;
            if(test_set.next()==true){
                approver_ssn = test_set.getInt("request_to");
            }
            

            if(result>0 && approver_ssn==ssn){
                db.commit();
                return status.SUCCESS;
            }

            return status.DB_UPDATION_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
            return status.DB_UPDATION_ERROR;
        }
    }

    /*Deals with: Office Departments */
    public ArrayList<Department> getDepartments(){
        try {
            PreparedStatement pstmt = db.prepareStatement("select * from departments");
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Department> departments = new ArrayList<>();

            while(rs.next()){
                Department department = new Department(rs);
                departments.add(department);
            }

            rs.close();

            return departments;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getDepartmentNumber(String d_name){
        try {
            PreparedStatement pstmt = db.prepareStatement("select department_id from departments where department_name=?");
            pstmt.setString(1,d_name);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getInt("department_id");
            }

            rs.close();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*Deals with: Office Branches */
    public ArrayList<OfficeBranch> getOfficeBranchs(){
        try {
            PreparedStatement pstmt = db.prepareStatement("select * from office_branches");
            ResultSet rs = pstmt.executeQuery();
            ArrayList<OfficeBranch> branchs = new ArrayList<>();

            while(rs.next()){
                OfficeBranch branch = new OfficeBranch(rs);
                branchs.add(branch);
            }

            rs.close();

            return branchs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
