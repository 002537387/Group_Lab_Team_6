/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
package Business;

import Business.CourseCatalog.Course;
import Business.CourseCatalog.CourseCatalog;
import Business.CourseSchedule.Assignment;
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
        CourseSchedule spring2024 = department.newCourseSchedule("Spring2024");
        
        CourseOffer info5100offer = fall2023.newCourseOffer("INFO5100");
        CourseOffer info6205offer = fall2023.newCourseOffer("INFO6205");
        CourseOffer damg7245offer = fall2023.newCourseOffer("DAMG7245");
        CourseOffer info5001offer = fall2023.newCourseOffer("INFO5001");
        CourseOffer csye6225offer = fall2023.newCourseOffer("CSYE6225");
        CourseOffer info5100offer2 = spring2024.newCourseOffer("INFO5100");
        CourseOffer csye6225offer2 = spring2024.newCourseOffer("CSYE6225");
        
        // ========== 4. 建立 Person ==========
        PersonDirectory persondirectory = department.getPersonDirectory();
        
        Person adminPerson = persondirectory.newPerson("1","Admin");
        Person student1Person = persondirectory.newPerson("2", "Adam Smith");
        Person student2Person = persondirectory.newPerson("3", "Betty Johnson");
        Person student3Person = persondirectory.newPerson("4", "Charlie Davis");
        Person student4Person = persondirectory.newPerson("5", "Diana Evans");
        Person student5Person = persondirectory.newPerson("6", "Ethan Foster");
        Person student6Person = persondirectory.newPerson("7", "Fiona Green");
        Person student7Person = persondirectory.newPerson("8", "George Harris");
        Person student8Person = persondirectory.newPerson("9", "Hannah Irving");
        Person student9Person = persondirectory.newPerson("10", "Isaac Jones");
        Person student10Person = persondirectory.newPerson("11", "Julia King");

        // Faculties (10)
        Person faculty1Person = persondirectory.newPerson("12", "Prof. Anderson");
        Person faculty2Person = persondirectory.newPerson("13", "Prof. Brown");
        Person faculty3Person = persondirectory.newPerson("14", "Prof. Chen");
        Person faculty4Person = persondirectory.newPerson("15", "Prof. Davis");
        Person faculty5Person = persondirectory.newPerson("16", "Prof. Evans");
        Person faculty6Person = persondirectory.newPerson("17", "Prof. Fisher");
        Person faculty7Person = persondirectory.newPerson("18", "Prof. Garcia");
        Person faculty8Person = persondirectory.newPerson("19", "Prof. Hughes");
        Person faculty9Person = persondirectory.newPerson("20", "Prof. Ivanov");
        Person faculty10Person = persondirectory.newPerson("21", "Prof. Jackson");

        // Registrar (1)
        Person registrarPerson = persondirectory.newPerson("22", "Registrar");

        // Other staff (8 more to reach 30 total)
        Person staff1Person = persondirectory.newPerson("23", "Staff Member 1");
        Person staff2Person = persondirectory.newPerson("24", "Staff Member 2");
        Person staff3Person = persondirectory.newPerson("25", "Staff Member 3");
        Person staff4Person = persondirectory.newPerson("26", "Staff Member 4");
        Person staff5Person = persondirectory.newPerson("27", "Staff Member 5");
        Person staff6Person = persondirectory.newPerson("28", "Staff Member 6");
        Person staff7Person = persondirectory.newPerson("29", "Staff Member 7");
        Person staff8Person = persondirectory.newPerson("30", "Staff Member 8");
        
        // ========== 5. 建立 Profile ==========
        
        // Admin
        EmployeeDirectory employeedirectory = department.getEmployeeDirectory();
        EmployeeProfile adminProfile = employeedirectory.newEmployeeProfile(adminPerson);

        // Students (10)
        StudentDirectory studentdirectory = department.getStudentDirectory();
        StudentProfile student1 = studentdirectory.newStudentProfile(student1Person);
        StudentProfile student2 = studentdirectory.newStudentProfile(student2Person);
        StudentProfile student3 = studentdirectory.newStudentProfile(student3Person);
        StudentProfile student4 = studentdirectory.newStudentProfile(student4Person);
        StudentProfile student5 = studentdirectory.newStudentProfile(student5Person);
        StudentProfile student6 = studentdirectory.newStudentProfile(student6Person);
        StudentProfile student7 = studentdirectory.newStudentProfile(student7Person);
        StudentProfile student8 = studentdirectory.newStudentProfile(student8Person);
        StudentProfile student9 = studentdirectory.newStudentProfile(student9Person);
        StudentProfile student10 = studentdirectory.newStudentProfile(student10Person);

        // Faculties (10)
        FacultyDirectory facultydirectory = department.getFacultyDirectory();
        FacultyProfile faculty1 = facultydirectory.newFacultyProfile(faculty1Person);
        FacultyProfile faculty2 = facultydirectory.newFacultyProfile(faculty2Person);
        FacultyProfile faculty3 = facultydirectory.newFacultyProfile(faculty3Person);
        FacultyProfile faculty4 = facultydirectory.newFacultyProfile(faculty4Person);
        FacultyProfile faculty5 = facultydirectory.newFacultyProfile(faculty5Person);
        FacultyProfile faculty6 = facultydirectory.newFacultyProfile(faculty6Person);
        FacultyProfile faculty7 = facultydirectory.newFacultyProfile(faculty7Person);
        FacultyProfile faculty8 = facultydirectory.newFacultyProfile(faculty8Person);
        FacultyProfile faculty9 = facultydirectory.newFacultyProfile(faculty9Person);
        FacultyProfile faculty10 = facultydirectory.newFacultyProfile(faculty10Person);

        // Registrar
        RegistrarDirectory registrardirectory = department.getRegistrarDirectory();
        RegistrarProfile registrar = registrardirectory.newRegistrarProfile(registrarPerson);
        
        // ========== 6. 指派教授 ==========
        info5100offer.AssignAsTeacher(faculty1);
        info6205offer.AssignAsTeacher(faculty2);
        damg7245offer.AssignAsTeacher(faculty1);
        info5001offer.AssignAsTeacher(faculty2);
        csye6225offer.AssignAsTeacher(faculty1);
        info5100offer2.AssignAsTeacher(faculty1);
        csye6225offer2.AssignAsTeacher(faculty1);
        
        // ========== 7. 产生座位 ==========
        info5100offer.generateSeats(25);
        info6205offer.generateSeats(25);
        damg7245offer.generateSeats(25);
        info5001offer.generateSeats(25);
        csye6225offer.generateSeats(25);
        info5100offer2.generateSeats(10);
        csye6225offer2.generateSeats(10);
        
        // ========== 8. 学生选课==========
        
        // Student 1 (Adam) - Fall 2023
        CourseLoad student1Fall2023 = student1.newCourseLoad("Fall2023");
        SeatAssignment sa1 = info5100offer.assignEmptySeat(student1Fall2023);
        SeatAssignment sa2 = info6205offer.assignEmptySeat(student1Fall2023);
        
        // 设定成绩
        // 若不手动设定，会自动根据作业成绩算出，需要这门课至少有一个作业
        sa1.setGrade("A");
        sa2.setGrade("B+");
        
        //為 INFO5100 創建作業
        java.util.Date now = new java.util.Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        
        // Assignment 1 - 已完成
        cal.set(2023, 9, 30);  // 10/30
        Assignment a1 = sa1.addAssignment(
            "Midterm Exam", 
            "Application design midterm examination", 
            cal.getTime(), 
            100
        );
        a1.setScore(85);
        a1.submit("Completed midterm exam");
        
        // Assignment 2 - 已完成
        cal.set(2023, 10, 6);  // 11/06
        Assignment a2 = sa1.addAssignment(
            "Final Project", 
            "Build a full-stack university system", 
            cal.getTime(), 
            100
        );
        a2.setScore(90);
        a2.submit("Submitted final project");
        
        // Assignment 3 - 待完成
        cal.set(2023, 10, 13);  // 11/13
        sa1.addAssignment(
            "Homework 3", 
            "Database schema design", 
            cal.getTime(), 
            100
        );
        
        // Assignment 4 - 待完成
        cal.set(2023, 10, 20);  // 11/20
        sa1.addAssignment(
            "Lab Assignment 5", 
            "React components implementation", 
            cal.getTime(), 
            100
        );
        
        // Student 2 (Betty) - Fall 2023
        CourseLoad student2Fall2023 = student2.newCourseLoad("Fall2023");
        SeatAssignment sa3 = info5100offer.assignEmptySeat(student2Fall2023);
        SeatAssignment sa4 = damg7245offer.assignEmptySeat(student2Fall2023);
        
        sa3.setGrade("A-");
        sa4.setGrade("B");
        
        
        CourseLoad student3Fall2023 = student3.newCourseLoad("Fall2023");
        SeatAssignment sa5 = info5100offer.assignEmptySeat(student3Fall2023);
        sa5.setGrade("B+");  

        CourseLoad student4Fall2023 = student4.newCourseLoad("Fall2023");
        SeatAssignment sa6 = info6205offer.assignEmptySeat(student4Fall2023);
        sa6.setGrade("A");  

        CourseLoad student5Fall2023 = student5.newCourseLoad("Fall2023");
        SeatAssignment sa7 = damg7245offer.assignEmptySeat(student5Fall2023);
        sa7.setGrade("B");  

        CourseLoad student6Fall2023 = student6.newCourseLoad("Fall2023");
        SeatAssignment sa8 = info5001offer.assignEmptySeat(student6Fall2023);
        sa8.setGrade("A-");  

        // Student 7 - Spring 2024
        CourseLoad student7Spring2024 = student7.newCourseLoad("Spring2024");
        SeatAssignment sa9 = csye6225offer2.assignEmptySeat(student7Spring2024);
        SeatAssignment sa10 = info5100offer2.assignEmptySeat(student7Spring2024);
        
        cal.set(2024, 2, 15);
        Assignment a5 = sa9.addAssignment(
            "Midterm Exam", 
            "Application design midterm examination", 
            cal.getTime(), 
            100
        );
        a5.setScore(85);
        a5.submit("Completed midterm exam");
        
        cal.set(2024, 3, 1);
        Assignment a6 = sa9.addAssignment(
            "Quiz1", 
            "Quiz 1", 
            cal.getTime(), 
            50
        );
        a6.setScore(40);
        a6.submit("Completed quiz 1");
        
        cal.set(2024, 2, 15);
        Assignment a7 = sa10.addAssignment(
            "Midterm Exam 1", 
            "Midterm examination 1", 
            cal.getTime(), 
            100
        );

        cal.set(2024, 3, 15);
        Assignment a8 = sa10.addAssignment(
            "Quiz2", 
            "Quiz 2", 
            cal.getTime(), 
            50
        );
        
        CourseLoad student8Fall2023 = student8.newCourseLoad("Fall2023");
        SeatAssignment sa11 = info5100offer.assignEmptySeat(student8Fall2023);
        sa11.setGrade("A");  

        CourseLoad student9Fall2023 = student9.newCourseLoad("Fall2023");
        SeatAssignment sa12 = info6205offer.assignEmptySeat(student9Fall2023);
        sa12.setGrade("B");  
        CourseLoad student10Fall2023 = student10.newCourseLoad("Fall2023");
        SeatAssignment sa13 = damg7245offer.assignEmptySeat(student10Fall2023);
        sa13.setGrade("A-");  
        
        // ========== 9. 建立 UserAccount ==========
        UserAccountDirectory uadirectory = department.getUserAccountDirectory();
        uadirectory.newUserAccount(adminProfile, "admin", "****");
        uadirectory.newUserAccount(student1, "student", "****");
        uadirectory.newUserAccount(student2, "student2", "****");
        uadirectory.newUserAccount(student3, "student3", "****");
        uadirectory.newUserAccount(student4, "student4", "****");
        uadirectory.newUserAccount(student5, "student5", "****");
        uadirectory.newUserAccount(student6, "student6", "****");
        uadirectory.newUserAccount(student7, "student7", "****");
        uadirectory.newUserAccount(student8, "student8", "****");
        uadirectory.newUserAccount(student9, "student9", "****");
        uadirectory.newUserAccount(student10, "student10", "****");
        uadirectory.newUserAccount(faculty1, "faculty", "****");
        uadirectory.newUserAccount(faculty2, "faculty2", "****");
        uadirectory.newUserAccount(faculty3, "faculty3", "****");
        uadirectory.newUserAccount(faculty4, "faculty4", "****");
        uadirectory.newUserAccount(faculty5, "faculty5", "****");
        uadirectory.newUserAccount(faculty6, "faculty6", "****");
        uadirectory.newUserAccount(faculty7, "faculty7", "****");
        uadirectory.newUserAccount(faculty8, "faculty8", "****");
        uadirectory.newUserAccount(faculty9, "faculty9", "****");
        uadirectory.newUserAccount(faculty10, "faculty10", "****");
        uadirectory.newUserAccount(registrar, "registrar", "****");
        
        // ========== 10. 设定财务余额 ==========
        student1.setBalance(0);  
        student2.setBalance(0);
        student3.setBalance(0);
        student4.setBalance(0);
        student5.setBalance(0);
        student6.setBalance(0);
        student7.setBalance(0);
        student8.setBalance(0);
        student9.setBalance(0);
        student10.setBalance(0);
        
        business.setDepartment(department);
        
        return business;
    }

}
