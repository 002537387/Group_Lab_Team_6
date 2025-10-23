/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

import Business.Profiles.Profile;



/**
 *
 * @author Ing-Ruei
 */
public class UserAccount {
    
    Profile profile;
    String username;
    String password;
    
    public UserAccount (Profile profile, String un, String pw){
         username = un;
         password = pw;
         this.profile = profile;
    }

    public String getPersonId(){
        return profile.getPerson().getPersonId();
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUserLoginName(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAssociatedPersonProfile(Profile profile) {
        this.profile = profile;
    }
    
    public boolean isMatch(String id){
        if(getPersonId().equals(id)) return true;
        return false;
    }
    
    public boolean IsValidUser(String un, String pw){       
        return username.equalsIgnoreCase(un) && password.equals(pw);      
    }
    
    
    
    public String getRole(){
        return profile.getRole();
    }
        
    public Profile getAssociatedPersonProfile(){
        return profile;
    }
        
    @Override
        public String toString(){
            
            return getPersonId();
        }
        
}

