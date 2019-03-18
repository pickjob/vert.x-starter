package app.route;

import app.common.IRoute;
import app.domain.MyFormBase;
import app.domain.MyFormText;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class HttpClientTestRoute implements IRoute {
    private static final Logger logger = LogManager.getLogger(HttpClientTestRoute.class);

    @Override
    public void configureRouter(Router router) {
        String baseUrl = "/http-client-test";
        router.get(baseUrl)
              .order(1)
              .handler(routingContext -> {
                  logger.info("queryNames: {}", routingContext.queryParam("name"));
                  JsonObject jsonObject = new JsonObject();
                  jsonObject.put("name", "Helen");
                  jsonObject.put("age", 14);
                  routingContext.response()
                                .putHeader("Content-Type", "application/json")
                                .end(jsonObject.toBuffer());
              })
              ;
        router.post(baseUrl)
              .order(1)
              .handler(routingContext -> {
                  JsonObject jsonObject = routingContext.getBodyAsJson();
                  logger.info("jsonObject: {}", jsonObject.toString());
                  routingContext.response()
                                .putHeader("Content-Type", "application/json")
                                .end(jsonObject.toBuffer());
              })
              ;
        // upload files
        router.post(baseUrl + "/files")
              .order(1)
              .handler(routingContext -> {
                  Set<FileUpload> uploads = routingContext.fileUploads();
                  for (FileUpload file : uploads) {
                      logger.info("fileName: {}, tmpFile: {}", file.fileName(), file.uploadedFileName());
                  }
                  routingContext.response().end("success");
              })
              ;
    }
}