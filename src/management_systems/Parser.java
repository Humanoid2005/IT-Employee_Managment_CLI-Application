package management_systems;

import classes.Employee;
import classes.Meeting;
import classes.StatusCodes;
import java.io.PrintStream;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private PrintStream out;
    private EmployeeManager employee_manager;
    private StatusCodes status;
    private final String RESET = "\u001B[0m";
    private final String RED = "\u001B[31m";
    private final String GREEN = "\u001B[32m";
    private final String YELLOW = "\u001B[93m";
    private final String BLUE = "\u001B[34m";
    private final String BLACK = "\u001B[30m";
    private final String GREENBG = "\u001B[102m";
    private final String BLUEBG = "\u001B[104m";
    private final String YELLOWBG = "\u001B[103m";
    private final String BOLD = "\u001B[1m";


    public Parser(PrintStream out){
        this.out = out;
        this.employee_manager = new EmployeeManager();
        this.status = new StatusCodes();
    }

    public void print(String message){
        this.out.println(message);
    }

    public void printSuccess(String message) {
        this.out.println(GREEN + message + RESET);
    }

    public void printError(String message) {
        this.out.println(RED + message + RESET);
    }

    public void printWarning(String message) {
        this.out.println(YELLOW + message + RESET);
    }

    public void printHeader(String message) {
        this.out.println(BOLD + BLUEBG + BLACK + " " + message + " " + RESET);
    }

    public void displayHelp(){

    }

    public void processCommands(Scanner scanner){
        employee_manager.openDB();
        printSuccess("DB connection has been established...");
        while(scanner.hasNextLine()){
            String command = scanner.nextLine();
            ArrayList<String> parsed_command = parseCommand(command); 
            String command_type = parsed_command.get(0).toLowerCase();
            int result = 1;

            try {
                switch(command_type){
                    case "exit":
                        employee_manager.closeDB();
                        printSuccess("DB connection has been successfully closed...");
                        printSuccess("Exiting Employee Management System. Goodbye!");
                        System.exit(0);
                        break;

                    case "login":
                        if (parsed_command.size() < 3) {
                            printError("Usage: login <SSN> <password>");
                            break;
                        }
                        int ssn = Integer.parseInt(parsed_command.get(1));
                        String password = parsed_command.get(2);
                        result = employee_manager.login(ssn, password);
                        if (result == status.SUCCESS) {
                            printSuccess("Successfully logged in");
                        } else if (result == status.WRONG_PASSWORD) {
                            printError("Wrong password");
                        } else {
                            printError("Invalid user");
                        }
                        break;

                    case "change_password":
                        if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                            printError("You must be logged in to perform this action");
                            break;
                        }
                        if (parsed_command.size() < 2) {
                            printError("Usage: change_password <new_password>");
                            break;
                        }
                        String newPassword = parsed_command.get(1);
                        result = employee_manager.changePassword(newPassword);
                        if (result == status.SUCCESS) {
                            printSuccess("Password changed successfully");
                        } else {
                            printError("Failed to change password");
                        }
                        break;    
                    
                    case "logout":
                        if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                            printError("You must be logged in to perform this action");
                            break;
                        }
                        result = employee_manager.logout(employee_manager.loggedInUser.employee_id);
                        if (result == status.SUCCESS) {
                            printSuccess("Successfully logged out");
                        } else {
                            printError("Failed to logout");
                        }
                        break;

                    case "help":
                        showHelp();
                        break;

                    case "add_employee":
                        if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                            printError("You must be logged in to perform this action");
                            break;
                        }
                        if (parsed_command.size() < 11) {
                            printError("Usage: add_employee <name> <password> <salary> <dept_id> <manager_id> <office_id> <dob(yyyy-MM-dd)> <doj(yyyy-MM-dd)> <address> <sex>");
                            break;
                        }
                        Employee e = new Employee();
                        e.employee_name = parsed_command.get(1);
                        e.employee_password = parsed_command.get(2);
                        e.salary = Integer.parseInt(parsed_command.get(3));
                        e.department_id = Integer.parseInt(parsed_command.get(4));
                        e.manager_id = Integer.parseInt(parsed_command.get(5));
                        e.office_location_id = Integer.parseInt(parsed_command.get(6));
                        e.dob = Date.valueOf(parsed_command.get(7));
                        e.doj = Date.valueOf(parsed_command.get(8));
                        e.house_address = parsed_command.get(9);
                        e.sex = parsed_command.get(10);
                                
                        result = employee_manager.register(e);
                        if (result == status.SUCCESS) {
                            printSuccess("Employee added successfully");
                        } else if (result == status.UNAUTHORISED_ACCESS) {
                            printError("You don't have permission to add employees");
                        } else {
                            printError("Failed to add employee");
                        }
                        break;
                    
                        case "fire_employee":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 2) {
                                printError("Usage: fire_employee <ssn>");
                                break;
                            }
                            int targetSSN = Integer.parseInt(parsed_command.get(1));
                            result = employee_manager.removeEmployee(targetSSN);
                            if (result == status.SUCCESS) {
                                printSuccess("Employee fired successfully");
                            } else if (result == status.UNAUTHORISED_ACCESS) {
                                printError("You don't have permission to fire employees");
                            } else {
                                printError("Failed to fire employee");
                            }
                            break;

                        case "view_employee_dependents":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 2) {
                                printError("Usage: view_employee_dependents <ssn>");
                                break;
                            }
                            int VEDtargetSSN = Integer.parseInt(parsed_command.get(1));
                            result = employee_manager.printEmployeeDependents(VEDtargetSSN);
                            if (result != status.SUCCESS) {
                                if (result == status.UNAUTHORISED_ACCESS) {
                                    printError("You don't have permission to view employee dependents");
                                } else {
                                    printError("Failed to fetch employee dependents");
                                }
                            }
                            break;

                        case "get_employee_performance_report":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 2) {
                                printError("Usage: get_employee_performance_report <ssn>");
                                break;
                            }
                            int EPRtargetSSN = Integer.parseInt(parsed_command.get(1));
                            result = employee_manager.displayPerformanceReport(EPRtargetSSN);
                            if (result != status.SUCCESS) {
                                if (result == status.UNAUTHORISED_ACCESS) {
                                    printError("You don't have permission to view this employee's report");
                                } else {
                                    printError("Failed to fetch employee report");
                                }
                            }
                            break;
                        
                        case "add_skill":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 2) {
                                printError("Usage: add_skill <skill>");
                                break;
                            }
                            String skill = parsed_command.get(1);
                            result = employee_manager.addSkill(skill);
                            if(result==status.SUCCESS){
                                printSuccess(skill+" successfully added to your skill list");
                            }
                            else{
                                printError("Failed to add to skill to your skill list");
                            }
                            break;

                        case "add_work_entry":
                        if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                            printError("You must be logged in to perform this action");
                            break;
                        }
                        if (parsed_command.size() < 5) {
                            printError("Usage: add_work_entry <project_id> <contribution> <hours_worked> <date_of_work(yyyy-mm-dd)");
                            break;
                        }
                        int Wproject_id = Integer.parseInt(parsed_command.get(1));
                        String contribution = parsed_command.get(2);
                        int Whours_worked = Integer.parseInt(parsed_command.get(3));
                        Date date_of_work = Date.valueOf(parsed_command.get(4));
                        result  = employee_manager.addWorkEntry(Wproject_id,contribution,Whours_worked,date_of_work);
                        if (result == status.SUCCESS) {
                            printSuccess("Work entry added successfully");
                        }
                        else {
                            printError("Failed to add employee work entry");
                        }
                        break;
                        case "reassign_employee":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 4) {
                                printError("Usage: reassign_employee <ssn> <dept_no> <salary>");
                                break;
                            }
                            int RtargetSSN = Integer.parseInt(parsed_command.get(1));
                            int deptNo = Integer.parseInt(parsed_command.get(2));
                            int salary = Integer.parseInt(parsed_command.get(3));
                            result = employee_manager.reassignEmployee(RtargetSSN, deptNo, salary);
                            if (result == status.SUCCESS) {
                                printSuccess("Employee reassigned successfully");
                            } else if (result == status.UNAUTHORISED_ACCESS) {
                                printError("You don't have permission to reassign employees");
                            } else {
                                printError("Failed to reassign employee");
                            }
                            break;

                        case "view_all_employees":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.getAllEmployees();
                            if (result != status.SUCCESS) {
                                if (result == status.UNAUTHORISED_ACCESS) {
                                    printError("You don't have permission to view all employees");
                                } else {
                                    printError("Failed to fetch employees");
                                }
                            }
                            break;

                        case "request_leave":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 4) {
                                printError("Usage: request_leave <request_to_ssn> <start_date(yyyy-MM-dd)> <end_date(yyyy-MM-dd)>");
                                break;
                            }
                            int requestTo = Integer.parseInt(parsed_command.get(1));
                            Date startDate = Date.valueOf(parsed_command.get(2));
                            Date endDate = Date.valueOf(parsed_command.get(3));
                            result = employee_manager.requestLeave(startDate, endDate, requestTo);
                            if (result == status.SUCCESS) {
                                printSuccess("Leave request sent successfully");
                            } else {
                                printError("Failed to send leave request");
                            }
                            break;

                        case "view_leave_requests":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.viewLeaveRequests();
                            if (result != status.SUCCESS) {
                                if (result == status.UNAUTHORISED_ACCESS) {
                                    printError("You don't have permission to view leave requests");
                                } else {
                                    printError("Failed to fetch leave requests");
                                }
                            }
                        
                        case "view_my_leave_requests":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.displayMyLeaveRequests();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch your leave requests");
                            }
                            break;

                        case "approve_leave":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            } 
                            if (parsed_command.size() < 2) {
                                printError("Usage: approve_leave <leave_id>");
                                break;
                            }
                            int leaveId = Integer.parseInt(parsed_command.get(1));
                            result = employee_manager.approveLeave(employee_manager.loggedInUser.employee_id, leaveId);
                            if (result == status.SUCCESS) {
                                printSuccess("Leave approved successfully");
                            } else if (result == status.UNAUTHORISED_ACCESS) {
                                printError("You don't have permission to approve leaves");
                            } else {
                                printError("Failed to approve leave");
                            }
                            break;

                        case "send_request":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            } 
                            if (parsed_command.size() < 3) {
                                printError("Usage: send_request <request_to_ssn> <description>");
                                break;
                            }

                            int RequestTo = Integer.parseInt(parsed_command.get(1));
                            String description = parsed_command.get(2);
                            result = employee_manager.sendRequest(RequestTo, description);
                            if (result == status.SUCCESS) {
                                printSuccess("Request sent successfully");
                            } else {
                                printError("Failed to send request");
                            }
                            break;
                        
                        case "view_requests":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.viewRequests();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch requests");
                            }
                            break;

                        case "view_my_requests":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.viewMyRequests();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch requests");

                            }
                            break;

                        case "accept_request":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 2) {
                                printError("Usage: accept_request <request_id>");
                                break;
                            }
                            int requestId = Integer.parseInt(parsed_command.get(1));
                            result = employee_manager.approveRequest(requestId);
                            if (result == status.SUCCESS) {
                                printSuccess("Request accepted successfully");
                            } else if (result == status.UNAUTHORISED_ACCESS) {
                                printError("You don't have permission to accept this request");
                            } else {
                                printError("Failed to approve request");
                            }
                            break;

                        case "create_meeting":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 8) {
                                printError("Usage: create_meeting <meeting_date(yyyy-mm-dd)> <meeting_start_time(hh:mm)> <meeting_end_time(hh:mm)> <meeting_mode> <meeting_location> <department_number> <purpose of meeting> ");
                                break;
                            }
                            Meeting meet = new Meeting();
                            meet.meeting_date = Date.valueOf(parsed_command.get(1));
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            java.util.Date parsedStartTime = timeFormat.parse(parsed_command.get(2));
                            java.util.Date parsedEndTime = timeFormat.parse(parsed_command.get(3));
                            meet.meeting_start_time = new Time(parsedStartTime.getTime());
                            meet.meeting_end_time = new Time(parsedEndTime.getTime());
                            meet.meeting_mode = parsed_command.get(4);
                            meet.meeting_location = parsed_command.get(5);
                            meet.department_id = Integer.parseInt(parsed_command.get(6));
                            meet.purpose_of_meeting = parsed_command.get(7);
                            meet.meeting_organiser_id = employee_manager.loggedInUser.employee_id;

                            result = employee_manager.createMeeting(meet);
                            if (result == status.SUCCESS) {
                                printSuccess("Meeting created successfully");
                            } else if (result == status.UNAUTHORISED_ACCESS) {
                                printError("You don't have permission to create meetings");
                            } else {
                                printError("Failed to create meeting");
                            }
                            break;

                        case "join_meeting":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 2) {
                                printError("Usage: join_meeting <meeting_id>");
                                break;
                            }
                            int meetingId = Integer.parseInt(parsed_command.get(1));
                            result = employee_manager.joinMeeting(meetingId);
                            if (result == status.SUCCESS) {
                                printSuccess("Joined meeting successfully");
                            } else {
                                printError("Failed to join meeting");
                            }
                            break;

                        case "leave_meeting":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 2) {
                                printError("Usage: leave_meeting <meeting_id>");
                                break;
                            }
                            int LmeetingId = Integer.parseInt(parsed_command.get(1));
                            result = employee_manager.leaveMeeting(LmeetingId);
                            if (result == status.SUCCESS) {
                                printSuccess("Left meeting successfully");
                            } else {
                                printError("Failed to leave meeting");
                            }
                            break;

                        case "view_my_meetings":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.showMyMeetings();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch meetings");
                            }
                            break; 

                        case "update_MoM":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            if (parsed_command.size() < 3) {
                                printError("Usage: update_mom <meeting_id> <minutes_of_meeting>");
                                break;
                            }
                            int MoMmeetingId = Integer.parseInt(parsed_command.get(1));
                            String mom = parsed_command.get(2);
                            result = employee_manager.updateMoM(MoMmeetingId, mom);
                            if (result == status.SUCCESS) {
                                printSuccess("Meeting minutes updated successfully");
                            } else if (result == status.UNAUTHORISED_ACCESS) {
                                printError("You don't have permission to update meeting minutes");
                            } else {
                                printError("Failed to update meeting minutes");
                            }
                            break;

                        case "get_experience_report":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.getExperienceReport();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch experience report");
                            }
                            break;

                        case "view_branch_employees":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.getEmployeesAtYourBranch();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch branch employees");
                            }
                            break;

                        case "view_departments":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.viewDepartments();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch departments");
                            }
                            break;

                        case "view_office_branches":
                            if (employee_manager.checkLoggedIn() != status.USER_LOGGED_IN) {
                                printError("You must be logged in to perform this action");
                                break;
                            }
                            result = employee_manager.viewOfficeBranches();
                            if (result != status.SUCCESS) {
                                printError("Failed to fetch office branches");
                            }
                            break;

                        default:
                            printError("Unknown command: " + command_type);
                            print("Type 'help' to see available commands");
                            break;
                }
            } catch (Exception e) {
                printError("Error executing command: " + e.getMessage());
                e.printStackTrace();
            }
            //PARSE COMMANDS
        }
    }

    private void showHelp() {
        int loginStatus = employee_manager.checkLoggedIn();
        
        printHeader("EMPLOYEE MANAGEMENT SYSTEM HELP");
        print("");
        
        // Authentication commands for everyone
        printHeader("AUTHENTICATION COMMANDS");
        System.out.printf("%-30s | %-50s\n", "login <ssn> <password>", "Log in to the system");
        System.out.printf("%-30s | %-50s\n", "logout", "Log out from the system");
        System.out.printf("%-30s | %-50s\n", "change_password <new_password>", "Change your password");
        System.out.printf("%-30s | %-50s\n", "help", "Display this help message");
        System.out.printf("%-30s | %-50s\n", "exit", "Exit the application");
        print("");
        
        if (loginStatus == status.USER_LOGGED_IN) {
            try {
                boolean isAdmin = employee_manager.role.equals("admin");

                if (isAdmin) {
                    printHeader("MANAGE EMPLOYEES (ADMIN ONLY)");
                    System.out.printf("%-30s | %-50s\n", "add_employee <name> <password> <salary> <dept_id> <manager_id> <office_id> <dob(yyyy-MM-dd)> <doj(yyyy-MM-dd)> <address> <sex>", "Add a new employee");
                    System.out.printf("%-30s | %-50s\n", "fire_employee <ssn>", "Remove an employee");
                    System.out.printf("%-30s | %-50s\n", "reassign_employee <ssn> <dept> <salary>", "Change employee department/salary");
                    System.out.printf("%-30s | %-50s\n", "get_employee_performance_report <ssn>", "View employee performance report");
                    System.out.printf("%-30s | %-50s\n", "view_employee_dependents <ssn>", "View employee dependents");
                    System.out.printf("%-30s | %-50s\n", "view_all_employees", "List all employees");
                    print("");
                    
                    printHeader("MANAGE REQUESTS (ADMIN ONLY)");
                    System.out.printf("%-30s | %-50s\n", "view_leave_requests", "View pending leave requests");
                    System.out.printf("%-30s | %-50s\n", "approve_leave <leave_id>", "Approve a leave request");
                    print("");
                    
                    printHeader("MANAGE MEETINGS (ADMIN ONLY)");
                    System.out.printf("%-30s | %-50s\n", "create_meeting <meeting_date(yyyy-mm-dd)> <meeting_start_time(hh:mm)> <meeting_end_time(hh:mm)> <meeting_mode> <meeting_location> <department_number> <purpose of meeting>", "Create a new meeting");
                    System.out.printf("%-30s | %-50s\n", "update_mom <meeting_id> <minutes>", "Update meeting minutes");
                    print("");
                }
                
                printHeader("EMPLOYEE ACTIONS");
                System.out.printf("%-30s | %-50s\n", "send_request <to_ssn> <description>", "Send a request to someone");
                System.out.printf("%-30s | %-50s\n", "request_leave <to_ssn> <start_date(yyyy-mm-dd)> <end_date(yyyy-mm-dd)>", "Request leave");
                System.out.printf("%-30s | %-50s\n", "view_my_leave_requests", "View your leave requests");
                System.out.printf("%-30s | %-50s\n", "get_experience_report", "Get your experience report");
                System.out.printf("%-30s | %-50s\n", "join_meeting <meeting_id>", "Join a meeting");
                System.out.printf("%-30s | %-50s\n", "leave_meeting <meeting_id>", "Leave a meeting");
                System.out.printf("%-30s | %-50s\n", "view_my_meetings", "View your meetings");
                System.out.printf("%-30s | %-50s\n", "view_requests", "View other requests");
                System.out.printf("%-30s | %-50s\n", "accept_request <request_id>", "Accept a request");
                System.out.printf("%-30s | %-50s\n", "add_skill <skill_name>", "Add a skill");
                System.out.printf("%-30s | %-50s\n", "add_work_entry <project_id> <contribution> <hours_worked> <date_of_work(yyyy-mm-dd)", "Add a work log of work done by employee");
                print("");
                printHeader("DISPLAY INFORMATION");
                System.out.printf("%-30s | %-50s\n", "view_branch_employees", "View employees at your branch");
                System.out.printf("%-30s | %-50s\n", "view_departments", "View all departments");
                System.out.printf("%-30s | %-50s\n", "view_office_branches", "View all office branches");
                print("");
            }
            catch(Exception e){
                
            }
        }
    }


    private ArrayList<String> parseCommand(String command){
        ArrayList<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(command);

        while(matcher.find()){
            if(matcher.group(1)!=null){
                tokens.add(matcher.group(1));
            }
            else{
                tokens.add(matcher.group(2));
            }
        }

        return tokens;
    }
}
