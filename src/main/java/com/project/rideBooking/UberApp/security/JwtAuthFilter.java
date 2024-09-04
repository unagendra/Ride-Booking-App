package com.project.rideBooking.UberApp.security;


import com.project.rideBooking.UberApp.entities.User;
import com.project.rideBooking.UberApp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

   private final JwtService jwtService;
   private final UserService userService;

   //Passes the Exception from the Spring Security Context to Dispatcher servlet
    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver handlerExceptionResolver;


    @Override //Every Filter has this method
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.Fetch the Authorization token from the request Header

        try{

      final String requestTokenHeader=request.getHeader("Authorization");  //"Bearer daedfkakf.gafasdbnk.hjafafjk"
        if (requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer")){
            //If no Token is present, Do nothing pass to next Filter
            filterChain.doFilter(request,response);
            return;
        }

        //If Token is present(Split the Token by separating the Bearer)
        String token=requestTokenHeader.split("Bearer ")[1];  //[" ","daedfkakf.gafasdbnk.hjafafjk"]

        //2.Extract UserId/email from the Token.

        Long userId=jwtService.getUserIdFromToken(token);

        //3.Validate the userId with the database user and Fetch the user based on userId
                                //Only add the user to SCH if he is not present
        if (userId!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

            User user = userService.getUserById(userId);

            //4.Add the user into Spring Security context Holder
            //create a UsernamePasswordAuthenticationToken (Authentication) obj

            UsernamePasswordAuthenticationToken authenticationTokenObj =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());  //specify user Authority here

            //optional(More information, IP address ,session about the request)
            authenticationTokenObj.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            //add the authentication obj to authentication class in SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authenticationTokenObj);

            //5. pass the request to next Filter(This is mandatory otherwise next filter is not called)
            filterChain.doFilter(request, response);

        }

        } catch (Exception ex){
            handlerExceptionResolver.resolveException(request , response ,null, ex);
        }


    }
}
