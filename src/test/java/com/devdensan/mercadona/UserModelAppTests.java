package com.devdensan.mercadona;

import com.devdensan.mercadona.model.Role;
import com.devdensan.mercadona.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserModelAppTests {

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createRole() {
		// given
		String roleName = "ADMIN";

		// when
		Role role = new Role(roleName);
		String result = role.toString();

		// then
		String expected = "Role{roleId=0, roleName='ADMIN'}";
		assertEquals(expected, result);
	}

	@Test
	void createUser() {
		// given
		String userName = "main_user";
		String password = "123456";
		String email = "123456@devdensan.com";
		Role role = new Role("ADMIN");

		// when
		User user = new User(userName, password, role, email);
		String result = user.toString();

		// then
		String expected = "User{userId=0, userName='main_user', password='123456', role=Role{roleId=0, roleName='ADMIN'}, email='123456@devdensan.com'}";
		assertEquals(expected, result);
	}

}
