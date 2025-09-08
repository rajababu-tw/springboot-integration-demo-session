This is a basic demo project (User Management) to showcase how different commonly used technology is integrated with springboot application.This is just for development purpose.
1.Run the docker-compose-up -d command first , it will start kafka, redis and postgres.
2.Run the application.
3.Run the notification service application -> https://github.com/rajababu-tw/springboot-integration-demo-notification-service
4.Sample API request for user creation :
      curl --location 'http://localhost:8080/api/users' \
      --header 'Content-Type: application/json' \
      --data-raw '{"name":"PawanKalyan1","email":"pawankalyan1@example.com"}'
