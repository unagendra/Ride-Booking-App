package com.project.rideBooking.UberApp.entities;

import com.project.rideBooking.UberApp.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "app_user", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment id
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;   //email id cannot be duplicate(Unique constraint)

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) //Create another table to store Set<Role>
    @Enumerated(EnumType.STRING)   //Tell hibernate to store roles in String format
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" +role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }
}
