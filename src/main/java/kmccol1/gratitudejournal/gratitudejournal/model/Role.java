//***************************************************************************************
//
//     Filename: Role.java
//     Author: Kyle McColgan
//     Date: 04 December 2024
//     Description: This file contains the Role entity class.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;

//***************************************************************************************

@Entity
@Table(name = "roles")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;  // Example values: "ROLE_USER", "ROLE_ADMIN"

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference  // Avoid circular reference when serializing Role
    private Set<User> users;

    //Constructors
    // No-argument constructor (required by JPA)
    public Role() {}

    // No-argument constructor (required by JPA)
    public Role(String name)
    {
        this.name = name;
    }

    // Getters and setters
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }

    @Override
    public String toString()
    {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}

//***************************************************************************************
