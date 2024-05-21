package com.fourbao.bookbao.backend.entity;

import com.fourbao.bookbao.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Deal_way deal_way;

    @Column(nullable = true)
    private String deal_place;

    @Column(nullable = false)
    private String image;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String ask_for;

    @ManyToOne
    @JoinColumn
    private User user;

    @Builder
    public Book(Long id, String title, String author, String publisher, int price, Deal_way deal_way, String deal_place, String image, String state, String ask_for)
    {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.price = price;
        this.deal_way = deal_way;
        this.deal_place = deal_place;
        this.image = image;
        this.state = state;
        this.ask_for = ask_for;
    }

    public enum Deal_way
    {
        DIRECT,
        DELIVERY,
        BOTH
    }
}
