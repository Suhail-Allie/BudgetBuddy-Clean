# BudgetBuddy – Personal Budgeting App

BudgetBuddy is a modern Android personal budgeting application developed in Kotlin. The app helps users manage their finances by tracking income, recording expenses, setting budget goals, organising spending into categories, viewing reports, and monitoring monthly budget progress.

This project was developed for the Open Source Coding / Mobile Development Portfolio of Evidence. It demonstrates Android development, RoomDB local storage, SharedPreferences, user authentication, automated testing, GitHub version control, GitHub Actions, and final POE feature integration.

---

## Project Overview

Managing personal finances can be difficult, especially for students and young adults who need to control spending and build better saving habits. BudgetBuddy was designed to make budgeting easier, clearer, and more engaging.

The app allows users to:

- Register and log in
- Add income
- Add expenses
- Create categories
- Set monthly budget goals
- Attach receipt images
- Filter expenses by date
- View reports and spending graphs
- Track monthly budget progress
- Earn budgeting badges

BudgetBuddy runs locally on an Android device and stores financial data using local storage.

---

## App Purpose

The purpose of BudgetBuddy is to help users improve their financial awareness by giving them a simple mobile tool to track where their money goes.

The app supports better budgeting by showing:

- Current balance
- Monthly spending progress
- Minimum and maximum budget goals
- Category spending
- Spending reports
- Gamification badges for positive budgeting behaviour

---

## Key Features

### 1. User Registration and Login

Users can create an account and log in with a username and password.

Each user has separate data, meaning one user’s income, expenses, goals, categories, badges, and reports do not appear on another user’s profile.

---

### 2. Modern Dashboard

The dashboard displays:

- Logged-in user name
- Current balance
- Budget goals
- Number of categories
- Monthly spending progress
- Progress percentage
- Budget status message
- Earned badges
- Recent session activity
- Navigation to all main app features

The dashboard was redesigned with a modern card-based layout, rounded buttons, improved spacing, and a clean colour scheme.

---

### 3. Categories

Users can create spending categories such as:

- Groceries
- Transport
- Entertainment
- Food
- Rent
- Data

Categories are used when adding expenses so that spending can be grouped and reported clearly.

---

### 4. Budget Goals

Users can set:

- Minimum monthly budget goal
- Maximum monthly budget goal

The app uses these values to show whether the user is:

- Below the minimum goal
- Within the monthly budget range
- Over the maximum budget goal

---

### 5. Income and Expense Tracking

Users can add income and expenses.

Expense entries include:

- Description/title
- Amount
- Date
- Category
- Optional notes
- Optional receipt image

The balance and reports update automatically when new entries are added.

---

### 6. Receipt Image Attachment

Users can attach a receipt image to an expense using the Android image picker. This helps users keep proof of purchases and improves the usefulness of expense tracking.

---

### 7. Date Filtering

Users can filter expenses by a selected start date and end date. This allows users to review spending for a specific period.

---

### 8. Reports

The reports screen displays:

- Total income
- Total expenses
- Remaining balance
- Budget goal reference
- Category breakdown
- Visual spending graph

The report screen was redesigned with a modern layout and clear financial summary sections.

---

## Final POE Features

The following final POE features were implemented:

### 1. Visual Budget Progress Dashboard

The dashboard includes a visual progress bar showing how much of the maximum monthly budget has been used. It also displays a budget status message to help the user understand whether they are staying within their goals.

---

### 2. Category Spending Graph

The reports screen includes a visual category spending graph. This allows users to compare spending between categories and identify spending trends.

---

### 3. Gamification Badges

BudgetBuddy includes a simple rewards system to make budgeting more engaging.

Badges include:

- First Expense
- Consistent Logger
- Budget Keeper
- Smart Saver

These badges reward users for logging expenses and staying within budget goals.

---

## Two Own Features

In addition to the required features, BudgetBuddy includes the following two own features:

### 1. User-Specific Data Separation

Each logged-in user has separate data. This prevents one user’s goals, expenses, categories, income, reports, and badges from appearing in another user’s account.

This makes the app more realistic and improves privacy between users.

---

### 2. Receipt Image Attachment

Users can attach a receipt image to an expense. This allows users to keep proof of spending and makes the expense tracking feature more practical.

---

## Modern UI Improvements

The app was redesigned to look more professional and premium.

UI improvements include:

- Modern purple and mint colour scheme
- Card-based layouts
- Rounded buttons
- Clean spacing
- Improved typography
- Better input fields
- Smooth screen transitions
- Consistent styling across all screens
- Improved dashboard layout
- Improved reports layout
- Improved login and register screens
- Improved filter and expense list screens

---

## Technologies Used

- Kotlin
- Android Studio
- XML layouts
- Room Database
- SQLite
- SharedPreferences
- Kotlin Coroutines
- JUnit testing
- GitHub
- GitHub Actions

---

## Database and Data Persistence

BudgetBuddy uses RoomDB to store expense records locally on the Android device.

Each expense stores:

- ID
- User ID
- Title/description
- Amount
- Category
- Type
- Date
- Receipt photo path

The `userId` field ensures that each logged-in user only sees their own expenses.

SharedPreferences are used for:

- User account details
- Current login session
- Budget goals
- Category lists

This combination allows the app to store financial records locally while also keeping each user’s data separate.

---

## User-Specific Data

The app stores a current user key when a user logs in. This user key is used to separate data between users.

This means:

- User 1 can add their own income, expenses, goals, categories, and badges.
- User 2 starts with a clean dashboard and separate data.
- Logging back into User 1 restores User 1’s data.

This improves the professionalism and usability of the app.

---

## GitHub Version Control

GitHub was used to manage the project source code and version history.

Version control was used to:

- Track project changes
- Save working versions
- Push final code to the remote repository
- Manage updates safely
- Keep evidence of development progress

Repository link:

https://github.com/Suhail-Allie/BudgetBuddy-Clean

---

## GitHub Actions

GitHub Actions was added to automatically build and test the Android project when code is pushed to GitHub.

The workflow checks that:

- The project builds outside the local computer
- Unit tests run successfully
- The app compiles correctly on GitHub

The latest GitHub Actions workflow run passed successfully.

---

## Automated Testing

Automated unit tests were added to check important BudgetBuddy logic.

The tests cover:

- Balance calculation
- Budget status calculation
- Progress percentage calculation
- Badge unlocking logic

This helps prove that important app calculations work correctly.

---

## App Screens

The app includes the following screens:

- Login Screen
- Register Screen
- Dashboard
- Category Management Screen
- Budget Goals Screen
- Add Expense Details Dialog
- Filter Expenses Screen
- Filtered Expense List Screen
- Reports Screen

---

## Screenshots

Screenshots can be added to the `screenshots` folder in the project.

Recommended screenshot files:

- `screenshots/login.png`
- `screenshots/register.png`
- `screenshots/dashboard.png`
- `screenshots/add_expense.png`
- `screenshots/categories.png`
- `screenshots/budget_goals.png`
- `screenshots/reports.png`
- `screenshots/filter.png`
- `screenshots/expense_list.png`
- `screenshots/github_actions.png`

### Dashboard

![Dashboard](screenshots/dashboard.png)

### Add Expense Details

![Add Expense Details](screenshots/add_expense.png)

### Reports and Graph

![Reports and Graph](screenshots/reports.png)

### GitHub Actions

![GitHub Actions](screenshots/github_actions.png)

---

## Video Demonstration

YouTube video demonstration:

https://youtube.com/shorts/NmNPfvr40QM?feature=share

The video demonstrates the main features of the app, including:

- Login and registration
- Dashboard
- Categories
- Budget goals
- Adding income
- Adding expenses
- Receipt attachment
- Monthly progress dashboard
- Gamification badges
- Reports and graph
- Expense filtering
- GitHub repository
- GitHub Actions

---

## APK

The final APK was built using Android Studio.

To build the APK manually:

```bash
./gradlew assembleDebug
```

The APK can be found at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

For final submission, the APK can be renamed to:

```text
BudgetBuddy_Final.apk
```

---

## How to Run the App

1. Clone the repository:

```bash
git clone https://github.com/Suhail-Allie/BudgetBuddy-Clean.git
```

2. Open the project in Android Studio.

3. Wait for Gradle sync to complete.

4. Build the project:

```bash
./gradlew assembleDebug
```

5. Run the app on a real Android phone or emulator.

---

## Final Testing Checklist

The final version of BudgetBuddy was tested for:

- User registration
- User login
- Separate user data
- Adding income
- Adding expenses
- Adding categories
- Setting minimum and maximum budget goals
- Receipt image attachment
- Dashboard progress tracking
- Badge unlocking
- Reports and spending graph
- Filtering expenses by date
- Navigation and back buttons
- GitHub Actions build success

---

## Team Contributions

### Suhail Allie – Authentication, Dashboard and Final Integration

- Login and registration functionality
- Dashboard UI and logic
- Balance tracking
- Final POE dashboard progress
- User-specific data improvements
- Final app integration

### Yusuf Alexander – Categories and Budget Goals

- Category creation and management
- Budget goal setup
- Minimum and maximum goal validation
- Input validation

### Liam Hendricks – Transactions and Reports

- Income tracking
- Expense tracking
- Report generation
- Category spending summaries
- Visual spending graph

### Cole Simons – Database and Filtering

- Room Database implementation
- Expense storage and retrieval
- User-specific expense filtering
- Date filtering functionality

### Zaheer Brown – Demo Video, Testing and GitHub Actions

- Video presentation
- Automated testing
- GitHub Actions workflow
- APK build support

---

## References

Android Developers. (n.d.). Android Studio documentation.  
https://developer.android.com/studio

Android Developers. (n.d.). Room persistence library.  
https://developer.android.com/training/data-storage/room

Android Developers. (n.d.). Save simple data with SharedPreferences.  
https://developer.android.com/training/data-storage/shared-preferences

Android Developers. (n.d.). Activity Result APIs.  
https://developer.android.com/training/basics/intents/result

GitHub Docs. (n.d.). GitHub Actions documentation.  
https://docs.github.com/en/actions

Kotlin Documentation. (n.d.). Kotlin language documentation.  
https://kotlinlang.org/docs/home.html