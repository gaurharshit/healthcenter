package com.gaur.healthcenter.config;

import static org.h2.tools.Server.createWebServer;

import lombok.extern.log4j.Log4j2;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 * This is a hack to start H2 server separately since, the application is using spring-boot-starter-reactor-netty.
 * H2ConsoleAutoConfiguration is not executed for spring webflux & netty (reactor based).
 * H2 server is started manually with this class as component.
 */
@Component
@Log4j2
public class H2ServerManual {

    @Value("${h2-server.port}")
    Integer h2ConsolePort;
    private Server webServer;

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        log.info("starting h2 console at port " + h2ConsolePort);
        this.webServer = createWebServer("-webPort", h2ConsolePort.toString(),
            "-tcpAllowOthers", "-webAllowOthers").start();
        log.info("H2 running at: {}", webServer.getURL());
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        log.info("stopping h2 console at port " + h2ConsolePort);
        this.webServer.stop();
    }
}