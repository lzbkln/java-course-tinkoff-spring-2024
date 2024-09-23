--liquibase formatted sql
-- ****************************************************
-- Create Table: github_branches
-- Author: lzbkl
-- Date: 03/17/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS github_branches (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    link_id BIGINT REFERENCES links (id),
    branches VARCHAR(255) ARRAY NOT NULL
);
