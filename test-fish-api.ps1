# PowerShell script to test the Fish API endpoints

$baseUrl = "http://localhost:8080/api/v1"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Testing Fish API Endpoints" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "1. Testing GET /api/v1/fish (List all fish)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/fish" -Method Get
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "2. Testing GET /api/v1/fish/1 (Get fish by ID)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/fish/1" -Method Get
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "3. Testing POST /api/v1/fish (Create new fish)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    $body = @{
        id = 0
        name = "Betta"
        scientificName = "Betta splendens"
        imageUrl = "https://example.com/betta.jpg"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "$baseUrl/fish" -Method Post -Body $body -ContentType "application/json"
    Write-Host "Created fish with ID: $response" -ForegroundColor Green
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "4. Testing GET /api/v1/fish (List all fish after creation)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/fish" -Method Get
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "5. Testing PUT /api/v1/fish/4 (Update fish)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    $body = @{
        id = 4
        name = "Betta Updated"
        scientificName = "Betta splendens"
        imageUrl = "https://example.com/betta-updated.jpg"
    } | ConvertTo-Json
    
    Invoke-RestMethod -Uri "$baseUrl/fish/4" -Method Put -Body $body -ContentType "application/json"
    Write-Host "Fish updated successfully" -ForegroundColor Green
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "6. Testing GET /api/v1/fish/4 (Get updated fish)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/fish/4" -Method Get
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "7. Testing DELETE /api/v1/fish/4 (Delete fish)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/fish/4" -Method Delete
    Write-Host "Fish deleted successfully" -ForegroundColor Green
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "8. Testing GET /api/v1/fish (List all fish after deletion)" -ForegroundColor Yellow
Write-Host "------------------------------------------" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/fish" -Method Get
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "All tests completed!" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

