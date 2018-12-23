package com.jaxws.series.top.down.approach.service;

import in.benchresources.entities.book.BookRequestType;
import in.benchresources.entities.book.BookResponseType;
import in.benchresources.services.bookservice.IBookService;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

@WebService(serviceName="BookService", endpointInterface="in.benchresources.services.bookservice.IBookService",
        targetNamespace="http://benchresources.in/services/BookService/", portName="BookServicePort", name="BookServiceImpl")
public class BookServiceImpl implements IBookService {

    // http://localhost:8080/ApacheCXF-JAX-WS-Top-Down/services/book/BookService?wsdl

    @Override
    public BookResponseType getBookByISDNRequestNumber(BookRequestType parameters) {

        // create object of responseType and set values & return
        BookResponseType bookResponseType = new BookResponseType();
        bookResponseType.setBookISBN(parameters.getIsbnNumber());
        bookResponseType.setBookName("Objective Microbiology");
        bookResponseType.setAuthor("S. Nandi");
        bookResponseType.setCategory("Microbiology");
        return bookResponseType;
    }
}