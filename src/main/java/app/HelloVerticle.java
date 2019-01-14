package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class HelloVerticle extends AbstractVerticle {
    private HttpServer server = null;

    @Override
    public void start() throws Exception {
        server = vertx.createHttpServer();
        server.requestHandler(request -> {
            request.response().end("Hello Vert.x");
        }).listen(8080);
    }

    @Override
    public void stop() throws Exception {
        if (server != null) {
            server.close();
        }
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HelloVerticle.class.getCanonicalName());
    }
}
