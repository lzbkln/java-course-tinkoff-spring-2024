--liquibase formatted sql
-- ****************************************************
-- Create Table: linkage
-- Author: lzbkl
-- Date: 03/11/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS linkage (
    chat_id BIGINT REFERENCES chats (id),
    link_id BIGINT REFERENCES links (id),

    PRIMARY KEY (chat_id, link_id)
    );
