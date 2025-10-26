/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.CourseSchedule;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 *
 * @author jim.hsieh
 */
public class Assignment {
    private String title;
    private String description;
    private Date dueDate;
    private int maxScore;
    private int score;
    private boolean submitted;
    private Date submittedDate;
    private String submissionText;
    public Assignment(String title, String description, Date dueDate, int maxScore) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxScore = maxScore;
        this.score = 0;
        this.submitted = false;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    public String getDueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        return sdf.format(dueDate);
    }
    
    public int getMaxScore() {
        return maxScore;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    public boolean isSubmitted() {
        return submitted;
    }
    
    public String getStatus() {
        return submitted ? "Done" : "Pending";
    }
    
    /**
     * Submit assignments
     */
    public boolean submit(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        this.submissionText = content;
        this.submitted = true;
        this.submittedDate = new Date();
        return true;
    }
    
    public String getSubmittedDate() {
        if (submittedDate == null) return "Not submitted";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(submittedDate);
    }
    @Override
    public String toString() {
        return title;
    }
}
