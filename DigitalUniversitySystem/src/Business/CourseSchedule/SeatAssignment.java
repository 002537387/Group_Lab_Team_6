/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.CourseSchedule;

import Business.CourseCatalog.Course;
import Business.CourseSchedule.CourseLoad;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.Seat;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;  
import java.util.Date;


/**
 *
 * @author kal bugrara
 */
public class SeatAssignment {
    String grade; //(Letter grade mappings: A=4.0, A-=3.7, B+=3.3, B=3.0, )
    Seat seat;
    boolean like; //true means like and false means not like
    public CourseLoad courseload;
    Map<String, Double> gpa = new HashMap<>();
    private ArrayList<Assignment> assignments;
    
    
    public SeatAssignment(CourseLoad cl, Seat s){
        seat = s;
        courseload = cl;
        gpa.put("A", 4.0);
        gpa.put("A-", 3.7);
        gpa.put("B+", 3.3);
        gpa.put("B", 3.0);
        gpa.put("B-", 2.7);
        gpa.put("C+", 2.3);
        gpa.put("C", 2.0);
        gpa.put("C-", 1.7);
        gpa.put("F", 0.0);
        
        assignments = new ArrayList<>();
    }
     
    public boolean getLike(){
        return like;
    }
    public void assignSeatToStudent(CourseLoad cl){
        courseload = cl;
    }
    
    public int getCreditHours(){
        return seat.getCourseCredits();
       
    }
    public Seat getSeat(){
        return seat;
    }
    public CourseOffer getCourseOffer(){
        
        return seat.getCourseOffer();
    }
    public Course getAssociatedCourse(){
        
        return getCourseOffer().getSubjectCourse();
    }
    public double GetCourseStudentScore(){
    if (grade == null) return 0;
    return getCreditHours() * gpa.get(grade);
}

    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public double getGPA(){
        if (grade == null) return 0;
        return gpa.get(grade);
    }

    public void printGrade(){
        System.out.println("Grade: " + grade);
    }
    
    public String toString(){
        return getCourseOffer().getCourseNumber();
    }
    public String getLetterGrade() {
        return grade;
    }
    public Assignment addAssignment(String title, String desc, Date due, int max) {
        Assignment a = new Assignment(title, desc, due, max);
        assignments.add(a);
        return a;
    }
     public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
     
    // 目前交了几个作业
    public String getAssignmentProgress() {
        if (assignments.isEmpty()) return "0/0";
        int submitted = 0;
        for (Assignment a : assignments) {
            if (a.isSubmitted()) submitted++;
        }
        return submitted + "/" + assignments.size();
    }
    
    // 所有作业的平均分，百分制
    public double getAssignmentAverageScore() {
        if (assignments.isEmpty()) return 0;
        double total = 0;
        for (Assignment a : assignments) {
            if (a.getMaxScore() > 0) {
                total += (a.getScore() * 100.0 / a.getMaxScore());
            }
        }
        return total / assignments.size();
    }
    
    /**
    * 根据作业平均分自动计算字母成绩
    */
    public String calculateFinalGrade() {
        if (assignments.isEmpty()) {
            return null;  // No assignments = no grade yet
        }
        
        double avgScore = getAssignmentAverageScore();
        if (avgScore >= 95) return "A";
        if (avgScore >= 90) return "A-";
        if (avgScore >= 87) return "B+";
        if (avgScore >= 84) return "B";
        if (avgScore >= 80) return "B-";
        if (avgScore >= 77) return "C+";
        if (avgScore >= 74) return "C";
        if (avgScore >= 70) return "C-";
        return "F";
    }

    /**
    * 自动从作业成绩设置最终成绩
    */
    public void autoSetGradeFromAssignments() {
        if (!assignments.isEmpty()) {
            this.grade = calculateFinalGrade();
        }
    }
}
