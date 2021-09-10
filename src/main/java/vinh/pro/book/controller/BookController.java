package vinh.pro.book.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.payload.request.book.BookCreateRequest;
import vinh.pro.book.payload.response.auth.ApiResponse;
import vinh.pro.book.payload.response.book.BookDetailResponse;
import vinh.pro.book.payload.response.book.BookResponse;
import vinh.pro.book.payload.response.pagination.PagingResponse;
import vinh.pro.book.security.CurrentUser;
import vinh.pro.book.security.UserPrincipal;
import vinh.pro.book.service.BookService;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @ResponseBody
    public Object getAllBook(@RequestParam("page_no") Integer pageNo,
                             @RequestParam("page_size") Integer pageSize,
                             @RequestParam(value = "sort_by", defaultValue = "title") String sortBy,
                             @RequestParam(value = "sort_type", defaultValue = "ASC") String sortType) {

        Sort.Direction direction = Sort.Direction.ASC.name().equals(sortType) ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(Collections.singletonList(new Sort.Order(direction, sortBy)));
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        List<BookResponse> bookResponses = bookService.getAllBook(pageRequest);
        Long totalBook = bookService.getTotalBook();

        PagingResponse pagingResponse =
                PagingResponse.builder()
                              .total(totalBook)
                              .pageNo(pageNo)
                              .pageSize(pageSize)
                              .records(bookResponses)
                              .build();

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.OK,
                "List all book is success",
                pagingResponse);
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAnyRole({'USER', 'ADMIN'})")
    public Object create(@CurrentUser UserPrincipal userPrincipal,
                         @RequestBody BookCreateRequest bookCreateRequest) {
        bookService.create(userPrincipal, bookCreateRequest);

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.CREATED,
                "Create book is success",
                null);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole({'USER', 'ADMIN'})")
    public Object edit(@CurrentUser UserPrincipal userPrincipal,
                       @PathVariable("id") Long id,
                       @RequestBody BookCreateRequest bookCreateRequest) {
        bookService.editBookById(userPrincipal, id, bookCreateRequest);

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.NO_CONTENT,
                "Edit book is success",
                null);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Object getDetail(@PathVariable("id") Long id) {
        BookDetailResponse bookDetailResponse = bookService.getBookDetailById(id);

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.OK,
                String.format("Book detail with id = [%d] is success", id),
                bookDetailResponse);
    }

    @GetMapping("/search")
    @ResponseBody
    public Object search(@RequestParam("page_no") Integer pageNo,
                         @RequestParam("page_size") Integer pageSize,
                         @RequestParam(value = "sort_by", defaultValue = "title") String sortBy,
                         @RequestParam(value = "sort_type", defaultValue = "ASC") String sortType,
                         @RequestParam("key") String key) {
        Sort.Direction direction = Sort.Direction.ASC.name().equals(sortType) ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(Collections.singletonList(new Sort.Order(direction, sortBy)));
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        List<BookResponse> bookResponses = bookService.getBookForSearch(key, pageRequest);
        Long totalBook = bookService.getTotalBookForSearch(key);

        PagingResponse pagingResponse =
                PagingResponse.builder()
                              .total(totalBook)
                              .pageNo(pageNo)
                              .pageSize(pageSize)
                              .records(bookResponses)
                              .build();

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.OK,
                String.format("List book for search with key = [%s] is success", key),
                pagingResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole({'USER', 'ADMIN'})")
    public Object delete(@PathVariable("id") Long id,
                         @CurrentUser UserPrincipal userPrincipal) {
        bookService.deleteBookById(id, userPrincipal);

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.NO_CONTENT,
                String.format("Delete book by id = [%d] is success", id),
                null);
    }

    @GetMapping("/me")
    @ResponseBody
    @PreAuthorize("hasAnyRole({'USER', 'ADMIN'})")
    public Object getMyBooks(@CurrentUser UserPrincipal userPrincipal,
                             @RequestParam("page_no") Integer pageNo,
                             @RequestParam("page_size") Integer pageSize,
                             @RequestParam(value = "sort_by", defaultValue = "title") String sortBy,
                             @RequestParam(value = "sort_type", defaultValue = "ASC") String sortType) {

        Sort.Direction direction = Sort.Direction.ASC.name().equals(sortType) ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(Collections.singletonList(new Sort.Order(direction, sortBy)));
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        List<BookResponse> bookResponseList = bookService.getBookListCurrentUser(userPrincipal, pageRequest);
        Long total = bookService.getTotalBookCurrentUser(userPrincipal);

        PagingResponse pagingResponse =
                PagingResponse.builder()
                              .total(total)
                              .pageNo(pageNo)
                              .pageSize(pageSize)
                              .records(bookResponseList)
                              .build();

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.OK,
                String.format("List book of current user with email = [%s] is success", userPrincipal.getEmail()),
                pagingResponse);
    }

    @GetMapping("/disable")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Object getDisableBooks(@RequestParam("page_no") Integer pageNo,
                                  @RequestParam("page_size") Integer pageSize,
                                  @RequestParam(value = "sort_by", defaultValue = "title") String sortBy,
                                  @RequestParam(value = "sort_type", defaultValue = "ASC") String sortType) {

        Sort.Direction direction = Sort.Direction.ASC.name().equals(sortType) ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(Collections.singletonList(new Sort.Order(direction, sortBy)));
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        List<BookResponse> bookResponseList = bookService.getDisableListBook(pageRequest);
        Long total = bookService.getTotalDisableListBook();

        PagingResponse pagingResponse =
                PagingResponse.builder()
                              .total(total)
                              .pageNo(pageNo)
                              .pageSize(pageSize)
                              .records(bookResponseList)
                              .build();

        return new ApiResponse(
                AppConstant.Code.BOOK_SERVICE_CODE_PREFIX,
                HttpStatus.OK,
                "List disable book is success",
                pagingResponse);
    }
}
