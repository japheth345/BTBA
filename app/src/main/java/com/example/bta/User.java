package com.example.bta;
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String seat;
    private String vehicle;
    private String depa;
    private String route;
    private String fare;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        firstName = "";
        lastName = "";
        email = "";
        password = "";
        role="";
        seat="";
        vehicle="";
        depa="";

    }

    public User(String firstName, String lastName, String email, String password,String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role=role;

    }
    public User(String firstName, String lastName, String email, String seat,String vehicle,String depa,String route) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.seat = seat;
        this.role=role;
        this.role=role;
        this.vehicle=vehicle;
        this.depa=depa;
        this.route=route;

    }
    public User(String firstName, String lastName, String email, String seat,String vehicle,String depa,String route,String fare) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.seat = seat;
        this.role=role;
        this.role=role;
        this.vehicle=vehicle;
        this.depa=depa;
        this.route=route;
        this.fare=fare;

    }
    public void setFirstName(String firstName) {
       this.firstName=firstName;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getSeat() {
        return seat;
    }
    public String getVehicle() {
        return vehicle;
    }
    public void setDeparture(String depa) {
        this.depa=depa;
    }
    public String getDeparture() {
        return depa;
    }
    public String getRole() {
        return role;
    }
    public String getRoute() {
        return route;
    }
    public void setFare(String fare2)
    {
        fare=fare2;
    }
    public String getFare(){
        return fare;
    }
}
