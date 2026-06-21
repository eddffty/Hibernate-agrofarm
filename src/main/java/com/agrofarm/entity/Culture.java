package com.agrofarm.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "culture", schema = "agrofarm")
public class Culture {

    public Culture() {
    }

    public Culture(String name, String lightSpectre, Integer wateringFrequency, String compositionSolution) {
        this.name = name;
        this.lightSpectre = lightSpectre;
        if (wateringFrequency <= 0) {
            throw new IllegalArgumentException("Частота полива должна быть больше нуля!");
        }
        this.wateringFrequency = wateringFrequency;
        this.compositionSolution = compositionSolution;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    @Column(nullable = false, unique = true, length = 32)
    private String name;


    @Column(name = "light_spectre", nullable = false, length = 32)
    private String lightSpectre;

    @Column(name = "watering_frequency", columnDefinition = "INTEGER CHECK (watering_frequency > 0)", nullable = false)
    private Integer wateringFrequency;

    @Column(name = "composition_solution", nullable = false)
    private String compositionSolution;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLightSpectre() {
        return lightSpectre;
    }

    public void setLightSpectre(String lightSpectre) {
        this.lightSpectre = lightSpectre;
    }

    public Integer getWateringFrequency() {
        return wateringFrequency;
    }

    public void setWateringFrequency(Integer wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }

    public String getCompositionSolution() {
        return compositionSolution;
    }

    public void setCompositionSolution(String compositionSolution) {
        this.compositionSolution = compositionSolution;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Culture culture)) return false;
        return Objects.equals(name, culture.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Culture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lightSpectre='" + lightSpectre + '\'' +
                ", wateringFrequency=" + wateringFrequency +
                ", compositionSolution='" + compositionSolution + '\'' +
                '}';
    }
}
