/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

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
    
    // ========== Transcript 方法 ==========
    
    public Transcript getTranscript() {
        return transcript;
    }
    
    public CourseLoad getCurrentCourseLoad() {
        return transcript.getCurrentCourseLoad();
    }
    
    public CourseLoad newCourseLoad(String semester) {
        return transcript.newCourseLoad(semester);
    }
    
    public CourseLoad getCourseLoadBySemester(String semester) {
        return transcript.getCourseLoadBySemester(semester);
    }
    
    public ArrayList<SeatAssignment> getCourseList() {
        return transcript.getCourseList();
    }
    
    // ========== 财务方法 ==========
    
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
    
    // ========== Override 方法 ==========
    
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
