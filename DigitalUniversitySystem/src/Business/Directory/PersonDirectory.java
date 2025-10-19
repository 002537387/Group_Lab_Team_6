/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Directory;

import Business.Person.Person;
import java.util.ArrayList;

/**
 *
 * @author Ing-Ruei
 */
public class PersonDirectory {
    
    ArrayList<Person> personlist ;

    public PersonDirectory (){
          
        personlist = new ArrayList();

    }

    public ArrayList<Person> getPersons(){
        return personlist;
    }
      
    public Person newPerson(String id) {

        Person p = new Person(id);
        personlist.add(p);
        return p;
    }

    public Person findPerson(String id) {

        for (Person p : personlist) {

            if (p.isMatch(id)) {
                return p;
            }
        }
            return null; //not found after going through the whole list
         }
    
}
