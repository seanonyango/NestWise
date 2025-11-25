# 🪺 NestWise — Personal Finance Tracker 
**A modern, intuitive Android finance app built with Jetpack Compose, Room, Retrofit, and WorkManager.**

NestWise helps users manage their daily financial lives through budgeting, tracking expenses, setting savings goals, importing CSV data, receiving daily financial tips, and more — all wrapped in a clean, responsive UI and backed by a structured MVVM + DI architecture.

---

## 🚀 Features

### **🔐 User Accounts**
- Email + password login  
- User registration with name & preferred currency 
- In-app session handling with SharedPreferences  
- Profile page for updating name & currency  
- Logout returns user to the Welcome page  

---

## 📊 **Dashboard Overview**
A personalized dashboard greeting the user:

### Displays real financial insights:
- **Monthly income total**
- **Monthly spending total**
- **Top spending category**
- **Active savings goals**
- **Daily financial tip (from API)**

---

## 💸 **Transactions**
- Add, edit, and delete transactions   
- Budget-sync actions (choose budget to assign spending to)   
- CSV import using a downloadable template  
- Clean material card UI with icons and action menus  

---

## 🎯 **Budgets**
- Create, edit, delete budgets  
- Track **spent vs. limit** with progress bars  
- Manual or semi-automatic spent adjustment  
- Integrated into dashboard insights  

---

## 🥅 **Savings Goals**
- Create, edit, delete goals  
- Track progress with visual indicators  
- Deadlines supported  
- Visible on dashboard summary  

---

## 📁 **CSV Import**
Users can:
- Download a pre-formatted CSV template  
- Fill multiple transactions in Excel / Sheets  
- Upload and instantly import them  

Template columns:
title, amount, category, date (yyyy-mm-dd), type (INCOME/EXPENSE)


---

## 🌐 **Financial Tip API Integration**
- API: **ZenQuotes /today** endpoint  
- Fetched:
  - On demand
  - Once daily via **WorkManager**
- Latest tip stored in Room  
- Dashboard shows “Today’s Tip”  
- Notification sent when a new tip is received  

---

## 🔔 **WorkManager + Notifications**
- Background worker runs every 24 hours  
- Fetches daily financial tip  
- Stores in database  
- Sends a notification  
- Custom notification channel  

---

## 🧱 **Architecture**
### Follows MVVM + Repository + Manual DI (AppContainer)
- **Room** for local persistence  
- **Retrofit** for networking  
- **WorkManager** for background tasks  
- **SharedPreferences** for session management  
- **Jetpack Compose** for UI  
- **Navigation Compose** for screen routing
 AppContainer
├── AppDatabase
│ ├── DAOs
│ └── Entities
├── Repositories
├── ViewModels
└── Retrofit API services





## 🗂️ **Tech Stack**
- **Kotlin**
- **Jetpack Compose (Material 3)**
- **Room Database**
- **Retrofit + OkHttp**
- **WorkManager**
- **Navigation Compose**
- **ViewModel + StateFlow**
- **SharedPreferences**
- **Material Design**
- **CSV Parsing**
---

## 📸 **Screenshots**
/screenshots/dashboard.png
/screenshots/transactions.png
/screenshots/goals.png
/screenshots/budgets.png
/screenshots/profile.png

## 🧪 **Testing**
- CSV Parser Unit Tests  
- DAO Tests using in-memory Room  
- Navigation UI test (Welcome → Login)

  ## 📝 Potential Improvements
- Cloud backup  
- Multi-account switching  
- ML-based budgeting suggestions  
- AI categorization  
- Currency conversion API
---

## 👤 Author
**[Sean Onyango]**  
Sean Onyango, Mobile Computing Project (CP3406)  

## 🤖 AI Assistance Disclosure
Some parts of this project (e.g., code scaffolding, documentation drafting, and troubleshooting) were created with the assistance of AI tools (OpenAI / ChatGPT). All AI-generated content was reviewed, modified, and integrated manually to ensure correctness and alignment with project requirements.
