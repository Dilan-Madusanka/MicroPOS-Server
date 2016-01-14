package ow.micropos.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import email.com.gmail.ttsai0509.escpos.com.ComUtils;
import email.com.gmail.ttsai0509.print.dispatcher.PrinterDispatcher;
import email.com.gmail.ttsai0509.print.dispatcher.PrinterDispatcherAsync;
import email.com.gmail.ttsai0509.print.printer.RawPrinter;
import email.com.gmail.ttsai0509.utils.LoggerOutputStream;
import email.com.gmail.ttsai0509.utils.NullOutputStream;
import email.com.gmail.ttsai0509.utils.PrinterConfig;
import email.com.gmail.ttsai0509.utils.TypedProperties;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ow.micropos.server.custom.WokPrintJobBuilder;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.menu.*;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.model.target.Seat;
import ow.micropos.server.model.target.Section;
import ow.micropos.server.repository.auth.PositionRepository;
import ow.micropos.server.repository.employee.EmployeeRepository;
import ow.micropos.server.repository.menu.*;
import ow.micropos.server.repository.target.CustomerRepository;
import ow.micropos.server.repository.target.SeatRepository;
import ow.micropos.server.repository.target.SectionRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// @SpringBootApplication
// IntelliJ IDEA 14.0.3 does not properly handle annotation.
// We must manually declare each sub-annotation to avoid
// autowiring problems.

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Bean
    PrinterDispatcher printerDispatcher() throws IOException {

        PrinterDispatcher dispatcher = new PrinterDispatcherAsync();

        TypedProperties properties = new TypedProperties(new FileSystemResource("printer.properties").getInputStream());

        // Get Printer Configurations (See printer.properties for proper format)
        int count = properties.getInt("printers");
        PrinterConfig[] printerConfigs = new PrinterConfig[count];
        for (int i = 0; i < count; i++)
            printerConfigs[i] = PrinterConfig.fromProperties(properties, "printer" + i);

        new Thread(() -> {

            Map<String, OutputStream> deviceMap = new HashMap<>();

            // Load default devices
            deviceMap.put("CLI", System.out);
            deviceMap.put("LOG", new LoggerOutputStream(PrinterDispatcher.class, LoggerOutputStream.Level.INFO));
            deviceMap.put("NULL", new NullOutputStream());

            // Load config devices
            for (PrinterConfig printerConfig : printerConfigs) {

                // Open SerialPort if it's not opened yet, and we are able to.
                if (!deviceMap.containsKey(printerConfig.device) && printerConfig.serialConfig != null) {
                    try {
                        SerialPort sp = ComUtils.connectSerialPort(printerConfig.device, 2000, printerConfig
                                .serialConfig);
                        deviceMap.put(printerConfig.device, sp.getOutputStream());

                    } catch (Error e) {
                        log.error("Check serial drivers. " + printerConfig.name + " using LOG.");

                    } catch (Exception e) {
                        log.warn("Check serial port " + printerConfig.device + ". " + printerConfig.name + " using " +
                                "LOG");

                    }
                }

                OutputStream device = deviceMap.get(printerConfig.device);

                // Device issues default to the Logger
                if (device == null)
                    dispatcher.registerPrinter(printerConfig.name, new RawPrinter(deviceMap.get("LOG")));
                else
                    dispatcher.registerPrinter(printerConfig.name, new RawPrinter(device));

            }

            // Dispatcher runs on same thread as opened OutputStreams
            dispatcher.run();

        }).start();

        return dispatcher;

    }

    @Bean
    WokPrintJobBuilder printJobBuilder() {
        return new WokPrintJobBuilder(21);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);

        context.getBean(Application.class).reset();

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

    /******************************************************************
     *                                                                *
     * For use with create-drop auto-ddl. Automatically load defaults *
     * for testing.                                                   *
     *                                                                *
     ******************************************************************/

    @Autowired
    ApplicationContext context;

    private void reset() {
        reset(context, ChargeRepository.class, "charges", new TypeReference<List<Charge>>() {});
        reset(context, PositionRepository.class, "positions", new TypeReference<List<Position>>() {});
        reset(context, EmployeeRepository.class, "employees", new TypeReference<List<Employee>>() {});
        reset(context, CustomerRepository.class, "customers", new TypeReference<List<Customer>>() {});
        reset(context, SectionRepository.class, "sections", new TypeReference<List<Section>>() {});
        reset(context, SeatRepository.class, "seats", new TypeReference<List<Seat>>() {});
        reset(context, CategoryRepository.class, "categories", new TypeReference<List<Category>>() {});
        reset(context, MenuItemRepository.class, "menuitems", new TypeReference<List<MenuItem>>() {});
        reset(context, ModifierGroupRepository.class, "modifiergroups", new TypeReference<List<ModifierGroup>>() {});
        reset(context, ModifierRepository.class, "modifiers", new TypeReference<List<Modifier>>() {});
    }

    private <T> void reset(
            ApplicationContext context,
            Class<? extends JpaRepository> repositoryClass,
            String resource,
            TypeReference<List<T>> listTypeRef
    ) {

        try {
            ObjectMapper mapper = context.getBean(ObjectMapper.class);
            JpaRepository repo = context.getBean(repositoryClass);
            Resource res = context.getResource("file:./test/" + resource + ".json");
            List<T> items = mapper.readValue(res.getFile(), listTypeRef);
            log.info("Loaded " + repo.save(items).size() + " items into " + repositoryClass.getSimpleName() + " from" +
                    " " + res.getFilename());
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

}
