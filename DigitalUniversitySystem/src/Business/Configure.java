/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
package Business;

import Business.Person.Person;
import Business.Directory.PersonDirectory;
import Business.Directory.EmployeeDirectory;
import Business.Profiles.EmployeeProfile;
import Business.Directory.StudentDirectory;
import Business.Profiles.StudentProfile;
import Business.Directory.FacultyDirectory;
import Business.Directory.RegistrarDirectory;
import Business.Profiles.FacultyProfile;
import Business.Directory.UserAccountDirectory;
import Business.Profiles.RegistrarProfile;


/**
 *
 * @author Ing-Ruei
 */
class Configure {

    static Business initialize() {
        Business business = new Business("Information Systems");

        // Create Persons
        PersonDirectory persondirectory = business.getPersonDirectory();
        // person representing sales organization        
        Person person001 = persondirectory.newPerson("1");
        Person person002 = persondirectory.newPerson("2");
        Person person003 = persondirectory.newPerson("3"); 
        Person person005 = persondirectory.newPerson("4");
        
        // Create Admins to manage the business
        EmployeeDirectory employeedirectory = business.getEmployeeDirectory();
        EmployeeProfile employeeprofile0 = employeedirectory.newEmployeeProfile(person001);
        
        StudentDirectory studentdirectory = business.getStudentDirectory();
        StudentProfile studentprofile0 = studentdirectory.newStudentProfile(person002);
        
        FacultyDirectory facultyDirectory = business.getFacultyDirectory();
        FacultyProfile facultyprofile0 = facultyDirectory.newFacultyProfile(person003);
        
        RegistrarDirectory registrarDirectory = business.getRegistrarDirectory();
        RegistrarProfile registrarprofile0 = registrarDirectory.newRegistrarProfile(person005);
        
   
        // Create User accounts that link to specific profiles
        UserAccountDirectory uadirectory = business.getUserAccountDirectory();
        uadirectory.newUserAccount(employeeprofile0, "admin", "****"); 
        uadirectory.newUserAccount(studentprofile0, "student", "****"); 
        uadirectory.newUserAccount(facultyprofile0, "faculty", "****");
        uadirectory.newUserAccount(registrarprofile0, "registrar", "****");

        return business;

    }

}
