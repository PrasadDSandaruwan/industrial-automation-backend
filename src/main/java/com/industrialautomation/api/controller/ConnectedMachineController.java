package com.industrialautomation.api.controller;


import com.industrialautomation.api.service.ConnectedMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectedMachineController {

    @Autowired
    private ConnectedMachineService connectedMachineService;


}
