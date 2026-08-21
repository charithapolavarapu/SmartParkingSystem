# 🚗 Smart Parking Management System

A **Java and MySQL-based Smart Parking Management System** designed to manage parking slots, vehicle entry and exit, parking records, parking fees, and parking analytics.

The system uses **JDBC** to connect the Java application with a MySQL database and provides an admin-based console interface for managing parking operations.

## ✨ Features

- 🔐 Admin login authentication
- 🅿️ View available and occupied parking slots
- 🚗 Park vehicles based on vehicle type
- 🏍️ Supports Car, Bike, and EV
- 📍 Automatically assigns an available parking slot
- 🚫 Prevents duplicate parking of the same vehicle
- 🕐 Records vehicle entry time
- 🚪 Records vehicle exit time
- 💰 Automatically calculates parking fees
- 🗄️ Stores parking information in MySQL
- 🔄 Loads parking slots and records from MySQL when the application starts
- 📋 View complete parking records
- 📊 Parking dashboard with:
  - Total parking slots
  - Occupied slots
  - Available slots
  - Cars currently parked
  - Bikes currently parked
  - EVs currently parked
  - Total parking revenue
- 🔎 Search parking records using vehicle number
- 📅 Search parking history by date

## 🛠️ Technologies Used

- **Java**
- **MySQL**
- **JDBC**
- **MySQL Connector/J**
- **Git**
- **GitHub**

## 📁 Project Structure

```text
SmartParkingSystem/
│
├── Main.java
├── AdminLogin.java
├── DBConnection.java
├── ParkingManager.java
├── ParkingSlot.java
├── ParkingRecord.java
├── ParkingFeeCalculator.java
├── Vehicle.java
│
├── database/
│   └── smart_parking.sql
│
├── lib/
│   └── mysql-connector-j-26.7.0.jar
│
├── .gitignore
└── README.md
## 📸 Screenshots

### 🅿️ Main Menu

![Smart Parking System Main Menu](screenshots/parking-menu.png)

### 📊 Parking Dashboard

![Parking Dashboard](screenshots/dashboard.png)

### 🔎 Search Parking Record

![Search Parking Record](screenshots/search-record.png)

### 📋 Parking Records

![Parking Records](screenshots/parking-records.png)

### 📅 Parking History by Date

![Parking History by Date](screenshots/parking-history.png)

## 👨‍💻 Author

**Polavarapu Prem Charitha**

B.Tech – Artificial Intelligence & Machine Learning
## 🗄️ Database Design

The Smart Parking Management System uses **MySQL** to store and manage parking information.

### Database Tables

#### 1. `vehicles`

Stores information about vehicles using the parking system.

| Column | Description |
|---|---|
| vehicle_number | Unique vehicle registration number |
| owner_name | Name of the vehicle owner |
| vehicle_type | Type of vehicle (Car/Bike/EV) |

#### 2. `parking_slots`

Stores information about available and occupied parking slots.

| Column | Description |
|---|---|
| slot_number | Unique parking slot number |
| vehicle_type | Vehicle type supported by the slot |
| is_occupied | Indicates whether the slot is occupied |

#### 3. `parking_records`

Stores vehicle parking history.

| Column | Description |
|---|---|
| vehicle_number | Vehicle registration number |
| slot_number | Assigned parking slot |
| entry_time | Vehicle entry date and time |
| exit_time | Vehicle exit date and time |
| parking_fee | Calculated parking fee |

### Database Relationship

```text
        VEHICLES
            |
            | vehicle_number
            |
            v
    PARKING_RECORDS
            |
            | slot_number
            |
            v
      PARKING_SLOTS
🗄️ Database Design