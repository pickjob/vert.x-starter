package app.route;

import app.common.IRoute;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.Set;

public class CORSRoute implements IRoute {

    @Override
    public void configureRouter(Router router) {
        router.route()
              .order(-1)
              .handler(
                      CorsHandler.create("*")
                                 .allowedMethods(Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS))
//                                 .allowCredentials(true)
                                 .allowedHeader("Content-Type")
              )
        ;
    }
}
