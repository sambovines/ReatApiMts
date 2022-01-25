package com.example.spring1.model;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @Size(min = 2, max = 30, message = "firstName should be between 2 and 30 characters")
    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    @Size(min = 2, max = 30, message = "lastName should be between 2 and 30 characters")
    private String lastName;

    @Column(name="phone_number")
    private String phoneNumber;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private Department department;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Id != null && Objects.equals(Id, user.Id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
