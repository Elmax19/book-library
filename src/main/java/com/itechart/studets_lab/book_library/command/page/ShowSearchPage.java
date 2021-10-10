package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;

public enum ShowSearchPage implements Command {
    INSTANCE;

    private static final ResponseContext SEARCH_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/search.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        return SEARCH_PAGE_RESPONSE;
    }
}