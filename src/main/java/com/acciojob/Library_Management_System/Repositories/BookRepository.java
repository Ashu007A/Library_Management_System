package com.acciojob.Library_Management_System.Repositories;

import com.acciojob.Library_Management_System.Enums.Genre;
import com.acciojob.Library_Management_System.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

//    @Query(value = "select * from book where genre =: 'FICTION'", nativeQuery = true)
    List<Book> findBooksByGenre(Genre genre);

}
