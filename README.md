# WebTechnico API

## Description
WebTechnico is a property management system built with Jakarta EE. The API provides endpoints for managing properties, property owners, and property repairs.

## Technologies Used
- **Jakarta EE**: For building the backend application.
- **JPA (Jakarta Persistence API)**: For database interaction and ORM.
- **Hibernate**: As the JPA provider for managing database operations.
- **MySQL**: As the relational database management system.
- **RESTful Web Services**: For exposing the APIs.
- **Maven**: For project build and dependency management.
- **Lombok**: For reducing boilerplate code with annotations.
- **Jackson**: For JSON serialization and deserialization.

## Installation and Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/mpapangelis/web-technico-app.git
   ```
2. Navigate to the project directory:
   ```bash
   cd web-technico-app
   ```
3. Deploy the application to your Jakarta EE server (e.g., WildFly).

4. Start your Jakarta EE server.

5. Access the application at `http://localhost:8080/Technico`.


## API Endpoints

### Property Endpoints

#### PropertyResource

- **POST** `/property/create`
  - Create a new property.

- **PUT** `/property/update/{id}`
  - Update an existing property by ID.

- **GET** `/property/{id}`
  - Retrieve details of a property by ID.

- **GET** `/property/propertyId/{propertyId}`
  - Retrieve a property by its identification number.

- **GET** `/property/owner/vat/{vatNumber}`
  - Retrieve properties by the owner's VAT number.

- **GET** `/property/owner/id/{ownerId}`
  - Retrieve properties by the owner's ID.

- **DELETE** `/property/softDelete/{id}`
  - Soft delete a property by ID.

- **DELETE** `/property/hardDelete/{id}`
  - Hard delete a property by ID.

### PropertyOwner Endpoints

#### PropertyOwnerResource

- **POST** `/propertyOwner/create`
  - Create a new property owner.

- **PUT** `/propertyOwner/update/{id}`
  - Update an existing property owner by ID.

- **GET** `/propertyOwner/{id}`
  - Retrieve details of a property owner by ID.

- **GET** `/propertyOwner/findByEmail/{email}`
  - Retrieve a property owner by email.

- **GET** `/propertyOwner/findByVat/{vat}`
  - Retrieve a property owner by VAT number.

- **GET** `/propertyOwner/findAll`
  - Retrieve all property owners.

- **DELETE** `/propertyOwner/softDelete/{id}`
  - Soft delete a property owner by ID.

- **DELETE** `/propertyOwner/hardDelete/{id}`
  - Hard delete a property owner by ID.

### PropertyRepair Endpoints

#### PropertyRepairResource

- **POST** `/propertyRepair/initiate`
  - Initiate a new property repair.

- **PUT** `/propertyRepair/update/{repairId}`
  - Update an existing property repair by ID.

- **GET** `/propertyRepair/searchByDateRange`
  - Search property repairs by date range.

- **GET** `/propertyRepair/searchBySubmissionDate`
  - Search property repairs by submission date.

- **GET** `/propertyRepair/searchByOwnerId`
  - Search property repairs by owner's ID.

- **DELETE** `/propertyRepair/softDelete/{id}`
  - Soft delete a property repair by ID.

- **DELETE** `/propertyRepair/hardDelete/{id}`
  - Hard delete a property repair by ID.

### Admin Endpoints

#### AdminResource

- **GET** `/admin/repairs`
  - Retrieve all property repairs.

- **GET** `/admin/repairs/active`
  - Retrieve all active property repairs.

- **GET** `/admin/repairs/inactive`
  - Retrieve all inactive property repairs.

- **GET** `/admin/repairs/pending`
  - Retrieve all pending property repairs.

- **GET** `/admin/properties`
  - Retrieve all properties.

- **POST** `/admin/repairProposition/{repairId}`
  - Update a repair proposition.


