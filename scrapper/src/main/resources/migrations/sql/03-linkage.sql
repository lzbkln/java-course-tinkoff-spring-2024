--liquibase formatted sql
-- ****************************************************
-- Create Table: linkage_table
-- Author: lzbkl
-- Date: 03/11/2024
-- ****************************************************

CREATE TABLE IF NOT EXISTS linkage (
    chat_id BIGINT,
    link_id BIGINT,

    PRIMARY KEY (chat_id, link_id),
    CONSTRAINT fk_chat_id FOREIGN KEY (chat_id) REFERENCES chats (id),
    CONSTRAINT fk_link_id FOREIGN KEY (link_id) REFERENCES links (id)
    );
