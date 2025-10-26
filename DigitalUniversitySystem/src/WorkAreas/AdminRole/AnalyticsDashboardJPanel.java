/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WorkAreas.AdminRole;

import Business.Business;
import Business.CourseSchedule.CourseOffer;
import Business.CourseSchedule.CourseSchedule;
import Business.UserAccounts.UserAccount;
import Business.Profiles.StudentProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author YourName
 */
public class AnalyticsDashboardJPanel extends javax.swing.JPanel {

    Business business;
    JPanel CardSequencePanel;
    
    /**
     * Creates new form AnalyticsDashboardJPanel
     */
    public AnalyticsDashboardJPanel(Business bz, JPanel jp) {
        initComponents();
        this.business = bz;
        this.CardSequencePanel = jp;
        
        loadAnalytics();
    }
    
    private void loadAnalytics() {
        calculateAndDisplayAnalytics();
    }
    
    private void calculateAndDisplayAnalytics(){
        // User analytics
        Map<String, Integer> roleCounts = new HashMap<>();
        roleCounts.put("Admin", 0);
        roleCounts.put("Faculty", 0);
        roleCounts.put("Student", 0);
        roleCounts.put("Registrar", 0);
        ArrayList<UserAccount> allUsers = business.getDepartment().getUserAccountDirectory().getUserAccountList();
        int totalUsers = 0;
        if (allUsers != null) {
            for (UserAccount ua : allUsers) {
                String role = ua.getRole();
                if (role != null) {
                    roleCounts.put(role, roleCounts.getOrDefault(role, 0) + 1);
                }
            }
            totalUsers = allUsers.size();
        }
        populateUsersByRoleTable(roleCounts, totalUsers);
        lblTotalUsersValue.setText(String.valueOf(totalUsers));

        // Course analytics
        Collection<CourseSchedule> schedules = business.getDepartment().getAllCourseSchedule();
        int totalCourses = 0;
        if (schedules != null) {
            for (CourseSchedule schedule : schedules) {
                ArrayList<CourseOffer> offers = schedule.getCourseOffers();
                totalCourses += (offers != null ? offers.size() : 0);
            }
        }
        populateCoursesPerSemesterTable(schedules, totalCourses);
        populateEnrollmentPerCourseTable(schedules);
        lblTotalCoursesValue.setText(String.valueOf(totalCourses));

        // Student and revenue analytics
        ArrayList<UserAccount> students = business.getDepartment().getUserAccountDirectory().findStudentAccount();
        int totalStudents = students.size();
        double totalRevenue = 0;
        double totalOutstanding = 0;
        for (UserAccount ua : students) {
            StudentProfile student = (StudentProfile) ua.getAssociatedPersonProfile();
            double creditsEnrolled = student.getCourseList().size() * 4; // 4 credits per course
            double tuitionOwed = creditsEnrolled * 1000;
            double balance = student.getBalance();
            double amountPaid = tuitionOwed + balance;
            totalRevenue += amountPaid;
            if (balance < 0) {
                totalOutstanding += -balance;
            }
        }
        populateTuitionRevenueTable(students);
        lblTotalStudentsValue.setText(String.valueOf(totalStudents));
        lblTotalRevenueValue.setText(String.format("$%.2f", totalRevenue));
    }
    
    // 1. Total Active Users by Role - FIXED VERSION
    private void populateUsersByRoleTable(Map<String, Integer> roleCounts, int totalUsers) {
        DefaultTableModel model = (DefaultTableModel) tblUsersByRole.getModel();
        model.setRowCount(0);

        String[] roles = {"Admin", "Faculty", "Student", "Registrar"};

        for (String role : roles) {
            Object[] row = new Object[2];
            row[0] = role;
            row[1] = roleCounts.get(role);
            model.addRow(row);
        }

        // Add total row
        Object[] totalRow = new Object[2];
        totalRow[0] = "TOTAL";
        totalRow[1] = totalUsers;
        model.addRow(totalRow);
    }

    // 2. Total Courses Offered Per Semester - FIXED VERSION
    private void populateCoursesPerSemesterTable(Collection<CourseSchedule> schedules, int totalCourses) {
        DefaultTableModel model = (DefaultTableModel) tblCoursesPerSemester.getModel();
        model.setRowCount(0);

        if (schedules != null && !schedules.isEmpty()) {
            for (CourseSchedule schedule : schedules) {
                ArrayList<CourseOffer> offers = schedule.getCourseOffers();
                int courseCount = (offers != null) ? offers.size() : 0;

                Object[] row = new Object[2];
                row[0] = schedule.getSemester();
                row[1] = courseCount;
                model.addRow(row);
            }

            // Add total row
            Object[] totalRow = new Object[2];
            totalRow[0] = "TOTAL";
            totalRow[1] = totalCourses;
            model.addRow(totalRow);
        } else {
            // No schedules found - show empty message
            Object[] row = new Object[2];
            row[0] = "No schedules available";
            row[1] = 0;
            model.addRow(row);
        }
    }
    
    // 3. Total Enrolled Students Per Course
    private void populateEnrollmentPerCourseTable(Collection<CourseSchedule> schedules) {
        DefaultTableModel model = (DefaultTableModel) tblEnrollmentPerCourse.getModel();
        model.setRowCount(0);
        
        if (schedules != null) {
            for (CourseSchedule schedule : schedules) {
                ArrayList<CourseOffer> offers = schedule.getCourseOffers();
                
                if (offers != null) {
                    for (CourseOffer offer : offers) {
                        Object[] row = new Object[4];
                        row[0] = schedule.getSemester();
                        row[1] = offer.getCourseNumber();
                        row[2] = offer.getSubjectCourse().getCourseName();
                        row[3] = offer.getEnrolledSeatAssignments().size();
                        model.addRow(row);
                    }
                }
            }
        }
    }
    
    // 4. Tuition Revenue Summary
    private void populateTuitionRevenueTable(ArrayList<UserAccount> students) {
        DefaultTableModel model = (DefaultTableModel) tblTuitionRevenue.getModel();
        model.setRowCount(0);
        
        double totalRevenue = 0;
        int totalStudents = 0;
        double totalOutstanding = 0;
        
        for (UserAccount ua : students) {
            StudentProfile student = (StudentProfile) ua.getAssociatedPersonProfile();
            
            // Calculate tuition
            double creditsEnrolled = student.getCourseList().size() * 4; // 4 credits per course
            double tuitionOwed = creditsEnrolled * 1500;
            
            // Get balance (negative means they owe money)
            double balance = student.getBalance();
            double amountPaid = tuitionOwed + balance; // If balance is -1000, they paid (tuitionOwed - 1000)
            double outstanding = -balance; // Positive outstanding
            
            if (balance < 0) {
                outstanding = -balance;
            } else {
                outstanding = 0;
            }
            
            Object[] row = new Object[5];
            row[0] = student.getPerson().getName();
            row[1] = student.getPerson().getPersonId();
            row[2] = String.format("$%.2f", tuitionOwed);
            row[3] = String.format("$%.2f", amountPaid);
            row[4] = String.format("$%.2f", outstanding);
            model.addRow(row);
            
            totalRevenue += amountPaid;
            totalOutstanding += outstanding;
            totalStudents++;
        }
        
        // Add total row
        Object[] totalRow = new Object[5];
        totalRow[0] = "TOTAL (" + totalStudents + " students)";
        totalRow[1] = "";
        totalRow[2] = "";
        totalRow[3] = String.format("$%.2f", totalRevenue);
        totalRow[4] = String.format("$%.2f", totalOutstanding);
        model.addRow(totalRow);
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
        jPanel1 = new javax.swing.JPanel();
        lblTotalUsers = new javax.swing.JLabel();
        lblTotalUsersValue = new javax.swing.JLabel();
        lblTotalCourses = new javax.swing.JLabel();
        lblTotalCoursesValue = new javax.swing.JLabel();
        lblTotalStudents = new javax.swing.JLabel();
        lblTotalStudentsValue = new javax.swing.JLabel();
        lblTotalRevenue = new javax.swing.JLabel();
        lblTotalRevenueValue = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsersByRole = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCoursesPerSemester = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblEnrollmentPerCourse = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblTuitionRevenue = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("University Analytics Dashboard");

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(240, 240, 240));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Summary Statistics"));

        lblTotalUsers.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTotalUsers.setText("Total Users:");

        lblTotalUsersValue.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTotalUsersValue.setForeground(new java.awt.Color(0, 102, 204));
        lblTotalUsersValue.setText("0");

        lblTotalCourses.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTotalCourses.setText("Total Courses:");

        lblTotalCoursesValue.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTotalCoursesValue.setForeground(new java.awt.Color(0, 153, 51));
        lblTotalCoursesValue.setText("0");

        lblTotalStudents.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTotalStudents.setText("Total Students:");

        lblTotalStudentsValue.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTotalStudentsValue.setForeground(new java.awt.Color(255, 102, 0));
        lblTotalStudentsValue.setText("0");

        lblTotalRevenue.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTotalRevenue.setText("Total Revenue:");

        lblTotalRevenueValue.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTotalRevenueValue.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalRevenueValue.setText("$0.00");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalUsers)
                    .addComponent(lblTotalUsersValue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalCourses)
                    .addComponent(lblTotalCoursesValue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalStudents)
                    .addComponent(lblTotalStudentsValue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalRevenue)
                    .addComponent(lblTotalRevenueValue, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTotalRevenue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalRevenueValue))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTotalStudents)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalStudentsValue))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTotalCourses)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalCoursesValue))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTotalUsers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalUsersValue)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Users by Role");

        tblUsersByRole.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Role", "Count"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblUsersByRole);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Courses Per Semester");

        tblCoursesPerSemester.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Semester", "Total Courses"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblCoursesPerSemester);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Enrollment Per Course");

        tblEnrollmentPerCourse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Semester", "Course Code", "Course Name", "Enrolled"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblEnrollmentPerCourse);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Tuition Revenue Summary");

        tblTuitionRevenue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student Name", "Student ID", "Tuition Owed", "Amount Paid", "Outstanding"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblTuitionRevenue);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnRefresh.setText("Refresh Data");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefresh))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnRefresh))
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).previous(CardSequencePanel);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        loadAnalytics();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblTotalCourses;
    private javax.swing.JLabel lblTotalCoursesValue;
    private javax.swing.JLabel lblTotalRevenue;
    private javax.swing.JLabel lblTotalRevenueValue;
    private javax.swing.JLabel lblTotalStudents;
    private javax.swing.JLabel lblTotalStudentsValue;
    private javax.swing.JLabel lblTotalUsers;
    private javax.swing.JLabel lblTotalUsersValue;
    private javax.swing.JTable tblCoursesPerSemester;
    private javax.swing.JTable tblEnrollmentPerCourse;
    private javax.swing.JTable tblTuitionRevenue;
    private javax.swing.JTable tblUsersByRole;
    // End of variables declaration//GEN-END:variables
}
