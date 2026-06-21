package com.agrofarm.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cell", schema = "agrofarm")
public class Cell {

    public Cell (){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "INTEGER CHECK (capacity > 0)")
    private Integer capacity;

    @OneToMany(mappedBy = "cell", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Sensor> sensors = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    public Cell(Integer capacity) {
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Вместимость ячейки должна быть больше нуля!");
        }
        this.capacity = capacity;
    }


    public Integer getId() {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell cell)) return false;
        return Objects.equals(id, cell.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "capacity=" + capacity +
                ", id=" + id +
                '}';
    }
}
