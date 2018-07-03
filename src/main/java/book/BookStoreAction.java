package book;

import com.jooq.h2.spring.BookData;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;
import ratpack.path.PathTokens;

import static com.tgt.oss.ratpack.protobuf.Protobuf.protobuf;


public class BookStoreAction implements Action<Chain> {
  @Override
  public void execute(Chain chain) throws Exception {
    chain
      .path(ctx -> {
        BookService service = ctx.get(BookService.class);
        ctx
          .byMethod(method -> method
            .get(() -> service
              .getBooks()
              .then(book -> ctx.render(protobuf(book)))
            )
            .post(() -> ctx
              .parse(Jackson.fromJson(BookData.class)) // Not parsing from Json. Parse it from protobuf
              .nextOp(service::addBook)
              .map(m -> "Added book for ")
              .then(ctx::render))
          );
      })
      .get(":id", ctx -> {
        BookService service = ctx.get(BookService.class);
        PathTokens pathTokens = ctx.getPathTokens();
        service
          .getBookById(Integer.parseInt(pathTokens.get("id")))
          .then(book->ctx.render(protobuf(book)));
      });
  }
}
