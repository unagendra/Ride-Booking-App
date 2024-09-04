package com.project.rideBooking.UberApp.dto;


import com.project.rideBooking.UberApp.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
    //password should not be returned to the Front end
}
