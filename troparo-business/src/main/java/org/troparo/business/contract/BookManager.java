package org.troparo.business.contract;

import org.troparo.model.Book;

import java.util.List;


public interface BookManager {

  String addBook(Book book);
  List<Book> getBooks();
}