package com.exc.unipi_agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
public class UnipiAgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnipiAgendaApplication.class, args);
		openPage();
	}
	static void openPage(){
		new RedirectView("/");
	}
}
