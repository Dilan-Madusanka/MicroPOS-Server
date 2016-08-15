package ow.micropos.server;

import email.com.gmail.ttsai0509.print.dispatcher.PrinterDispatcher;
import ow.micropos.server.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
@EnableScheduling
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Bean
    PrinterDispatcher printerDispatcher() throws IOException {
        return PrinterDispatcherFactory.transientConnectionDispatcher();
    }

    @Bean
    PrintJobBuilder printJobBuilder() {
        return new PrintJobBuilder(21);
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class);

        try {
            log.info("MicroPOS Server Host Address : " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            log.info("MicroPOS Server Host Address : UNKNOWN");
        }

        try {
            log.info("MicroPOS Server Host Name    : " + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            log.info("MicroPOS Server Host Name    : UNKNOWN");
        }

    }

}

