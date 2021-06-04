package com.exc.unipi_agenda;

import com.exc.unipi_agenda.model.Meeting;
import com.exc.unipi_agenda.model.Participant;
import com.exc.unipi_agenda.model.User;
import com.exc.unipi_agenda.model.UserNotification;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SpringBootTest
class UnipiAgendaApplicationTests {

	@Test
	void contextLoads() {
		Model m1 = new Model() {
			@Override
			public Model addAttribute(String s, Object o) {
				return null;
			}

			@Override
			public Model addAttribute(Object o) {
				return null;
			}

			@Override
			public Model addAllAttributes(Collection<?> collection) {
				return null;
			}

			@Override
			public Model addAllAttributes(Map<String, ?> map) {
				return null;
			}

			@Override
			public Model mergeAttributes(Map<String, ?> map) {
				return null;
			}

			@Override
			public boolean containsAttribute(String s) {
				return false;
			}

			@Override
			public Object getAttribute(String s) {
				return null;
			}

			@Override
			public Map<String, Object> asMap() {
				return null;
			}
		};
		User u = User.login("Brinel","123",m1);
		if (u==null){
			System.out.println("null u User");
			return;
		}
		for (UserNotification n:u.getNotificationList()){
			System.out.println("Notifications----");
			System.out.println("date: "+n.getDatetime());
			System.out.println("msg: "+n.getMessage());
			System.out.println("viewed: "+n.isViewed());
		}

		for (Meeting m:u.getMeetings()){
			System.out.println(m.getName()+"----------------");
			System.out.println("id: "+m.getId());
			System.out.println("admin: "+m.getAdmin().getUsername());
			System.out.println("duration: "+m.getDuration());
			System.out.println("date: "+m.getDatetime());
			for (Participant p:m.getParticipants()){
				System.out.println("participant: "+p.getUsername());

			}
		}
	}

}
