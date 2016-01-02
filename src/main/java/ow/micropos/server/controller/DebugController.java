package ow.micropos.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.service.PrintService;

@RestController
@RequestMapping(value = "/debug")
public class DebugController {

    @Autowired PrintService printService;


}
