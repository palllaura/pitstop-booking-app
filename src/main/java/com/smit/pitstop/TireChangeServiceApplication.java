package com.smit.pitstop;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "pitstop-booking-app")
public class TireChangeServiceApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TireChangeServiceApplication.class, args);
    }

}
