package com.fourbao.bookbao.backend.entity;

import com.fourbao.bookbao.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private String schoolNum;

    @Setter
    @Column(nullable = false)
    private String email;

    @Builder
    public User(String name, String schoolNum, String email) {
        this.name = name;
        this.schoolNum = schoolNum;
        this.email = email;
    }
}
