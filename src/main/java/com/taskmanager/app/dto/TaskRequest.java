// DTO for creating/updating a task
package com.taskmanager.app.dto;

public class TaskRequest {
    private String title;
    private String description;
    private boolean completed;
    private Long userId;  // to associate task with user

    public TaskRequest() {}

    public TaskRequest(String title, String description, boolean completed, Long userId) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.userId = userId;
    }
        public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
