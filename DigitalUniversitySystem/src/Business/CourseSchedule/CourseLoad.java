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
     * Get list of all course offerings in this course load
     */
    public ArrayList<CourseOffer> getCourseOfferList() {
        ArrayList<CourseOffer> list = new ArrayList<>();
        for (SeatAssignment sa : seatassignments) {
            list.add(sa.getCourseOffer());
        }
        return list;
    }
    /**
     * Calculate total tuition fees for all courses in this course load
     */
    public double getTuitionFee() {
        double sum = 0;
        for (SeatAssignment sa : seatassignments) {
            sum += sa.getCourseOffer().getTuitionFee();
        }
        return sum;
    }
            
    /**
     * Calculate weighted average GPA for all courses in this course load
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
     * Calculate total credit hours for all courses in this course load
     */
    public int getTotalCredits() {
        int sum = 0;
        for (SeatAssignment sa : seatassignments) {
            sum += sa.getCreditHours();
        }
        return sum;
    }
    
    /**
     * Get the semester name for this course load
     */
    public String getSemester() {
        return semester;
    }
    
    /**
     * Drop a course from this course load
     */
    public void dropCourse(SeatAssignment sa) {
        seatassignments.remove(sa);
    }
    
    /**
    * Get the student profile associated with this course load
    */
    public StudentProfile getStudent() {
        return student;
    }

    /**
    * Set the student profile for this course load
    */
    public void setStudent(StudentProfile student) {
        this.student = student;
    }
    
    /**
     * Calculate semester GPA (only includes courses with letter grades)
     */
    public double calculateSemesterGPA() {
        double totalGradePoints = 0.0;
        int totalCredits = 0;
    
        for (SeatAssignment sa : seatassignments) {
            // Only calculate GPA for courses with letter grades
            if (sa.getLetterGrade() != null) {
                totalGradePoints += sa.getGPA() * sa.getCreditHours();
                totalCredits += sa.getCreditHours();
            }
        }
    
        // Return 0.0 if no graded courses exist
        return totalCredits == 0 ? 0.0 : totalGradePoints / totalCredits;
    }

    /**
     * Get total graded credits for this semester
     */
    public int getGradedCredits() {
        int credits = 0;
        for (SeatAssignment sa : seatassignments) {
            if (sa.getLetterGrade() != null) {
                credits += sa.getCreditHours();
            }
        }
        return credits;
    }
    
    @Override
    public String toString() {
     return semester;
    }
}
