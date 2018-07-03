package book;

import com.jooq.h2.spring.BookData;
import com.jooq.h2.spring.BookDataProto;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultBookService implements BookService {

  private final BookRepository bookRepository;

  public DefaultBookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public Promise<BookDataProto.BookList> getBooks() {
    return convertToBookProtoList(bookRepository.getBooks());
  }


  @Override
  public Promise<BookDataProto.Book> getBookById(int bookId) {
    return convertPromiseBookToBookProto(bookRepository.getBookId(bookId));
  }

  private Promise<BookDataProto.Book> convertPromiseBookToBookProto(Promise<BookData> bookDataPromise) {
    return bookDataPromise.flatMap(bookData ->
        Promise.value(
        convertToBookProto(bookData))
      );
  }


  @Override
  public Operation addBook(BookData bookData) {
    return bookRepository.addBook(bookData);
  }


  private Promise<BookDataProto.BookList> convertToBookProtoList(Promise<List<BookData>> promiseBookDataList) {
    BookDataProto.BookList bookProtoList = BookDataProto.BookList.newBuilder().build();
    return promiseBookDataList.flatMap(bookDataPromise ->
      Promise.value(
        (bookDataPromise
          .stream()
          .map(bookData -> convertToBookProto(bookData))
          .map(book -> bookProtoList.toBuilder().addBook(book).buildPartial()))
          .collect(Collectors.toList()).get(0)
      ).onError(throwable -> throwable.getMessage())
    );
  }


  private BookDataProto.Book convertToBookProto(BookData bookData) {
    return BookDataProto.Book.newBuilder()
      .setAuthorId(bookData.getAuthorId())
      .setPublished(bookData.getPublishedIn())
      .setBookTitle(bookData.getTitle())
      .build();
  }


}
