package com.agrofarm.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "accounting_of_parties", schema = "agrofarm")
public class AccountingOfParties {

    public AccountingOfParties() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "accountingOfParties", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "cultures", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> cultures = new ArrayList<>();

    public AccountingOfParties(BigDecimal cost, List<String> cultures) {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Стоимость партии должна быть больше нуля!");
        }
        this.cost = cost;
        this.cultures = cultures;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public List<String> getCultures() {
        return cultures;
    }

    public void setCultures(List<String> cultures) {
        this.cultures = cultures;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AccountingOfParties that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountingOfParties{" +
                "id='" + id  +
                ", cost=" + cost +
                ", cultures=" + cultures +
                '}';
    }
}
