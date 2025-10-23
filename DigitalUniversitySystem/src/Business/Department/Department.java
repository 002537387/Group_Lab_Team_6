/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Department;

import Business.CourseCatalog.Course;
import Business.CourseCatalog.CourseCatalog;
import Business.CourseSchedule.CourseLoad;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.CourseSchedule;
import Business.Degree.Degree;

import Business.Directory.FacultyDirectory;
import Business.Directory.PersonDirectory;
import Business.Directory.StudentDirectory;
import Business.Profiles.StudentProfile;
import java.util.HashMap;
import Business.Directory.UserAccountDirectory;  
import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class Department {

     String name;
    CourseCatalog coursecatalog;
    PersonDirectory persondirectory;
    StudentDirectory studentdirectory;
    FacultyDirectory facultydirectory;
    UserAccountDirectory useraccountdirectory;  
    Degree degree;
    HashMap<String, CourseSchedule> mastercoursecatalog;

    public Department(String n) {
        name = n;
        mastercoursecatalog = new HashMap<>();
        coursecatalog = new CourseCatalog(this);
        studentdirectory = new StudentDirectory(); //pass the department object so it stays linked to it
        persondirectory = new PersonDirectory();
        facultydirectory = new FacultyDirectory(); 
        useraccountdirectory = new UserAccountDirectory();
        degree = new Degree("MSIS");
        
    }

    public String getName() {
        return name;
    }
    
    public void addCoreCourse(Course c){
        degree.addCoreCourse(c);
        
    }
    public void addElectiveCourse(Course c){
        degree.addElectiveCourse(c);
        
    }
    public PersonDirectory getPersonDirectory() {

        return persondirectory;

    }

    public StudentDirectory getStudentDirectory() {
    return studentdirectory;
    }

    public CourseSchedule newCourseSchedule(String semester) {

        CourseSchedule cs = new CourseSchedule(semester, coursecatalog);
        mastercoursecatalog.put(semester, cs);
        return cs;
    }

    public CourseSchedule getCourseSchedule(String semester) {

        return mastercoursecatalog.get(semester);

    }

    public CourseCatalog getCourseCatalog() {

        return coursecatalog;

    }

    public Course newCourse(String n, String nm, int cr) {

        Course c = coursecatalog.newCourse(n, nm, cr);
        return c;
    }

    public int calculateRevenuesBySemester(String semester) {

        CourseSchedule css = mastercoursecatalog.get(semester);

        return css.calculateTotalRevenues();

    }

    public void RegisterForAClass(String studentid, String cn, String semester) {

        StudentProfile sp = studentdirectory.findStudent(studentid);

        CourseLoad cl = sp.getCurrentCourseLoad();

        CourseSchedule cs = mastercoursecatalog.get(semester);

        CourseOffer co = cs.getCourseOfferByNumber(cn);

        co.assignEmptySeat(cl);

    }
     public FacultyDirectory getFacultyDirectory() {
        return facultydirectory;
    }
    
    public UserAccountDirectory getUserAccountDirectory() {
        return useraccountdirectory;
    }

    public ArrayList<Business.Person.Person> searchPersons() {
        return persondirectory.getPersons();
    }
}
