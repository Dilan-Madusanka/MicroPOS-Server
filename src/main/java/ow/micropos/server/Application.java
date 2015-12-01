package ow.micropos.server;

import email.com.gmail.ttsai0509.escpos.ESCPos;
import email.com.gmail.ttsai0509.escpos.PrinterDispatcher;
import email.com.gmail.ttsai0509.escpos.PrinterDispatcherEmpty;
import email.com.gmail.ttsai0509.escpos.PrinterDispatcherImpl;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;


// @SpringBootApplication
// IntelliJ IDEA 14.0.3 does not properly handle annotation.
// We must manually declare each sub-annotation to avoid
// autowiring problems.

// Interface repositories must be excluded from
// @EnableAutoConfiguration. I think they are being automatically
// imported as repositories, but fails because both are repositories
// of unmanaged beans: Identifiable and Archivable.

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan
public class Application {

    @Bean(name = "printerDispatcher")
    PrinterDispatcher printerDispatcher() throws NoSuchPortException, PortInUseException,
            UnsupportedCommOperationException, IOException {
        SerialPort sp = ESCPos.connectSerialPort("COM1");
        PrinterDispatcher pd = new PrinterDispatcherImpl(sp.getOutputStream());
        //PrinterDispatcher pd = new PrinterDispatcherEmpty();
        new Thread(pd).start();
        return pd;
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class);


        context.getBean(Initializer.class).init();

    }

}
