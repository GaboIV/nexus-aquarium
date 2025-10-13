#!/bin/bash

# Script to test the Fish API endpoints

BASE_URL="http://localhost:8080/api/v1"

echo "=========================================="
echo "Testing Fish API Endpoints"
echo "=========================================="
echo ""

echo "1. Testing GET /api/v1/fish (List all fish)"
echo "------------------------------------------"
curl -s -X GET "$BASE_URL/fish" | json_pp || curl -s -X GET "$BASE_URL/fish"
echo ""
echo ""

echo "2. Testing GET /api/v1/fish/1 (Get fish by ID)"
echo "------------------------------------------"
curl -s -X GET "$BASE_URL/fish/1" | json_pp || curl -s -X GET "$BASE_URL/fish/1"
echo ""
echo ""

echo "3. Testing POST /api/v1/fish (Create new fish)"
echo "------------------------------------------"
curl -s -X POST "$BASE_URL/fish" \
  -H "Content-Type: application/json" \
  -d '{"id":0,"name":"Betta","scientificName":"Betta splendens","imageUrl":"https://example.com/betta.jpg"}'
echo ""
echo ""

echo "4. Testing GET /api/v1/fish (List all fish after creation)"
echo "------------------------------------------"
curl -s -X GET "$BASE_URL/fish" | json_pp || curl -s -X GET "$BASE_URL/fish"
echo ""
echo ""

echo "5. Testing PUT /api/v1/fish/4 (Update fish)"
echo "------------------------------------------"
curl -s -X PUT "$BASE_URL/fish/4" \
  -H "Content-Type: application/json" \
  -d '{"id":4,"name":"Betta Updated","scientificName":"Betta splendens","imageUrl":"https://example.com/betta-updated.jpg"}'
echo ""
echo ""

echo "6. Testing GET /api/v1/fish/4 (Get updated fish)"
echo "------------------------------------------"
curl -s -X GET "$BASE_URL/fish/4" | json_pp || curl -s -X GET "$BASE_URL/fish/4"
echo ""
echo ""

echo "7. Testing DELETE /api/v1/fish/4 (Delete fish)"
echo "------------------------------------------"
curl -s -X DELETE "$BASE_URL/fish/4"
echo ""
echo ""

echo "8. Testing GET /api/v1/fish (List all fish after deletion)"
echo "------------------------------------------"
curl -s -X GET "$BASE_URL/fish" | json_pp || curl -s -X GET "$BASE_URL/fish"
echo ""
echo ""

echo "=========================================="
echo "All tests completed!"
echo "=========================================="

