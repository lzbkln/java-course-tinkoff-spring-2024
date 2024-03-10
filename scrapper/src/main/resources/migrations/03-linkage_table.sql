--liquibase formatted sql
-- ****************************************************
-- Create Table: linkage_table
-- Author: lzbkl
-- Date: 03/11/2024
-- ****************************************************

CREATE TABLE linkage_table (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT,
    link_id BIGINT,

    CONSTRAINT fk_chat_id FOREIGN KEY (chat_id) REFERENCES chat (chat_id),
    CONSTRAINT fk_link_id FOREIGN KEY (link_id) REFERENCES links (link_id)
);
