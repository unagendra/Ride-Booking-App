# Ride-Booking-App
A fully functional backend of the cab Ride-Booking-App with all API endpoints fully developed and tested.

Tech Stack: Spring boot v3.3, Java 22, PostGIS(Postgres Sql extension), OSRM Api, Spring Security, AWS Code pipeline(CI/CD), AWS Elastic bean stalk, RDS Database.

Key Features:


Low Level Design:

![image](https://github.com/user-attachments/assets/1d504c2a-4db0-4978-845b-1b8452015d04)

Entity Relationship Diagram:
[Uber_UML_Diagram.pdf](https://github.com/user-attachments/files/16875695/Uber_UML_Diagram.pdf)

## Entities

The ERD consists of the following main entities:

1. **User**: Represents a user of the ride-sharing application.Each user has an id (unique identifier), roles (Set of roles like Rider, Driver, Admin), name, email, password, and a location.

2.Rider: Represent rider, Each rider has an id, rating and a reference to the User entity.

3.Driver:Represent driver, Each rider has an id, rating, vechicleID,available (Driver availability), currentLocation and a reference to the User entity.

3. **RideRequest**: Represents a request for a ride made by a rider. It has an id, the rider (a User entity), status (PENDING,CONFIRMED,CANCELLED), pick-up location, drop-off location, requested time, payment method (CASH, WALLET), fare amount, and a reference to the Rider user.

4. **Ride**: Represents an actual ride that has been created after the Driver accepts Ride Request and is ONGOING.
It has an id, references to the Rider and Driver users, status (ONFIRMED, ONGOING, ENDED, CANCELLED), pick-up and drop-off locations, start and end times, createdTime, fare , one-time password (OTP) for verification, and payment method.

6. **Payment**: Represents a payment made for a ride. It has an id, reference to the Ride, payment status, and payment time.

7. **Wallet**: Represents a user's wallet for storing money. It has an id, reference to the User, current balance, and a list of payments made.

8. **Rating**: Represents a rating given by a user for a ride. It has an id, references to the Ride and User, and a rating type.

## Relationships

The entities have the following relationships:

1. **User** has a one-to-many relationship with **RideRequest**, **Ride**, **Payment**, **Wallet**, and **Rating**:
   - A user can make multiple ride requests, but each ride request is made by one user.
   - A user can be a rider or driver in multiple rides, but each ride has one rider and one driver.
   - A user can make multiple payments, but each payment is made by one user.
   - A user can have one wallet, and each wallet belongs to one user.
   - A user can give multiple ratings, but each rating is given by one user.

2. **RideRequest** has a one-to-one relationship with **Ride**:
   - Each ride request can be associated with at most one ride, and each ride is associated with one ride request.

3. **Ride** has a one-to-one relationship with **Payment**:
   - Each ride can have at most one payment, and each payment is associated with one ride.

## Example Flow:

1. **Creating a ride request**:
   - rider creates a new ride request with pick-up location as "123 Main St" (Represted in coordinates [Latitude,Longitude]) and drop-off location as "456 Oak Ave"          (Represted in coordinates[Latitude,Longitude]) and Payment Method.
   - The ride request is saved with a unique id, and other details.
   - Fare of the Ride is calculated using Strategy Design Pattern, and it is returned back to the rider.
   - REQUEST: POST localhost:8080/rider/requestRide
   - ![image](https://github.com/user-attachments/assets/09e45e03-ea66-4fdf-add1-63174a5f9692)
  
   - RESPONSE: ![image](https://github.com/user-attachments/assets/27be01ff-199e-4773-b366-862c40c44522)
   - ![image](https://github.com/user-attachments/assets/bb5791e5-587f-47a7-91e3-4a09ca07a12e)


2. **Accepting a ride request**:
   - Driver accepts the ride request made by Rider.
   - The ride request status is updated to accepted, and a new ride entity is created with references to rider and driver.
   - The ride entity also includes the pick-up and drop-off locations, start time, and other details.
   - REQUEST: POST localhost:8080/driver/acceptRide/1
   - RESPONSE:
   - ![image](https://github.com/user-attachments/assets/dcd2e7cc-4877-426e-893f-aa632e7df846)
   - ![image](https://github.com/user-attachments/assets/bf1bfc94-dbf0-4dd9-b403-23f2eebef0d3)


3. **Making a payment**:
   - After the ride is completed, a payment entity is created with the ride id, payment status as "pending", and payment time.
   - User A's wallet balance is checked and updated based on the ride fare.
   - If User A has sufficient balance, the payment status is updated to "paid". Otherwise, it remains in "pending" status.

4. **Giving a rating**:
   - After the ride is completed, User A (rider) can give a rating to User B (driver).
   - A new rating entity is created with the ride id, User A's id, and the rating type (e.g., 5 stars).
  

  
