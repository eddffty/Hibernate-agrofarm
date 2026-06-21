package com.agrofarm.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "equipment", schema = "agrofarm")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "change_history", columnDefinition = "TEXT")
    private String changeHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    public Equipment() {
    }

    public Equipment(String changeHistory) {
        this.changeHistory = changeHistory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChangeHistory() {
        return changeHistory;
    }

    public void setChangeHistory(String changeHistory) {
        this.changeHistory = changeHistory;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Equipment equipment)) return false;
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", changeHistory='" + changeHistory + '\'' +
                '}';
    }
}



