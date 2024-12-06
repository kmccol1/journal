//***************************************************************************************
//
//     Filename: RoleRepository.java
//     Author: Kyle McColgan
//     Date: 27 November 2024
//     Description: This file provides database connectivity
//                  for role based access control functionality.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.repository;

//***************************************************************************************

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import kmccol1.gratitudejournal.gratitudejournal.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>
{
    Role findByName(String name); //For finding a role by its name...
}

//***************************************************************************************