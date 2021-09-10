package vinh.pro.book.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.constant.PrefixLog;
import vinh.pro.book.entity.Book;
import vinh.pro.book.enums.RoleNameType;
import vinh.pro.book.exception.BookAppException;
import vinh.pro.book.payload.request.book.BookCreateRequest;
import vinh.pro.book.payload.response.book.BookDetailResponse;
import vinh.pro.book.payload.response.book.BookResponse;
import vinh.pro.book.repository.BookRepository;
import vinh.pro.book.security.UserPrincipal;
import vinh.pro.book.utils.CommonUtils;
import vinh.pro.book.validation.CommonValidation;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBook(PageRequest pageRequest) {
        log.info(PrefixLog.BOOK + "get all book starting...");

        CommonValidation.validatePageSize(
                pageRequest.getPageSize(),
                AppConstant.Common.BOOK_LIST_SIZE_MIN,
                AppConstant.Common.BOOK_LIST_SIZE_MAX,
                "list book");

        List<BookResponse> bookList = bookRepository.findAllBook(pageRequest);

        log.info(PrefixLog.BOOK + "get all book finished");

        return bookList;
    }

    @Transactional(readOnly = true)
    public Long getTotalBook() {
        log.info(PrefixLog.BOOK + "get total book starting...");
        Long total = bookRepository.countByEnabledIsTrue();
        log.info(PrefixLog.BOOK + "get total book finish");

        return total;
    }

    @Transactional
    public void create(UserPrincipal currentUser, BookCreateRequest bookCreateRequest) {
        log.info(PrefixLog.BOOK + "create new book starting...");
        LocalDateTime now = LocalDateTime.now();
        Book book = Book.builder()
                        .title(bookCreateRequest.getTitle())
                        .author(bookCreateRequest.getAuthor())
                        .description(bookCreateRequest.getDescription())
                        .image(bookCreateRequest.getImage())
                        .userId(currentUser.getId())
                        .enabled(true)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

        bookRepository.save(book);

        log.info(PrefixLog.BOOK + "create new book finished");
    }

    @Transactional
    public void editBookById(UserPrincipal currentUser, Long bookId, BookCreateRequest bookCreateRequest) {
        log.info(PrefixLog.BOOK + "edit book with id = [{}] starting...", bookId);

        Book book = bookRepository.selectBookById(bookId).orElseThrow(
                () -> new BookAppException(
                        AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                        String.format("Could not found book with id = [%d]", bookId),
                        HttpStatus.BAD_REQUEST));

        if (!currentUser.getId().equals(book.getUserId())) {
            throw new BookAppException(
                    AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                    "Can not update book of other author!",
                    HttpStatus.FORBIDDEN);
        }

        book.setTitle(bookCreateRequest.getTitle());
        book.setAuthor(bookCreateRequest.getAuthor());
        book.setDescription(bookCreateRequest.getDescription());
        book.setImage(bookCreateRequest.getImage());
        book.setUpdatedAt(LocalDateTime.now());

        bookRepository.save(book);

        log.info(PrefixLog.BOOK + "edit book with id = [{}] finished", bookId);
    }

    @Transactional(readOnly = true)
    public BookDetailResponse getBookDetailById(Long id) {
        log.info(PrefixLog.BOOK + "get book detail with id = [{}] starting...", id);

        BookDetailResponse bookDetailResponse = bookRepository.getBookDetailById(id).orElseThrow(
                () -> new BookAppException(
                        AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                        String.format("Could not found book with id = [%d]", id),
                        HttpStatus.BAD_REQUEST));

        log.info(PrefixLog.BOOK + "get book detail with id = [{}] finished", id);

        return bookDetailResponse;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getBookForSearch(String key, PageRequest pageRequest) {
        log.info(PrefixLog.BOOK + "get book list for search with key search = [{}] starting...", key);

        CommonValidation.validatePageSize(
                pageRequest.getPageSize(),
                AppConstant.Common.BOOK_LIST_SIZE_MIN,
                AppConstant.Common.BOOK_LIST_SIZE_MAX,
                "list book for search");

        String keyInEscape = CommonUtils.escapeMetaCharacters(key);
        List<BookResponse> bookResponseList = bookRepository.findAllBookForSearch(keyInEscape, pageRequest);

        log.info(PrefixLog.BOOK + "get book list for search with key search = [{}] finished", key);

        return bookResponseList;
    }


    @Transactional(readOnly = true)
    public Long getTotalBookForSearch(String key) {
        log.info(PrefixLog.BOOK + "get total book for search with key search = [{}] starting...", key);

        String keyInEscape = CommonUtils.escapeMetaCharacters(key);
        Long total = bookRepository.countBookForSearch(keyInEscape);

        log.info(PrefixLog.BOOK + "get total book for search with key search = [{}] finish", key);

        return total;
    }

    @Transactional
    public void deleteBookById(Long id, UserPrincipal currentUser) {
        log.info(PrefixLog.BOOK + "delete book by id = [{}] starting...", id);

        Book book = bookRepository.selectBookById(id).orElseThrow(
                () -> new BookAppException(
                        AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                        String.format("Could not found book by id = [%d]", id),
                        HttpStatus.BAD_REQUEST));

        log.info(PrefixLog.BOOK + "role of current user = [{}]", currentUser.getAuthorities().toString());

        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleNameType.ROLE_ADMIN.name()))
                || currentUser.getId().equals(book.getUserId())) {
            book.setEnabled(false);
            bookRepository.save(book);
        } else {
            throw new BookAppException(
                    AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                    "Could not delete book of other author",
                    HttpStatus.FORBIDDEN);
        }

        log.info(PrefixLog.BOOK + "delete book by id = [{}] finished", id);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getBookListCurrentUser(UserPrincipal userPrincipal, PageRequest pageRequest) {
        log.info(PrefixLog.BOOK + "list book of current user that has email = [{}] starting...",
                 userPrincipal.getEmail());

        CommonValidation.validatePageSize(
                pageRequest.getPageSize(),
                AppConstant.Common.BOOK_LIST_SIZE_MIN,
                AppConstant.Common.BOOK_LIST_SIZE_MAX,
                "list book of current user");

        List<BookResponse> bookResponseList = bookRepository
                .findAllBookByCurrentUser(userPrincipal.getId(), pageRequest);

        log.info(PrefixLog.BOOK + "list book of current user that has email = [{}] finished",
                 userPrincipal.getEmail());

        return bookResponseList;

    }

    @Transactional(readOnly = true)
    public Long getTotalBookCurrentUser(UserPrincipal userPrincipal) {
        log.info(PrefixLog.BOOK + "get total book of current user that has email = [{}] starting...",
                 userPrincipal.getEmail());

        Long total = bookRepository.countBookByCurrentUser(userPrincipal.getId());

        log.info(PrefixLog.BOOK + "get total book of current user that has email = [{}] finished",
                 userPrincipal.getEmail());

        return total;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getDisableListBook(PageRequest pageRequest) {
        log.info(PrefixLog.BOOK + "get disable list book starting...");

        CommonValidation.validatePageSize(
                pageRequest.getPageSize(),
                AppConstant.Common.BOOK_LIST_SIZE_MIN,
                AppConstant.Common.BOOK_LIST_SIZE_MAX,
                "disable list book");

        List<BookResponse> bookResponseList = bookRepository.findAllDisableBook(pageRequest);

        log.info(PrefixLog.BOOK + "get disable list book finished");

        return bookResponseList;
    }

    @Transactional(readOnly = true)
    public Long getTotalDisableListBook() {
        log.info(PrefixLog.BOOK + "get total disable book starting...");

        Long total = bookRepository.countAllDisableBook();

        log.info(PrefixLog.BOOK + "get total disable book finished");

        return total;
    }
}
