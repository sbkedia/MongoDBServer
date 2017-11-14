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
public class Track {

        private String user_ID, date, Time, Movement;

        Track() {
            this.user_ID = user_ID;
            this.date = date;
            this.Time = Time;
            this.Movement = Movement;
        }

        public void setuserID(String user_ID) {
            this.user_ID = user_ID;
        }

        public String getuserID() {
            return this.user_ID;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return this.date;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getTime() {
            return this.Time;
        }

        public void setMovement(String movement) {
            this.Movement = movement;
        }

        public String getMovement() {
            return this.Movement;
        }

    }

