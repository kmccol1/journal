//***************************************************************************************
//
//     Filename: UserRepository.java
//     Author: Kyle McColgan
//     Date: 27 November 2024
//     Description: This file provides functionality for user-related functionality.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.repository;

//***************************************************************************************

import kmccol1.gratitudejournal.gratitudejournal.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

//***************************************************************************************

public interface UserRepository extends CrudRepository<User, Integer>
{
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

//***************************************************************************************

