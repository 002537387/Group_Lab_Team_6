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
    private String schedule;  // "Mon/Wed 9:00-10:30"
    private boolean enrollmentOpen = true;  // 默认开放注册

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
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    /**
    * 获取注册状态
    */
    public boolean isEnrollmentOpen() {
        return enrollmentOpen;
    }

    /**
    * 开放注册
    */
    public void openEnrollment() {
        this.enrollmentOpen = true;
    }

    /**
    * 关闭注册
    */
    public void closeEnrollment() {
        this.enrollmentOpen = false;
    }

    /**
    * 设置注册状态
    */
    public void setEnrollmentOpen(boolean open) {
        this.enrollmentOpen = open;
    }

    /**
    * 修改容量
    * @param newCapacity 新容量
    * @return 是否成功
    */
    public boolean updateCapacity(int newCapacity) {
        int currentEnrolled = getTotalRegistedStudent();
    
        // 验证：新容量不能小于已注册学生数
        if (newCapacity < currentEnrolled) {
            return false;
        }
    
        int currentCapacity = seatlist.size();
    
        if (newCapacity > currentCapacity) {
            // 增加座位
            for (int i = currentCapacity; i < newCapacity; i++) {
                seatlist.add(new Seat(this, i));
            }
        } else if (newCapacity < currentCapacity) {
            // 减少座位 - 只移除空座位
            for (int i = seatlist.size() - 1; i >= newCapacity; i--) {
                if (!seatlist.get(i).isOccupied()) {
                    seatlist.remove(i);
                } else {
                    return false; // 不能移除已占用的座位
                }
            }
        }
    
        return true;
    }

    /**
    * 获取所有已注册学生的SeatAssignment
    */
    public ArrayList<SeatAssignment> getEnrolledSeatAssignments() {
        ArrayList<SeatAssignment> enrolled = new ArrayList<>();
        for (Seat s : seatlist) {
            if (s.isOccupied()) {
                enrolled.add(s.getSeatAssignment());  // ⚠️ 需要Seat有这个方法
            }
        }
        return enrolled;
    }
    
    @Override
    public String toString() {
        return course.getCOurseNumber();
    }

}
