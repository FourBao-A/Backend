package com.fourbao.bookbao.backend.repository;

import com.fourbao.bookbao.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySchoolId(String schoolId);

    Boolean existsBySchoolId(String schoolId);
}
