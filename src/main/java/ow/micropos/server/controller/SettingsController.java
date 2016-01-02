package ow.micropos.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.model.Permission;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.SettingsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/settings")
public class SettingsController {

    @Autowired AuthService authService;
    @Autowired SettingsService sService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, String> getSettings(
            HttpServletRequest request,
            @RequestParam(value = "keys", required = true) String[] keys
    ) {

        authService.authorize(request, Permission.CLIENT_SETTINGS);

        return sService.getSettings(keys);

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean postSetting(
            HttpServletRequest request,
            @RequestParam(value = "key", required = true) String key,
            @RequestParam(value = "val", required = true) String val
    ) {

        authService.authorize(request, Permission.CLIENT_SETTINGS);

        sService.putSetting(key, val);

        return true;

    }

}
