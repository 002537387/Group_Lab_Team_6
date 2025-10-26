/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package WorkAreas.RegistrarRole;

import Business.Business;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.CourseSchedule;
import Business.CourseSchedule.SeatAssignment;
import Business.Department.Department;
import Business.Profiles.RegistrarProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wanyuchiu
 */
public class ReportingAndAnalysisJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ReportingAndAnalysisJPanel
     */
    JPanel CardSequencePanel;
    Business business;
    
    public ReportingAndAnalysisJPanel(Business bz, JPanel jp) {
        
        business = bz;
        this.CardSequencePanel = jp;
        
        
        initComponents();
        
        // 初始化下拉框
        initializeSemesterComboBoxes();
        
        // 初始化课程下拉框
        initializeCourseComboBox();
        
        // 默认显示所有学期的招生报告
        populateEnrollmentReport("All");
        
        // 添加下拉框监听器
        addComboBoxListeners();
    }
    /**
     * 初始化学期下拉框
     */
    private void initializeSemesterComboBoxes() {
        ComboBoxSelectedSemester.removeAllItems();
        ComboBoxSelectedSemesterGPA.removeAllItems();
        
        ComboBoxSelectedSemester.addItem("All");
        ComboBoxSelectedSemesterGPA.addItem("All");
        
        Department department = business.getDepartment();
        Collection<CourseSchedule> schedules = department.getAllCourseSchedule();
        
        for (CourseSchedule schedule : schedules) {
            String semester = schedule.getTerm();
            ComboBoxSelectedSemester.addItem(semester);
            ComboBoxSelectedSemesterGPA.addItem(semester);
        }
    }
    
     /**
     * 初始化课程下拉框
     */
    private void initializeCourseComboBox() {
        ComboBoxCourseNumber.removeAllItems();
        ComboBoxCourseNumber.addItem("All");
        
        Department department = business.getDepartment();
        Collection<CourseSchedule> schedules = department.getAllCourseSchedule();
        
        ArrayList<String> courseNumbers = new ArrayList<>();
        
        for (CourseSchedule schedule : schedules) {
            ArrayList<CourseOffer> offers = schedule.getCourseOffers();
            for (CourseOffer offer : offers) {
                String courseNumber = offer.getCourseNumber();
                if (!courseNumbers.contains(courseNumber)) {
                    courseNumbers.add(courseNumber);
                    ComboBoxCourseNumber.addItem(courseNumber);
                }
            }
        }
    }
    
    /**
     * 添加下拉框监听器
     */
    private void addComboBoxListeners() {
        ComboBoxSelectedSemester.addActionListener(e -> {
            String selectedSemester = ComboBoxSelectedSemester.getSelectedItem().toString();
            populateEnrollmentReport(selectedSemester);
        });
        
        ComboBoxSelectedSemesterGPA.addActionListener(e -> {
            String selectedSemester = ComboBoxSelectedSemesterGPA.getSelectedItem().toString();
            String selectedCourse = ComboBoxCourseNumber.getSelectedItem().toString();
            populateGPAReport(selectedSemester, selectedCourse);
        });
        
        ComboBoxCourseNumber.addActionListener(e -> {
            String selectedSemester = ComboBoxSelectedSemesterGPA.getSelectedItem().toString();
            String selectedCourse = ComboBoxCourseNumber.getSelectedItem().toString();
            populateGPAReport(selectedSemester, selectedCourse);
        });
    }
    
    /**
     * 生成招生报告 - Enrollment by Department/Course
     */
    private void populateEnrollmentReport(String filterSemester) {
        
        DefaultTableModel model = (DefaultTableModel) tblSemester.getModel();
        model.setRowCount(0);
        
        Department department = business.getDepartment();
        Collection<CourseSchedule> schedules = department.getAllCourseSchedule();
        
        for (CourseSchedule schedule : schedules) {
            String semester = schedule.getTerm();
            
            // 如果筛选学期不是"All"，则只显示该学期
            if (!filterSemester.equals("All") && !semester.equals(filterSemester)) {
                continue;
            }
            
            ArrayList<CourseOffer> offers = schedule.getCourseOffers();
            
            for (CourseOffer offer : offers) {
                String courseNumber = offer.getCourseNumber();
                String faculty = offer.getFaculty();
                int capacity = offer.getTotalSeats();
                int enrolled = offer.getTotalRegistedStudent();
                int available = offer.getTotalEmptySeat();
                
                // 计算注册率
                double enrollmentRate = capacity > 0 ? (enrolled * 100.0 / capacity) : 0;
                String enrollmentRateStr = String.format("%.1f%%", enrollmentRate);
                
                Object[] row = {
                    semester,
                    courseNumber,
                    faculty,
                    capacity,
                    enrolled,
                    available,
                    enrollmentRateStr
                };
                
                model.addRow(row);
            }
        }
    }
    
    /**
     * 生成GPA分布报告 - GPA Distribution by Program
     */
    private void populateGPAReport(String filterSemester, String filterCourse) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        // GPA映射
        Map<String, Double> gpaMap = new HashMap<>();
        gpaMap.put("A", 4.0);
        gpaMap.put("A-", 3.7);
        gpaMap.put("B+", 3.3);
        gpaMap.put("B", 3.0);
        gpaMap.put("B-", 2.7);
        gpaMap.put("C+", 2.3);
        gpaMap.put("C", 2.0);
        gpaMap.put("C-", 1.7);
        gpaMap.put("F", 0.0);
        
        // 初始化成绩分布统计
        Map<String, Integer> gradeDistribution = new HashMap<>();
        for (String grade : gpaMap.keySet()) {
            gradeDistribution.put(grade, 0);
        }
        
        Department department = business.getDepartment();
        Collection<CourseSchedule> schedules = department.getAllCourseSchedule();
        
        int totalGradedAssignments = 0;
        
        // 统计所有课程的成绩分布
        for (CourseSchedule schedule : schedules) {
            String semester = schedule.getTerm();
            
            // 学期筛选
            if (!filterSemester.equals("All") && !semester.equals(filterSemester)) {
                continue;
            }
            
            ArrayList<CourseOffer> offers = schedule.getCourseOffers();
            
            for (CourseOffer offer : offers) {
                String courseNumber = offer.getCourseNumber();
                
                // 课程筛选
                if (!filterCourse.equals("All") && !courseNumber.equals(filterCourse)) {
                    continue;
                }
               
                ArrayList<SeatAssignment> enrolledStudents = offer.getEnrolledSeatAssignments();
                
                for (SeatAssignment sa : enrolledStudents) {
                    String grade = sa.getLetterGrade();
                    if (grade != null && gradeDistribution.containsKey(grade)) {
                        gradeDistribution.put(grade, gradeDistribution.get(grade) + 1);
                        totalGradedAssignments++;
                    }
                }
            }
        }
     // 按成绩顺序显示
        String[] gradeOrder = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "F"};
        
        for (String grade : gradeOrder) {
            int count = gradeDistribution.get(grade);
            double percentage = totalGradedAssignments > 0 ? (count * 100.0 / totalGradedAssignments) : 0;
            double gpaPoints = gpaMap.get(grade);
            
            Object[] row = {
                grade,
                count,
                String.format("%.1f%%", percentage),
                gpaPoints
            };
            
            model.addRow(row);
        }
        
        // 显示总数信息
        if (totalGradedAssignments == 0) {
            JOptionPane.showMessageDialog(this, 
                "No graded assignments found for the selected filter.", 
                "No Data", 
                JOptionPane.INFORMATION_MESSAGE);
        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSemester = new javax.swing.JTable();
        lblSemesters = new javax.swing.JLabel();
        ComboBoxSelectedSemester = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblSemester = new javax.swing.JLabel();
        ComboBoxSelectedSemesterGPA = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ComboBoxCourseNumber = new javax.swing.JComboBox<>();
        btnBack = new javax.swing.JButton();

        jLabel1.setText("Enrollment Report");

        tblSemester.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Semester", "Course Number", "Faculty", "Total Capacity", "Enrolled Student", "Avaliable Seat", "Enrollment Rate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSemester);

        lblSemesters.setText("Semester :");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Grade", "Number of student ", "Percentage", "GPA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        lblSemester.setText("Semester :");

        jLabel2.setText("Course Number :");

        jLabel3.setText("GPA Distribution Report");

        btnBack.setText("<<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ComboBoxCourseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSemester)
                                .addGap(29, 29, 29)
                                .addComponent(ComboBoxSelectedSemesterGPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSemesters)
                                .addGap(29, 29, 29)
                                .addComponent(ComboBoxSelectedSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(btnBack)))
                .addContainerGap(361, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSemesters)
                    .addComponent(ComboBoxSelectedSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSemester)
                    .addComponent(ComboBoxSelectedSemesterGPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ComboBoxCourseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxCourseNumber;
    private javax.swing.JComboBox<String> ComboBoxSelectedSemester;
    private javax.swing.JComboBox<String> ComboBoxSelectedSemesterGPA;
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblSemester;
    private javax.swing.JLabel lblSemesters;
    private javax.swing.JTable tblSemester;
    // End of variables declaration//GEN-END:variables
}
