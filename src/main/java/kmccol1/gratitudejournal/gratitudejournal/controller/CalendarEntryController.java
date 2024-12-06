//***************************************************************************************
//
//     Filename: CalendarEntryController.java
//     Author: Kyle McColgan
//     Date: 05 December 2024
//     Description: This controller class provides CalendarEntry class functionality.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.controller;

import kmccol1.gratitudejournal.gratitudejournal.model.CalendarEntry;
import kmccol1.gratitudejournal.gratitudejournal.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        if (entries == null || entries.isEmpty())
        {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(entries); // 200 OK with the list of entries
    }

    // Get a calendar entry by ID
    @GetMapping("/{userId}/{entryId}")
    public ResponseEntity<?> getEntryById(@PathVariable Integer userId, @PathVariable Integer entryId)
    {
        Optional<CalendarEntry> entry = calendarService.getEntryById(userId, entryId);  // Pass both userId and entryId to the service method

        if (entry.isPresent())
        {
            // If the entry exists, return it with a 200 OK response
            return ResponseEntity.ok(entry.get());
        }
        else
        {
            // If the entry is not found, return a 404 NOT FOUND response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calendar entry not found");
        }
    }

    // POST (create) a new calendar entry
    @PostMapping
    public ResponseEntity<CalendarEntry> createEntry(@RequestBody CalendarEntry entry)
    {
        try
        {
            CalendarEntry createdEntry = calendarService.createEntry(entry);
            return new ResponseEntity<>(createdEntry, HttpStatus.CREATED);
        }
        catch (RuntimeException e)
        {
            // Handle unexpected error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT (update) an existing calendar entry
    @PutMapping("/{id}")
    public ResponseEntity<CalendarEntry> updateEntry(@PathVariable Integer id, @RequestBody CalendarEntry entry)
    {
        CalendarEntry updatedEntry = calendarService.updateEntry(id, entry);
        return updatedEntry != null ? ResponseEntity.ok(updatedEntry) : ResponseEntity.notFound().build();
    }

    // DELETE a calendar entry by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Integer entryId)
    {
        try
        {
            calendarService.deleteEntry(entryId);  // Try to delete the entry
            return ResponseEntity.noContent().build();  // Return 204 No Content if successful
        }
        catch (ResponseStatusException e)
        {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                return ResponseEntity.notFound().build();  // Return 404 if entry not found
            }
            throw e;  // Rethrow any other unexpected exceptions
        }
    }
}

//***************************************************************************************

