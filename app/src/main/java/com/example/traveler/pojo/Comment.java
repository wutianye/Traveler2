package com.example.traveler.pojo;

/**
 * Created by admin on 2019/4/15.
 */

public class Comment {
    private Long commentId;
    private String publisher;
    private String content;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment(Long commentId, String publisher, String content) {
        this.commentId = commentId;
        this.publisher = publisher;
        this.content = content;
    }
}
