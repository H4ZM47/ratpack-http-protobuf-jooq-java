package book;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.tgt.oss.ratpack.protobuf.ProtobufModule;

import javax.inject.Singleton;

public class BookStoreModule extends AbstractModule {


  @Override
  protected void configure() {
    install(new ProtobufModule());
  }




  @Provides
  @Singleton
  public BookService meetingService(BookRepository meetingRepository) {
    return new DefaultBookService(meetingRepository);
  }
}
