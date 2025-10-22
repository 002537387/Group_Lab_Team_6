/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Business.Department.Department;
import Business.Directory.PersonDirectory;
import Business.Directory.EmployeeDirectory;
import Business.Directory.FacultyDirectory;
import Business.Directory.RegistrarDirectory;
import Business.Directory.StudentDirectory;
import Business.Directory.UserAccountDirectory;

/**
 *
 * @author Ing-Ruei
 */
public class Business {

    String name;
    PersonDirectory persondirectory; 
    EmployeeDirectory employeedirectory;
    UserAccountDirectory useraccountdirectory;
    StudentDirectory studentdirectory;
    FacultyDirectory facultyDirectory;
    RegistrarDirectory registrarDirectory;
    private Department department; 


    public Business(String n) {
        name = n;

        persondirectory = new PersonDirectory();
        employeedirectory = new EmployeeDirectory(this);
        useraccountdirectory = new UserAccountDirectory();
        studentdirectory = new StudentDirectory();
        facultyDirectory = new FacultyDirectory();
        registrarDirectory = new RegistrarDirectory();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }


    
    public PersonDirectory getPersonDirectory() {
        return persondirectory;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return useraccountdirectory;
    }

    public EmployeeDirectory getEmployeeDirectory() {
        return employeedirectory;
    }

    public StudentDirectory getStudentDirectory(){
        return studentdirectory;
    }

    public FacultyDirectory getFacultyDirectory(){
        return facultyDirectory;
    }
    
    public RegistrarDirectory getRegistrarDirectory(){
        return registrarDirectory;
    }
}
