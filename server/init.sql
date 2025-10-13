-- Script de inicialización para PostgreSQL
-- Crea la base de datos y las tablas necesarias

-- Crear la base de datos si no existe
CREATE DATABASE nexus_aquarium_db;

-- Conectar a la base de datos
\c nexus_aquarium_db;

-- Crear tabla de peces
CREATE TABLE IF NOT EXISTS fish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    scientific_name VARCHAR(255),
    family VARCHAR(255),
    size_cm INTEGER,
    temperature_min INTEGER,
    temperature_max INTEGER,
    ph_min DECIMAL(3,1),
    ph_max DECIMAL(3,1),
    hardness_min INTEGER,
    hardness_max INTEGER,
    temperament VARCHAR(100),
    diet VARCHAR(255),
    origin VARCHAR(255),
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo
INSERT INTO fish (name, scientific_name, family, size_cm, temperature_min, temperature_max, ph_min, ph_max, temperament, diet, origin, description) VALUES
('Guppy', 'Poecilia reticulata', 'Poeciliidae', 4, 22, 28, 6.5, 8.5, 'Pacífico', 'Omnívoro', 'América del Sur', 'Pez muy popular y fácil de mantener. Ideal para principiantes.'),
('Neón Tetra', 'Paracheirodon innesi', 'Characidae', 3, 20, 26, 5.0, 7.0, 'Pacífico', 'Omnívoro', 'América del Sur', 'Pez pequeño y colorido, perfecto para acuarios comunitarios.'),
('Betta', 'Betta splendens', 'Osphronemidae', 6, 24, 30, 6.0, 8.0, 'Agresivo', 'Carnívoro', 'Asia', 'Pez solitario con colores vibrantes. No debe mantenerse con otros bettas.'),
('Corydoras', 'Corydoras paleatus', 'Callichthyidae', 6, 20, 26, 6.0, 8.0, 'Pacífico', 'Omnívoro', 'América del Sur', 'Pez de fondo muy activo y sociable.'),
('Molly', 'Poecilia sphenops', 'Poeciliidae', 8, 22, 28, 7.0, 8.5, 'Pacífico', 'Omnívoro', 'América Central', 'Pez resistente y prolífico, ideal para acuarios comunitarios.');

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_fish_name ON fish(name);
CREATE INDEX IF NOT EXISTS idx_fish_scientific_name ON fish(scientific_name);
CREATE INDEX IF NOT EXISTS idx_fish_family ON fish(family);

-- Crear función para actualizar updated_at automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Crear trigger para actualizar updated_at
CREATE TRIGGER update_fish_updated_at 
    BEFORE UPDATE ON fish 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
