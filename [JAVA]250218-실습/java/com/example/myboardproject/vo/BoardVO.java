package com.example.myboardproject.vo;

import java.sql.Timestamp;

public class BoardVO {
    private int id;
    private String title;
    private String content;
    private Timestamp createdAt;

    public BoardVO() {
    }

    public BoardVO(int id, String title, String content, Timestamp createdAt, Timestamp updatedAt, Timestamp deletedAt, MemberVO writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.writer = writer;
    }

    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private MemberVO writer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public MemberVO getWriter() {
        return writer;
    }

    public void setWriter(MemberVO writer) {
        this.writer = writer;
    }
}
