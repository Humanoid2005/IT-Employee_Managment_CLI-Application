# IT Services - Employee Management System

## Project Overview
DBLAB-P094 IT Services – Employee Management System is a comprehensive database system for managing employee information, project assignments, leaves, meetings, skills, and more within an organization.

## Developer Information
- **Name:** Sriram Srikanth
- **Roll Number:** IMT2023115
- **Project ID:** DBLAB-P094

## System Users
- **Employee:** Regular staff members with self-service capabilities
- **Administrator (HR):** Personnel with administrative privileges

## Use Cases

### Authentication
- Login
- Logout
- Change password

### Admin Operations
| Name | Actor | Action |
|------|-------|--------|
| Add Employee | Admin | Creates a new employee record in the system |
| Fire Employee | Admin | Removes an employee from the system |
| Reassign Employee | Admin | Changes an employee's salary or department |
| Approve Leaves | Admin | Reviews and approves employee leave requests |
| View Leave Requests | Admin | Views all pending leave requests |
| Create Meetings | Admin | Schedules new meetings |
| Update Minutes of Meeting | Admin | Records meeting notes after completion |
| Get Employee Performance Report | Admin | Views work logs and performance metrics for employees |
| Get Employee's Dependents Data | Admin | Views information about employee dependents |

### Employee Operations
| Name | Actor | Action |
|------|-------|--------|
| Request Leave | Employee | Submits a request for time off |
| Get Experience Report | Employee | Views their work history and experience |
| Join Meeting | Employee | Registers attendance for a scheduled meeting |
| Leave Meeting | Employee | Removes themselves from a meeting |
| View My Meetings | Employee | Shows all meetings the employee is scheduled to attend |
| Add Work Log Entry | Employee | Records work performed on projects |
| Add Skill | Employee | Updates their skill profile |

### Common Operations
| Name | Actor | Action |
|------|-------|--------|
| View Branch Employees | Admin/Employee | Lists employees working at a specific branch |
| View Department Details | Admin/Employee | Shows information about departments |
| View Office Branches | Admin/Employee | Lists all office locations |
| Send Requests | Admin/Employee | Submits general requests to other employees |
| View Requests | Admin/Employee | Shows pending requests |
| Accept Requests | Admin/Employee | Approves received requests |

## Database Structure

### employees
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| employee_id | int | Primary Key, Auto Increment |
| employee_password | varchar(100) | NOT NULL |
| employee_name | varchar(50) | NOT NULL |
| salary | int | NOT NULL |
| department_id | int | Foreign Key (departments.department_id) |
| manager_id | int | Foreign Key (employees.employee_id) |
| office_location_id | int | Foreign Key (office_branches.branch_id) |
| dob | date | NOT NULL |
| doj | date | NOT NULL |
| house_address | varchar(100) | NOT NULL |
| sex | char(1) | |

### work_log
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| employee_id | int | Foreign Key (employees.employee_id) |
| project_id | int | Foreign Key (projects.project_id) |
| hours_worked | int | NOT NULL |
| date_of_work | date | NOT NULL |
| contribution | varchar(200) | NOT NULL |

### dependents
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| employee_id | int | Foreign Key (employees.employee_id) |
| dependent_name | varchar(50) | NOT NULL |
| sex | char(1) | |
| dob | date | |
| relation | varchar(50) | NOT NULL |

### employee_leaves
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| leave_id | int | Primary Key, Auto Increment |
| employee_id | int | Foreign Key (employees.employee_id) |
| request_to | int | Foreign Key (employees.employee_id) |
| start_date | date | NOT NULL |
| end_date | date | NOT NULL |
| approved | boolean | DEFAULT false |

### meetings
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| meeting_id | int | Primary Key, Auto Increment |
| meeting_mode | varchar(50) | |
| meeting_date | date | NOT NULL |
| meeting_start_time | time | NOT NULL |
| meeting_end_time | time | NOT NULL |
| meeting_location | varchar(100) | |
| meeting_organiser_id | int | Foreign Key (employees.employee_id) |
| department_id | int | Foreign Key (departments.department_id) |
| purpose_of_meeting | varchar(200) | NOT NULL |
| minutes_of_meeting | varchar(400) | |

### attends_meeting
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| meeting_id | int | Foreign Key (meetings.meeting_id) |
| employee_id | int | Foreign Key (employees.employee_id) |

### skills
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| employee_id | int | Foreign Key (employees.employee_id) |
| skill_name | varchar(50) | NOT NULL |

### departments
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| department_id | int | Primary Key, Auto Increment |
| department_name | varchar(100) | NOT NULL |
| manager_id | int | Foreign Key (employees.employee_id) |

### projects
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| project_id | int | Primary Key, Auto Increment |
| project_name | varchar(100) | NOT NULL |
| project_description | varchar(100) | |
| department_id | int | Foreign Key (departments.department_id) |
| project_start_date | date | NOT NULL |
| tentative_completion_date | date | |

### office_branches
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| branch_id | int | Primary Key, Auto Increment |
| city | varchar(100) | NOT NULL |
| state | varchar(100) | NOT NULL |
| country | varchar(100) | NOT NULL |
| address | varchar(100) | NOT NULL |
| size_of_office | int | |

### requests
| Attribute | Data Type | Constraints |
|-----------|-----------|------------|
| request_id | int | Primary Key, Auto Increment |
| request_from | int | Foreign Key (employees.employee_id) |
| request_to | int | Foreign Key (employees.employee_id) |
| request_description | varchar(50) | |
| approved | boolean | DEFAULT false |