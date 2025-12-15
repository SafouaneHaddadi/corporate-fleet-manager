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

## Local Development Setup (Docker)

To run and test the application locally (identical to school environment):

```bash
# 1. Clone the project
git clone https://github.com/SafouaneHaddadi/corporate-fleet-manager.git
cd corporate-fleet-manager

# 2. Build the application
mvn clean package

# 3. Start containers (Oracle + WildFly)
docker-compose up -d

# 4. Access the application
http://localhost:8180/
```

## Context

**Academic project** for the "Applications Informatiques 3" course (Jakarta EE module) at HEPH-Condorcet.
