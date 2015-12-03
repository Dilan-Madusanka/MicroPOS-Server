package ow.micropos.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import email.com.gmail.ttsai0509.escpos.ESCPos;
import email.com.gmail.ttsai0509.print.Printer;
import email.com.gmail.ttsai0509.print.PrinterDispatcher;
import email.com.gmail.ttsai0509.print.PrinterDispatcherAsync;
import email.com.gmail.ttsai0509.utils.LoggerOutputStream;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.charge.Charge;
import ow.micropos.server.model.menu.Category;
import ow.micropos.server.model.menu.MenuItem;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.menu.ModifierGroup;
import ow.micropos.server.model.people.Customer;
import ow.micropos.server.model.people.Employee;
import ow.micropos.server.model.seating.Seat;
import ow.micropos.server.model.seating.Section;
import ow.micropos.server.repository.auth.PositionRepository;
import ow.micropos.server.repository.charge.ChargeRepository;
import ow.micropos.server.repository.menu.CategoryRepository;
import ow.micropos.server.repository.menu.MenuItemRepository;
import ow.micropos.server.repository.menu.ModifierGroupRepository;
import ow.micropos.server.repository.menu.ModifierRepository;
import ow.micropos.server.repository.people.CustomerRepository;
import ow.micropos.server.repository.people.EmployeeRepository;
import ow.micropos.server.repository.seating.SeatRepository;
import ow.micropos.server.repository.seating.SectionRepository;

import java.io.IOException;
import java.io.OutputStream;
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

    @Value("${micropos.printers}")
    @Bean(name = "printerDispatcher")
    PrinterDispatcher printerDispatcher(String microposPrinters) {

        PrinterDispatcher pd = new PrinterDispatcherAsync();

        String[] printerConfigs = microposPrinters.split(":");

        Map<String, OutputStream> deviceMap = new HashMap<>();
        deviceMap.put("CLI", System.out);
        deviceMap.put("LOG", new LoggerOutputStream(PrinterDispatcher.class, LoggerOutputStream.Level.INFO));

        for (String printerConfig : printerConfigs) {

            String[] config = printerConfig.split(",");

            if (config.length < 2) {
                log.warn("Invalid printer configuration : " + printerConfig);
                continue;
            }

            String printerName = config[0];
            String printerDevice = config[1];

            if (!deviceMap.containsKey(printerDevice)) {
                try {
                    SerialPort sp = ESCPos.connectSerialPort(printerDevice);
                    deviceMap.put(printerDevice, sp.getOutputStream());
                } catch (Error e) {
                    log.error("Could not load serial drivers.");
                    deviceMap.put(printerDevice, System.out);
                } catch (Exception e) {
                    log.warn("Unable to open serial port " + printerDevice);
                    deviceMap.put(printerDevice, System.out);
                }
            }

            pd.registerPrinter(Printer.streamPrinter(printerName, deviceMap.get(printerDevice)));

        }

        new Thread(pd).start();

        return pd;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        context.getBean(Application.class).reset();

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
