# Persons Finder API

A Spring Boot application that helps you manage person profiles and find people nearby using geospatial queries. It features an AI-powered bio generator using Google's Gemini API.

## Features

*   **Create Person**: Create a profile with a name, job title, and hobbies. The system automatically generates a quirky bio using AI.
*   **Update Location**: Update a person's real-time location (latitude/longitude).
*   **Find Nearby**: Find all people within a specific radius (in meters) of a given location.
*   **Geospatial Support**: Uses PostgreSQL with PostGIS for efficient spatial indexing and querying.

## Prerequisites

*   [Docker](https://www.docker.com/get-started)
*   [Docker Compose](https://docs.docker.com/compose/install/)
*   A Google Gemini API Key (Get one [here](https://makersuite.google.com/app/apikey))

## How to Run

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd persons-finder
    ```

2.  **Set your Gemini API Key:**
    You need to export your API key as an environment variable so Docker can pick it up.
    ```bash
    export GEMINI_API_KEY=your_actual_api_key_here
    ```

3.  **Start the application:**
    Run the following command to build the app and start the database and application containers.
    ```bash
    docker-compose up --build
    ```
    *Note: If this is your first time running it, or if you have database schema errors, run `docker-compose down -v` first to reset the database volume.*

4.  **Access the Application:**
    *   **API Base URL:** `http://localhost:8080`
    *   **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (Interactive API documentation)

## API Endpoints

### 1. Create a Person
Creates a new person and generates an AI bio.

*   **Endpoint:** `POST /api/v1/persons`
*   **Body:**
    ```json
    {
      "name": "Alice Smith",
      "jobTitle": "Software Engineer",
      "hobbies": ["Hiking", "Chess"],
      "location": {
        "latitude": 40.7128,
        "longitude": -74.0060
      }
    }
    ```
*   **Curl:**
    ```bash
    curl -X POST http://localhost:8080/api/v1/persons \
      -H "Content-Type: application/json" \
      -d '{
        "name": "Alice Smith",
        "jobTitle": "Software Engineer",
        "hobbies": ["Hiking", "Chess"],
        "location": { "latitude": 40.7128, "longitude": -74.0060 }
      }'
    ```

### 2. Update Location
Updates the location for an existing person.

*   **Endpoint:** `PUT /api/v1/persons/{id}/location`
*   **Body:**
    ```json
    {
      "latitude": 34.0522,
      "longitude": -118.2437
    }
    ```
*   **Curl:**
    ```bash
    # Replace {id} with the actual ID returned from the creation step
    curl -X PUT http://localhost:8080/api/v1/persons/1/location \
      -H "Content-Type: application/json" \
      -d '{ "latitude": 34.0522, "longitude": -118.2437 }'
    ```

### 3. Find People Nearby
Finds people within a specified radius (in meters).

*   **Endpoint:** `GET /api/v1/persons/nearby`
*   **Parameters:**
    *   `latitude`: Query latitude
    *   `longitude`: Query longitude
    *   `radius`: Search radius in meters
*   **Curl:**
    ```bash
    curl "http://localhost:8080/api/v1/persons/nearby?latitude=40.7128&longitude=-74.0060&radius=5000"
    ```
