package com.itechart.studets_lab.book_library.command.search;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.page.ShowSearchPage;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.service.impl.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum FindBook implements Command {
    INSTANCE;

    private final BookService bookService = BookService.getInstance();
    private static final String BOOKS_PARAMETER_NAME = "books";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String AUTHORS_PARAMETER_NAME = "authors";
    private static final String GENRES_PARAMETER_NAME = "genres";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";

    @Override
    public ResponseContext execute(RequestContext request) {
        final String title = String.valueOf(request.getParameter(TITLE_PARAMETER_NAME));
        final String authors = String.valueOf(request.getParameter(AUTHORS_PARAMETER_NAME));
        final String genres = String.valueOf(request.getParameter(GENRES_PARAMETER_NAME));
        final String description = String.valueOf(request.getParameter(DESCRIPTION_PARAMETER_NAME));
        final BookCriteria bookCriteria = createCriteria(title, authors, genres, description);
        Optional<List<Book>> books = bookService.findByCriteria(bookCriteria);
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        if (books.isPresent()) {
            List<Book> bookList = books.get();
            request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, bookList.size() % 10 + 1);
            List<Book> rezList = new ArrayList<>();
            for (int i = 10 * (pageNumber - 1); i < 10 * pageNumber && i < bookList.size(); i++) {
                rezList.add(bookList.get(i));
            }
            request.setAttribute(BOOKS_PARAMETER_NAME, rezList);
        }
        return ShowSearchPage.INSTANCE.execute(request);
    }

    // TODO ты не проверяешь параметры на null. У тебя явно этот метод упадет, если какой то из параметров будет null
    private BookCriteria createCriteria(String title, String authors, String genres, String description) {
        BookCriteria.CriteriaBuilder bookCriteria = BookCriteria.builder();
        if (!title.equals("")) {
            bookCriteria.title(title);
        }
        // TODO попробуй title.isEmpty()
        // TODO а вообще рекомендую взглянуть на библиотеку org.apache.commons и в частности на org.apache.commons.lang3.StringUtils для работы со стрингами
        // TODO например можно это все обернуть в StringUtils.isNotBlank
        // TODO org.apache.commons вам можно использовать и там есть очень много полезного уже реализованного функционала
        if (!authors.equals("")) {
            bookCriteria.authors(parseStringIntoStringList(authors));
        }
        if (!genres.equals("")) {
            bookCriteria.genres(parseStringIntoStringList(genres));
        }
        if (!description.equals("")) {
            bookCriteria.description(description);
        }
        return bookCriteria.build();
    }

    /*TODO вместо этого метода лучше использовать уже готовое решение: Collections.singletonList(set) . Посмотри, что еще есть интересного в этом классе*/
    private List<String> parseStringIntoStringList(String set) {
        List<String> rezList = new ArrayList<>();
        for (String word : set.split(";")) {
            rezList.add(word.trim());
        }
        return rezList;
    }
}
