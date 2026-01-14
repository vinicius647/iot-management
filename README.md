## Reference Documentation

- The iot-management project is the Warehouse Service, which will receive, process, and send the sensor's data to a Rabbitmq Queue.
- There is a module central-service that is the Central Monitoring Service, which will read the data from the Rabbitmq Queue and check the thresholds configured for each sensor.
- There is a file sensor-ports.json that contains the mapping of sensor types to their respective ports.

## Setup Instructions
- Start a Rabbitmq server using the docker-compose.yml placed in the root directory of the project:
  ```bash
  docker-compose up -d
  ```

## Technologies Used
- Spring Boot
- RabbitMQ
- Java 17
