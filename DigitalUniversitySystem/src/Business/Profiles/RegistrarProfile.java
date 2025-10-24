/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;

/**
 *
 * @author Ing-Ruei
 */
public class RegistrarProfile extends Profile {

    Person person;
    private String rname;
    private String email;
    private String phone;
    private String office;
    private String officeHours;

    public RegistrarProfile(Person p) {
        super(p);
        this.person = p;

    }

    @Override
    public String getRole() {
        return "Registrar";
    }

    public boolean isMatch(String id) {
        return person.getPersonId().equals(id);
    }

    public String getRName() {
        return rname;
    }

    public void setRName(String rname) {
        this.rname = rname;
    }
 
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }
}
