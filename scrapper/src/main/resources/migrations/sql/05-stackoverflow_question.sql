--liquibase formatted sql
-- ****************************************************
-- Create Table: stackoverflow_question
-- Author: lzbkl
-- Date: 03/18/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS stackoverflow_question (
    link_id BIGINT,
    answer_count BIGINT NOT NULL,

    CONSTRAINT fk_link_id FOREIGN KEY (link_id) REFERENCES links (id)
);
