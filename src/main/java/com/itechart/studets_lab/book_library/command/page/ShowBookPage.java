package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.UrlPatterns;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.service.BookService;
import com.itechart.studets_lab.book_library.service.BorrowService;
import com.itechart.studets_lab.book_library.service.ReaderService;
import com.itechart.studets_lab.book_library.service.impl.BookServiceImpl;
import com.itechart.studets_lab.book_library.service.impl.BorrowServiceImpl;
import com.itechart.studets_lab.book_library.service.impl.ReaderServiceImpl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public enum ShowBookPage implements Command {
    INSTANCE;

    private static final String ID_PARAMETER_NAME = "bookId";
    private static final String BOOK_ATTRIBUTE_NAME = "book";
    private static final String BOOK_AUTHORS_ATTRIBUTE_NAME = "authors";
    private static final String BOOK_GENRES_ATTRIBUTE_NAME = "genres";
    private static final String BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME = "availableCount";
    private static final String AVAILABLE_DATE_ATTRIBUTE_NAME = "availableDate";
    private static final String BOOK_BORROWS_ATTRIBUTE_NAME = "borrows";
    private static final String EMAILS_ATTRIBUTE_NAME = "emails";
    private static final String STATUS_ATTRIBUTE_NAME = "statuses";
    private static final String PERIODS_ATTRIBUTE_NAME = "periods";
    private static final String EMPTY_ATTRIBUTE_VALUE = "null";
    private final BookService bookService = BookServiceImpl.getInstance();
    private final BorrowService borrowService = BorrowServiceImpl.getInstance();
    private final ReaderService readerService = ReaderServiceImpl.getInstance();

    private static final ResponseContext BOOK_PAGE_RESPONSE = new ResponseContext(UrlPatterns.BOOK, false);

    @Override
    public ResponseContext execute(RequestContext request) {
        String id = String.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final int bookId = (EMPTY_ATTRIBUTE_VALUE.equals(id)) ? 0 : Integer.parseInt(id);
        Book book = bookService.findByKey(bookId);
        if (book != null) {
            setRequestAttributes(request, book);
        }
        return BOOK_PAGE_RESPONSE;
    }

    private void setRequestAttributes(RequestContext request, Book book) {
        request.setAttribute(BOOK_ATTRIBUTE_NAME, book);
        request.setAttribute(BOOK_AUTHORS_ATTRIBUTE_NAME, parseList(book.getAuthors()));
        request.setAttribute(BOOK_GENRES_ATTRIBUTE_NAME, parseList(book.getGenres()));
        List<BorrowDto> borrows = borrowService.findBorrowsOfBook(book.getId());
        if (!borrows.isEmpty()) {
            request.setAttribute(BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME, book.getTotalAmount() - borrows.size());
            request.setAttribute(BOOK_BORROWS_ATTRIBUTE_NAME, borrows);
            request.setAttribute(AVAILABLE_DATE_ATTRIBUTE_NAME, getAvailableDate(borrows));
        } else {
            request.setAttribute(BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME, book.getTotalAmount());
        }
        request.setAttribute(EMAILS_ATTRIBUTE_NAME, readerService.findAllEmails());
        request.setAttribute(PERIODS_ATTRIBUTE_NAME, borrowService.findAllPeriods());
        request.setAttribute(STATUS_ATTRIBUTE_NAME, borrowService.findAllStatuses());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private LocalDate getAvailableDate(List<BorrowDto> borrows) {
        Comparator<BorrowDto> comparator = Comparator.comparing(o -> o.getReturnDate().plusMonths(o.getDuration()));
        BorrowDto borrow = borrows.stream().min(comparator).get();
        return borrow.getBorrowDate().plusMonths(borrow.getDuration());
    }

    private String parseList(List<String> list) {
        return String.join(" ", list);
    }
}
