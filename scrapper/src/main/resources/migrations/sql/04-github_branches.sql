--liquibase formatted sql
-- ****************************************************
-- Create Table: github_branches
-- Author: lzbkl
-- Date: 03/18/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS github_branches (
    link_id BIGINT,
    branches TEXT[] NOT NULL,

    CONSTRAINT fk_link_id FOREIGN KEY (link_id) REFERENCES links (id)
);
