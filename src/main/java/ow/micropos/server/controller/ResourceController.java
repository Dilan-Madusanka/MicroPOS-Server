package ow.micropos.server.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(value = "")
public class ResourceController {

    private final String filesPath = System.getProperty("user.dir") + "/public";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

    @ResponseBody
    @RequestMapping(value = "/md5/{file:.+}", method = RequestMethod.GET)
    public String getMD5(
            HttpServletResponse response,
            @PathVariable("file") String file
    ) {

        response.addHeader("Content-Type", "text/plain");
        response.addHeader("Content-Disposition", "inline; filename=" + file);

        FileSystemResource resource = new FileSystemResource(filesPath + "/" + file);

        try (FileInputStream fis = new FileInputStream(resource.getFile())) {
            return DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    @RequestMapping(value = "/gen/{file:.+}", method = RequestMethod.GET)
    public String getProperties(
            HttpServletResponse response,
            @PathVariable("file") String file
    ) {

        String url = getUrl();

        String output;

        switch (file) {
            case "micropos.bat":
                output = "@echo off \n" +
                        "curl.exe -sS " + url + "/file/client.jar --output client.jar\n" +
                        "java -jar client.jar --url=" + url;
                break;

            case "micropos32.bat":
                output = "@echo off \n" +
                        "curl32.exe -sS " + url + "/file/client.jar --output client.jar\n" +
                        "java -jar client.jar --url=" + url + "\n";
                break;

            default:
                output = "";
                break;
        }

        response.setHeader("Content-Length", String.valueOf(output.length()));
        response.setHeader("Content-Type", "text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=" + file);
        return output;
    }

    @RequestMapping(value = "/file/{file:.+}", method = RequestMethod.GET)
    public FileSystemResource getFile(
            HttpServletResponse response,
            @PathVariable("file") String file
    ) {

        FileSystemResource resource = new FileSystemResource(filesPath + "/" + file);

        String resourceLength = "";
        try {
            resourceLength = Long.toString(resource.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setHeader("Content-Length", resourceLength);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + file);
        return resource;
    }

    private String getPort() {
        return "80";
    }

    private String getAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    private String getUrl() {
        return "http://" + getAddress() + ":" + getPort();
    }

}
