package org.troparo.business.contract;

import org.troparo.model.Book;

import java.util.HashMap;
import java.util.List;


public interface BookManager {

  String addBook(Book book);
  List<Book> getBooks();
  Book getBookById(int id);
  List<Book> getBooksByCriterias(HashMap<String, String> map);
  String updateBook(Book book);

  String remove(int id);

  int getAvailable(String isbn);

  String addCopy(String isbn, int copies);
}