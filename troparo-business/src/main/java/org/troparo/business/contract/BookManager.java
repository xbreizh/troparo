package org.troparo.business.contract;

import org.troparo.model.Book;

import javax.inject.Named;


public interface BookManager {

  String addBook(Book book);
}