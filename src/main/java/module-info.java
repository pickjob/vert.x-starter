module pickjob.vertx.starter {
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires com.google.common;

    requires vertx.core;
    requires vertx.web;

    opens app to vertx.core
            ;
    opens app.domain to com.fasterxml.jackson.databind
            ;
}
