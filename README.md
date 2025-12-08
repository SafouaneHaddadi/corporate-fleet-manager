# Corporate Fleet Manager

Simple company vehicle management system.

## Features
- Employees: view available vehicles and make bookings
- Fleet manager: add/edit vehicles, approve bookings, record maintenance
- Two interfaces:
  - Back-office → Servlets + JSP
  - REST API → JAX-RS

## Main entities
- User (employee/manager)
- Vehicle
- Reservation
- Maintenance

## Technologies
- Jakarta EE 10
- WildFly
- Oracle Database
- Hibernate + Maven
- Servlet/JSP + JAX-RS

## How to run

```bash
# 1. Clone the project
git clone https://github.com/SafouaneHaddadi/corporate-fleet-manager.git
cd corporate-fleet-manager

# 2. Open in IntelliJ IDEA → Maven → Reload project

# 3. Run on WildFly
- Add WildFly server in IntelliJ 
- Deploy "corporate-fleet-manager:war exploded"
- Application context: /corporate-fleet-manager

# 4. Open browser
http://localhost:8180/corporate-fleet-manager/
