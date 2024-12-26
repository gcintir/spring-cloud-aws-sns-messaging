package org.projects.amazon.sns.messaging;

import java.util.Objects;

public class UserAccountMessage {
    private String userId;
    private String status; // CREATED, REMOVED

    public UserAccountMessage() {
    }

    public UserAccountMessage(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserAccountMessage{" +
                "userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountMessage that = (UserAccountMessage) o;
        return Objects.equals(userId, that.userId) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, status);
    }
}
