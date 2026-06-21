package com.agrofarm.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shelf", schema = "agrofarm")
public class Shelf {

    protected Shelf(){}

    public Shelf(String cultureName, Integer level) {
        this.cultureName = cultureName;
        this.level = level;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "culture_name", nullable = false)
    private String cultureName;

    @Column(nullable = false)
    private Integer level;

    @OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Culture> cultures = new ArrayList<>();


    @OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cell> cells = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCultureName() {
        return cultureName;
    }

    public void setCultureName(String cultureName) {
        this.cultureName = cultureName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Culture> getCultures() {
        return cultures;
    }

    public void setCultures(List<Culture> cultures) {
        this.cultures = cultures;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shelf s)) return false;
        return Objects.equals(id, s.id);
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "id=" + id +
                ", cultureName='" + cultureName + '\'' +
                ", level=" + level +
                '}';
    }
}
