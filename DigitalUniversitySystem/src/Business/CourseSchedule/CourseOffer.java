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
import java.util.HashMap;

/**
 *
 * @author kal bugrara
 */
public class CourseOffer {

    Course course;
    ArrayList<Seat> seatlist;
    FacultyAssignment facultyassignment;
    private String schedule;  // "Mon/Wed 9:00-10:30"
    private boolean enrollmentOpen = true;  // Default: enrollment is open

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
     * Get the tuition fee for this course
     */
    public int getTuitionFee() {
        return course.getCoursePrice();
    }
    
    /**
     * Get the name of the assigned faculty member
     */
    public String getFaculty() {
        if (facultyassignment == null) return "TBA";
        return facultyassignment.getFacultyProfile().getFacultyName();
    }
    
    /**
     * Get the number of remaining empty seats
     */
    public int getTotalEmptySeat() {
        int count = 0;
        for (Seat s : seatlist) {
            if (!s.isOccupied()) count++;
        }
            return count;
        }
    /**
     * Get the number of students currently enrolled
     */
    public int getTotalRegistedStudent() {
        int count = 0;
        for (Seat s : seatlist) {
            if (s.isOccupied()) count++;
        }
        return count;
    }
    /**
     * Get the number of remaining empty seats
     */
    public int getSeatsRemaining() {
        return getTotalEmptySeat();
    }
    
    /**
     * Get the total seat capacity for this course offering
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
    * Check if enrollment is currently open for this course
    */
    public boolean isEnrollmentOpen() {
        return enrollmentOpen;
    }

    /**
    * Open enrollment for this course offer
    */
    public void openEnrollment() {
        this.enrollmentOpen = true;
    }

    /**
    * Close enrollment for this course offer
    */
    public void closeEnrollment() {
        this.enrollmentOpen = false;
    }

    /**
    * Set the enrollment status for this course offer
    */
    public void setEnrollmentOpen(boolean open) {
        this.enrollmentOpen = open;
    }

    /**
    * Update the seat capacity for this course offer
    */
    public boolean updateCapacity(int newCapacity) {
        int currentEnrolled = getTotalRegistedStudent();
    
        // Validation: new capacity cannot be less than enrolled student count
        if (newCapacity < currentEnrolled) {
            return false;
        }
    
        int currentCapacity = seatlist.size();
    
        if (newCapacity > currentCapacity) {
            // Increase capacity by adding new seats
            for (int i = currentCapacity; i < newCapacity; i++) {
                seatlist.add(new Seat(this, i));
            }
        } else if (newCapacity < currentCapacity) {
            // Decrease capacity by removing empty seats from the end
            for (int i = seatlist.size() - 1; i >= newCapacity; i--) {
                if (!seatlist.get(i).isOccupied()) {
                    seatlist.remove(i);
                } else {
                    return false; // Cannot remove occupied seats
                }
            }
        }
    
        return true;
    }

    /**
    * Get all seat assignments for enrolled students
    */
    public ArrayList<SeatAssignment> getEnrolledSeatAssignments() {
        ArrayList<SeatAssignment> enrolled = new ArrayList<>();
        for (Seat s : seatlist) {
            if (s.isOccupied()) {
                enrolled.add(s.getSeatAssignment());
            }
        }
        return enrolled;
    }
    
    /**
     * Get students ranked by their assignment scores (highest to lowest)
     */
    public ArrayList<SeatAssignment> getRankedStudents() {
        // Step 1: Get all enrolled students
        ArrayList<SeatAssignment> allEnrolledStudents = getEnrolledSeatAssignments();
    
        // Step 2: Sort by assignment average score (highest to lowest)
        allEnrolledStudents.sort((student1, student2) -> Double.compare(
            student2.getAssignmentAverageScore(),  // Higher score first
            student1.getAssignmentAverageScore()
        ));
    
        return allEnrolledStudents;
    }

    /**
     * Calculate the class average GPA for this course offer
     */
    public double getClassAverageGPA() {
        // Get all enrolled students
        ArrayList<SeatAssignment> allEnrolledStudents = getEnrolledSeatAssignments();
    
        if (allEnrolledStudents.isEmpty()) {
            return 0.0;
        }
    
        double totalGPA = 0.0;
        int gradedStudentCount = 0;
    
        // Sum up GPAs for students who have been graded
        for (SeatAssignment student : allEnrolledStudents) {
            if (student.getLetterGrade() != null) {  // Only count graded students
                totalGPA += student.getGPA();
                gradedStudentCount++;
            }
        }
    
        // Return average or 0 if no students have been graded yet
        return gradedStudentCount == 0 ? 0.0 : totalGPA / gradedStudentCount;
        }
    
    /**
     * Get grade distribution for this course offer
     */
    public HashMap<String, Integer> getGradeDistribution() {
        HashMap<String, Integer> distribution = new HashMap<>();
    
        // Initialize all possible grades with count of 0
        distribution.put("A", 0);
        distribution.put("A-", 0);
        distribution.put("B+", 0);
        distribution.put("B", 0);
        distribution.put("B-", 0);
        distribution.put("C+", 0);
        distribution.put("C", 0);
        distribution.put("C-", 0);
        distribution.put("F", 0);
        distribution.put("Not Graded", 0);  // Students without grades yet
    
        // Count the number of students for each grade
        ArrayList<SeatAssignment> enrolled = getEnrolledSeatAssignments();
        for (SeatAssignment sa : enrolled) {
            String grade = sa.getLetterGrade();
            if (grade != null) {
                distribution.put(grade, distribution.get(grade) + 1);
            } else {
                distribution.put("Not Graded", distribution.get("Not Graded") + 1);
            }
        }
    
        return distribution;
    }
    
    @Override
    public String toString() {
        return course.getCOurseNumber();
    }

}
