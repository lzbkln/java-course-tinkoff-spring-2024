--liquibase formatted sql
-- ****************************************************
-- Create Table: chats
-- Author: lzbkl
-- Date: 03/11/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS chats (
    chat_id BIGINT NOT NULL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);
