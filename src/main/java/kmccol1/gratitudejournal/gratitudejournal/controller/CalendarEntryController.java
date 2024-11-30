//***************************************************************************************
//
//     Filename: CalendarEntryController.java
//     Author: Kyle McColgan
//     Date: 29 November 2024
//     Description: This controller class provides CalendarEntry class functionality.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.controller;

import kmccol1.gratitudejournal.gratitudejournal.model.CalendarEntry;
import kmccol1.gratitudejournal.gratitudejournal.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//***************************************************************************************

@RestController
@RequestMapping("/api/calendar") // Base URL for calendar entry endpoints
public class CalendarEntryController
{

    private final CalendarService calendarService;

    @Autowired
    public CalendarEntryController(CalendarService calendarService)
    {
        this.calendarService = calendarService;
    }

    // GET all calendar entries for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CalendarEntry>> getEntriesByUserId(@PathVariable Integer userId)
    {
        List<CalendarEntry> entries = calendarService.getEntriesByUserId(userId);
        return ResponseEntity.ok(entries);
    }

    // GET a single calendar entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<CalendarEntry> getEntryById(@PathVariable Integer id)
    {
        Optional<CalendarEntry> entry = calendarService.getEntryById(id);
        return entry.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST (create) a new calendar entry
    @PostMapping
    public ResponseEntity<CalendarEntry> createEntry(@RequestBody CalendarEntry entry)
    {
        CalendarEntry createdEntry = calendarService.createEntry(entry);
        return ResponseEntity.status(201).body(createdEntry);
    }

    // DELETE a calendar entry by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Integer id)
    {
        calendarService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
