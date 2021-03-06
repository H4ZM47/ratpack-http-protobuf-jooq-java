package util;

import book.BookRepository;
import book.DefaultBookRepository;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import javax.inject.Singleton;
import javax.sql.DataSource;

public class JooqModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(BookRepository.class).to(DefaultBookRepository.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  public DSLContext dslContext(DataSource dataSource) {
    return DSL.using(new DefaultConfiguration().derive(dataSource));
  }
}
