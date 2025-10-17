/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Directory;

import Business.Person.Person;
import Business.Profiles.RegistrarProfile;

import java.util.ArrayList;

/**
 *
 * @author Ing-Ruei
 */
public class RegistrarDirectory {


    ArrayList<RegistrarProfile> registrarlist;

    public RegistrarDirectory() {

     registrarlist = new ArrayList();

    }

    public RegistrarProfile newRegistrarProfile(Person p) {

        RegistrarProfile rp = new RegistrarProfile(p);
        registrarlist.add(rp);
        return rp;
    }

    public RegistrarProfile findRegistrar(String id) {

        for (RegistrarProfile rp : registrarlist) {

            if (rp.isMatch(id)) {
                return rp;
            }
        }
            return null; //not found after going through the whole list
         }
    
}
