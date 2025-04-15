# Spring Boot GraphQL Batch Loader Demo

*This project was fully developed by Augment Agent AI based on user requirements and instructions*

A demonstration of efficient GraphQL implementation in Spring Boot using batch loading to solve the N+1 query problem.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Data Model](#data-model)
- [GraphQL Schema](#graphql-schema)
- [Batch Loading](#batch-loading)
- [Query Examples](#query-examples)
- [Setup and Running](#setup-and-running)
- [Technologies Used](#technologies-used)

## Overview

This project demonstrates how to implement an efficient GraphQL API using Spring Boot and batch loading. It showcases a multi-level object hierarchy with departments, employees, tasks, and categories, and demonstrates how to avoid the N+1 query problem using batch loading techniques.

## Architecture

The application follows a layered architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                      GraphQL API Layer                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │Query Resolver│  │Batch Mapping│  │Schema Configuration │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                       Service Layer                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │Department   │  │Employee     │  │Task & Category      │  │
│  │Service      │  │Service      │  │Services             │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                    Repository Layer                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │JPA          │  │Custom Query │  │Filtering &          │  │
│  │Repositories │  │Methods      │  │Pagination           │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                      Data Layer                             │
│  ┌─────────────────────────────────────────────────────┐    │
│  │                 H2 In-Memory Database               │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## Features

- **GraphQL API** with nested object queries
- **Batch loading** to solve the N+1 query problem
- **Filtering** by various criteria (name, status, etc.)
- **Pagination** support
- **In-memory H2 database** with JPA entities
- **Multi-level object hierarchy** (4 levels deep)

## Data Model

The application models a simple organizational structure:

```
┌───────────────┐       ┌───────────────┐       ┌───────────────┐       ┌───────────────┐
│  Department   │       │   Employee    │       │     Task      │       │   Category    │
├───────────────┤       ├───────────────┤       ├───────────────┤       ├───────────────┤
│ id            │       │ id            │       │ id            │       │ id            │
│ name          │◄──────┤ departmentId  │◄──────┤ employeeId    │       │ name          │
│ location      │  1:N  │ name          │  1:N  │ title         │       │ description   │
└───────────────┘       │ email         │       │ description   │       └───────┬───────┘
                        │ position      │       │ status        │               │
                        └───────────────┘       │ dueDate       │◄──────────────┘
                                                │ categoryId    │       1:N
                                                └───────────────┘
```

## GraphQL Schema

The GraphQL schema defines types for Department, Employee, Task, and Category, along with queries that support filtering and pagination:

```graphql
type Query {
  departments(name: String, location: String, page: Int, size: Int): [Department]
  department(id: ID!): Department
  employees(name: String, position: String, departmentId: ID, page: Int, size: Int): [Employee]
  employee(id: ID!): Employee
  tasks(title: String, status: String, employeeId: ID, categoryId: ID, page: Int, size: Int): [Task]
  task(id: ID!): Task
  categories(name: String, page: Int, size: Int): [Category]
  category(id: ID!): Category
}

type Department {
  id: ID!
  name: String!
  location: String
  employees: [Employee]
}

type Employee {
  id: ID!
  name: String!
  email: String!
  position: String
  department: Department
  tasks: [Task]
}

type Task {
  id: ID!
  title: String!
  description: String
  status: String!
  dueDate: String
  employee: Employee
  category: Category
}

type Category {
  id: ID!
  name: String!
  description: String
  tasks: [Task]
}
```

## Batch Loading

The application uses Spring GraphQL's @BatchMapping annotation to efficiently load related data:

```
┌─────────────────┐                 ┌─────────────────┐
│  GraphQL Query  │                 │  Database       │
│  (Multiple      │                 │                 │
│   Entities)     │                 │                 │
└────────┬────────┘                 └────────┬────────┘
         │                                   │
         │ Request                           │
         ▼                                   │
┌─────────────────┐                          │
│  Batch Loader   │                          │
│  Collects IDs   │                          │
└────────┬────────┘                          │
         │                                   │
         │ Single Query with                 │
         │ Multiple IDs                      │
         ▼                                   ▼
┌─────────────────┐                 ┌─────────────────┐
│  Repository     │◄────────────────┤  Execute Single │
│  Layer          │  Return Results │  Batch Query    │
└────────┬────────┘                 └─────────────────┘
         │
         │ Map Results to
         │ Original Entities
         ▼
┌─────────────────┐
│  Return Data    │
│  to Client      │
└─────────────────┘
```

Instead of executing N+1 queries (1 for the parent collection + N for each child relationship), batch loading collects all IDs and makes a single query to fetch all related entities at once.

## Query Examples

### Basic Query with Filtering

```graphql
query {
  departments(name: "Engineering") {
    id
    name
    location
    employees {
      id
      name
      position
    }
  }
}
```

### Query with Pagination

```graphql
query {
  employees(page: 0, size: 2) {
    id
    name
    position
    department {
      name
    }
  }
}
```

### Multi-level Query with Filtering

```graphql
query {
  tasks(status: "IN_PROGRESS", employeeId: 1) {
    id
    title
    status
    employee {
      name
    }
    category {
      name
    }
  }
}
```

### Deep Nested Query

```graphql
query {
  departments {
    id
    name
    employees {
      id
      name
      tasks {
        id
        title
        category {
          id
          name
        }
      }
    }
  }
}
```

## Setup and Running

### Prerequisites

- Java 17 or higher
- Gradle

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```
   ./gradlew bootRun
   ```
4. Access GraphiQL at http://localhost:8080/graphiql
5. Access H2 Console at http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb, Username: sa, Password: empty)

## Technologies Used

- **Spring Boot 3.4.4**: Application framework
- **Spring GraphQL**: GraphQL implementation
- **Spring Data JPA**: Data access layer
- **H2 Database**: In-memory database
- **Lombok**: Reduces boilerplate code
- **GraphiQL**: Interactive GraphQL IDE

## Credits

This project was fully designed and implemented by Augment Agent AI based on user requirements and guidance. It demonstrates how AI can independently develop complete, efficient, and well-structured Spring Boot applications with advanced features like GraphQL and batch loading with minimal human intervention.
