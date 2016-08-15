package ow.micropos.server.common;

import email.com.gmail.ttsai0509.serial.config.*;

import java.util.List;


/******************************************************************
 *                                                                *
 * fromProperties(properties, "printer0");
 *
 * Properties File :
 *  printer0-name=RECEIPT
 *  printer0-device=COM18
 *  printer0-baud=BAUD_9600
 *  printer0-data=DATABITS_8
 *  printer0-parity=PARITY_NONE
 *  printer0-stop=STOPBITS_1
 *  printer0-flow=FLOWCONTROL_NONE
 *                                                                *
 ******************************************************************/

public class PrinterConfig {

    public static PrinterConfig fromProperties(TypedProperties properties, String printerPrefix) {

        List<String> keys = properties.getProperties(printerPrefix);

        String name = null;
        String device = null;
        Baud baud = null;
        DataBits data = null;
        Parity parity = null;
        StopBits stop = null;
        FlowControl flow = null;

        for (String key : keys) {
            if (key.endsWith("name"))
                name = properties.getStr(key);
            else if (key.endsWith("device"))
                device = properties.getStr(key);
            else if (key.endsWith("baud"))
                baud = properties.getEnum(Baud.class, key);
            else if (key.endsWith("data"))
                data = properties.getEnum(DataBits.class, key);
            else if (key.endsWith("parity"))
                parity = properties.getEnum(Parity.class, key);
            else if (key.endsWith("stop"))
                stop = properties.getEnum(StopBits.class, key);
            else if (key.endsWith("flow"))
                flow = properties.getEnum(FlowControl.class, key);
        }

        if (baud == null || data == null || parity == null || stop == null || flow == null)
            return new PrinterConfig(name, device, null);
        else
            return new PrinterConfig(name, device, new SerialConfig(baud, data, parity, stop, flow));

    }

    public final String name;
    public final String device;
    public final SerialConfig serialConfig;

    public PrinterConfig(
            String name,
            String device,
            SerialConfig serialConfig
    ) {
        this.name = name;
        this.device = device;
        this.serialConfig = serialConfig;
    }

}
