/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.CourseSchedule.CourseLoad;
import Business.Person.Person;
import Business.CourseSchedule.SeatAssignment;
import java.util.ArrayList;

/**
 *
 * @author Ing-Ruei
 */
public class StudentProfile extends Profile {

    Person person;
    ArrayList<SeatAssignment> courseList;
    CourseLoad courseLoad;

    public StudentProfile(Person p) {
        super(p);
        courseList = new ArrayList<>();
    }

    public CourseLoad getCurrentCourseLoad() {
        return courseLoad;
    }

    public ArrayList<SeatAssignment> getCourseList() {
        return courseList;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public boolean isMatch(String id) {
        return person.getPersonId().equals(id);
    }

}
