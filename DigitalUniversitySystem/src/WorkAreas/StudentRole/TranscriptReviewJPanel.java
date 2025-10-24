/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package WorkAreas.StudentRole;
import Business.Business;
import Business.CourseSchedule.CourseLoad;
import Business.CourseSchedule.SeatAssignment;
import Business.Profiles.StudentProfile;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jim.hsieh
 */
public class TranscriptReviewJPanel extends javax.swing.JPanel {

    Business business;
    StudentProfile student;
    JPanel CardSequencePanel;

    /**
     * Creates new form TranscriptReviewJPanel
     */
    public TranscriptReviewJPanel(Business bz, StudentProfile sp, JPanel panel) {
        initComponents();
        this.business = bz;
        this.student = sp;
        this.CardSequencePanel = panel;
    
        if (!student.canViewTranscript()) {
        JOptionPane.showMessageDialog(this,
            "You have an outstanding balance of $" + 
            String.format("%.2f", student.getBalance()) + 
            "\nPlease pay your tuition before viewing your transcript.",
            "Payment Required",
            JOptionPane.WARNING_MESSAGE);
        
        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
        return;
    }
        populateSemesters();
        populateTranscriptTable();
        displaySummary();
    }
     private void populateTranscriptTable() {
           DefaultTableModel model = (DefaultTableModel) tblAcademicTranscript.getModel();
    model.setRowCount(0);
    
    String selectedSemester = (String) cmbSemester.getSelectedItem();
    
    // 獲取所有學期的課程記錄
    HashMap<String, CourseLoad> courseLoadMap = student.getTranscript().getCourseloadlist();
    
    // 計算 Overall GPA（所有學期累計）
    double overallTotalGradePoints = 0;
    int overallTotalCredits = 0;
    
    for (CourseLoad cl : courseLoadMap.values()) {
        for (SeatAssignment sa : cl.getSeatAssignments()) {
            if (sa.getLetterGrade() != null) {
                overallTotalGradePoints += sa.getGPA() * sa.getCreditHours();
                overallTotalCredits += sa.getCreditHours();
            }
        }
    }
    
    double overallGPA = overallTotalCredits > 0 ? overallTotalGradePoints / overallTotalCredits : 0;
    
    // 累計的 Overall GPA Points（用於顯示）
    double cumulativeGPAPoints = 0;
    
    // 按學期顯示
    for (String semester : courseLoadMap.keySet()) {
        // 如果選擇了特定學期，只顯示該學期
        if (!"All Semesters".equals(selectedSemester) && !semester.equals(selectedSemester)) {
            continue;
        }
        
        CourseLoad cl = courseLoadMap.get(semester);
        
        // 計算該學期的 Term GPA
        double termTotalGradePoints = 0;
        int termTotalCredits = 0;
        
        ArrayList<SeatAssignment> seats = cl.getSeatAssignments();
        for (SeatAssignment sa : seats) {
            if (sa.getLetterGrade() != null) {
                termTotalGradePoints += sa.getGPA() * sa.getCreditHours();
                termTotalCredits += sa.getCreditHours();
            }
        }
        
        double termGPA = termTotalCredits > 0 ? termTotalGradePoints / termTotalCredits : 0;
        
        // 判斷 Academic Standing
        String academicStanding = determineAcademicStanding(termGPA, overallGPA);
        
        // 顯示該學期的課程
        for (SeatAssignment sa : seats) {
            String courseCode = sa.getCourseOffer().getCourseNumber();
            String courseName = sa.getCourseOffer().getSubjectCourse().getCourseName();
            int credits = sa.getCreditHours();
            String grade = sa.getLetterGrade() != null ? sa.getLetterGrade() : "N/A";
            
            // 計算該課程的 Term GPA Points
            double courseGPAPoints = sa.getGPA() * credits;
            cumulativeGPAPoints += courseGPAPoints;
            
            model.addRow(new Object[]{
                semester,                                    // Term
                academicStanding,                           // Academic Standing
                courseCode,                                 // Course ID
                courseName,                                 // Course Name
                grade,                                      // Grade
                credits,                                    // Credits
                String.format("%.2f", courseGPAPoints),    // Term GPA Points
                String.format("%.2f", cumulativeGPAPoints) // Overall GPA Points (累計)
            });
        }
        
        // 添加該學期小計
        model.addRow(new Object[]{
            semester + " Total",
            "",
            "",
            "",
            "",
            termTotalCredits,
            String.format("%.2f", termTotalGradePoints),
            ""
        });
        
        // 空行分隔
        model.addRow(new Object[]{"", "", "", "", "", "", "", ""});
    }
    }
     private void populateSemesters() {
    cmbSemester.removeAllItems();
    cmbSemester.addItem("All Semesters");
    
    HashMap<String, CourseLoad> courseLoadMap = student.getTranscript().getCourseloadlist();
    for (String semester : courseLoadMap.keySet()) {
        cmbSemester.addItem(semester);
    }
    
    // 默認選擇 "All Semesters"
    cmbSemester.setSelectedIndex(0);
}

private String determineAcademicStanding(double termGPA, double overallGPA) {
    // Good Standing: Term GPA ≥ 3.0 AND Overall GPA ≥ 3.0
    if (termGPA >= 3.0 && overallGPA >= 3.0) {
        return "Good Standing";
    }
    
    // Academic Probation: Overall GPA < 3.0 (regardless of term GPA)
    if (overallGPA < 3.0) {
        return "Academic Probation";
    }
    
    // Academic Warning: Term GPA < 3.0 (even if overall GPA ≥ 3.0)
    if (termGPA < 3.0) {
        return "Academic Warning";
    }
    
    return "Good Standing";
}
    
    private void displaySummary() {
        // 計算累計 GPA 和總學分
        ArrayList<SeatAssignment> allCourses = student.getCourseList();
        
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (SeatAssignment sa : allCourses) {
            if (sa.getLetterGrade() != null) {
                totalGradePoints += sa.getGPA() * sa.getCreditHours();
                totalCredits += sa.getCreditHours();
            }
        }
        
        double cumulativeGPA = totalCredits > 0 ? totalGradePoints / totalCredits : 0;
        
        lblTotalCredits.setText("Total Credits:" + totalCredits);
        lblCumulative.setText(String.format("Cumulative GPA:%.2f", cumulativeGPA));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new javax.swing.JButton();
        lblAcademicTranscript = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAcademicTranscript = new javax.swing.JTable();
        lblTotalCredits = new javax.swing.JLabel();
        lblCumulative = new javax.swing.JLabel();
        lblSelectSemester = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        lblAcademicTranscript.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lblAcademicTranscript.setText("Academic Transcript");
        add(lblAcademicTranscript, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 240, -1));

        tblAcademicTranscript.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Term", "Academic Standing", "Course ID", "Course Name", "Grade", "Credits", "Term GPA Points", "Overall GPA Points"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAcademicTranscript);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 1040, 260));

        lblTotalCredits.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        lblTotalCredits.setText("Total Credits:0");
        add(lblTotalCredits, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 470, 110, -1));

        lblCumulative.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        lblCumulative.setText("Cumulative GPA:0.00");
        add(lblCumulative, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 470, -1, -1));

        lblSelectSemester.setText("Selelct Semester:");
        add(lblSelectSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 120, -1));

        cmbSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterActionPerformed(evt);
            }
        });
        add(cmbSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 110, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
    ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        // TODO add your handling code here:
        populateTranscriptTable();
    }//GEN-LAST:event_cmbSemesterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAcademicTranscript;
    private javax.swing.JLabel lblCumulative;
    private javax.swing.JLabel lblSelectSemester;
    private javax.swing.JLabel lblTotalCredits;
    private javax.swing.JTable tblAcademicTranscript;
    // End of variables declaration//GEN-END:variables
}
