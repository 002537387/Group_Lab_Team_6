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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eric
 */
public class TuitionInsightJPanel extends javax.swing.JPanel {

    private Business business;
    private FacultyProfile faculty;
    JPanel CardSequencePanel;
    
    /**
     * Creates new form TuitionInsightJPanel
     */
    public TuitionInsightJPanel(Business business, FacultyProfile faculty, JPanel panel) {
        
        this.business = business;
        this.faculty = faculty;
        this.CardSequencePanel = panel;
        
        initComponents();
        
        txtTotalRevenue.setEnabled(false);
        populateSemesters();
        
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
        }
    }
    
    /**
     * Generate tuition report for selected semester
     */
    private void generateTuition() {
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        
        if (selectedSemester == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a semester!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Department department = business.getDepartment();
        CourseSchedule schedule = department.getCourseSchedule(selectedSemester);
        
        if (schedule == null) {
            JOptionPane.showMessageDialog(this, 
                "No courses found for semester: " + selectedSemester, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            clearReport();
            return;
        }
        
        // Get courses taught by this faculty
        ArrayList<CourseOffer> facultyCourses = new ArrayList<>();
        for (CourseOffer co : schedule.getCourseOffers()) {
            FacultyProfile fp = co.getFacultyProfile();
            if (fp != null && fp.equals(faculty)) {
                facultyCourses.add(co);
            }
        }
        
        if (facultyCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "You are not teaching any courses in " + selectedSemester, 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
            clearReport();
            return;
        }
        
        // Display report
        populateTuitionTable(facultyCourses);
        calculateTotalRevenue(facultyCourses);
    }
    
    /**
     * Clear report
     */
    private void clearReport() {
        DefaultTableModel model = (DefaultTableModel) tblTuitionSummary.getModel();
        model.setRowCount(0);
        txtTotalRevenue.setText("$0");
    }
    
    /**
     * Populate tuition table
     */
    private void populateTuitionTable(ArrayList<CourseOffer> courses) {
        DefaultTableModel model = (DefaultTableModel) tblTuitionSummary.getModel();
        model.setRowCount(0);
        
        for (CourseOffer co : courses) {
            Course course = co.getSubjectCourse();
            
            String courseNumber = course.getCOurseNumber();
            int enrolled = co.getTotalRegistedStudent();
            int tuitionPerStudent = co.getTuitionFee();
            int totalRevenue = co.getTotalCourseRevenues();
            
            Object[] row = {
                courseNumber,
                enrolled,
                "$" + String.format("%,d", tuitionPerStudent),
                "$" + String.format("%,d", totalRevenue)
            };
            
            model.addRow(row);
        }
    }
    
    /**
     * Calculate and display total revenue
     */
    private void calculateTotalRevenue(ArrayList<CourseOffer> courses) {
        int totalRevenue = 0;
        
        for (CourseOffer co : courses) {
            totalRevenue += co.getTotalCourseRevenues();
        }
        
        txtTotalRevenue.setText("$" + String.format("%,d", totalRevenue));
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
        btnGenerateTuition = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTuitionSummary = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtTotalRevenue = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Tuition/Enrollment Insight");

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

        btnGenerateTuition.setText("Generate Tuition");
        btnGenerateTuition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateTuitionActionPerformed(evt);
            }
        });

        jLabel3.setText("Tuition Summary by Course");

        tblTuitionSummary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Course Number", "Enrolled", "$ / Student", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblTuitionSummary);

        jLabel4.setText("Total Tuition Revenue:");

        txtTotalRevenue.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(btnGenerateTuition))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(109, 109, 109)
                                .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(btnBack)
                        .addGap(141, 141, 141)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jLabel4)
                        .addGap(35, 35, 35)
                        .addComponent(txtTotalRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnBack))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(28, 28, 28)
                .addComponent(btnGenerateTuition)
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTotalRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(389, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbSemesterActionPerformed

    private void btnGenerateTuitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateTuitionActionPerformed
        // TODO add your handling code here:
        generateTuition();
    }//GEN-LAST:event_btnGenerateTuitionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnGenerateTuition;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTuitionSummary;
    private javax.swing.JTextField txtTotalRevenue;
    // End of variables declaration//GEN-END:variables
}
