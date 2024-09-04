package com.project.rideBooking.UberApp.security;


import com.project.rideBooking.UberApp.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    //Method to get the Secret Key
    public SecretKey getSecretKey(){
       return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * .issuedAt(new Date())
     * .expiration(new Date(System.currentTimeMillis()+1000*60))
            * new Date(): Creates a new Date object with the current date and time.
            * System.currentTimeMillis(): Returns the current time in milliseconds since the Unix epoch (January 1, 1970).
            * 1000 * 60: Represents 60,000 milliseconds, which is equivalent to 1 minute.
            * new Date(System.currentTimeMillis() + 1000 * 60): Creates a new Date object that is set to 1 minute from the current time.
     *
     *
     * @param user
     * @return String(JWT Token)
     */
//generate Token for the user
    public String generateAccessToken(User user){
    //crete JWT Token and return the token
      return   Jwts.builder()
                .subject(user.getId().toString())  //identify the user
                //Pass inside the Payload()
                .claim("email",user.getEmail())
//                .claim("role", Set.of("ADMIN","USER"))
                .claim("role",user.getRoles().toString())         //passing role to JWT Payload
                //Every JWT should have issued at Time & Expiration time
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10)) //10mins
                //pass the Secret Key
                .signWith(getSecretKey())
                .compact();
    }

    //generate Token for the user
    public String generateRefreshToken(User user){
        //crete JWT Token and return the token
        return   Jwts.builder()
                .subject(user.getId().toString())  //identify the user

                //Every JWT should have issued at Time & Expiration time
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 1000L *60*60*24*30*6))
                //pass the Secret Key
                .signWith(getSecretKey())
                .compact();
    }

    //How to get the user Id from the Token
    public Long getUserIdFromToken(String token){
        //creating instance of the claims interface
        Claims claims=Jwts.parser()     //retrieve JWT Token
                .verifyWith(getSecretKey())  //check the secret key of the token with the Secret key
                .build()
                .parseSignedClaims(token)  //check if the Token is Valid or not
                .getPayload() ;     //get the Payload from the Token
            return Long.valueOf(claims.getSubject());

    }

}
