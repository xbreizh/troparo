package org.troparo.web.service;

import org.troparo.business.contract.BookManager;
import org.troparo.entities.book.*;
import org.troparo.model.Book;
import org.troparo.services.bookservice.BusinessException;
import org.troparo.services.bookservice.IBookService;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@WebService(serviceName = "BookService", endpointInterface = "org.troparo.services.bookservice.IBookService",
        targetNamespace = "http://troparo.org/services/BookService/", portName = "BookServicePort", name = "BookServiceImpl")
public class BookServiceImpl implements IBookService {

    @Inject
    private BookManager bookManager;

    private String exception = "";
    private List<Book> bookList = new ArrayList<>();
    private BookTypeOut bookTypeOut=null;
    private BookTypeIn bookTypeIn=null;
    private BookListType bookListType = new BookListType();
    private Book book=null;

    @Override
    public UpdateBookResponseType updateBook(UpdateBookRequestType parameters) throws BusinessException {
        UpdateBookResponseType ar = new UpdateBookResponseType();
        ar.setReturn(true);
        /*Book book = new Book();*/
        bookTypeIn = parameters.getBookTypeIn();
        convertBootypeInIntoBook();
        System.out.println("bookManager: " + bookManager);
        exception = bookManager.updateBook(book);
        if (exception != null) {
            throw new BusinessException(exception);
        }

        return ar;
    }

    @Override
    public GetBookByIdResponseType getBookById(GetBookByIdRequestType parameters) throws BusinessException {

        System.out.println("new method added");
        GetBookByIdResponseType rep = new GetBookByIdResponseType();
        BookTypeOut bt = new BookTypeOut();
        Book book = bookManager.getBookById(parameters.getReturn());
        if(book == null){
            throw new BusinessException("no book found with that id");
        }else {
            bt.setId(book.getBookId());
            bt.setISBN(book.getIsbn());
            bt.setTitle(book.getTitle());
            bt.setAuthor(book.getAuthor());
            bt.setEdition(book.getEdition());
            bt.setNbPages(book.getNbPages());
            bt.setKeywords(book.getIsbn());
            rep.setBookTypeOut(bt);
        }
        return  rep;
    }

    @Override
    public GetAvailableResponseType getAvailable(GetAvailableRequestType parameters) throws BusinessException {
        GetAvailableResponseType ar = new GetAvailableResponseType();
        int i = bookManager.getAvailable(parameters.getISBN());
        System.out.println("i: "+i);
        ar.setReturn(i);

        return ar;
    }

    @Override
    public GetBookByCriteriasResponseType getBookByCriterias(GetBookByCriteriasRequestType parameters) throws BusinessException {
        HashMap<String, String> map = new HashMap<>();
        BookCriterias criterias = parameters.getBookCriterias();
        map.put("ISBN", criterias.getISBN());
        map.put("Title", criterias.getTitle());
        map.put("Author", criterias.getAuthor());
        System.out.println("map: "+map);
        /*bookListType.getBookTypeOut().clear();*/
        bookList = bookManager.getBooksByCriterias(map);
        GetBookByCriteriasResponseType brt = new GetBookByCriteriasResponseType();
        System.out.println("bookListType beg: "+bookListType.getBookTypeOut().size());

        convertBookIntoBookTypeOut();
        /*bookListType.getBookTypeOut().add(bookTypeOut); // add bookType to the movieListType*/
        System.out.println("bookListType end: "+bookListType.getBookTypeOut().size());
        brt.setBookListType(bookListType);
        return brt;
    }

    private void convertBookIntoBookTypeOut() {
        bookListType.getBookTypeOut().clear();
        for (Book book : bookList) {

            // set values retrieved from DAO class
            bookTypeOut = new BookTypeOut();
            bookTypeOut.setId(book.getBookId());
            bookTypeOut.setISBN(book.getIsbn());
            bookTypeOut.setTitle(book.getTitle());
            bookTypeOut.setAuthor(book.getAuthor());
            bookTypeOut.setEdition(book.getEdition());

            try {
                GregorianCalendar c = new GregorianCalendar();
                Date date = book.getPublication();
                XMLGregorianCalendar xmlDate;
                xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                bookTypeOut.setPublication(xmlDate);
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }

            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setNbPages(book.getNbPages());
            bookTypeOut.setKeywords(book.getKeywords());
            bookListType.getBookTypeOut().add(bookTypeOut);
        }
        System.out.println("bookListType end: "+bookListType.getBookTypeOut().size());
    }



    @Override
    public BookListResponseType getAllBooks(BookListRequestType parameters) throws BusinessException {

        bookList = bookManager.getBooks();
        System.out.println("size list: " + bookList.size());

        BookListResponseType bookListResponseType = new BookListResponseType();

        convertBookIntoBookTypeOut();
        // add bookType to the movieListType

        bookListResponseType.setBookListType(bookListType);
        return bookListResponseType;

    }

    @Override
    public RemoveBookResponseType removeBook(RemoveBookRequestType parameters) throws BusinessException {
        RemoveBookResponseType ar = new RemoveBookResponseType();
        ar.setReturn(true);

        System.out.println("bookManager: " + bookManager);
        exception = bookManager.remove(parameters.getId());
        if (exception != null) {
            throw new BusinessException(exception);
        }

        return ar;

    }

    @Override
    public AddBookResponseType addBook(AddBookRequestType parameters) throws BusinessException {
        AddBookResponseType ar = new AddBookResponseType();
        ar.setReturn(true);
        bookTypeIn = parameters.getBookTypeIn();
        convertBootypeInIntoBook();
        System.out.println("bookManager: " + bookManager);
        exception = bookManager.addBook(book);
        if (exception != null) {
            System.out.println(exception);
            throw new BusinessException(exception);
        }

        return ar;
    }

    private void convertBootypeInIntoBook() {
        book = new Book();
        book.setIsbn(bookTypeIn.getISBN());
        book.setTitle(bookTypeIn.getTitle());
        book.setAuthor(bookTypeIn.getAuthor());
        book.setInsert_date(new Date());
        book.setPublication(new Date());
        book.setEdition(bookTypeIn.getEdition());
        book.setNbPages(bookTypeIn.getNbPages());
        book.setKeywords(bookTypeIn.getKeywords());
    }
}
