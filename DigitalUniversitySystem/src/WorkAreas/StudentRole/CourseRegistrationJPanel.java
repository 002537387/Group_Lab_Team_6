/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package WorkAreas.StudentRole;
import Business.Business;
import Business.CourseSchedule.CourseLoad;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.CourseSchedule;
import Business.CourseSchedule.SeatAssignment;
import Business.Department.Department;
import Business.Profiles.FacultyProfile;
import Business.Profiles.StudentProfile;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author jim.hsieh
 */
public class CourseRegistrationJPanel extends javax.swing.JPanel {
Business business;
    StudentProfile student;
    JPanel CardSequencePanel;
    Department department;
    /**
     * Creates new form CourseRegistrationJPanel
     */
    public CourseRegistrationJPanel(Business bz, StudentProfile sp, JPanel panel) {
        initComponents();
         this.business = bz;
        this.student = sp;
        this.CardSequencePanel = panel;
        this.department = business.getDepartment();
        
        populateSemesters();
        refreshCourses();
    }
     private void populateSemesters() {
        // 添加可選學期
        cmbSemester.removeAllItems();
        cmbSemester.addItem("Fall2023");
        cmbSemester.addItem("Spring2024");
        cmbSemester.addItem("Fall2024");
        cmbSemester.addItem("Spring2025");
    }
    
    private void refreshCourses() {
        populateAvailableCourses();
        populateMyCourses();
        updateTotalCredits();
    }
    
    private void populateAvailableCourses() {
        DefaultTableModel model = (DefaultTableModel) tblAvailableCourses.getModel();
        model.setRowCount(0);
        
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        if (selectedSemester == null) return;
        
        // 獲取該學期的課程表
        CourseSchedule schedule = department.getCourseSchedule(selectedSemester);
        if (schedule == null) {
            model.addRow(new Object[]{
                "No courses available for " + selectedSemester, "", "", "", "", ""
            });
            return;
        }
        
        // 獲取學生已註冊的課程
        ArrayList<String> registeredCourseNumbers = new ArrayList<>();
        CourseLoad currentLoad = student.getCourseLoadBySemester(selectedSemester);
        if (currentLoad != null) {
            for (SeatAssignment sa : currentLoad.getSeatAssignments()) {
                registeredCourseNumbers.add(sa.getCourseOffer().getCourseNumber());
            }
        }
        
        // 顯示所有可選課程
        ArrayList<CourseOffer> courseOffers = schedule.getCourseOffers();
        for (CourseOffer offer : courseOffers) {
            String courseCode = offer.getCourseNumber();
            String courseName = offer.getSubjectCourse().getCourseName();
            int credits = offer.getCreditHours();
            
            // 獲取教授名稱
            String professor = "TBA";
            FacultyProfile faculty = offer.getFacultyProfile();
            if (faculty != null) {
                professor = faculty.getPerson().getName();
            }
            
            // 座位信息
            int seatsRemaining = offer.getSeatsRemaining();
            int totalSeats = offer.getTotalSeats();
            String seats = seatsRemaining + "/" + totalSeats;
            
            // 判斷按鈕文字
            String action;
            if (registeredCourseNumbers.contains(courseCode)) {
                action = "Registered";
            } else if (seatsRemaining <= 0) {
                action = "Full";
            } else {
                action = "Register";
            }
            
            model.addRow(new Object[]{
                courseCode,
                courseName,
                credits,
                professor,
                seats,
                action
            });
        }
    }
    
    private void populateMyCourses() {
        DefaultTableModel model = (DefaultTableModel) tblMyCourses.getModel();
        model.setRowCount(0);
        
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        if (selectedSemester == null) return;
        
        CourseLoad courseLoad = student.getCourseLoadBySemester(selectedSemester);
        if (courseLoad == null || courseLoad.getSeatAssignments().isEmpty()) {
            model.addRow(new Object[]{
                "No courses registered for " + selectedSemester, "", "", "", "", ""
            });
            return;
        }
        
        for (SeatAssignment sa : courseLoad.getSeatAssignments()) {
            CourseOffer offer = sa.getCourseOffer();
            String courseCode = offer.getCourseNumber();
            String courseName = offer.getSubjectCourse().getCourseName();
            int credits = sa.getCreditHours();
            
            String professor = "TBA";
            FacultyProfile faculty = offer.getFacultyProfile();
            if (faculty != null) {
                professor = faculty.getPerson().getName();
            }
            
            String status = "Enrolled";
            
            model.addRow(new Object[]{
                courseCode,
                courseName,
                credits,
                professor,
                status,
                "Drop"
            });
        }
    }
    
    private void updateTotalCredits() {
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        if (selectedSemester == null) {
            lblTotalCredits.setText("Total Credits: 0");
            return;
        }
        
        CourseLoad courseLoad = student.getCourseLoadBySemester(selectedSemester);
        int totalCredits = 0;
        if (courseLoad != null) {
            totalCredits = courseLoad.getTotalCredits();
        }
        
        lblTotalCredits.setText("Total Credits: " + totalCredits);
    }
    
    private void handleRegisterCourse(int row) {
        String selectedSemester = (String) cmbSemester.getSelectedItem();
    String courseCode = (String) tblAvailableCourses.getValueAt(row, 0);
    String action = (String) tblAvailableCourses.getValueAt(row, 5);
    
    // 檢查是否已註冊或已滿
    if (action.equals("Registered")) {
        JOptionPane.showMessageDialog(this,
            "You are already registered for this course.",
            "Already Registered",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    if (action.equals("Full")) {
        JOptionPane.showMessageDialog(this,
            "This course is full. No seats available.",
            "Course Full",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // 獲取課程
    CourseSchedule schedule = department.getCourseSchedule(selectedSemester);
    CourseOffer offer = schedule.getCourseOffer(courseCode);
    
    if (offer == null) {
        JOptionPane.showMessageDialog(this,
            "Course not found.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // 檢查學分限制（最多 8 學分）
    CourseLoad currentLoad = student.getCourseLoadBySemester(selectedSemester);
    int currentCredits = 0;
    if (currentLoad != null) {
        currentCredits = currentLoad.getTotalCredits();
    }
    
    int newCredits = currentCredits + offer.getCreditHours();
    if (newCredits > 8) {
        JOptionPane.showMessageDialog(this,
            "Cannot register. This would exceed the maximum credit limit (8).\n" +
            "Current credits: " + currentCredits + "\n" +
            "Course credits: " + offer.getCreditHours(),
            "Credit Limit Exceeded",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // 註冊課程
    if (currentLoad == null) {
        currentLoad = student.newCourseLoad(selectedSemester);
    }
    
    SeatAssignment sa = offer.assignEmptySeat(currentLoad);
    
    if (sa != null) {
        // ← 添加學費到餘額
        double tuitionFee = offer.getTuitionFee();
        student.setBalance(student.getBalance() + tuitionFee);
        
        JOptionPane.showMessageDialog(this,
            "Successfully registered for " + courseCode + "!\n" +
            "Tuition fee $" + String.format("%.2f", tuitionFee) + " has been added to your balance.",
            "Registration Successful",
            JOptionPane.INFORMATION_MESSAGE);
        refreshCourses();
    } else {
        JOptionPane.showMessageDialog(this,
            "Registration failed. Course may be full.",
            "Registration Failed",
            JOptionPane.ERROR_MESSAGE);
    }
    }
    
    private void handleDropCourse(int row) {
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        String courseCode = (String) tblMyCourses.getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to drop " + courseCode + "?",
            "Confirm Drop",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        CourseLoad courseLoad = student.getCourseLoadBySemester(selectedSemester);
        if (courseLoad == null) return;
        
        // 找到對應的 SeatAssignment
        SeatAssignment toRemove = null;
        for (SeatAssignment sa : courseLoad.getSeatAssignments()) {
            if (sa.getCourseOffer().getCourseNumber().equals(courseCode)) {
                toRemove = sa;
                break;
            }
        }
        if (toRemove != null) {
            
            CourseOffer droppedCourse = toRemove.getCourseOffer();
            double tuitionFee = droppedCourse.getTuitionFee();
            courseLoad.dropCourse(toRemove);
            student.refundForDroppedCourse(droppedCourse); 
            
            JOptionPane.showMessageDialog(this,"Successfully dropped " + courseCode + ".\n" +"Tuition fee $" + tuitionFee + " has been refunded.","Drop Successful",JOptionPane.INFORMATION_MESSAGE);
            refreshCourses();
        }
    }
      private void searchCourses() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter search text.",
                "Search Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) tblAvailableCourses.getModel();
        model.setRowCount(0);
        
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        if (selectedSemester == null) return;
        
        CourseSchedule schedule = department.getCourseSchedule(selectedSemester);
        if (schedule == null) return;
        
        // 獲取學生已註冊的課程
        ArrayList<String> registeredCourseNumbers = new ArrayList<>();
        CourseLoad currentLoad = student.getCourseLoadBySemester(selectedSemester);
        if (currentLoad != null) {
            for (SeatAssignment sa : currentLoad.getSeatAssignments()) {
                registeredCourseNumbers.add(sa.getCourseOffer().getCourseNumber());
            }
        }
        
        // 根據選擇的搜索類型進行搜索
        ArrayList<CourseOffer> courseOffers = schedule.getCourseOffers();
        int resultCount = 0;
        
        for (CourseOffer offer : courseOffers) {
            boolean matchFound = false;
            
            // 判斷搜索類型
            if (rbCourseId.isSelected()) {
                // 按 Course ID 搜索
                String courseCode = offer.getCourseNumber().toLowerCase();
                if (courseCode.contains(searchText)) {
                    matchFound = true;
                }
            } else if (rbTeacher.isSelected()) {
                // 按 Teacher 搜索
                String professor = "TBA";
                FacultyProfile faculty = offer.getFacultyProfile();
                if (faculty != null) {
                    professor = faculty.getPerson().getName().toLowerCase();
                }
                if (professor.contains(searchText)) {
                    matchFound = true;
                }
            } else if (rbCourseName.isSelected()) {
                // 按 Course Name 搜索
                String courseName = offer.getSubjectCourse().getCourseName().toLowerCase();
                if (courseName.contains(searchText)) {
                    matchFound = true;
                }
            }
            
            // 如果找到匹配，添加到表格
            if (matchFound) {
                resultCount++;
                String courseCode = offer.getCourseNumber();
                String courseName = offer.getSubjectCourse().getCourseName();
                int credits = offer.getCreditHours();
                
                String professor = "TBA";
                FacultyProfile faculty = offer.getFacultyProfile();
                if (faculty != null) {
                    professor = faculty.getPerson().getName();
                }
                
                int seatsRemaining = offer.getSeatsRemaining();
                int totalSeats = offer.getTotalSeats();
                String seats = seatsRemaining + "/" + totalSeats;
                
                String action;
                if (registeredCourseNumbers.contains(courseCode)) {
                    action = "Registered";
                } else if (seatsRemaining <= 0) {
                    action = "Full";
                } else {
                    action = "Register";
                }
                
                model.addRow(new Object[]{
                    courseCode,
                    courseName,
                    credits,
                    professor,
                    seats,
                    action
                });
            }
        }
        
        // 顯示搜索結果數量
        if (resultCount == 0) {
            model.addRow(new Object[]{
                "No courses found matching: " + searchText,
                "", "", "", "", ""
            });
        }
    }
    
    private void clearSearch() {
        txtSearch.setText("");
        rbCourseId.setSelected(true);
        refreshCourses();
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchButtonGroup = new javax.swing.ButtonGroup();
        btnBack = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblSelectSemester = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAvailableCourses = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMyCourses = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        lblTotalCredits = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        rbCourseId = new javax.swing.JRadioButton();
        rbTeacher = new javax.swing.JRadioButton();
        rbCourseName = new javax.swing.JRadioButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnClearSearch = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 17, -1, -1));

        lblTitle.setText("Course Registration");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(457, 20, -1, -1));

        lblSelectSemester.setText("Select Semester:");
        add(lblSelectSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 66, 110, -1));

        cmbSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterActionPerformed(evt);
            }
        });
        add(cmbSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 63, 130, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setText("Available Courses");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(712, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jSplitPane1.setTopComponent(jPanel1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tblAvailableCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course Code", "Course Name", "Credits", "Professor", "Seats", "Register"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAvailableCourses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAvailableCoursesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAvailableCourses);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(322, 322, 322))
        );

        jSplitPane1.setRightComponent(jPanel3);

        jPanel2.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 930, 220));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setText("My Registered Courses");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addContainerGap(713, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(32, 32, 32))
        );

        jSplitPane2.setTopComponent(jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        tblMyCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course Code", "Course Name", "Credits", "Professor", "Status", "Drop"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMyCourses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMyCoursesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblMyCourses);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel6);

        jPanel4.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 930, 210));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 110, -1));

        lblTotalCredits.setText("Total Credits:");
        add(lblTotalCredits, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 650, 220, -1));

        jLabel1.setText("Search by:");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 110, 110, -1));

        searchButtonGroup.add(rbCourseId);
        rbCourseId.setSelected(true);
        rbCourseId.setText("Course ID");
        add(rbCourseId, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, -1, -1));

        searchButtonGroup.add(rbTeacher);
        rbTeacher.setText("Teacher");
        rbTeacher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTeacherActionPerformed(evt);
            }
        });
        add(rbTeacher, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, -1, -1));

        searchButtonGroup.add(rbCourseName);
        rbCourseName.setText("Course Name");
        add(rbCourseName, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, -1, -1));
        add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 300, -1));

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, 100, -1));

        btnClearSearch.setText("Clear");
        btnClearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSearchActionPerformed(evt);
            }
        });
        add(btnClearSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, 110, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
    ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        refreshCourses();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        // TODO add your handling code here:
         refreshCourses();
    }//GEN-LAST:event_cmbSemesterActionPerformed

    private void tblAvailableCoursesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAvailableCoursesMouseClicked
        // TODO add your handling code here:
        int row = tblAvailableCourses.rowAtPoint(evt.getPoint());
    int col = tblAvailableCourses.columnAtPoint(evt.getPoint());
    
    // 檢查是否點擊了 Action 列（第5列）
    if (col == 5 && row >= 0) {
        handleRegisterCourse(row);
    }
    }//GEN-LAST:event_tblAvailableCoursesMouseClicked

    private void tblMyCoursesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMyCoursesMouseClicked
        // TODO add your handling code here:
        int row = tblMyCourses.rowAtPoint(evt.getPoint());
    int col = tblMyCourses.columnAtPoint(evt.getPoint());
    
    // 檢查是否點擊了 Action 列（第5列）
    if (col == 5 && row >= 0) {
        handleDropCourse(row);
    }//GEN-LAST:event_tblMyCoursesMouseClicked
    }
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        searchCourses();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
        // TODO add your handling code here:
        clearSearch();
    }//GEN-LAST:event_btnClearSearchActionPerformed

    private void rbTeacherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTeacherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbTeacherActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClearSearch;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JLabel lblSelectSemester;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalCredits;
    private javax.swing.JRadioButton rbCourseId;
    private javax.swing.JRadioButton rbCourseName;
    private javax.swing.JRadioButton rbTeacher;
    private javax.swing.ButtonGroup searchButtonGroup;
    private javax.swing.JTable tblAvailableCourses;
    private javax.swing.JTable tblMyCourses;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
