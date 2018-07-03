package book;

import com.jooq.h2.spring.BookData;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import java.util.List;

public interface BookRepository {
  Promise<List<BookData>> getBooks();
  Operation addBook(BookData bookData);

  Promise<BookData> getBookId(int bookId);
}
