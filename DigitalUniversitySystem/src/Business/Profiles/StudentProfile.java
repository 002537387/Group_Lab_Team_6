/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Business;
import Business.Person.Person;
import Business.Person.Transcript;
import Business.CourseSchedule.CourseLoad;
import Business.CourseSchedule.SeatAssignment;
import Business.CourseSchedule.CourseOffer;
import java.util.ArrayList;

/**
 *
 * @author Ing-Ruei
 */
public class StudentProfile extends Profile {

    Person person;
    Transcript transcript;
    
    private double balance = 0.0;
    private ArrayList<PaymentRecord> paymentHistory = new ArrayList<>();
    
    public StudentProfile(Person p) {
        super(p);
        this.person = p;
        transcript = new Transcript(this);
    }
    
    // ========== Transcript method ==========
    
    public Transcript getTranscript() {
        return transcript;
    }
    
    public CourseLoad getCurrentCourseLoad() {
        return transcript.getCurrentCourseLoad();
    }
    
    public CourseLoad newCourseLoad(String semester) {
        CourseLoad cl = transcript.newCourseLoad(semester);
        cl.setStudent(this);  // 
        return cl;
    }
    
    public CourseLoad getCourseLoadBySemester(String semester) {
        return transcript.getCourseLoadBySemester(semester);
    }
    
    public ArrayList<SeatAssignment> getCourseList() {
        return transcript.getCourseList();
    }
    
    // ========== Financial method ==========
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public boolean payTuition(double amount) {
        if (amount <= 0 || balance <= 0) return false;
        balance -= amount;
        if (balance < 0) balance = 0;
        
        paymentHistory.add(new PaymentRecord(
            new java.util.Date(), 
            amount, 
            transcript.getCurrentCourseLoad().getSemester()
        ));
        return true;
    }
    
    public boolean canViewTranscript() {
        return balance <= 0;
    }
    
    public ArrayList<PaymentRecord> getPaymentHistory() {
        return paymentHistory;
    }
    
    public void refundForDroppedCourse(CourseOffer co) {
        double refund = co.getTuitionFee();
        balance -= refund;
        if (balance < 0) balance = 0;
    }
    
    /**
     * 
     * Calculate overall GPA across all semesters (only includes graded courses)
     * @return Overall GPA
     */
    public double calculateOverallGPA() {
        double totalGradePoints = 0.0;
        int totalCredits = 0;
    
        // Go through All Semester
        for (CourseLoad cl : transcript.getCourseloadlist().values()) {
            for (SeatAssignment sa : cl.getSeatAssignments()) {
                // 只计算有letter grade的课程
                if (sa.getLetterGrade() != null) {
                    totalGradePoints += sa.getGPA() * sa.getCreditHours();
                    totalCredits += sa.getCreditHours();
                }
            }
        }
    
        // No rated Course，return 0.0
        return totalCredits == 0 ? 0.0 : totalGradePoints / totalCredits;
    }

    /**
     * 
     * Get total graded credits across all semesters
     */
    public int getTotalGradedCredits() {
        int credits = 0;
        for (CourseLoad cl : transcript.getCourseloadlist().values()) {
            for (SeatAssignment sa : cl.getSeatAssignments()) {
                if (sa.getLetterGrade() != null) {
                    credits += sa.getCreditHours();
                }
            }
        }
        return credits;
    }

    /**
     * 
     * Get GPA for a specific semester
     * @param semester 
     * @return GPA
     */
    public double getSemesterGPA(String semester) {
        CourseLoad cl = transcript.getCourseLoadBySemester(semester);
        if (cl == null) return 0.0;
        return cl.calculateSemesterGPA();
    }
   
    
    // ========== Override method==========
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public String toString() {
        return person.getPersonId();
    }
    
    public boolean isMatch(String id) {
        return person.getPersonId().equals(id);
    }
    
   

}
