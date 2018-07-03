import book.BookStoreAction;
import book.BookStoreModule;
import com.google.common.collect.Lists;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import util.HerokuUtils;
import util.JooqModule;

import java.util.List;


public class App {
  public static void main(String[] args) throws Exception {
    List<String> programArgs = Lists.newArrayList(args);
    programArgs.addAll(
      HerokuUtils.extractDbProperties
        .apply(System.getenv("DATABASE_URL"))
    );

    RatpackServer.start(serverSpec -> serverSpec
      .serverConfig(config -> config
        .baseDir(BaseDir.find())
        //.yaml("h2.yaml")
        //.yaml("redis.yaml")
        .env()
        .sysProps()
        .args(programArgs.stream().toArray(String[]::new))
        //.require("/db", HikariConfig.class)
       // .require("/redis", RedisConfig.class)
      )
      .registry(Guice.registry(bindings -> bindings
        .module(HikariModule.class, config -> {
          config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
          //config.addDataSourceProperty("URL", "jdbc:h2:~/bookStore;INIT=RUNSCRIPT FROM 'classpath:/V1.2__create_schema_h2.sql'");
          config.addDataSourceProperty("URL", "jdbc:h2:~/bookStore");
          config.setUsername("sa");
          config.setPassword("");
        })
        .module(JooqModule.class)
       // .module(RedisModule.class)
        .module(BookStoreModule.class)
        .bind(BookStoreAction.class)
      ))
      .handlers(chain -> chain.
        prefix("book", BookStoreAction.class)
        .get(ctx -> ctx.render("Hello World!"))
      )

    );
  }
}
