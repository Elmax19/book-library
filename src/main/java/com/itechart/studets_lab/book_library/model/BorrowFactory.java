package com.itechart.studets_lab.book_library.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowFactory {
    private static final BorrowFactory INSTANCE = new BorrowFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String BOOK_ID_COLUMN_NAME = "bookId";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String BORROW_DATE_COLUMN_NAME = "borrowDate";
    private static final String DURATION_COLUMN_NAME = "timePeriod";
    private static final String RETURN_DATE_COLUMN_NAME = "returnDate";
    private static final String COMMENT_COLUMN_NAME = "comment";
    private static final String STATUS_COLUMN_NAME = "status";

    private BorrowFactory() {
    }

    public static BorrowFactory getInstance() {
        return INSTANCE;
    }

    public Borrow create(ResultSet resultSet) throws SQLException {
        return Borrow.builder()
                .id(resultSet.getInt(ID_COLUMN_NAME))
                .bookId(resultSet.getInt(BOOK_ID_COLUMN_NAME))
                .email(resultSet.getString(EMAIL_COLUMN_NAME))
                .borrowDate(resultSet.getDate(BORROW_DATE_COLUMN_NAME).toLocalDate())
                .duration(resultSet.getInt(DURATION_COLUMN_NAME))
                .returnDate(resultSet.getDate(RETURN_DATE_COLUMN_NAME).toLocalDate())
                .comment(resultSet.getString(COMMENT_COLUMN_NAME))
                .status(resultSet.getString(STATUS_COLUMN_NAME))
                .build();
    }
}