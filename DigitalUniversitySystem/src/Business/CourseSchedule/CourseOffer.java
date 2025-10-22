/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.CourseSchedule;

import Business.CourseCatalog.Course;
import Business.Profiles.FacultyAssignment;
import Business.Profiles.FacultyProfile;
import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class CourseOffer {

    Course course;
    ArrayList<Seat> seatlist;
    FacultyAssignment facultyassignment;

    public CourseOffer(Course c) {
        course = c;
        seatlist = new ArrayList();
    }
     
    public void AssignAsTeacher(FacultyProfile fp) {

        facultyassignment = new FacultyAssignment(fp, this);
    }

    public FacultyProfile getFacultyProfile() {
        return facultyassignment.getFacultyProfile();
    }

    public String getCourseNumber() {
        return course.getCOurseNumber();
    }

    public void generateSeats(int n) {

        for (int i = 0; i < n; i++) {

            seatlist.add(new Seat(this, i));

        }

    }

    public Seat getEmptySeat() {

        for (Seat s : seatlist) {

            if (!s.isOccupied()) {
                return s;
            }
        }
        return null;
    }


    public SeatAssignment assignEmptySeat(CourseLoad cl) {

        Seat seat = getEmptySeat();
        if (seat == null) {
            return null;
        }
        SeatAssignment sa = seat.newSeatAssignment(cl); //seat is already linked to course offer
        cl.registerStudent(sa); //coures offer seat is now linked to student
        return sa;
    }

    public int getTotalCourseRevenues() {

        int sum = 0;

        for (Seat s : seatlist) {
            if (s.isOccupied() == true) {
                sum = sum + course.getCoursePrice();
            }

        }
        return sum;
    }
    public Course getSubjectCourse(){
        return course;
    }
    public int getCreditHours(){
        return course.getCredits();
    }
    /**
     * 取得学费
     */
    public int getTuitionFee() {
        return course.getCoursePrice();
    }
    
    /**
     * 取得教授名字
     */
    public String getFaculty() {
        if (facultyassignment == null) return "TBA";
        return facultyassignment.getFacultyProfile().getFacultyName();
    }
    
    /**
     * 取得剩余座位数
     */
    public int getTotalEmptySeat() {
        int count = 0;
        for (Seat s : seatlist) {
            if (!s.isOccupied()) count++;
        }
            return count;
        }
    /**
     * 取得已注册学生数
     */
    public int getTotalRegistedStudent() {
        int count = 0;
        for (Seat s : seatlist) {
            if (s.isOccupied()) count++;
        }
        return count;
    }
    /**
     * 獲取剩餘座位數（別名方法）
     */
    public int getSeatsRemaining() {
        return getTotalEmptySeat();
    }
    
    /**
     * 獲取總座位數
     */
    public int getTotalSeats() {
        return seatlist.size();
    }
    
    @Override
    public String toString() {
        return course.getCOurseNumber();
    }

}
