package com.example.traveler.pojo;

/**
 * Created by admin on 2019/4/15.
 */

public class Comment {
    private Long commentId;
    private User publisher;
    private String content;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment(Long commentId, User publisher, String content) {
        this.commentId = commentId;
        this.publisher = publisher;
        this.content = content;
    }
}
