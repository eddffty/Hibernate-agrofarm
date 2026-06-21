package com.agrofarm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sensor", schema = "agrofarm")
public class Sensor {

    public Sensor(){}

    public Sensor(String name, BigDecimal humidityLevel, BigDecimal temperature) {
        this(name, humidityLevel, temperature, BigDecimal.valueOf(6.50));
    }

    public Sensor(String name, BigDecimal humidityLevel, BigDecimal temperature, BigDecimal soilAcidityLevel) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название датчика должно быть указано!");
        }
        if (humidityLevel == null || humidityLevel.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Уровень влажности должен быть больше нуля!");
        }
        if (temperature == null || temperature.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Температура должна быть больше нуля!");
        }
        if (soilAcidityLevel == null || soilAcidityLevel.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Кислотность почвы должна быть больше нуля!");
        }
        this.name = name;
        this.humidityLevel = humidityLevel;
        this.temperature = temperature;
        this.soilAcidityLevel = soilAcidityLevel;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cell_id", nullable = false)
    private Cell cell;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Equipment> equipments = new ArrayList<>();

    @Column(nullable = false, unique = true, length = 32)
    private String name;

    @Column(name = "humidity_level", nullable = false, precision = 3, scale = 2)
    private BigDecimal humidityLevel;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal temperature;

    @Column(name = "soil_acidity_level", nullable = false, precision = 3, scale = 2)
    private BigDecimal soilAcidityLevel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public BigDecimal getHumidityLevel() {
        return humidityLevel;
    }

    public void setHumidityLevel(BigDecimal humidityLevel) {
        this.humidityLevel = humidityLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getSoilAcidityLevel() {
        return soilAcidityLevel;
    }

    public void setSoilAcidityLevel(BigDecimal soilAcidityLevel) {
        this.soilAcidityLevel = soilAcidityLevel;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sensor sensor)) return false;
        return Objects.equals(name, sensor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", humidityLevel=" + humidityLevel +
                ", temperature=" + temperature +
                ", soilAcidityLevel=" + soilAcidityLevel +
                '}';
    }
}
