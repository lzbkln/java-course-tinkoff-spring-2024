--liquibase formatted sql
-- ****************************************************
-- Create Table: stackoverflow_question
-- Author: lzbkl
-- Date: 03/17/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS stackoverflow_question (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    link_id BIGINT REFERENCES links (id),
    answer_count BIGINT NOT NULL
);
