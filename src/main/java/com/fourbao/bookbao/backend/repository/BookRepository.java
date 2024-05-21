package com.fourbao.bookbao.backend.repository;

import com.fourbao.bookbao.backend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>
{

}
