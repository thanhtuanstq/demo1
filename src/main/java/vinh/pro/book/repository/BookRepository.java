package vinh.pro.book.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vinh.pro.book.entity.Book;
import vinh.pro.book.payload.response.book.BookDetailResponse;
import vinh.pro.book.payload.response.book.BookResponse;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository
        extends JpaRepository<Book, Long> {

    @Query("SELECT b " +
            "FROM Book b " +
            "WHERE b.id = :id AND b.enabled = TRUE")
    Optional<Book> selectBookById(@Param("id") Long id);

    @Query("SELECT new vinh.pro.book.payload.response.book.BookResponse" +
            "(b.id, " +
            "b.title, " +
            "b.author) " +
            "FROM Book AS b " +
            "WHERE b.enabled = TRUE")
    List<BookResponse> findAllBook(Pageable pageable);

    Long countByEnabledIsTrue();

    @Query("SELECT new vinh.pro.book.payload.response.book.BookDetailResponse" +
            "(b.id, " +
            "b.title, " +
            "b.author, " +
            "b.description, " +
            "b.image, " +
            "b.createdAt, " +
            "b.updatedAt) " +
            "FROM Book AS b " +
            "WHERE b.id = :id AND b.enabled = TRUE")
    Optional<BookDetailResponse> getBookDetailById(@Param("id") Long id);

    @Query("SELECT new vinh.pro.book.payload.response.book.BookResponse" +
            "(b.id, " +
            "b.title, " +
            "b.author) " +
            "FROM Book AS b " +
            "WHERE b.enabled = TRUE " +
            "AND (b.title LIKE CONCAT('%', :key, '%') OR b.author LIKE CONCAT('%', :key, '%'))")
    List<BookResponse> findAllBookForSearch(@Param("key") String key, Pageable pageable);

    @Query("SELECT COUNT(b.id) " +
            "FROM Book AS b " +
            "WHERE " +
            "b.enabled = TRUE " +
            "AND (b.title LIKE CONCAT('%', :key, '%') OR b.author LIKE CONCAT('%', :key, '%'))")
    Long countBookForSearch(@Param("key") String key);

    @Query("SELECT new vinh.pro.book.payload.response.book.BookResponse" +
            "(b.id, " +
            "b.title, " +
            "b.author) " +
            "FROM Book AS b " +
            "WHERE b.enabled = TRUE " +
            "AND b.userId = :userId")
    List<BookResponse> findAllBookByCurrentUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(b.id) " +
            "FROM Book AS b " +
            "WHERE " +
            "b.enabled = TRUE " +
            "AND b.userId = :userId")
    Long countBookByCurrentUser(@Param("userId") Long userId);

    @Query("SELECT new vinh.pro.book.payload.response.book.BookResponse" +
            "(b.id, " +
            "b.title, " +
            "b.author) " +
            "FROM Book AS b " +
            "WHERE b.enabled = FALSE")
    List<BookResponse> findAllDisableBook(Pageable pageable);

    @Query("SELECT COUNT(b.id) " +
            "FROM Book AS b " +
            "WHERE " +
            "b.enabled = FALSE")
    Long countAllDisableBook();
}
