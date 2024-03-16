--liquibase formatted sql
-- ****************************************************
-- Create Table: links
-- Author: lzbkl
-- Date: 03/11/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS links (
    link_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    url TEXT NOT NULL UNIQUE
);
