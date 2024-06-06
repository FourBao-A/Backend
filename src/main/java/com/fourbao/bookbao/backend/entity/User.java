package com.fourbao.bookbao.backend.entity;

import com.fourbao.bookbao.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String schoolId;

    @Setter
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    @Builder
    public User(String name, String schoolId, String email) {
        this.name = name;
        this.schoolId = schoolId;
        this.email = email;
    }

    public void addBook(Book book) {
        this.books.add(book);
        book.setUser(this);
    }

    public void removeBook(Book book)
    {
        this.books.remove(book);
        book.setUser(null);
    }
}
