--liquibase formatted sql
-- ****************************************************
-- Create Table: github_branches
-- Author: lzbkl
-- Date: 03/17/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS github_branches (
    link_id BIGINT REFERENCES links (id),
    branches TEXT ARRAY NOT NULL
);
