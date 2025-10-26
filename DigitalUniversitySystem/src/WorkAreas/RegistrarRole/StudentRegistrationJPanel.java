        /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package WorkAreas.RegistrarRole;

import Business.Business;
import Business.CourseCatalog.Course;
import Business.CourseCatalog.CourseCatalog;
import Business.CourseSchedule.CourseLoad;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.CourseSchedule;
import Business.CourseSchedule.SeatAssignment;
import Business.Department.Department;
import Business.Directory.StudentDirectory;
import Business.Profiles.RegistrarProfile;
import Business.Profiles.StudentProfile;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wanyuchiu
 */
public class StudentRegistrationJPanel extends javax.swing.JPanel {

    /**
     * Creates new form StudentRegistrationJPanel
     */
    
    JPanel CardSequencePanel;
    Business business;
    RegistrarProfile registrar;
    
    public StudentRegistrationJPanel(Business bz, JPanel jp,RegistrarProfile rp) {
        
        business = bz;
        this.CardSequencePanel = jp;
        this.registrar = rp;

        initComponents();
        
        
        Department department = business.getDepartment();
        
        // 填充學期下拉框
        Collection<CourseSchedule> schedules = department.getAllCourseSchedule();
        if (schedules == null || schedules.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No semesters available!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (CourseSchedule schedule : schedules) {
            ComboBoxSemester.addItem(schedule.getSemester());
        }
        
        // 填充學生下拉框
        StudentDirectory studentDirectory = department.getStudentDirectory();
        ArrayList<StudentProfile> students = studentDirectory.getStudent();
        for (StudentProfile studentProfile : students) {
            ComboBoxStudentName.addItem(studentProfile.getPerson().getName());  
        }
        
        // 填充課程下拉框（所有學期的課程）
        for (CourseSchedule schedule : schedules) {
            ArrayList<CourseOffer> offers = schedule.getCourseOffers();
            for (CourseOffer offer : offers) {
                ComboBoxCourseNum.addItem(offer.getCourseNumber());
            }
        }
        
        // 載入初始表格數據
        loadStudentRegistrations();
    }
    
    
        
    private void loadStudentRegistrations() {
        
        String selectedSemester = (String) ComboBoxSemester.getSelectedItem();
        String selectedStudentName = (String) ComboBoxStudentName.getSelectedItem();
        
        DefaultTableModel model = (DefaultTableModel) tblStudentRegist.getModel();
        model.setRowCount(0); // 清空表格
        
        if (selectedSemester == null || selectedStudentName == null) {
            return;
        }
        
        Department department = business.getDepartment();
        StudentDirectory studentDirectory = department.getStudentDirectory();
        
        // 查找選中的學生
        StudentProfile selectedStudent = null;
        for (StudentProfile sp : studentDirectory.getStudent()) {
            if (sp.getPerson().getName().equals(selectedStudentName)) {
                selectedStudent = sp;
                break;
            }
        }
        
        if (selectedStudent == null) {
            return;
        }
        
        // 獲取該學生在該學期的課程負載
        CourseLoad courseLoad = selectedStudent.getCourseLoadBySemester(selectedSemester);
        
        if (courseLoad == null || courseLoad.getSeatAssignments().isEmpty()) {
            // 沒有註冊任何課程
            return;
        }
        
        // 顯示已註冊的課程
        for (SeatAssignment sa : courseLoad.getSeatAssignments()) {
            CourseOffer offer = sa.getCourseOffer();
            
            Object[] row = new Object[3];
            row[0] = selectedSemester;
            row[1] = selectedStudentName;
            row[2] = offer.getCourseNumber();
            
            model.addRow(row);
        }
    }
            
            
        
     private void updateCourseListBySemester() {
        String selectedSemester = (String) ComboBoxSemester.getSelectedItem();
        
        if (selectedSemester == null) return;
        
        ComboBoxCourseNum.removeAllItems();
        
        Department department = business.getDepartment();
        CourseSchedule schedule = department.getCourseSchedule(selectedSemester);
        
        if (schedule != null) {
            ArrayList<CourseOffer> offers = schedule.getCourseOffers();
            for (CourseOffer offer : offers) {
                ComboBoxCourseNum.addItem(offer.getCourseNumber());
            }
        }
    }
    
    
   

    
       
            
        
   
        // 辅助方法：根据名字查找学生

   
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        ComboBoxSemester = new javax.swing.JComboBox<>();
        btnBack = new javax.swing.JButton();
        lblSemester = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudentRegist = new javax.swing.JTable();
        ComboBoxStudentName = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        ComboBoxCourseNum = new javax.swing.JComboBox<>();
        btnDrop = new javax.swing.JButton();

        jLabel2.setText("Student Registration");

        ComboBoxSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxSemesterActionPerformed(evt);
            }
        });

        btnBack.setText("<<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblSemester.setText("Semester :");

        jLabel1.setText("Student Name :");

        jLabel3.setText("CourseNumber:");

        tblStudentRegist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Semester", "Student", "CourseNumber"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblStudentRegist);

        ComboBoxStudentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxStudentNameActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDrop.setText("Drop");
        btnDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSemester)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ComboBoxSemester, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ComboBoxStudentName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ComboBoxCourseNum, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(43, 43, 43)
                .addComponent(btnSave)
                .addGap(18, 18, 18)
                .addComponent(btnDrop)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(155, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(jLabel2))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSemester)
                    .addComponent(ComboBoxSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ComboBoxStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSave)
                        .addComponent(btnDrop))
                    .addComponent(ComboBoxCourseNum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
         // 1. 獲取選中值
        String semester = (String) ComboBoxSemester.getSelectedItem();
        String studentName = (String) ComboBoxStudentName.getSelectedItem();
        String courseNumber = (String) ComboBoxCourseNum.getSelectedItem();
        
        // 2. 驗證
        if (semester == null || studentName == null || courseNumber == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select all fields!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Department department = business.getDepartment();
            StudentDirectory studentDirectory = department.getStudentDirectory();
            
            // 3. 查找學生
            StudentProfile selectedStudent = null;
            for (StudentProfile sp : studentDirectory.getStudent()) {
                if (sp.getPerson().getName().equals(studentName)) {
                    selectedStudent = sp;
                    break;
                }
            }
            
            if (selectedStudent == null) {
                JOptionPane.showMessageDialog(this, 
                    "Student not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 4. 檢查是否已經註冊過這門課（從系統中檢查，不只是表格）
            CourseLoad courseLoad = selectedStudent.getCourseLoadBySemester(semester);
            if (courseLoad != null) {
                for (SeatAssignment sa : courseLoad.getSeatAssignments()) {
                    if (sa.getCourseOffer().getCourseNumber().equals(courseNumber)) {
                        JOptionPane.showMessageDialog(this, 
                            "This student is already registered for this course!\n\n" +
                            "Student: " + studentName + "\n" +
                            "Course: " + courseNumber + "\n" +
                            "Semester: " + semester, 
                            "Duplicate Registration", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }
            
            // 5. 查找課程
            CourseSchedule courseSchedule = department.getCourseSchedule(semester);
            if (courseSchedule == null) {
                JOptionPane.showMessageDialog(this, 
                    "Semester not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            CourseOffer courseOffer = courseSchedule.getCourseOfferByNumber(courseNumber);
            if (courseOffer == null) {
                JOptionPane.showMessageDialog(this, 
                    "Course not found in this semester!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 6. 檢查課程是否還有座位
            if (courseOffer.getEmptySeat() == null) {
                JOptionPane.showMessageDialog(this, 
                    "Course is full! No seats available.", 
                    "Registration Failed", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 7. 檢查學分限制（最多 8 學分）
            int currentCredits = 0;
            if (courseLoad != null) {
                currentCredits = courseLoad.getTotalCredits();
            }
            
            int newCredits = currentCredits + courseOffer.getCreditHours();
            if (newCredits > 8) {
                JOptionPane.showMessageDialog(this, 
                    "Cannot register. This would exceed the maximum credit limit (8).\n\n" +
                    "Current credits: " + currentCredits + "\n" +
                    "Course credits: " + courseOffer.getCreditHours() + "\n" +
                    "Total would be: " + newCredits, 
                    "Credit Limit Exceeded", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 8. 創建或獲取 CourseLoad
            if (courseLoad == null) {
                courseLoad = selectedStudent.newCourseLoad(semester);
            }
            
            // 9. 註冊課程
            SeatAssignment sa = courseOffer.assignEmptySeat(courseLoad);
            
            if (sa == null) {
                JOptionPane.showMessageDialog(this, 
                    "Registration failed. Course may be full.", 
                    "Registration Failed", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 10. 更新學費餘額
            double courseTuition = courseOffer.getTuitionFee();
            selectedStudent.setBalance(selectedStudent.getBalance() + courseTuition);
            
            // 11. 重新載入表格顯示
            loadStudentRegistrations();
            
            // 12. 成功消息
            JOptionPane.showMessageDialog(this, 
                "Student registered successfully!\n\n" +
                "Student: " + studentName + "\n" +
                "Course: " + courseNumber + "\n" +
                "Semester: " + semester + "\n" +
                "Tuition added: $" + String.format("%.2f", courseTuition) + "\n" +
                "New Balance: $" + String.format("%.2f", selectedStudent.getBalance()), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Registration failed: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } 
    }//GEN-LAST:event_btnSaveActionPerformed

    private void ComboBoxStudentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxStudentNameActionPerformed
        // TODO add your handling code here:
          loadStudentRegistrations();
    }//GEN-LAST:event_ComboBoxStudentNameActionPerformed

    private void btnDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropActionPerformed
        // TODO add your handling code here:
         // 1. 獲取選中的值
        String semester = (String) ComboBoxSemester.getSelectedItem();
        String studentName = (String) ComboBoxStudentName.getSelectedItem();
        String courseNumber = (String) ComboBoxCourseNum.getSelectedItem();
        
        // 2. 驗證輸入
        if (semester == null || studentName == null || courseNumber == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select semester, student, and course!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. 確認對話框
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to drop this course?\n\n" +
            "Student: " + studentName + "\n" +
            "Course: " + courseNumber + "\n" +
            "Semester: " + semester,
            "Confirm Drop",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            Department department = business.getDepartment();
            StudentDirectory studentDirectory = department.getStudentDirectory();
            
            // 4. 查找學生
            StudentProfile selectedStudent = null;
            for (StudentProfile sp : studentDirectory.getStudent()) {
                if (sp.getPerson().getName().equals(studentName)) {
                    selectedStudent = sp;
                    break;
                }
            }
            
            if (selectedStudent == null) {
                JOptionPane.showMessageDialog(this, 
                    "Student not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 5. 獲取課程負載
            CourseLoad courseLoad = selectedStudent.getCourseLoadBySemester(semester);
            
            if (courseLoad == null) {
                JOptionPane.showMessageDialog(this, 
                    "Student is not enrolled in any courses this semester!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 6. 查找要退的課程
            SeatAssignment toRemove = null;
            for (SeatAssignment sa : courseLoad.getSeatAssignments()) {
                if (sa.getCourseOffer().getCourseNumber().equals(courseNumber)) {
                    toRemove = sa;
                    break;
                }
            }
            
            if (toRemove == null) {
                JOptionPane.showMessageDialog(this, 
                    "Student is not enrolled in this course!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 7. 獲取學費金額（用於退款）
            double refundAmount = toRemove.getCourseOffer().getTuitionFee();
            
            // 8. 從課程負載中移除
            courseLoad.dropCourse(toRemove);
            
            // 9. 退還學費
            selectedStudent.refundForDroppedCourse(toRemove.getCourseOffer());
            
            // 10. 釋放座位
            toRemove.getSeat().releaseSeat();
            
            // 11. 重新載入表格
            loadStudentRegistrations();
            
            // 12. 顯示成功消息
            JOptionPane.showMessageDialog(this, 
                "Course dropped successfully!\n\n" +
                "Student: " + studentName + "\n" +
                "Course: " + courseNumber + "\n" +
                "Refund: $" + String.format("%.2f", refundAmount) + "\n" +
                "New Balance: $" + String.format("%.2f", selectedStudent.getBalance()), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Drop failed: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    
    
    }//GEN-LAST:event_btnDropActionPerformed

    private void ComboBoxSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxSemesterActionPerformed
        // TODO add your handling code here:
           loadStudentRegistrations();
           updateCourseListBySemester(); // 更新課程列表
    }//GEN-LAST:event_ComboBoxSemesterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxCourseNum;
    private javax.swing.JComboBox<String> ComboBoxSemester;
    private javax.swing.JComboBox<String> ComboBoxStudentName;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDrop;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSemester;
    private javax.swing.JTable tblStudentRegist;
    // End of variables declaration//GEN-END:variables

   
}
