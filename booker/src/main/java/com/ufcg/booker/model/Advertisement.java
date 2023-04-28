package com.ufcg.booker.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ad")
public class Advertisement {

    @Id
    @GeneratedValue
    private Long id;
    private Long idUser;
    private Long idBook;
    private boolean active;
    private boolean borrowed;

    @Deprecated
    protected Advertisement(){}

    public Advertisement(Long idUser, Long idBook){
        this.idUser = idUser;
        this.idBook = idBook;
        this.active = true;
        this.borrowed = false;
    }

    public Long getId() {
        return id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdBook() {
        return idBook;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isBorrowed() {
        return borrowed;
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
        return active == that.active && borrowed == that.borrowed && id.equals(that.id) && idUser.equals(that.idUser) && idBook.equals(that.idBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUser, idBook, active, borrowed);
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idBook='" + idBook + '\'' +
                ", active=" + active +
                ", borrowed=" + borrowed +
                '}';
    }
}
