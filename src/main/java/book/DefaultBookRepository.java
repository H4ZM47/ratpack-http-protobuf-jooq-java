package book;

import com.jooq.h2.spring.BookData;
import org.jooq.DSLContext;
import org.jooq.example.db.h2.Tables;
import ratpack.exec.Blocking;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.example.db.h2.tables.Book.BOOK;


public class DefaultBookRepository implements BookRepository {
  private final DSLContext context;

  @Inject
  public DefaultBookRepository(DSLContext context) {
    this.context = context;
  }

  @Override
  public Promise<List<BookData>> getBooks() {
     return Blocking.get(()->getCollectBooks());
  }

  @Override
  public Operation addBook(BookData bookData) {
    return Blocking.op(() ->
      context.newRecord(BOOK, bookData).store()
    );
  }

  @Override
  public Promise<BookData> getBookId(int bookId) {
    return Blocking.get(()->getBookData(bookId));
  }


  private List<BookData> getCollectBooks() {
    return context
      .select(BOOK.fields())
      .from(BOOK)
      .fetchInto(BookData.class)
      .stream()
      .collect(Collectors.toList());
  }


  private BookData getBookData(int bookId) {
    return context.select(Tables.BOOK.fields()).from(Tables.BOOK).where(Tables.BOOK.ID.eq(bookId)).fetchInto(BookData.class)
      .stream()
      .findFirst()
      .get();
  }



}
