package com.ufcg.booker.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ad")
public class Advertisement {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    private Long bookId;
    private String description;
    private boolean active;
    private boolean borrowed;

    @Deprecated
    protected Advertisement(){}

    public Advertisement(User user, Long bookId, String adDescription){
        this.user = user;
        this.bookId = bookId;
        this.description = adDescription;
        this.active = true;
        this.borrowed = false;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getDescription() {
        return description;
    }
    public boolean isActive() {
        return active;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advertisement that = (Advertisement) o;
        return active == that.active && borrowed == that.borrowed && id.equals(that.id) && user.equals(that.user) && bookId.equals(that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, bookId, active, borrowed);
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", user=" + user +
                ", idBook='" + bookId + '\'' +
                ", active=" + active +
                ", borrowed=" + borrowed +
                '}';
    }
}
