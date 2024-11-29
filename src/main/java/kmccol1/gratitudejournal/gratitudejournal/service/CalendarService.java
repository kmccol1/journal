package kmccol1.gratitudejournal.gratitudejournal.service;

import kmccol1.gratitudejournal.gratitudejournal.model.CalendarEntry;
import kmccol1.gratitudejournal.gratitudejournal.repository.CalendarEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarService
{

    private final CalendarEntryRepository calendarEntryRepository;
    private final UserService userService;  // Optional for validation

    @Autowired
    public CalendarService(CalendarEntryRepository calendarEntryRepository, UserService userService) {
        this.calendarEntryRepository = calendarEntryRepository;
        this.userService = userService;
    }

    public List<CalendarEntry> getEntriesByUserId(Integer userId) {
        return calendarEntryRepository.findByUserId(userId);
    }

    public CalendarEntry createEntry(CalendarEntry entry)
    {
        // Check if user exists without throwing an exception
        if (!userService.userExists(entry.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return calendarEntryRepository.save(entry);
    }

    public Optional<CalendarEntry> getEntryById(Integer id) {
        return calendarEntryRepository.findById(id);
    }

    public void deleteEntry(Integer id)
    {
        calendarEntryRepository.deleteById(id);
    }
}
