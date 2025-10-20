/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
package Business;

import Business.CourseCatalog.Course;
import Business.CourseCatalog.CourseCatalog;
import Business.CourseSchedule.CourseLoad;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.CourseSchedule;
import Business.CourseSchedule.SeatAssignment;
import Business.Department.Department;
import Business.Person.Person;
import Business.Directory.PersonDirectory;
import Business.Directory.EmployeeDirectory;
import Business.Profiles.EmployeeProfile;
import Business.Directory.StudentDirectory;
import Business.Profiles.StudentProfile;
import Business.Directory.FacultyDirectory;
import Business.Directory.RegistrarDirectory;
import Business.Profiles.FacultyProfile;
import Business.Directory.UserAccountDirectory;
import Business.Profiles.RegistrarProfile;


/**
 *
 * @author Ing-Ruei
 */
class Configure {

    static Business initialize() {
        Business business = new Business("Information Systems");
        
        // ========== 1. 建立 Department ==========
        Department department = new Department("Information Systems");
        
        // ========== 2. 建立课程 ==========
        CourseCatalog courseCatalog = department.getCourseCatalog();
        
        Course info5100 = courseCatalog.newCourse("Application Engineering", "INFO5100", 4);
        Course info6205 = courseCatalog.newCourse("Program Structure", "INFO6205", 4);
        Course damg7245 = courseCatalog.newCourse("Data Mining", "DAMG7245", 4);
        Course info5001 = courseCatalog.newCourse("Application Design", "INFO5001", 4);
        Course csye6225 = courseCatalog.newCourse("Cloud Computing", "CSYE6225", 4);
        
        // 设定必修和选修
        department.addCoreCourse(info5100);  // INFO 5100 是必修
        department.addElectiveCourse(info6205);
        department.addElectiveCourse(damg7245);
        department.addElectiveCourse(info5001);
        department.addElectiveCourse(csye6225);
        
        // ========== 3. 建立学期课表 ==========
        CourseSchedule fall2023 = department.newCourseSchedule("Fall2023");
        
        CourseOffer info5100offer = fall2023.newCourseOffer("INFO5100");
        CourseOffer info6205offer = fall2023.newCourseOffer("INFO6205");
        CourseOffer damg7245offer = fall2023.newCourseOffer("DAMG7245");
        CourseOffer info5001offer = fall2023.newCourseOffer("INFO5001");
        CourseOffer csye6225offer = fall2023.newCourseOffer("CSYE6225");
        
        // ========== 4. 建立 Person ==========
        PersonDirectory persondirectory = department.getPersonDirectory();
        
        Person adminPerson = persondirectory.newPerson("Admin");
        Person student1Person = persondirectory.newPerson("Adam Smith");
        Person student2Person = persondirectory.newPerson("Betty Johnson");
        Person faculty1Person = persondirectory.newPerson("Prof. Anderson");
        Person faculty2Person = persondirectory.newPerson("Prof. Brown");
        Person registrarPerson = persondirectory.newPerson("Registrar");
        
        // ========== 5. 建立 Profile ==========
        
        // Admin
        EmployeeDirectory employeedirectory = business.getEmployeeDirectory();
        EmployeeProfile adminProfile = employeedirectory.newEmployeeProfile(adminPerson);
        
        // Student
        StudentDirectory studentdirectory = department.getStudentDirectory();
        StudentProfile student1 = studentdirectory.newStudentProfile(student1Person);
        StudentProfile student2 = studentdirectory.newStudentProfile(student2Person);
        
        // Faculty
        FacultyDirectory facultydirectory = department.getFacultyDirectory();
        FacultyProfile faculty1 = facultydirectory.newFacultyProfile(faculty1Person);
        FacultyProfile faculty2 = facultydirectory.newFacultyProfile(faculty2Person);
        
        // Registrar
        RegistrarDirectory registrardirectory = business.getRegistrarDirectory();
        RegistrarProfile registrar = registrardirectory.newRegistrarProfile(registrarPerson);
        
        // ========== 6. 指派教授 ==========
        info5100offer.AssignAsTeacher(faculty1);
        info6205offer.AssignAsTeacher(faculty2);
        damg7245offer.AssignAsTeacher(faculty1);
        info5001offer.AssignAsTeacher(faculty2);
        csye6225offer.AssignAsTeacher(faculty1);
        
        // ========== 7. 产生座位 ==========
        info5100offer.generateSeats(25);
        info6205offer.generateSeats(25);
        damg7245offer.generateSeats(25);
        info5001offer.generateSeats(25);
        csye6225offer.generateSeats(25);
        
        // ========== 8. 学生选课==========
        
        // Student 1 (Adam) - Fall 2023
        CourseLoad student1Fall2023 = student1.newCourseLoad("Fall2023");
        SeatAssignment sa1 = info5100offer.assignEmptySeat(student1Fall2023);
        SeatAssignment sa2 = info6205offer.assignEmptySeat(student1Fall2023);
        
        // 设定成绩
        sa1.setGrade("A");
        sa2.setGrade("B+");
        
        // Student 2 (Betty) - Fall 2023
        CourseLoad student2Fall2023 = student2.newCourseLoad("Fall2023");
        SeatAssignment sa3 = info5100offer.assignEmptySeat(student2Fall2023);
        SeatAssignment sa4 = damg7245offer.assignEmptySeat(student2Fall2023);
        
        sa3.setGrade("A-");
        sa4.setGrade("B");
        
        // ========== 9. 建立 UserAccount ==========
        UserAccountDirectory uadirectory = business.getUserAccountDirectory();
        uadirectory.newUserAccount(adminProfile, "admin", "****");
        uadirectory.newUserAccount(student1, "student", "****");
        uadirectory.newUserAccount(student2, "student2", "****");
        uadirectory.newUserAccount(faculty1, "faculty", "****");
        uadirectory.newUserAccount(registrar, "registrar", "****");
        
        // ========== 10. 设定财务余额 ==========
        student1.setBalance(14400);  
        student2.setBalance(14400);
        
        //business.setDepartment(department);
        
        return business;
    }

}
