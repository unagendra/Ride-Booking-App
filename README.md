# Ride-Booking-App
A fully functional backend of the cab Ride-Booking-App with all API endpoints fully developed and tested.

Tech Stack: Spring boot v3.3, Java 22, PostGIS(Postgres Sql extension), OSRM Api, Spring Security, AWS Code pipeline(CI/CD), AWS Elastic bean stalk, RDS Database.


Low Level Design:

![image](https://github.com/user-attachments/assets/1d504c2a-4db0-4978-845b-1b8452015d04)

Entity Relationship Diagram:
[Uber_UML_Diagram.pdf](https://github.com/user-attachments/files/16875695/Uber_UML_Diagram.pdf)

## Entities

The ERD consists of the following main entities:

1. **User**: Represents a user of the ride-sharing application.
 Each user has an id (unique identifier),
  roles (list of roles like Rider, Driver, Admin),
   name, email, password, and a location.

2. **RideRequest**: Represents a request for a ride made by a user. It has an id, the rider (a User entity), status (pending, accepted, rejected), pick-up location, drop-off location, requested time, payment method, fare amount, and a reference to the Rider user.

3. **Ride**: Represents an actual ride that has been accepted and is in progress. It has an id, references to the Rider and Driver users, status (started, in-progress, completed), pick-up and drop-off locations, start and end times, fare amount, one-time password (OTP) for verification, and payment method.

4. **Payment**: Represents a payment made for a ride. It has an id, reference to the Ride, payment status, and payment time.

5. **Wallet**: Represents a user's wallet for storing money. It has an id, reference to the User, current balance, and a list of payments made.

6. **Rating**: Represents a rating given by a user for a ride. It has an id, references to the Ride and User, and a rating type.

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
   - User A (rider) creates a new ride request with pick-up location as "123 Main St" and drop-off location as "456 Oak Ave".
   - The ride request is saved with a unique id, User A's id as the rider, pending status, requested time, and other details.

2. **Accepting a ride request**:
   - User B (driver) accepts the ride request made by User A.
   - The ride request status is updated to accepted, and a new ride entity is created with references to User A (rider) and User B (driver).
   - The ride entity also includes the pick-up and drop-off locations, start time, and other details.

3. **Making a payment**:
   - After the ride is completed, a payment entity is created with the ride id, payment status as "pending", and payment time.
   - User A's wallet balance is checked and updated based on the ride fare.
   - If User A has sufficient balance, the payment status is updated to "paid". Otherwise, it remains in "pending" status.

4. **Giving a rating**:
   - After the ride is completed, User A (rider) can give a rating to User B (driver).
   - A new rating entity is created with the ride id, User A's id, and the rating type (e.g., 5 stars).
  
5. 
[Uploadin{
	"info": {
		"_postman_id": "ed83c311-1a6a-415f-bf14-0e8e7abfafe4",
		"name": "UberApp",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "36415165"
	},
	"item": [
		{
			"name": "rideRequet",
			"item": [
				{
					"name": "rideRequest",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\r\n\"pickupLocation\":{\r\n    \"coordinates\":[\r\n        74.221323,\r\n        28.33423123\r\n    ]\r\n},\r\n\r\n    \"dropOffLocation\":{\r\n    \"coordinates\":[\r\n        74.1213,\r\n        28.234123\r\n    ]\r\n},\r\n\r\n\"paymentMethod\":\"CASH\"\r\n\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "localhost:8080/rider/requestRide",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"rider",
								"requestRide"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Driver",
			"item": [
				{
					"name": "acceptRide",
					"request": {
						"method": "POST",
						"header": [],
						"url": {
							"raw": "localhost:8080/driver/acceptRide/2",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"driver",
								"acceptRide",
								"2"
							]
						}
					},
					"response": []
				},
				{
					"name": "startRide",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"otp\":\"6227\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "localhost:8080/driver/startRide/1",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"driver",
								"startRide",
								"1"
							]
						}
					},
					"response": []
				},
				{
					"name": "endRide",
					"request": {
						"method": "POST",
						"header": [],
						"url": {
							"raw": "localhost:8080/driver/endRide/1",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"driver",
								"endRide",
								"1"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Auth",
			"item": [
				{
					"name": "SignUp",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"name\":\"UNag\",\r\n    \"email\":\"nag@gmail.com\",\r\n    \"password\":\"password\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "localhost:8080/auth/signup",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"auth",
								"signup"
							]
						}
					},
					"response": []
				},
				{
					"name": "login",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"email\":\"nag@gmail.com\",\r\n    \"password\":\"password\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "localhost:8080/auth/login",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"auth",
								"login"
							]
						}
					},
					"response": []
				},
				{
					"name": "onBoardNewDriver",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiI0MSIsImVtYWlsIjoibmFnQGdtYWlsLmNvbSIsInJvbGUiOiJbUklERVJdIiwiaWF0IjoxNzI0Njk0NjI1LCJleHAiOjE3MjQ2OTUyMjV9.x0zHbjaIHDrTfCb6GNOnSLbyo_SSTx0cFHO_ucRoQL-_qusSF6EN2T42aJZxo8Rx",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"vehicleId\":\"BADFGHHH\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "localhost:8080/auth/onBoardNewDriver/41",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"auth",
								"onBoardNewDriver",
								"41"
							]
						}
					},
					"response": []
				}
			]
		}
	]
}g UberApp.postman_collection.json…]()


