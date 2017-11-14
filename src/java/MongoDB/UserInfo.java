/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MongoDB;

/**
 *
 * @author Shruti
 */
public class UserInfo {
    
    private String user_ID, First_Name, Last_Name, Email, Password, Role;

        UserInfo() {
            this.user_ID = "";
            this.First_Name = "";
            this.Last_Name = "";
            this.Email = "";
            this.Password = "";
            this.Role = "";
        }

        public void setUserID(String user_ID) {
            this.user_ID = user_ID;
        }

        public String getUserID() {
            return this.user_ID;
        }

        public void setFName(String First_Name) {
            this.First_Name = First_Name;
        }

        public String getFName() {
            return this.First_Name;
        }

        public void setLName(String Last_Name) {
            this.Last_Name = Last_Name;
        }

        public String getLName() {
            return this.Last_Name;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getEmail() {
            return this.Email;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getPassword() {
            return this.Password;
        }

        public void setRole(String Role) {
            this.Role = Role;
        }

        public String getRole() {
            return this.Role;
        }

    
}
