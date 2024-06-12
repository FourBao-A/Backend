package com.fourbao.bookbao.backend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


//BaseEntity 클래스는 모든 엔티티의 기본 속성을 정의하는 추상 클래스입니다.
//이 클래스를 상속받는 모든 엔티티는 이 클래스의 속성을 자동으로 상속받게 됩니다.

@Getter
@MappedSuperclass
public class BaseEntity {
     //@CreationTimestamp 어노테이션을 사용하여 엔티티가 생성될 때 현재 시간이 자동으로 설정됩니다.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
