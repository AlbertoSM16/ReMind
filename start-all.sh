#!/bin/bash

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== 1. MySQL ==="
docker compose up -d
echo "MySQL listo en localhost:3306"

echo ""
echo "=== 2. Backend ==="
cd "$ROOT_DIR/back-end/back"
./mvnw spring-boot:run > /tmp/backend.log 2>&1 &
BACK_PID=$!
echo "Backend PID: $BACK_PID (log: /tmp/backend.log)"

echo ""
echo "=== 3. Frontend ==="
cd "$ROOT_DIR/frontend/ReMind"
./start.sh > /tmp/frontend.log 2>&1 &
FRONT_PID=$!
echo "Frontend PID: $FRONT_PID (log: /tmp/frontend.log)"

echo ""
echo "=== LISTO ==="
echo "Frontend: http://localhost:4200"
echo "Backend:  http://localhost:8080"
echo ""
echo "Para detener:"
echo "  kill $BACK_PID $FRONT_PID"
echo "  docker compose down"
