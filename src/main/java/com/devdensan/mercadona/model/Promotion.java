package com.devdensan.mercadona.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id", updatable = false)
    private int promotionId;

    @Column(name = "discount_percentage")
    private int discountPercentage;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private LocalDate endDate;

    public Promotion() {
    }

    public Promotion(int discountPercentage, LocalDate startDate, LocalDate endDate) {
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Promotion(int promotionId, int discountPercentage, LocalDate startDate, LocalDate endDate) {
        this.promotionId = promotionId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "promotionId=" + promotionId +
                ", discountPercentage=" + discountPercentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
