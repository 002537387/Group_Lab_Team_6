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

    public RegistrarProfile(Person p) {
        super(p);

    }

    @Override
    public String getRole() {
        return "Registrar";
    }

    public boolean isMatch(String id) {
        return person.getPersonId().equals(id);
    }

}
