# Pitstop Booking Application
**Pitstop Booking Application** is a web-based system that allows users to book service times for vehicle maintenance. It provides an easy-to-use interface for selecting vehicle types, service locations, and available time slots based on user-defined filters.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Usage](#usage)
- [Contact](#contact)

## Overview
The Pitstop Booking Application allows users to:
* Select a vehicle type (car or truck)
* Choose a service location
* Specify a date range to find available times
* View and book available service times

This project uses Vaadin for the front-end and a backend service to retrieve available times from multiple APIs.

## Features
* Vehicle Type Selection: Users can choose between cars or trucks.
* Location-Based Booking: Service locations are filtered based on the selected vehicle type.
* Time Slot Selection: Users can pick an available time slot for their vehicle service.
* Error Handling: Notifications are shown when no time slots are available or if the API call fails.

## Technologies Used
* Java (Backend)
* Vaadin (UI Framework)
* Spring Boot (Framework for dependency injection and API calls)
* REST APIs for fetching time slots
* Jackson for JSON processing
* Java HTTP Client for making API calls

## Prerequisites
Before running this project, ensure you have the following installed:
* Java 17 or higher
* Maven
* An IDE (e.g., IntelliJ IDEA, Eclipse)

## Usage
### Booking a Service:
1. Select a Vehicle Type: Choose either "Car" or "Truck".
2. Pick a Location: The available locations will be shown based on your vehicle type.
3. Select a Date Range: Choose the date range during which you'd like to book a time.
4. View Available Times: The available time slots will be displayed for the selected date range.
5. Book a Time Slot: Once you find a suitable time, click the "Book Time!" button to complete the booking.

## Contact
For any issues or requests, feel free to contact:
* Email: **palllaura.tln@gmail.com**
* GitHub: **palllaura**