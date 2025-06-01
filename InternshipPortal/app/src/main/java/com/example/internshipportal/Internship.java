package com.example.internshipportal;

public class Internship {
    private String _id;
    private String title;
    private String company;
    private String description;
    private String stipend;
    private String location;

    public Internship(String title, String company, String description, String stipend, String location) {
        this.title=title;
        this.company=company;
        this.description=description;
        this.stipend=stipend;
        this.location=location;
    }

    // Getters
    public String get_id() { return _id; }
    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getDescription() { return description; }
    public String getStipend() { return stipend; }
    public String getLocation() { return location; }
}
