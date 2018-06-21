package com.jvm.isa.service;

import static com.jvm.isa.service.UserServiceConstants.TEST_CITY;
import static com.jvm.isa.service.UserServiceConstants.TEST_EMAIL;
import static com.jvm.isa.service.UserServiceConstants.TEST_FIRST_NAME;
import static com.jvm.isa.service.UserServiceConstants.TEST_LAST_NAME;
import static com.jvm.isa.service.UserServiceConstants.TEST_PASSWORD;
import static com.jvm.isa.service.UserServiceConstants.TEST_INCORRECT_REPEAT_PASSWORD;
import static com.jvm.isa.service.UserServiceConstants.TEST_CORRECT_REPEAT_PASSWORD;
import static com.jvm.isa.service.UserServiceConstants.TEST_PHONE_NUMBER;
import static com.jvm.isa.service.UserServiceConstants.TEST_USERNAME;
import static com.jvm.isa.service.UserServiceConstants.TEST_EMPTY;
import static com.jvm.isa.service.UserServiceConstants.TEST_INCORRECT_OLD_PASSWORD;
import static com.jvm.isa.service.UserServiceConstants.TEST_INCORRECT_EMAIL;
import static com.jvm.isa.service.UserServiceConstants.TEST_INCORRECT_PHONE_NUMBER;

import static com.jvm.isa.service.UserServiceConstants.EVERYTHING_IS_RIGHT;
import static com.jvm.isa.service.UserServiceConstants.INCORRECT_PHONE_NUMBER;
import static com.jvm.isa.service.UserServiceConstants.INCORRECT_EMAIL;
import static com.jvm.isa.service.UserServiceConstants.YOU_ARE_NOT_THE_FIRST_TO_ENTER_THE_SAME_NEW_PASSWORD_THE_SECOND_TIME;
import static com.jvm.isa.service.UserServiceConstants.YOU_DID_NOT_ENTER_THE_CORRECT_OLD_PASSWORD;
import static com.jvm.isa.service.UserServiceConstants.THIS_USERNAME_ALREADY_EXISTS;
import static com.jvm.isa.service.UserServiceConstants.ALL_FIELDS_ARE_NOT_FILLED;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.User;
import com.jvm.isa.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void beforeTesting() {
		userRepository.deleteAll();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ BEFORE #######################################");
	}
	
	/*@AfterClass
	public static void afterClassTesting() {
		userRepository.deleteAll();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ BEFORE #######################################");
	}*/
	
	/*@After
	public void afterTesting() {
		userRepository.deleteAll();
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ AFTER #######################################");
	}*/
	
	@Test
	@Transactional
	@Rollback(true) //it can be omitted because it is true by default
	public void testRegistrate() {
		RegisteredUser ru = new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		boolean success = userService.registrate(ru);
		
		assertTrue(success);
	}
	
	@Test
	@Transactional
	@Rollback(true) //it can be omitted because it is true by default
	public void testExists() {
		boolean fail = userService.exists(TEST_USERNAME);
		assertFalse(fail);
		
		RegisteredUser ru = new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		userRepository.save(ru);
		
		boolean success = userService.exists(TEST_USERNAME);
		assertTrue(success);
	}
	
	@Test
	@Transactional
	@Rollback(true) //it can be omitted because it is true by default
	public void testGetUser() {
		User user1 = userService.getUser(TEST_USERNAME, TEST_PASSWORD);
		assertNull(user1);
		
		RegisteredUser ru = new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		userRepository.save(ru);
		
		User user2 = userService.getUser(TEST_USERNAME, TEST_PASSWORD);
		assertNotNull(user2);
		assertThat(user2.getUsername()).isEqualTo(TEST_USERNAME);
		assertThat(user2.getPassword()).isEqualTo(TEST_PASSWORD);
	}
	
	
	@Test
	public void testCorrectUser() {
		User oldUser = null;
		int res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(EVERYTHING_IS_RIGHT, res);
		
		res = userService.correctUser(oldUser, TEST_EMPTY, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_EMPTY, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_PASSWORD, TEST_EMPTY, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_EMPTY, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_EMPTY, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMPTY, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_EMPTY, TEST_PHONE_NUMBER);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_EMPTY);
		assertEquals(ALL_FIELDS_ARE_NOT_FILLED, res);
		
		res = userService.correctUser(null, TEST_USERNAME, null, TEST_PASSWORD, TEST_INCORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(YOU_ARE_NOT_THE_FIRST_TO_ENTER_THE_SAME_NEW_PASSWORD_THE_SECOND_TIME, res);
		
		res = userService.correctUser(null, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_INCORRECT_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(INCORRECT_EMAIL, res);
		
		res = userService.correctUser(null, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_INCORRECT_PHONE_NUMBER);
		assertEquals(INCORRECT_PHONE_NUMBER, res);
		
		RegisteredUser newUser = new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER); 
		oldUser = userRepository.save(newUser);
		
		res = userService.correctUser(null, TEST_USERNAME, null, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(THIS_USERNAME_ALREADY_EXISTS, res);
		
		res = userService.correctUser(oldUser, TEST_USERNAME, TEST_INCORRECT_OLD_PASSWORD, TEST_PASSWORD, TEST_CORRECT_REPEAT_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		assertEquals(YOU_DID_NOT_ENTER_THE_CORRECT_OLD_PASSWORD, res);
	}
}
