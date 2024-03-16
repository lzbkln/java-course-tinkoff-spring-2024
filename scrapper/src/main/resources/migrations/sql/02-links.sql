--liquibase formatted sql
-- ****************************************************
-- Create Table: links
-- Author: lzbkl
-- Date: 03/11/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS links (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    url TEXT NOT NULL UNIQUE,
    last_updated_at TIMESTAMP WITH TIME ZONE
);
