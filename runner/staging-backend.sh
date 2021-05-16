#!/usr/bin/bash

# Run the staging backend server

fuser -k 9090/tcp || true
java -jar staging-backend/libs/backend-0.0.1-SNAPSHOT.jar --server.port=9090 --spring.profiles.active=dev
