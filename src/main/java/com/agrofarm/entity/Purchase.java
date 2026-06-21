package com.agrofarm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "purchase", schema = "agrofarm")
public class Purchase {

    public Purchase() {
    }

    public Purchase(BigDecimal germinationPercentage, String supplier) {
        if (germinationPercentage == null || germinationPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Процент всхожести должен быть больше нуля!");
        }
        if (supplier == null || supplier.isBlank()) {
            throw new IllegalArgumentException("Поставщик должен быть указан!");
        }
        this.germinationPercentage = germinationPercentage;
        this.supplier = supplier;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private AccountingOfParties accountingOfParties;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cultures", referencedColumnName = "name", nullable = false)
    private Culture culture;

    @Column(name = "germination_percentage", nullable = false, precision = 3, scale = 2)
    private BigDecimal germinationPercentage;

    @Column(nullable = false, length = 50)
    private String supplier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountingOfParties getAccountingOfParties() {
        return accountingOfParties;
    }

    public void setAccountingOfParties(AccountingOfParties accountingOfParties) {
        this.accountingOfParties = accountingOfParties;
    }

    public Culture getCulture() {
        return culture;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public BigDecimal getGerminationPercentage() {
        return germinationPercentage;
    }

    public void setGerminationPercentage(BigDecimal germinationPercentage) {
        this.germinationPercentage = germinationPercentage;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Purchase purchase)) return false;
        return Objects.equals(id, purchase.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", germinationPercentage=" + germinationPercentage +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
