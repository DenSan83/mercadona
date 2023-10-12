package com.devdensan.mercadona.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id", updatable = false)
    private int promotion_id;

    @Column(name = "discount_percentage")
    private int discount_percentage;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private LocalDate start_date;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private LocalDate end_date;

    public Promotion() {
    }

    public Promotion(int discount_percentage, LocalDate start_date, LocalDate end_date) {
        this.discount_percentage = discount_percentage;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Promotion(int promotion_id, int discount_percentage, LocalDate start_date, LocalDate end_date) {
        this.promotion_id = promotion_id;
        this.discount_percentage = discount_percentage;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(int promotion_id) {
        this.promotion_id = promotion_id;
    }

    public int getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(int discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "discount_percentage=" + discount_percentage +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                '}';
    }
}
