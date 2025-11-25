# Rapido Ride-Sharing System 🛵

A comprehensive Java-based ride-sharing application that simulates real-world ride-hailing services like Rapido. This system supports multiple user roles including Riders, Drivers, and Administrators with complete ride management functionality.

## 🚀 Features

### 👥 Multi-Role System

- **Riders**: Book rides, view nearby drivers, make payments, and view ride history
- **Drivers**: Go online/offline, accept ride requests, complete rides, and track earnings
- **Admins**: Manage users, approve drivers, set pricing, and view system analytics

### 🎯 Core Functionality

- **Ride Booking**: Real-time ride requests with distance calculation and fare estimation
- **Driver Matching**: Automatic assignment of available drivers to ride requests
- **Payment System**: Multiple payment methods (UPI, Cash, Wallet)
- **Real-time Tracking**: ETA calculation and ride status updates
- **Data Persistence**: Automatic saving and loading of all system data
- **Admin Controls**: Comprehensive user and ride management tools

## 🛠️ Technical Architecture

### Classes Overview

- **User** (Abstract Base Class): Core user properties and authentication
- **Rider**: Manages ride history and booking operations
- **Driver**: Handles ride acceptance, earnings, and online status
- **Admin**: System administration and user management
- **Ride**: Complete ride lifecycle management with receipt generation
- **RapidoSystem**: Main system controller with menu navigation and data persistence

### Design Patterns

- **Serialization**: For data persistence across sessions
- **Polymorphism**: Dynamic dashboard rendering based on user role
- **Encapsulation**: Secure data access through getters/setters
- **Iterator Pattern**: Safe collection traversal for user management

## 📋 Prerequisites

- Java JDK 8 or higher
- Basic understanding of Object-Oriented Programming
- Command-line interface for interaction

## 🚀 Installation & Execution

1. **Create project directory:**

   -mkdir rapido-system
   -cd rapido-system
   -mkdir rapido

2. **Save all Java files in the rapido directory:**

3. **Compile all Java files:**
   -javac rapido/\*.java

4. **Run the application:**
   -java rapido.Main
