package com.project.rideBooking.UberApp.service.impl;


import com.project.rideBooking.UberApp.dto.DriverDto;
import com.project.rideBooking.UberApp.dto.SignupDto;
import com.project.rideBooking.UberApp.dto.UserDto;
import com.project.rideBooking.UberApp.entities.Driver;
import com.project.rideBooking.UberApp.entities.User;
import com.project.rideBooking.UberApp.entities.enums.Role;
import com.project.rideBooking.UberApp.exceptions.ResourceNotFoundException;
import com.project.rideBooking.UberApp.exceptions.RuntimeConflictException;
import com.project.rideBooking.UberApp.repository.UserRepository;
import com.project.rideBooking.UberApp.security.JwtService;
import com.project.rideBooking.UberApp.service.AuthService;
import com.project.rideBooking.UberApp.service.DriverService;
import com.project.rideBooking.UberApp.service.RiderService;
import com.project.rideBooking.UberApp.service.WalletService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

   private final UserRepository userRepository;
   private  final ModelMapper mapper;
   private  final RiderService riderService;
   private final WalletService walletService;
   private final DriverService driverService;
   private final PasswordEncoder passwordEncoder;
   private final AuthenticationManager authenticationManager;
   private final JwtService jwtService;

    @Override
    public String[] login(String email, String password) {

        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        //get the authenticated user from the authentication object
        User user= (User) authentication.getPrincipal();

       String accessToken= jwtService.generateAccessToken(user);
       String refreshToken= jwtService.generateRefreshToken(user);

        return new String[] {accessToken,refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        //Verify if the User is already present in User Entity (Email)
        Optional<User> user=userRepository.findByEmail(signupDto.getEmail());
        if (user.isPresent()){
//            if(user != null)
            throw new RuntimeConflictException("Cannot signup, User already exists with email "+signupDto.getEmail());
        }

        //Covert DTO to USER and setRole as Rider(Default Role)
        User mappedUser=mapper.map(signupDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));

        //Encode the Password Entered By the User
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        //Save data to User Entity(name,password,email,roles)
        User savedUser= userRepository.save(mappedUser);

        // create user related entities
        //Create a rider profile and Save it to rider Entity , Every user is rider by default hence rider profile and Save it
        riderService.createNewRider(savedUser);

        //        TODO add wallet related service here (//create new wallet when user Sign up)
        walletService.createNewWallet(savedUser);

        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
    //get the user from userID
        User user=userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User not found with id "+userId));
        //Check if the User Role is already Driver or not
        if (user.getRoles().contains(Role.DRIVER)){
            throw new RuntimeConflictException("User with id "+userId+" is already a Driver");
        }

        //Create New Driver and Update the Role of the newly Created Driver in the USer table as Driver
        Driver createDriver= Driver.builder()
                .user(user)   //signup
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();
        //Update the role of the user/newly created Driver as Role.Driver in the User Table
        user.getRoles().add(Role.DRIVER);
        userRepository.save(user);

        Driver savedDriver=driverService.createNewDriver(createDriver);

        return mapper.map(savedDriver,DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }


}
