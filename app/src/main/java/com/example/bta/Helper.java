package com.example.bta;

import java.util.ArrayList;

public class Helper
{
public static Vehicle veh;
public static User bookings;
public static User user;
public static String uid;
    public static int n;

    public static String[] getBuses(String choose){

        String[] options;
        options = new String[9];
        options[0] = "Select Vehicle";
        options[1] = "Shuttle Class A";
        options[2] = "Shuttle Class B";
        options[3] = "Shuttle Class C";
        options[4] = "Shuttle Class D";
        options[5] = "Shuttle Class E";
        options[6] = "Shuttle Class F";
        options[7] = "Shuttle Class G";
        options[8] = "Shuttle Class H";
        return options;

    }
    public static String[] getRoutes(String choose){

        String[] options;
        options = new String[9];
        options[0] = "Select Route";
        options[1] = "Kisii-Nairobi";
        options[2] = "Kisii-Mombasa";
        options[3] = "Kisii-Eldoret";
        options[4] = "Nairobi-Kisii";
        options[5] = "Mombasa-Kisii";
        options[6] = "Eldoret-Kisii";
        options[7] = "Nairobi-Mombasa";
        options[8] = "Mombasa-Nairobi";
        return options;

    }
    public static String[] getTime(String choose){

        String[] options;
        options = new String[5];
        options[0] = "Select DepartureTime";
        options[1] = "7:00am";
        options[2] = "10:00 am";
        options[3] = "7:00 pm";
        options[4] = "10:00 pm";

        return options;

    }
    public static void setUser(User user2)
    {
        user=user2;
    }
    public static User getUser()
    {
        return user;
    }
    public static void setVehicle(Vehicle veh2)
    {
        veh=veh2;
    }
    public static Vehicle getVehicle()
    {
        return veh;
    }
    public void setBooking(User bookings)
    {
        this.bookings=bookings;
    }
    public User getBooking()
    {
        return bookings;
    }
    public static void setUID(String uid2)
    {
        uid=uid2;
    }
    public static String getUID()
    {
        return uid;
    }
    public static void setN(int n2)
    {
        n2=n2;
    }
    public static int getN()
    {
        return n;
    }
}
