package ow.micropos.server.common;


import email.com.gmail.ttsai0509.print.dispatcher.PrinterDispatcher;
import email.com.gmail.ttsai0509.print.dispatcher.PrinterDispatcherAsync;
import email.com.gmail.ttsai0509.print.printer.JSerialFactoryPrinter;
import email.com.gmail.ttsai0509.print.printer.OutputStreamFactoryPrinter;
import email.com.gmail.ttsai0509.print.printer.OutputStreamPrinter;
import email.com.gmail.ttsai0509.serial.SerialFactory;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public enum PrinterDispatcherFactory {

    ; // Factory

    private static final Logger log = LoggerFactory.getLogger(PrinterDispatcherFactory.class);

    /*
     * No Timeout Support for RXTX OutputStream.write()
     * https://groups.google.com/forum/#!topic/comp.lang.java.programmer/3cigxj5X1Rg
     * http://stackoverflow.com/questions/3843363/thread-interrupt-not-ending-blocking-call-on-input-stream-read
     */

    public static PrinterDispatcher transientConnectionDispatcher() throws IOException {
        PrinterDispatcher dispatcher = new PrinterDispatcherAsync();

        TypedProperties properties = new TypedProperties(new FileSystemResource("printer.properties").getInputStream());

        // Get Printer Configurations (See printer.properties for proper format)
        int count = properties.getInt("printers");
        PrinterConfig[] printerConfigs = new PrinterConfig[count];
        for (int i = 0; i < count; i++)
            printerConfigs[i] = PrinterConfig.fromProperties(properties, "printer" + i);

        // Start PrintDispatcher on a separate thread
        new Thread(() -> {
            for (PrinterConfig printerConfig : printerConfigs) {

                if (printerConfig.device.equals("CLI") || printerConfig.device.equals("LOG")) {
                    dispatcher.registerPrinter(
                            printerConfig.name,
                            new OutputStreamFactoryPrinter(NoCloseSystemOutputStream::new)
                    );

                } else if (printerConfig.serialConfig != null) {
                    dispatcher.registerPrinter(
                            printerConfig.name,
                            new JSerialFactoryPrinter(printerConfig.device, printerConfig.serialConfig)
                    );
                }
            }

            dispatcher.run();

        }).start();

        return dispatcher;
    }

    public static PrinterDispatcher persistentConnectionDispatcher() throws IOException {

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

            // Load config devices
            for (PrinterConfig printerConfig : printerConfigs) {

                // Open SerialPort if it's not opened yet, and we are able to.
                if (!deviceMap.containsKey(printerConfig.device) && printerConfig.serialConfig != null) {
                    try {
                        SerialPort sp = SerialFactory.rxtxSerialPort(printerConfig.device, printerConfig.serialConfig, 2000);
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
                    dispatcher.registerPrinter(printerConfig.name, new OutputStreamPrinter(deviceMap.get("LOG")));
                else
                    dispatcher.registerPrinter(printerConfig.name, new OutputStreamPrinter(device));

            }

            // Dispatcher runs on same thread as opened OutputStreams
            dispatcher.run();

        }).start();

        return dispatcher;
    }
}
