package com.fourbao.bookbao.backend.repository;

import com.fourbao.bookbao.backend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>
{
    @Query(
            "SELECT b FROM Book b WHERE " +
            "b.title LIKE concat('%', :search, '%') "
    )
    List<Book> findByKeyword(@Param("search") String search);

    List<Book> findByUserIdAndSaleState(long id, Book.SaleState saleState);

}