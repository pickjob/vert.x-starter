package app;

import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(VertxExtension.class)
@DisplayName("Unit test for HelloVerticle")
public class HelloVerticleTest {
    private static final Logger logger = LogManager.getLogger(HelloVerticleTest.class);

    @Test
    @DisplayName("test default rouet /{UUID}")
    void testDefaultRoute(Vertx vertx, VertxTestContext testContext) {
        WebClient webClient = WebClient.create(vertx);
        Checkpoint deploymentCheckpoint = testContext.checkpoint();
        Checkpoint requestCheckpoint = testContext.checkpoint();

        vertx.deployVerticle(new HelloVerticle(), testContext.succeeding(id -> {
            deploymentCheckpoint.flag();
            webClient.get(8080, "localhost", "/" + UUID.randomUUID().toString())
                     .as(BodyCodec.string())
                     .send(testContext.succeeding(resp -> {
                         logger.info("status: {}", resp.statusCode());
                         logger.info("body: {}", resp.body());
                         testContext.verify(() -> {
                             Assertions.assertEquals(resp.statusCode(), 200);
                             requestCheckpoint.flag();
                         });
                     }));
        }));
    }

    @Test
    @DisplayName("test zookeeper rouet /zookeeper")
    void testZookeepeRoute(Vertx vertx, VertxTestContext testContext) {
        WebClient webClient = WebClient.create(vertx);
        Checkpoint requestCheckpoint = testContext.checkpoint();

        vertx.deployVerticle(new HelloVerticle(), testContext.succeeding(id -> {
            webClient.get(8080, "localhost", "/zookeeper")
                    .as(BodyCodec.string())
                    .send(testContext.succeeding(resp -> {
                        logger.info("status: {}", resp.statusCode());
                        logger.info("body: {}", resp.body());
                        testContext.verify(() -> {
                            Assertions.assertEquals(resp.statusCode(), 200);
                            requestCheckpoint.flag();
                        });
                    }));
        }));
    }
}
