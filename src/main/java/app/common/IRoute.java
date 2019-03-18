package app.common;

import io.vertx.ext.web.Router;

public interface IRoute {

    void configureRouter(Router router);
}
