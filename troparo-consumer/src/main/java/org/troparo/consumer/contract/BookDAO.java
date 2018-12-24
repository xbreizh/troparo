package org.troparo.consumer.contract;

import org.troparo.model.Book;

import java.util.List;

public interface BookDAO {

    public List<Book> getBooks();
}