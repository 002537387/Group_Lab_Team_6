/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package WorkAreas.StudentRole;
import Business.Business;
import Business.CourseCatalog.Course;
import Business.CourseSchedule.SeatAssignment;
import Business.Department.Department;
import Business.Degree.Degree;
import Business.Profiles.StudentProfile;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author jim.hsieh
 */
public class GraduationAuditJPanel extends javax.swing.JPanel {

    Business business;
    StudentProfile student;
    JPanel CardSequencePanel;
    Department department;
    Degree degree;
    // 畢業要求常數
    private static final int TOTAL_CREDITS_REQUIRED = 32;
    private static final int CORE_CREDITS_REQUIRED = 8;
    private static final int ELECTIVE_CREDITS_REQUIRED = 24;

    /**
     * Creates new form GraduationAuditJPanel
     */
    public GraduationAuditJPanel(Business bz, StudentProfile sp, JPanel panel) {
        initComponents();
        this.business = bz;
        this.student = sp;
        this.CardSequencePanel = panel;
        this.department = business.getDepartment();
        this.degree = department.getDegree();
        
        populateGraduationAudit();
    }
     private void populateGraduationAudit() {
        // 獲取學生所有已完成的課程
        ArrayList<SeatAssignment> completedCourses = student.getCourseList();
        
        // 計算總學分
        int totalCreditsCompleted = 0;
        for (SeatAssignment sa : completedCourses) {
            if (sa.getLetterGrade() != null) {
                totalCreditsCompleted += sa.getCreditHours();
            }
        }
        
        // 更新 Summary
        updateSummary(totalCreditsCompleted);
        
        // 填充必修課表格
        populateCoreCourses(completedCourses);
        
        // 填充選修課表格
        populateElectiveCourses(completedCourses);
        
        // 更新選修課統計
        updateElectiveStats(completedCourses);
    }
    
    private void updateSummary(int totalCreditsCompleted) {
        int creditsRemaining = TOTAL_CREDITS_REQUIRED - totalCreditsCompleted;
        if (creditsRemaining < 0) creditsRemaining = 0;
        
        // 更新標籤
        lblTotalRequired.setText("Total Credits Required: " + TOTAL_CREDITS_REQUIRED);
        lblCompleted.setText("Credits Completed: " + totalCreditsCompleted);
        lblRemaining.setText("Credits Remaining: " + creditsRemaining);
        
        // 更新進度條
        int progressPercentage = (int) ((totalCreditsCompleted * 100.0) / TOTAL_CREDITS_REQUIRED);
        if (progressPercentage > 100) progressPercentage = 100;
        progressBar.setValue(progressPercentage);
        lblProgress.setText("Progress: " + progressPercentage + "%");
        
        // 更新畢業狀態
        if (totalCreditsCompleted >= TOTAL_CREDITS_REQUIRED) {
            lblStatus.setText("Graduation Status: ✓ Ready to Graduate!");
            lblStatus.setForeground(new java.awt.Color(0, 128, 0)); // 綠色
        } else if (totalCreditsCompleted >= TOTAL_CREDITS_REQUIRED * 0.75) {
            lblStatus.setText("Graduation Status: ⚠ On Track");
            lblStatus.setForeground(new java.awt.Color(255, 165, 0)); // 橙色
        } else {
            lblStatus.setText("Graduation Status: ⚠️ Need More Credits");
            lblStatus.setForeground(new java.awt.Color(255, 0, 0)); // 紅色
        }
    }
    
    private void populateCoreCourses(ArrayList<SeatAssignment> completedCourses) {
        DefaultTableModel model = (DefaultTableModel) tblCoreCourses.getModel();
        model.setRowCount(0);
        
        // 獲取所有必修課
        ArrayList<Course> coreCourses = degree.getCoreCourses();
        
        if (coreCourses == null || coreCourses.isEmpty()) {
            model.addRow(new Object[]{
                "No core courses defined", "", "", "", ""
            });
            return;
        }
        
        // 創建已完成課程的映射
        ArrayList<String> completedCourseNumbers = new ArrayList<>();
        for (SeatAssignment sa : completedCourses) {
            completedCourseNumbers.add(sa.getCourseOffer().getCourseNumber());
        }
        
        // 顯示每門必修課的狀態
        for (Course course : coreCourses) {
            String courseCode = course.getCOurseNumber();
            String courseName = course.getCourseName();
            int credits = course.getCredits();
            
            String status;
            String grade = "-";
            
            // 檢查是否已完成
            boolean isCompleted = false;
            for (SeatAssignment sa : completedCourses) {
                if (sa.getCourseOffer().getCourseNumber().equals(courseCode)) {
                    isCompleted = true;
                    grade = sa.getLetterGrade() != null ? sa.getLetterGrade() : "IP";
                    break;
                }
            }
            
            status = isCompleted ? "✓ Completed" : "✗ Not Taken";
            
            model.addRow(new Object[]{
                courseCode,
                courseName,
                credits,
                status,
                grade
            });
        }
    }
    
    private void populateElectiveCourses(ArrayList<SeatAssignment> completedCourses) {
        DefaultTableModel model = (DefaultTableModel) tblElectives.getModel();
        model.setRowCount(0);
        
        // 獲取所有選修課
        ArrayList<Course> electiveCourses = degree.getElectiveCourses();
        ArrayList<String> coreCourseCodes = new ArrayList<>();
        
        // 獲取必修課代碼列表
        for (Course c : degree.getCoreCourses()) {
            coreCourseCodes.add(c.getCOurseNumber());
        }
        
        // 顯示學生已修的選修課
        boolean hasElectives = false;
        for (SeatAssignment sa : completedCourses) {
            String courseCode = sa.getCourseOffer().getCourseNumber();
            
            // 如果不是必修課，就是選修課
            if (!coreCourseCodes.contains(courseCode)) {
                hasElectives = true;
                
                String courseName = sa.getCourseOffer().getSubjectCourse().getCourseName();
                int credits = sa.getCreditHours();
                String semester = sa.getSeat().getCourseOffer().toString(); // 簡化
                String grade = sa.getLetterGrade() != null ? sa.getLetterGrade() : "IP";
                
                model.addRow(new Object[]{
                    courseCode,
                    courseName,
                    credits,
                    semester,
                    grade
                });
            }
        }
        
        if (!hasElectives) {
            model.addRow(new Object[]{
                "No elective courses taken yet", "", "", "", ""
            });
        }
    }
    
    private void updateElectiveStats(ArrayList<SeatAssignment> completedCourses) {
        // 計算已完成的選修課學分
        int electiveCreditsCompleted = 0;
        
        ArrayList<String> coreCourseCodes = new ArrayList<>();
        for (Course c : degree.getCoreCourses()) {
            coreCourseCodes.add(c.getCOurseNumber());
        }
        
        for (SeatAssignment sa : completedCourses) {
            String courseCode = sa.getCourseOffer().getCourseNumber();
            
            // 如果不是必修課，計入選修學分
            if (!coreCourseCodes.contains(courseCode)) {
                if (sa.getLetterGrade() != null) {
                    electiveCreditsCompleted += sa.getCreditHours();
                }
            }
        }
        
        int electiveRemaining = ELECTIVE_CREDITS_REQUIRED - electiveCreditsCompleted;
        if (electiveRemaining < 0) electiveRemaining = 0;
        
        lblElectiveRequired.setText("Elective Credits Required: " + ELECTIVE_CREDITS_REQUIRED);
        lblElectiveCompleted.setText("Elective Credits Completed: " + electiveCreditsCompleted);
        lblElectiveRemaining.setText("Elective Credits Remaining: " + electiveRemaining);
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
        lblTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        lblGraduationRequirementSummary = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblTotalRequired = new javax.swing.JLabel();
        lblCompleted = new javax.swing.JLabel();
        lblRemaining = new javax.swing.JLabel();
        lblProgress = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCoreCourses = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblElectives = new javax.swing.JTable();
        lblElectiveRequired = new javax.swing.JLabel();
        lblElectiveCompleted = new javax.swing.JLabel();
        lblElectiveRemaining = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 30, -1, -1));

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lblTitle.setText("Graduation Audit");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(438, 28, 150, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lblGraduationRequirementSummary.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lblGraduationRequirementSummary.setText("Graduation Requirements Summary");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblGraduationRequirementSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(485, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblGraduationRequirementSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane2.setTopComponent(jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lblTotalRequired.setText("Total Credits Required:");

        lblCompleted.setText("Credits Completed:");

        lblRemaining.setText("Credits Remaining:");

        lblProgress.setText("Progress:");

        lblStatus.setText("Graduation Status:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTotalRequired)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCompleted)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRemaining)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblStatus)
                .addGap(22, 22, 22))
        );

        jSplitPane2.setRightComponent(jPanel3);

        jPanel1.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 810, 230));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel8.setText("Core Courses(Required)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel8)
                .addContainerGap(588, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setTopComponent(jPanel5);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        tblCoreCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course Code", "Course Name", "Credits", "Status", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblCoreCourses);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel7);

        jPanel4.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 300, 810, 260));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel9.setText("Elective Courses");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(609, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(40, 40, 40))
        );

        jSplitPane3.setTopComponent(jPanel8);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        tblElectives.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course Code", "Course Name", "Credits", "Semester", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblElectives);

        lblElectiveRequired.setText("Elective Credits Required:");

        lblElectiveCompleted.setText("Elective Credits Completed:");

        lblElectiveRemaining.setText("Elective Credits Remaining:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblElectiveRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblElectiveCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblElectiveRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblElectiveRequired)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblElectiveCompleted)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblElectiveRemaining)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel9);

        jPanel6.add(jSplitPane3, java.awt.BorderLayout.CENTER);

        add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 560, 810, 310));
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
         CardSequencePanel.remove(this);
    ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JLabel lblCompleted;
    private javax.swing.JLabel lblElectiveCompleted;
    private javax.swing.JLabel lblElectiveRemaining;
    private javax.swing.JLabel lblElectiveRequired;
    private javax.swing.JLabel lblGraduationRequirementSummary;
    private javax.swing.JLabel lblProgress;
    private javax.swing.JLabel lblRemaining;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalRequired;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTable tblCoreCourses;
    private javax.swing.JTable tblElectives;
    // End of variables declaration//GEN-END:variables
}
