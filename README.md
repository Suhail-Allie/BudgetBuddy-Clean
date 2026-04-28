# BudgetBuddy – Personal Budgeting App

## Overview
BudgetBuddy is an Android mobile application designed to help users manage their personal finances. The app allows users to track income and expenses, set budget goals, categorize spending, and generate reports to better understand their financial habits.

This project was developed as a group assignment and demonstrates Android development, local data storage, and UI design.

---

## Features

### Authentication
- User registration and login
- Credentials stored using SharedPreferences

### Dashboard
- Displays current balance
- Shows transaction history
- Displays budget goals and category count

### Categories
- Create and manage spending categories
- Input validation included

### Budget Goals
- Set minimum and maximum monthly goals
- Goals displayed on dashboard

### Transactions
- Add income and expenses
- Automatic balance updates
- Stored in local database

### Database (RoomDB)
- Stores expenses, categories, and goals
- Uses DAO for database operations

### Filtering
- Filter expenses by date range
- Uses timestamp-based queries

### Reports
- Displays total spending per category
- Helps users analyze spending habits

---

### Tech Stack

- **Language:** Kotlin  
- **IDE:** Android Studio  
- **Database:** Room Database (SQLite)  
- **UI:** XML Layouts  
- **Storage:** SharedPreferences  

---

### App Screens

- Login Screen  
- Register Screen  
- Dashboard  
- Category Management  
- Budget Goals  
- Expense List  
- Filter Screen  
- Report Screen  

---

## Team Contributions

### Suhail Allie – Authentication & Dashboard
- Login and Register functionality
- Dashboard UI and logic
- Balance tracking

### Yusuf Alexander – Categories & Budget Goals
- Category creation and management
- Budget goal setup (min/max)
- Input validation

### Liam Hendricks – Transactions & Reports
- Income and expense tracking
- Report generation (totals per category)

### Cole Simons – Database & Filtering
- Room Database implementation
- Expense storage and retrieval
- Date filtering functionality

### Zaheer Brown - Demo Video & Testing
- Video presentation of functionality
- github actions
- Automated Testing
- APK built
 
---

## How to Run the App

1. Clone the repository:
   ```bash
   git clone https://github.com/Suhail-Allie/BudgetBuddy-Clean.git
