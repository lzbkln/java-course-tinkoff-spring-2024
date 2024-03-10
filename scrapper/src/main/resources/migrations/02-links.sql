--liquibase formatted sql
-- ****************************************************
-- Create Table: links
-- Author: lzbkl
-- Date: 03/11/2024
-- ****************************************************

CREATE TABLE links (
    link_id BIGSERIAL PRIMARY KEY,
    url TEXT NOT NULL UNIQUE
);
