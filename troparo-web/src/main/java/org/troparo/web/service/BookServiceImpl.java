package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.entities.book.*;
import org.troparo.model.Book;
import org.troparo.services.bookservice.BusinessException;
import org.troparo.services.bookservice.IBookService;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.*;

@WebService(serviceName = "BookService", endpointInterface = "org.troparo.services.bookservice.IBookService",
        targetNamespace = "http://troparo.org/services/BookService/", portName = "BookServicePort", name = "BookServiceImpl")
public class BookServiceImpl implements IBookService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private BookManager bookManager;

    private String exception = "";
    private List<Book> bookList = new ArrayList<>();
    private BookTypeOut bookTypeOut = null;
    private BookTypeIn bookTypeIn = null;
    private BookListType bookListType = new BookListType();
    private Book book = null;

    // Create
    @Override
    public AddBookResponseType addBook(AddBookRequestType parameters) throws BusinessException {
        AddBookResponseType ar = new AddBookResponseType();
        ar.setReturn(true);
        bookTypeIn = parameters.getBookTypeIn();
        convertBootypeInIntoBook();
        logger.info("bookManager: " + bookManager);
        exception = bookManager.addBook(book);
        if (!exception.equals("")) {
            logger.info(exception);
            throw new BusinessException(exception);
        }

        return ar;
    }
    // Converts Input into Book for business
    private void convertBootypeInIntoBook() {
        book = new Book();
        book.setIsbn(bookTypeIn.getISBN().toUpperCase());
        book.setTitle(bookTypeIn.getTitle().toUpperCase());
        book.setAuthor(bookTypeIn.getAuthor().toUpperCase());
        logger.info(bookTypeIn.getPublicationYear());
        book.setPublicationYear(bookTypeIn.getPublicationYear());
        book.setEdition(bookTypeIn.getEdition().toUpperCase());
        book.setNbPages(bookTypeIn.getNbPages());
        book.setKeywords(bookTypeIn.getKeywords().toUpperCase());
        logger.info("pub date: "+book.getPublicationYear());
    }

    // Update
    @Override
    public UpdateBookResponseType updateBook(UpdateBookRequestType parameters) throws BusinessException {
        UpdateBookResponseType ar = new UpdateBookResponseType();
        ar.setReturn(true);
        bookTypeIn = parameters.getBookTypeIn();
        convertBootypeInIntoBook();
        logger.info("bookManager: " + bookManager);
        exception = bookManager.updateBook(book);
        if (!exception.equals("")) {
            throw new BusinessException(exception);
        }

        return ar;
    }

    @Override
    public AddCopyResponseType addCopy(AddCopyRequestType parameters) throws BusinessException {
        AddCopyResponseType ar = new AddCopyResponseType();
        ar.setReturn(true);
        String isbn = parameters.getISBN().toUpperCase();
        int copies = parameters.getNbCopies();

        logger.info("bookManager: " + bookManager);
        exception = bookManager.addCopy(isbn, copies);
        if (!exception.equals("")) {
            throw new BusinessException(exception);
        }

        return ar;
    }

    // Get One
    @Override
    public GetBookByIdResponseType getBookById(GetBookByIdRequestType parameters) throws BusinessException {

        logger.info("new method added");
        GetBookByIdResponseType rep = new GetBookByIdResponseType();
        BookTypeOut bt = new BookTypeOut();
        Book book = bookManager.getBookById(parameters.getReturn());
        if (book == null) {
            throw new BusinessException("no book found with that id");
        } else {
            bt.setId(book.getId());
            bt.setISBN(book.getIsbn());
            bt.setTitle(book.getTitle());
            bt.setAuthor(book.getAuthor());
            bt.setEdition(book.getEdition());
            bt.setNbPages(book.getNbPages());
            bt.setKeywords(book.getIsbn());
            rep.setBookTypeOut(bt);
        }
        return rep;
    }

    // Get All
    @Override
    public BookListResponseType getAllBooks() throws BusinessException {
        bookList = bookManager.getBooks();
        logger.info("size list: " + bookList.size());

        BookListResponseType bookListResponseType = new BookListResponseType();

        convertBookIntoBookTypeOut();
        // add bookType to the movieListType

        bookListResponseType.setBookListType(bookListType);
        return bookListResponseType;
    }


    // Get List By Criterias
    @Override
    public GetBookByCriteriasResponseType getBookByCriterias(GetBookByCriteriasRequestType parameters) throws BusinessException {
        HashMap<String, String> map = new HashMap<>();
        BookCriterias criterias = parameters.getBookCriterias();
        map.put("ISBN", criterias.getISBN().toUpperCase());
        map.put("Title", criterias.getTitle().toUpperCase());
        map.put("Author", criterias.getAuthor().toUpperCase());
        logger.info("map: " + map);
        /*bookListType.getBookTypeOut().clear();*/
        bookList = bookManager.getBooksByCriterias(map);
        GetBookByCriteriasResponseType brt = new GetBookByCriteriasResponseType();
        logger.info("bookListType beg: " + bookListType.getBookTypeOut().size());

        convertBookIntoBookTypeOut();
        /*bookListType.getBookTypeOut().add(bookTypeOut); // add bookType to the movieListType*/
        logger.info("bookListType end: " + bookListType.getBookTypeOut().size());
        brt.setBookListType(bookListType);
        return brt;
    }



    // Delete
    @Override
    public RemoveBookResponseType removeBook(RemoveBookRequestType parameters) throws BusinessException {
        RemoveBookResponseType ar = new RemoveBookResponseType();
        ar.setReturn(true);

        logger.info("bookManager: " + bookManager);
        exception = bookManager.remove(parameters.getId());
        if (!exception.equals("")) {
            throw new BusinessException(exception);
        }

        return ar;

    }

    // Get number Available
    @Override
    public GetAvailableResponseType getAvailable(GetAvailableRequestType parameters) throws BusinessException {
        GetAvailableResponseType ar = new GetAvailableResponseType();
        int i = bookManager.getAvailable(parameters.getISBN().toUpperCase());
        logger.info("i: " + i);
        ar.setReturn(i);

        return ar;
    }

    // Converts Book from Business into output
    private void convertBookIntoBookTypeOut() {
        bookListType.getBookTypeOut().clear();
        for (Book book : bookList) {

            // set values retrieved from DAO class
            bookTypeOut = new BookTypeOut();
            bookTypeOut.setId(book.getId());
            bookTypeOut.setISBN(book.getIsbn());
            bookTypeOut.setTitle(book.getTitle());
            bookTypeOut.setAuthor(book.getAuthor());
            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setPublicationYear(book.getPublicationYear());
            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setNbPages(book.getNbPages());
            bookTypeOut.setKeywords(book.getKeywords());
            bookListType.getBookTypeOut().add(bookTypeOut);
        }
        logger.info("bookListType end: " + bookListType.getBookTypeOut().size());
    }



}
