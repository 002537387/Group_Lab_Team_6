/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.CourseSchedule;

import Business.Profiles.StudentProfile;
import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class CourseLoad {
    String semester;
    ArrayList<SeatAssignment> seatassignments;
    private StudentProfile student;
    
    public CourseLoad(String s){
        seatassignments = new ArrayList();
        semester = s;
    }
    public SeatAssignment newSeatAssignment(CourseOffer co){
        
        Seat seat = co.getEmptySeat(); // seat linked to courseoffer
        if (seat==null) return null;
        SeatAssignment sa = seat.newSeatAssignment(this);
        seatassignments.add(sa);  //add to students course 
        return sa;
    }
    
    public void registerStudent(SeatAssignment sa){
        
        
        sa.assignSeatToStudent(this);
        seatassignments.add(sa);
    }
    
    public double getSemesterScore(){  
    double sum = 0;                
    for (SeatAssignment sa: seatassignments){
        sum = sum + sa.GetCourseStudentScore();
    }
    return sum;
}
        public ArrayList<SeatAssignment> getSeatAssignments(){
            return seatassignments;
        }
        
        /**
     * 取得课程清单
     */
    public ArrayList<CourseOffer> getCourseOfferList() {
        ArrayList<CourseOffer> list = new ArrayList<>();
        for (SeatAssignment sa : seatassignments) {
            list.add(sa.getCourseOffer());
        }
        return list;
    }
    /**
     * 计算学费总额
     */
    public double getTuitionFee() {
        double sum = 0;
        for (SeatAssignment sa : seatassignments) {
            sum += sa.getCourseOffer().getTuitionFee();
        }
        return sum;
    }
            
    /**
     * 计算平均 GPA
     */
    public String getAverageGPA() {
        double sum = 0;
        int totalCredits = 0;
        for (SeatAssignment sa : seatassignments) {
            sum += sa.getGPA() * sa.getCreditHours();
            totalCredits += sa.getCreditHours();
        }
        if (totalCredits == 0) return "0.00";
        return String.format("%.2f", sum / totalCredits);
    }
    /**
     * 计算总学分
     */
    public int getTotalCredits() {
        int sum = 0;
        for (SeatAssignment sa : seatassignments) {
            sum += sa.getCreditHours();
        }
        return sum;
    }
    
    /**
     * 取得学期名称
     */
    public String getSemester() {
        return semester;
    }
    
    /**
     * 退课
     */
    public void dropCourse(SeatAssignment sa) {
        seatassignments.remove(sa);
    }
    
    /**
    * 获取学生档案
    */
    public StudentProfile getStudent() {
        return student;
    }

    /**
    * 设置学生档案
    */
    public void setStudent(StudentProfile student) {
        this.student = student;
    }
    
    @Override
    public String toString() {
     return semester;
    }
}
