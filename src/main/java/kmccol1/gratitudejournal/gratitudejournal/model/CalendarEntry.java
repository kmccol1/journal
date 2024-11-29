//***************************************************************************************
//
//     Filename: CalendarEntry.java
//     Author: Kyle McColgan
//     Date: 27 November 2024
//     Description: This file provides authentication
//                  and authorization response formatting.
//
//***************************************************************************************


package kmccol1.gratitudejournal.gratitudejournal.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "calendar_entries")
public class CalendarEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate entryDate;

    private Integer userId;  // Store user ID as a reference (not a foreign key)

    // Default constructor
    public CalendarEntry() {}

    // Constructor with all fields
    public CalendarEntry(String title, String content, LocalDate entryDate, Integer userId) {
        this.title = title;
        this.content = content;
        this.entryDate = entryDate;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
