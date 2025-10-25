/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package WorkAreas.FacultyRole;

import Business.Business;
import Business.CourseCatalog.Course;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.CourseSchedule;
import Business.Department.Department;
import Business.Profiles.FacultyProfile;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eric
 */
public class PerformanceReportingJPanel extends javax.swing.JPanel {

    private Business business;
    private FacultyProfile faculty;
    JPanel CardSequencePanel;
    private CourseOffer selectedCourse;
    
    /**
     * Creates new form PerformanceReportingJPanel
     */
    public PerformanceReportingJPanel(Business business, FacultyProfile faculty, JPanel panel) {
        
        this.business = business;
        this.faculty = faculty;
        this.CardSequencePanel = panel;
        
        initComponents();
        
        setDetailsDisabled();
        populateSemesters();
    }

    private void setDetailsDisabled() {
        txtCourse.setEnabled(false);
        txtEnrolled.setEnabled(false);
        txtAverageGrade.setEnabled(false);
    }
    
    private void populateSemesters() {
        cmbSemester.removeAllItems();
        
        Department department = business.getDepartment();
        if (department == null) {
            JOptionPane.showMessageDialog(this, "Department not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ArrayList<String> semesters = new ArrayList<>(department.mastercoursecatalog.keySet());
        
        if (semesters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No semesters found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (String semester : semesters) {
            cmbSemester.addItem(semester);
        }
        
        if (cmbSemester.getItemCount() > 0) {
            cmbSemester.setSelectedIndex(0);
            populateCoursesForSemester();
        }
    }
    
    /**
     * Load courses for selected semester
     */
    private void populateCoursesForSemester() {
        cmbCourse.removeAllItems();
        
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        if (selectedSemester == null) return;
        
        Department department = business.getDepartment();
        CourseSchedule schedule = department.getCourseSchedule(selectedSemester);
        
        if (schedule == null) return;
        
        // Get courses taught by this faculty
        for (CourseOffer co : schedule.getCourseOffers()) {
            FacultyProfile fp = co.getFacultyProfile();
            if (fp != null && fp.equals(faculty)) {
                cmbCourse.addItem(co.getCourseNumber());
            }
        }
    }
    
    /**
     * Generate performance report
     */
    private void generateReport() {
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        String selectedCourseNumber = (String) cmbCourse.getSelectedItem();
        
        if (selectedSemester == null || selectedCourseNumber == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select semester and course!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Find course offer
        Department department = business.getDepartment();
        CourseSchedule schedule = department.getCourseSchedule(selectedSemester);
        selectedCourse = schedule.getCourseOfferByNumber(selectedCourseNumber);
        
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, 
                "Course not found!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Display report
        displayPerformanceSummary();
        displayGradeDistribution();
        
    }
    
    /**
     * Display performance summary
     */
    private void displayPerformanceSummary() {
        Course course = selectedCourse.getSubjectCourse();
        
        // Basic course info
        txtCourse.setText(course.getCOurseNumber());
        txtEnrolled.setText(String.valueOf(selectedCourse.getTotalRegistedStudent()));
        
        // Performance
        double avgGPA = selectedCourse.getClassAverageGPA();
        txtAverageGrade.setText(String.format("%.2f", avgGPA));
        
    }
    
    /**
     * Display grade distribution table
     */
    private void displayGradeDistribution() {
        HashMap<String, Integer> distribution = selectedCourse.getGradeDistribution();
        int totalStudents = selectedCourse.getTotalRegistedStudent();
        
        DefaultTableModel model = (DefaultTableModel) tblGradeDistribution.getModel();
        model.setRowCount(0);
        
        // Order of grades to display
        String[] gradeOrder = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "F", "Not Graded"};
        
        for (String grade : gradeOrder) {
            int count = distribution.get(grade);
            double percentage = totalStudents == 0 ? 0.0 : (count * 100.0 / totalStudents);
            
            Object[] row = {
                grade,
                count,
                String.format("%.1f%%", percentage)
            };
            
            model.addRow(row);
        }
    }
    
    /**
     * Clear all displays
     */
    private void clearReport() {
        txtCourse.setText("");
        txtEnrolled.setText("");
        txtAverageGrade.setText("");
        
        DefaultTableModel model = (DefaultTableModel) tblGradeDistribution.getModel();
        model.setRowCount(0);
        
        selectedCourse = null;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cmbCourse = new javax.swing.JComboBox<>();
        btnGenerateReport = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCourse = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEnrolled = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAverageGrade = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGradeDistribution = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Performance Reporting");

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel2.setText("Select Semester:");

        cmbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterActionPerformed(evt);
            }
        });

        jLabel3.setText("Select Course:");

        cmbCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCourseActionPerformed(evt);
            }
        });

        btnGenerateReport.setText("Generate Report");
        btnGenerateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateReportActionPerformed(evt);
            }
        });

        jLabel4.setText("Course Performance Summary");

        jLabel5.setText("Course:");

        txtCourse.setEditable(false);

        jLabel6.setText("Total Enrolled:");

        txtEnrolled.setEditable(false);

        jLabel7.setText("Average Grade:");

        txtAverageGrade.setEditable(false);

        jLabel8.setText("Grade Distribtion");

        tblGradeDistribution.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Grade", "Count", "Percentage"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblGradeDistribution);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(122, 122, 122)
                                .addComponent(btnBack))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(173, 173, 173)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))))
                        .addGap(120, 120, 120)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cmbCourse, javax.swing.GroupLayout.Alignment.LEADING, 0, 126, Short.MAX_VALUE)
                                    .addComponent(cmbSemester, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(104, 104, 104)
                                .addComponent(btnGenerateReport))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(319, 319, 319)
                        .addComponent(jLabel4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCourse)
                    .addComponent(txtEnrolled)
                    .addComponent(txtAverageGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(102, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(64, 64, 64)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(316, 316, 316))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnBack))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(btnGenerateReport)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtEnrolled, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtAverageGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(282, 282, 282)
                        .addComponent(jLabel8)))
                .addContainerGap(156, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateReportActionPerformed
        // TODO add your handling code here:
        generateReport();
    }//GEN-LAST:event_btnGenerateReportActionPerformed

    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        // TODO add your handling code here:
        populateCoursesForSemester();
        clearReport();
    }//GEN-LAST:event_cmbSemesterActionPerformed

    private void cmbCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCourseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCourseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnGenerateReport;
    private javax.swing.JComboBox<String> cmbCourse;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblGradeDistribution;
    private javax.swing.JTextField txtAverageGrade;
    private javax.swing.JTextField txtCourse;
    private javax.swing.JTextField txtEnrolled;
    // End of variables declaration//GEN-END:variables
}
