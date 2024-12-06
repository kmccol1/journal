//***************************************************************************************
//
//     Filename: CalendarEntryRepository.java
//     Author: Kyle McColgan
//     Date: 05 December 2024
//     Description: This file provides functionality for CalendarEntry-related functionality.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.repository;

import kmccol1.gratitudejournal.gratitudejournal.model.CalendarEntry;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

public interface CalendarEntryRepository extends CrudRepository<CalendarEntry, Integer>
{
    List<CalendarEntry> findByUserId(Integer userID);
    Optional<CalendarEntry> findByUserIdAndEntryId(Integer userId, Integer entryId);

}

//***************************************************************************************
