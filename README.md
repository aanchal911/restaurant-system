# 🍽️ Aanchal's Multicuisine Restaurant Management System

<div align="center">

![Restaurant Banner](https://img.shields.io/badge/🍽️_Aanchal's_Restaurant-Management_System-FF6B6B?style=for-the-badge&logo=restaurant&logoColor=white)

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Swing](https://img.shields.io/badge/Swing-GUI-4CAF50?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)

**A Complete Restaurant Management Solution with Multi-Cuisine Support**

[🚀 Quick Start](#-quick-start) • [📋 Features](#-features) • [🏗️ Architecture](#️-system-architecture) • [📊 Database](#-database-schema) • [🎯 Usage](#-usage-guide)

</div>

---

## 🌟 System Overview

```mermaid
graph TD
    A[🏪 Restaurant System] --> B[👨‍💼 Admin Panel]
    A --> C[🍽️ Menu Management]
    A --> D[📋 Order Processing]
    A --> E[💰 Billing System]
    
    B --> B1[🔐 Authentication]
    B --> B2[📝 Menu Editing]
    B --> B3[👥 User Management]
    
    C --> C1[🍛 Multi-Cuisine Menu]
    C --> C2[🏷️ Category Management]
    C --> C3[💵 Price Management]
    
    D --> D1[🪑 Table Booking]
    D --> D2[➕ Add Items]
    D --> D3[📊 Order Tracking]
    
    E --> E1[🧾 Bill Generation]
    E --> E2[💳 Payment Processing]
    E --> E3[📈 Sales Reports]
    
    style A fill:#FF6B6B,stroke:#333,stroke-width:3px,color:#fff
    style B fill:#4ECDC4,stroke:#333,stroke-width:2px,color:#fff
    style C fill:#45B7D1,stroke:#333,stroke-width:2px,color:#fff
    style D fill:#96CEB4,stroke:#333,stroke-width:2px,color:#fff
    style E fill:#FFEAA7,stroke:#333,stroke-width:2px,color:#333
```

## 🎯 Key Features

<table>
<tr>
<td width="50%">

### 🍛 **Multi-Cuisine Menu**
- **Punjabi Delicacies** 🥘
- **South Indian Specialties** 🍛
- **Chinese Favorites** 🥢
- **Fresh Soups & Appetizers** 🍲
- **Aromatic Rice & Biryanis** 🍚
- **Freshly Baked Breads** 🍞
- **Refreshing Beverages** 🥤
- **Delicious Desserts** 🍰

</td>
<td width="50%">

### ⚡ **Core Functionality**
- 🔐 **Secure Admin Authentication**
- 📋 **Real-time Order Management**
- 🪑 **Table Booking System**
- 💰 **Automated Billing**
- 📊 **Live Order Tracking**
- 🎨 **Modern GUI Interface**
- 🗄️ **MySQL Database Integration**
- 📱 **Responsive Design**

</td>
</tr>
</table>

## 🏗️ System Architecture

```mermaid
flowchart LR
    subgraph "🖥️ Presentation Layer"
        A[Rst.java<br/>Main GUI]
        B[SimpleOrderDialog<br/>Order Interface]
        C[BillingDialog<br/>Billing System]
        D[AdminLoginDialog<br/>Admin Access]
        E[MenuEditDialog<br/>Menu Management]
    end
    
    subgraph "🔧 Business Logic"
        F[OrderManager<br/>Order Processing]
        G[Database<br/>Data Operations]
        H[AdminAuth<br/>Authentication]
        I[MenuItem<br/>Menu Model]
    end
    
    subgraph "🗄️ Data Layer"
        J[(MySQL Database<br/>restaurant)]
        K[DatabaseConnection<br/>Connection Pool]
    end
    
    A --> F
    A --> G
    B --> F
    C --> F
    D --> H
    E --> G
    
    F --> K
    G --> K
    H --> K
    
    K --> J
    
    style A fill:#FF6B6B,stroke:#333,stroke-width:2px,color:#fff
    style J fill:#4ECDC4,stroke:#333,stroke-width:2px,color:#fff
```

## 📊 Database Schema

```mermaid
erDiagram
    CATEGORY {
        int category_id PK
        varchar name
    }
    
    MENU {
        int item_id PK
        int category_id FK
        varchar name
        decimal price
        text description
        boolean is_active
        timestamp created_at
    }
    
    RESTAURANT_TABLE {
        int table_id PK
        varchar table_name
        int seats
        boolean occupied
    }
    
    ORDERS {
        int order_id PK
        int table_id FK
        boolean is_closed
        timestamp opened_at
        timestamp closed_at
    }
    
    ORDER_ITEMS {
        int order_item_id PK
        int order_id FK
        int item_id FK
        int quantity
        decimal unit_price
        timestamp added_at
    }
    
    CATEGORY ||--o{ MENU : "has"
    RESTAURANT_TABLE ||--o{ ORDERS : "books"
    ORDERS ||--o{ ORDER_ITEMS : "contains"
    MENU ||--o{ ORDER_ITEMS : "includes"
```

## 🚀 Quick Start

### Prerequisites
- ☕ **Java 8+** installed
- 🗄️ **MySQL Server** running
- 📁 **MySQL Connector JAR** (included in `/lib`)

### Installation Steps

```bash
# 1️⃣ Clone or download the project
git clone <repository-url>
cd restaurant-system

# 2️⃣ Setup MySQL Database
mysql -u root -p < db/rst_schema.sql

# 3️⃣ Configure database connection (if needed)
# Edit DatabaseConnection.java with your MySQL credentials

# 4️⃣ Run the application
./run.bat
```

### 🎮 One-Click Setup
```bash
# Windows Users - Just double-click!
setup_db.bat    # Sets up database
run.bat         # Launches application
```

## 📱 User Interface Preview

```
╔══════════════════════════════════════════════════════════════╗
║                 AANCHAL'S MULTICUISINE RESTAURANT            ║
╠══════════════════════════════════════════════════════════════╣
║  🍽️ Display Area                    │  🎛️ Control Panel      ║
║  ┌─────────────────────────────────┐ │  ┌─────────────────┐  ║
║  │ === WELCOME MESSAGE ===         │ │  │ Show Menu       │  ║
║  │                                 │ │  │ Book Table      │  ║
║  │ 🏪 Aanchal's Restaurant         │ │  │ Active Orders   │  ║
║  │ A Culinary Journey Around       │ │  │ Add Items       │  ║
║  │ the World                       │ │  │ Final Billing   │  ║
║  │                                 │ │  │ Admin Panel     │  ║
║  │ 🍛 Our Signature Cuisines:      │ │  │ Exit            │  ║
║  │ • Punjabi Delicacies           │ │  └─────────────────┘  ║
║  │ • South Indian Flavors         │ │                      ║
║  │ • Chinese Specialties          │ │                      ║
║  │ • Fresh Beverages & Desserts   │ │                      ║
║  └─────────────────────────────────┘ │                      ║
╚══════════════════════════════════════════════════════════════╝
```

## 🎯 Usage Guide

### 👨‍💼 For Restaurant Staff

1. **📋 View Menu**
   ```
   Click "Show Menu" → Browse all available items by category
   ```

2. **🪑 Take Orders**
   ```
   Click "Book Table & Order" → Select table → Add items → Confirm
   ```

3. **➕ Add More Items**
   ```
   Click "Add Items to Order" → Select existing table → Add items
   ```

4. **💰 Generate Bill**
   ```
   Click "Final Billing" → Select table → Print/View bill
   ```

### 👨‍💻 For Administrators

1. **🔐 Admin Login**
   ```
   Click "Admin Login" → Enter credentials → Access admin panel
   ```

2. **📝 Edit Menu**
   ```
   Admin Panel → Edit Menu → Add/Remove/Modify items
   ```

## 📊 Performance Metrics

```mermaid
pie title Restaurant System Performance
    "Order Processing" : 35
    "Menu Management" : 25
    "Database Operations" : 20
    "User Interface" : 15
    "Authentication" : 5
```

## 🔧 Technical Specifications

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Frontend** | Java Swing | Modern GUI Interface |
| **Backend** | Java SE | Business Logic |
| **Database** | MySQL 8.0+ | Data Persistence |
| **Architecture** | MVC Pattern | Clean Code Structure |
| **Authentication** | Custom Auth | Secure Admin Access |
| **Build Tool** | Batch Scripts | Easy Deployment |

## 📁 Project Structure

```
restaurant-system/
├── 📂 src/                    # Source code
│   ├── 🎯 Rst.java           # Main application
│   ├── 🍽️ MenuItem.java      # Menu item model
│   ├── 📋 OrderManager.java   # Order processing
│   ├── 🔐 AdminAuth.java     # Authentication
│   ├── 🗄️ DatabaseConnection.java
│   └── 📱 *Dialog.java       # UI dialogs
├── 📂 db/                     # Database files
│   └── 🗄️ rst_schema.sql     # Database schema
├── 📂 lib/                    # Dependencies
│   └── 📦 mysql-connector-j-9.4.0/
├── 🚀 run.bat                # Launch script
├── ⚙️ setup_db.bat           # Database setup
└── 📖 README.md              # This file
```

## 🌟 Feature Highlights

### 🎨 **Modern UI Design**
- Clean, intuitive interface
- Color-coded buttons for easy navigation
- Responsive layout with proper spacing
- Professional restaurant branding

### 🔒 **Security Features**
- Admin authentication system
- Secure database connections
- Input validation and sanitization
- Session management

### 📊 **Real-time Operations**
- Live order tracking
- Instant menu updates
- Dynamic table management
- Real-time billing calculations

### 🍽️ **Multi-Cuisine Support**
- Categorized menu system
- Flexible item management
- Price management
- Description and details

## 🚀 Advanced Features

```mermaid
mindmap
  root((🏪 Restaurant System))
    🔐 Security
      Admin Authentication
      Data Validation
      Secure Connections
    📊 Analytics
      Order Tracking
      Sales Reports
      Performance Metrics
    🎨 UI/UX
      Modern Design
      Intuitive Navigation
      Responsive Layout
    🗄️ Database
      MySQL Integration
      CRUD Operations
      Data Integrity
```

## 🛠️ Development Setup

### For Developers

1. **Environment Setup**
   ```bash
   # Install Java Development Kit
   java -version  # Verify Java 8+
   
   # Install MySQL Server
   mysql --version  # Verify MySQL installation
   ```

2. **IDE Configuration**
   ```bash
   # Add MySQL Connector to classpath
   # Configure build path with lib/mysql-connector-j-9.4.0.jar
   ```

3. **Database Configuration**
   ```sql
   -- Create database and user
   CREATE DATABASE restaurant;
   CREATE USER 'restaurant_user'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON restaurant.* TO 'restaurant_user'@'localhost';
   ```

## 🎯 Future Enhancements

- 📱 **Mobile App Integration**
- 🌐 **Web-based Interface**
- 📊 **Advanced Analytics Dashboard**
- 💳 **Payment Gateway Integration**
- 🔔 **Real-time Notifications**
- 📈 **Inventory Management**
- 👥 **Customer Management System**
- 🎁 **Loyalty Program**

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. 🍴 Fork the repository
2. 🌿 Create a feature branch
3. 💻 Make your changes
4. ✅ Test thoroughly
5. 📝 Submit a pull request

## 📞 Support

Need help? We've got you covered!

- 📧 **Email**: support@aanchals-restaurant.com
- 📱 **Phone**: +1-234-567-8900
- 🌐 **Website**: www.aanchals-restaurant.com
- 💬 **Chat**: Available 24/7

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

### 🌟 **Made with ❤️ for Aanchal's Restaurant** 🌟

**Experience the Future of Restaurant Management**

[![⭐ Star this repo](https://img.shields.io/badge/⭐-Star_this_repo-yellow?style=for-the-badge)](https://github.com/your-repo)
[![🍴 Fork](https://img.shields.io/badge/🍴-Fork-blue?style=for-the-badge)](https://github.com/your-repo/fork)
[![📥 Download](https://img.shields.io/badge/📥-Download-green?style=for-the-badge)](https://github.com/your-repo/archive/main.zip)

*Transforming dining experiences, one order at a time* 🍽️✨

</div>