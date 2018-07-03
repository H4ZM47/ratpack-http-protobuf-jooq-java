package book;

import com.jooq.h2.spring.BookData;
import com.jooq.h2.spring.BookDataProto;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

public interface BookService {
  Promise<BookDataProto.BookList>  getBooks();
  Promise<BookDataProto.Book> getBookById(int bookId);
  Operation addBook(BookData book);
  //Operation rateMeeting(String id, String rating);
}
