package com.example.bta;
public class Vehicle {
    String id,name,route,departure,fare,seats,rseats;

    public Vehicle(String id,String name,String route,String departure,String fare,String seats,String rseats)
    {
        this.id=id;
        this.name=name;
        this.route=route;
        this.departure=departure;
        this.fare=fare;
        this.seats=seats;
        this.rseats=rseats;
    }
    public String getID() {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getRoute()
    {
        return route;
    }
    public String getDeparture()
    {
        return departure;
    }
    public String getFare()
    {
        return fare;
    }
    public String getSeats()
    {
        return seats;
    }
    public String getRseats()
    {
        return rseats;
    }
    public void setRseats(String s)
    {
        this.rseats=s;
    }
}
