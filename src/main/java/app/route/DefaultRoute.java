package app.route;

import app.common.IRoute;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class DefaultRoute implements IRoute {

    @Override
    public void configureRouter(Router router) {
        router.route()
              .order(Integer.MAX_VALUE)
              .handler(routingContext -> {
                  routingContext.response().end("Hello vertx!");
              })
        ;
    }
}
