package com.wherescape.vmsmicroservice.cucumber.stepdefs;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import com.testcomapny.vmsmicroservice.VmsmicroserviceApp;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = VmsmicroserviceApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
