//***************************************************************************************
//
//     Filename: CalendarEntryControllerTests.java
//     Author: Kyle McColgan
//     Date: 05 December 2024
//     Description: This file contains unit tests for the controller classes.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal;

import kmccol1.gratitudejournal.gratitudejournal.controller.CalendarEntryController;
import kmccol1.gratitudejournal.gratitudejournal.model.CalendarEntry;
import kmccol1.gratitudejournal.gratitudejournal.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

@ExtendWith(MockitoExtension.class)
class CalendarEntryControllerTests
{
    @InjectMocks
    private CalendarEntryController controller; // Inject the mocked service into the controller

    @Mock
    private CalendarService service; // Mock the CalendarService

    private CalendarEntry sampleEntry;

    @BeforeEach
    void setUp()
    {
        // Initialize a sample calendar entry
        sampleEntry = new CalendarEntry();
        sampleEntry.setId(1);
        sampleEntry.setTitle("Sample Title");
        sampleEntry.setContent("Sample Content");
        sampleEntry.setEntryDate(LocalDate.now());
        sampleEntry.setUserId(123);
    }

    // Test 1: Controller - Get entries endpoint
    @Test
    void testControllerGetEntries()
    {
        // Arrange: Prepare a sample list of entries
        List<CalendarEntry> entries = new ArrayList<>();
        entries.add(sampleEntry);

        // Mock the service call to return the prepared list
        when(service.getEntriesByUserId(123)).thenReturn(entries);

        // Act: Call the controller method
        ResponseEntity<List<CalendarEntry>> response = controller.getEntriesByUserId(123);

        // Assert: Check the response status and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo(sampleEntry.getTitle());

        // Verify: Ensure the service was called with the correct userId
        verify(service).getEntriesByUserId(123);
    }


	// Test 2: Controller - Add new entry endpoint
	@Test
	void testControllerAddEntry()
	{
		when(service.createEntry(sampleEntry)).thenReturn(sampleEntry);
		ResponseEntity<CalendarEntry> response = controller.createEntry(sampleEntry);
		assertThat(response.getBody().getTitle()).isEqualTo("Sample Title");
	}

    //3. Controller - Get entry by ID
    //Test if the controller returns the correct entry by its ID.
    @Test
    void testControllerGetEntryById() {
        // Arrange: Mock the service to return a list of entries when called with a specific userId
        List<CalendarEntry> entries = new ArrayList<>();
        entries.add(sampleEntry); // Add your sample entry to the list

        when(service.getEntriesByUserId(123)).thenReturn(entries); // Mock the service method

        // Act: Call the controller method to get entries by user ID
        ResponseEntity<List<CalendarEntry>> response = controller.getEntriesByUserId(123);

        // Assert: Check that the response status is OK and the list is not empty
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1); // Assuming it returns a single entry

        // Access the first entry from the list and check its title
        assertThat(response.getBody().get(0).getTitle()).isEqualTo(sampleEntry.getTitle());

        // Verify: Ensure the service was called with the correct userId
        verify(service).getEntriesByUserId(123);
    }

    //4. Controller - Get entry by ID Not Found
    //Test if the controller correctly handles when no entry is found by ID.
    @Test
    void testControllerGetEntryByIdNotFound()
    {
        // Arrange: Mock the service to return an empty list when no entries are found for the user
        when(service.getEntriesByUserId(123)).thenReturn(Collections.emptyList());

        // Act: Call the controller method to get entries by user ID
        ResponseEntity<List<CalendarEntry>> response = controller.getEntriesByUserId(123);

        // Assert: Ensure the response status is NO_CONTENT (204) when no entries are found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify: Ensure the service was called with the correct userId
        verify(service).getEntriesByUserId(123);
    }

    //5. Controller - Delete entry
    //Test the controller's ability to delete an entry.
    @Test
    void testControllerDeleteEntryNotFound()
    {
        // Arrange: Mock the service to throw ResponseStatusException when entry not found
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Calendar entry not found"))
                .when(service).deleteEntry(101);

        // Act: Call the controller method to delete the entry
        ResponseEntity<Void> response = controller.deleteEntry(101);

        // Assert: Ensure the status is NOT_FOUND (404)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // Verify: Ensure the service was called with the correct entryId
        verify(service).deleteEntry(101);
    }


    //6. Controller - Delete entry Not Found
    //Test if the controller returns the correct response when trying to delete a non-existent entry.
//    @Test
//    void testControllerDeleteEntryNotFound()
//    {
//        // Arrange: Mock the service to return false indicating the entry was not found
//        when(service.deleteEntry(123, 101)).thenReturn(false);
//
//        // Act: Call the controller method to delete the entry
//        ResponseEntity<Void> response = controller.deleteEntry(123, 101);
//
//        // Assert: Ensure the status is NOT_FOUND (404)
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//        // Verify: Ensure the service was called with the correct userId and entryId
//        verify(service).deleteEntry(123, 101);
//    }

    //7. Controller - Update entry
    //Test if the controller correctly handles updating an entry.
    @Test
    void testControllerUpdateEntry()
    {
        // Arrange: Mock the service to return the updated entry
        when(service.updateEntry(123, sampleEntry)).thenReturn(sampleEntry);

        // Act: Call the controller method to update the entry
        ResponseEntity<CalendarEntry> response = controller.updateEntry(123, sampleEntry);

        // Assert: Check that the response status is OK (200) and the body is the updated entry
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(sampleEntry.getTitle());

        // Verify: Ensure the service was called with the correct parameters
        verify(service).updateEntry(123, sampleEntry);
    }

    //8. Controller - Update entry Not Found
    //Test if the controller correctly handles trying to update a non-existent entry.
    @Test
    void testControllerUpdateEntryNotFound()
    {
        // Arrange: Mock the service to return an empty Optional indicating the entry wasn't found
        when(service.updateEntry(123, sampleEntry)).thenReturn(null);

        // Act: Call the controller method
        ResponseEntity<CalendarEntry> response = controller.updateEntry(123, sampleEntry);

        // Assert: Ensure the status is NOT_FOUND (404)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // Verify: Ensure the service was called with the correct parameters
        verify(service).updateEntry(123, sampleEntry);
    }

    //9. Controller - Handle Bad Request
    //Test if the controller returns a BAD_REQUEST status when the input is invalid.
//    @Test
//    void testControllerBadRequest()
//    {
//        // Arrange: Simulate a bad request scenario by passing null as entry
//        when(service.createEntry(null)).thenThrow(new IllegalArgumentException("Entry cannot be null"));
//
//        // Act: Call the controller method with invalid data (null entry)
//        ResponseEntity<CalendarEntry> response = controller.createEntry(null);
//
//        // Assert: Ensure the status is BAD_REQUEST (400)
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    }

    //10. Controller - Handle Internal Server Error
    //Test if the controller returns an INTERNAL_SERVER_ERROR status when an unexpected exception occurs.
    @Test
    void testControllerInternalServerError()
    {
        // Arrange: Simulate an unexpected exception during service method execution
        when(service.createEntry(sampleEntry)).thenThrow(new RuntimeException("Unexpected error"));

        // Act: Call the controller method
        ResponseEntity<CalendarEntry> response = controller.createEntry(sampleEntry);

        // Assert: Ensure the status is INTERNAL_SERVER_ERROR (500)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

//***************************************************************************************