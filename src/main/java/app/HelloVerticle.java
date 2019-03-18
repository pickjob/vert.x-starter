package app;

import app.common.IRoute;
import app.utils.ScanUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(HelloVerticle.class);
    private HttpServer server = null;

    @Override
    public void start() throws Exception {
        server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setUploadsDirectory("uploads"));
        configureRouter(router);
        server.requestHandler(router)
              .listen(8080);
    }

    @Override
    public void stop() throws Exception {
        if (server != null) {
            server.close();
        }
    }

    private void configureRouter(Router router) {
        String packageName = this.getClass().getPackage().getName() + ".route";
        try {
            for (String clsName : ScanUtil.scanClasses(packageName)) {
                Class<?> cls = Class.forName(clsName);
                Object obj = cls.getDeclaredConstructor().newInstance();
                if (obj instanceof IRoute) {
                    logger.info("init router: {}", clsName);
                    ((IRoute)obj).configureRouter(router);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloVerticle());
    }
}
