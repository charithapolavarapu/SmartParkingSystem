# Smart Parking Management System

A Java and MySQL based Smart Parking Management System that manages parking slots, vehicle entry and exit, parking records, and parking fee calculation.

## Features

- View available and occupied parking slots
- Park vehicles based on vehicle type
- Supports Car, Bike, and EV
- Automatically assigns an available parking slot
- Record vehicle entry time
- Record vehicle exit time
- Calculate parking fees
- Store vehicle and parking information in MySQL
- View complete parking records
- Parking dashboard showing:
  - Total slots
  - Occupied slots
  - Available slots
  - Cars parked
  - Bikes parked
  - EVs parked
  - Total revenue

## Technologies Used

- Java
- MySQL
- JDBC
- MySQL Connector/J
- Git
- GitHub

## Project Structure

```text
SmartParkingSystem/
├── Main.java
├── DBConnection.java
├── ParkingManager.java
├── ParkingSlot.java
├── ParkingRecord.java
├── ParkingFeeCalculator.java
├── Vehicle.java
├── database/
│   └── smart_parking.sql
├── lib/
│   └── mysql-connector-j-26.7.0.jar
├── .gitignore
└── README.md

