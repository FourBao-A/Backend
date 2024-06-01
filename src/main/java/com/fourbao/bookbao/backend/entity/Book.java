package com.fourbao.bookbao.backend.entity;

import com.fourbao.bookbao.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Book extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private int price;

    @Email
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealWay dealWay;

    private String dealPlace;

    private String image;

    private String state;

    private String askFor;

    @Column(nullable = false)
    private SaleState saleState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Book(String title, String author, String publisher, int price, String contactEmail, DealWay dealWay, String dealPlace, String image, String state, String askFor, SaleState saleState) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.contactEmail = contactEmail;
        this.dealWay = dealWay;
        this.dealPlace = dealPlace;
        this.image = image;
        this.state = state;
        this.askFor = askFor;
        this.saleState = saleState;
    }

    public enum DealWay
    {
        DIRECT,     // 직거래
        DELIVERY   // 택배
    }

    public enum SaleState {
        SOLD,
        NOT_SOLD
    }

}
