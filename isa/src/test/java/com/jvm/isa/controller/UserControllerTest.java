package com.jvm.isa.controller;


import static com.jvm.isa.controller.UserControllerConstants.EVERYTHING_IS_RIGHT;
import static com.jvm.isa.controller.UserControllerConstants.TEST_CITY;
import static com.jvm.isa.controller.UserControllerConstants.TEST_CORRECT_REPEAT_PASSWORD;
import static com.jvm.isa.controller.UserControllerConstants.TEST_EMAIL;
import static com.jvm.isa.controller.UserControllerConstants.TEST_FIRST_NAME;
import static com.jvm.isa.controller.UserControllerConstants.TEST_INCORRECT_OLD_PASSWORD;
import static com.jvm.isa.controller.UserControllerConstants.TEST_LAST_NAME;
import static com.jvm.isa.controller.UserControllerConstants.TEST_NEW_PASSWORD;
import static com.jvm.isa.controller.UserControllerConstants.TEST_NEW_USERNAME;
import static com.jvm.isa.controller.UserControllerConstants.TEST_PASSWORD;
import static com.jvm.isa.controller.UserControllerConstants.TEST_PHONE_NUMBER;
import static com.jvm.isa.controller.UserControllerConstants.TEST_REPEAT_NEW_PASSWORD;
import static com.jvm.isa.controller.UserControllerConstants.TEST_USERNAME;
import static com.jvm.isa.controller.UserControllerConstants.YOU_DID_NOT_ENTER_THE_CORRECT_OLD_PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jvm.isa.TestUtil;
import com.jvm.isa.domain.Activation;
import com.jvm.isa.domain.RegisteredUser;
import com.jvm.isa.domain.User;
import com.jvm.isa.domain.UserStatus;
import com.jvm.isa.domain.UserType;
import com.jvm.isa.repository.ActivationRepository;
import com.jvm.isa.repository.UserRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	
	private static final String URL_PREFIX = "/users";

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			                                      MediaType.APPLICATION_JSON.getSubtype(), 
			                                      Charset.forName("utf8")
			                                     );
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ActivationRepository activationRepository;
	
	//private MockHttpSession httpSession;
	
	@PostConstruct
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		//this.httpSession = new MockHttpSession(webApplicationContext.getServletContext());
	}
	
	@Before
	public void beforeTesting() {
		userRepository.deleteAll();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ BEFORE #######################################");
	}
	
	/*@After
	public void afterTesting() {
		userRepository.deleteAll();
	}*/
	
	
	@Test
	public void testGetLoggedUser() throws Exception {
		// za rest metodu koju gadjamo koristi se HttpSession
		HashMap<String, Object> sessionAttr = new HashMap<String, Object>();
		sessionAttr.put("loggedUser", new User("-1", "-1", UserType.REGISTERED_USER, UserStatus.PENDING)); // ovaj user nam govori da trenutno nemamo 
																								//ulogovanog korisnika
		RegisteredUser ru = new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		ru.setUserStatus(UserStatus.ACTIVATED);
		userRepository.save(ru);
		// prvo smo registrovali nekog korisnika
		
		
		// TRAZENJE ULOGOVANOG
		ResultActions ra = mockMvc.perform(get(URL_PREFIX + "/logged_user").sessionAttrs(sessionAttr))
				.andExpect(status().isOk());
		String returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		User user = TestUtil.jsonToT(returnJson, User.class);
		
		assertEquals("-1", user.getUsername());
		assertEquals("-1", user.getPassword());
		
		
		// LOGOVANJE
		HashMap<String, String> hm1 = new HashMap<String, String>();
		hm1.put("username", TEST_USERNAME);
		hm1.put("password", TEST_PASSWORD);
		String json = TestUtil.objectTojson(hm1);
		ra = this.mockMvc.perform(put(URL_PREFIX + "/login").sessionAttrs(sessionAttr).contentType(contentType).content(json)).andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_PASSWORD, user.getPassword());
		sessionAttr.put("loggedUser", user);
		//-----------------------------------------------------------------------------------------------------------------------
		
		
		// TRAZENJE ULOGOVANOG
		ra = mockMvc.perform(get(URL_PREFIX + "/logged_user").sessionAttrs(sessionAttr))
				.andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		
		assertEquals(user.getUsername(), TEST_USERNAME);
		assertEquals(user.getPassword(), TEST_PASSWORD);
		
	}
	
	
	@Test
	public void testRegistrate() throws Exception {
		User user = userRepository.findByUsernameAndPassword(TEST_USERNAME, TEST_PASSWORD);
		assertNull(user);
		
		//--------------------------------------------------------------------------------------------------
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("username", TEST_USERNAME);
		hm.put("password", TEST_PASSWORD);
		hm.put("repeatPassword", TEST_CORRECT_REPEAT_PASSWORD);
		hm.put("firstName", TEST_FIRST_NAME);
		hm.put("lastName", TEST_LAST_NAME);
		hm.put("email", TEST_EMAIL);
		hm.put("city", TEST_CITY);
		hm.put("phoneNumber", TEST_PHONE_NUMBER);
		
		String json = TestUtil.objectTojson(hm);
		ResultActions ra = this.mockMvc.perform(post(URL_PREFIX + "/registrate").contentType(contentType).content(json)).andExpect(status().isOk());
		
		user = userRepository.findByUsernameAndPassword(TEST_USERNAME, TEST_PASSWORD);
		assertNotNull(user);
		
		String returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		boolean successRegistrate = TestUtil.jsonToT(returnJson, Boolean.class);
		assertTrue(successRegistrate);
		
		//-----------------------------------------------------------------------------------------------------------------------------------
		// pokusaj registrovanja istog korisnika
		
		ra = this.mockMvc.perform(post(URL_PREFIX + "/registrate").contentType(contentType).content(json)).andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		boolean failRegistrate = TestUtil.jsonToT(returnJson, Boolean.class);
		assertFalse(failRegistrate);
	}
	
	@Test
	public void testLogin() throws Exception {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("username", TEST_USERNAME);
		hm.put("password", TEST_PASSWORD);
		
		String json = TestUtil.objectTojson(hm);
		ResultActions ra = this.mockMvc.perform(put(URL_PREFIX + "/login").contentType(contentType).content(json)).andExpect(status().isOk());
		String returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		User user = TestUtil.jsonToT(returnJson, User.class);
		assertEquals("-1", user.getUsername());
		assertEquals("-1", user.getPassword());
		
	
		userRepository.save(new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER));
		// prvo moram registrovati nekog korisnika, a zatim cemo proveriti logovanje
		
		ra = this.mockMvc.perform(put(URL_PREFIX + "/login").contentType(contentType).content(json)).andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, User.class);
		// korisnik je registrovan, ali nije jos aktivairan njegov nalog, zato ocekujemo neuspesno logovanje
		assertEquals("-1", user.getUsername());
		assertEquals("-1", user.getPassword());
		
		//--------------------------------------------------------------------------------------------
		
		user = userRepository.findByUsernameAndPassword(TEST_USERNAME, TEST_PASSWORD);
		user.setUserStatus(UserStatus.ACTIVATED);
		userRepository.save(user);
		// sad smo aktivirali nalog registrovanog korisnika, i ocekujemo uspesno logovanje
		
		ra = this.mockMvc.perform(put(URL_PREFIX + "/login").contentType(contentType).content(json)).andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_PASSWORD, user.getPassword());
		
	}
	
	public void testActivateAccount() throws Exception {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("username", TEST_USERNAME);
		hm.put("password", TEST_PASSWORD);
		hm.put("repeatPassword", TEST_CORRECT_REPEAT_PASSWORD);
		hm.put("firstName", TEST_FIRST_NAME);
		hm.put("lastName", TEST_LAST_NAME);
		hm.put("email", TEST_EMAIL);
		hm.put("city", TEST_CITY);
		hm.put("phoneNumber", TEST_PHONE_NUMBER);
		
		String json = TestUtil.objectTojson(hm);
		ResultActions ra = this.mockMvc.perform(post(URL_PREFIX + "/registrate").contentType(contentType).content(json)).andExpect(status().isOk());
		// REGISTROVANJE
	
		
		//---------------------------------------------------------------------------------------------------------------------------------
		
		HashMap<String, String> hm2 = new HashMap<String, String>();
		hm2.put("username", TEST_USERNAME);
		hm2.put("password", TEST_PASSWORD);
		
		json = TestUtil.objectTojson(hm2);
		ra = this.mockMvc.perform(put(URL_PREFIX + "/login").contentType(contentType).content(json))
				.andExpect(status().isOk());
		String returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		User user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_PASSWORD, user.getPassword());
		// neuspesan pokusaj logovanja, jer korisnikov nalog nije aktiviran
		
		
		//---------------------------------------------------------------------------------------------------------------------------------
		
		user = userRepository.findByUsernameAndPassword(TEST_USERNAME, TEST_PASSWORD);
		Activation activation = activationRepository.findByUser(user);
		ra = this.mockMvc.perform(post(URL_PREFIX + "/activate/" + activation.getIdForActivation()).contentType(contentType).content(json)).andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		boolean successActivate = TestUtil.jsonToT(returnJson, Boolean.class);
		assertTrue(successActivate);
		
		
		json = TestUtil.objectTojson(hm2);
		ra = this.mockMvc.perform(put(URL_PREFIX + "/login").contentType(contentType).content(json))
				.andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_PASSWORD, user.getPassword());
		// uspesan pokusaj logovanja, jer korisnikov nalog je aktiviran
		

		//---------------------------------------------------------------------------------------------------------------------------------
		
		ra = this.mockMvc.perform(post(URL_PREFIX + "/activate/" + activation.getIdForActivation()).contentType(contentType).content(json)).andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		boolean failActivate = TestUtil.jsonToT(returnJson, Boolean.class);
		assertFalse(failActivate);
		// neuspesno aktiviranje korisnikovog naloga jer je vec aktiviran
	}
	
	@Test
	public void testSaveChangesOnProfile() throws Exception {
		RegisteredUser ru = new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		ru.setUserStatus(UserStatus.ACTIVATED);
		userRepository.save(ru);
		// REGISTROVANJE
		
		// za rest metodu koju gadjamo koristi se HttpSession
		HashMap<String, Object> sessionAttr = new HashMap<String, Object>();
		sessionAttr.put("loggedUser", new User("-1", "-1", UserType.REGISTERED_USER, UserStatus.PENDING)); // ovaj user nam govori da trenutno nemamo 
																										//ulogovanog korisnika
		
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("username", TEST_USERNAME);
		hm.put("password", TEST_PASSWORD);
		
		String json = TestUtil.objectTojson(hm);
		ResultActions ra = this.mockMvc.perform(put(URL_PREFIX + "/login").sessionAttrs(sessionAttr).contentType(contentType).content(json))
				.andExpect(status().isOk());
		String returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		User user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_PASSWORD, user.getPassword());
		sessionAttr.put("loggedUser", user);
		// ulogovali smo se
		
		
		HashMap<String, String> hm2 = new HashMap<String, String>();
		hm2.put("username", TEST_NEW_USERNAME);
		hm2.put("oldPassword", TEST_INCORRECT_OLD_PASSWORD);
		hm2.put("newPassword", TEST_NEW_PASSWORD);
		hm2.put("repeatNewPassword", TEST_REPEAT_NEW_PASSWORD);
		hm2.put("firstName", TEST_FIRST_NAME);
		hm2.put("lastName", TEST_LAST_NAME);
		hm2.put("email", TEST_EMAIL);
		hm2.put("city", TEST_CITY);
		hm2.put("phoneNumber", TEST_PHONE_NUMBER);
		
		json = TestUtil.objectTojson(hm2);
		ra = this.mockMvc.perform(put(URL_PREFIX + "/save_changes_on_profile").sessionAttrs(sessionAttr).contentType(contentType).content(json))
				.andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		int res = TestUtil.jsonToT(returnJson, Integer.class);
		assertEquals(YOU_DID_NOT_ENTER_THE_CORRECT_OLD_PASSWORD, res);
		// neuspesno menjane
		
		hm2.put("oldPassword", TEST_PASSWORD);
		
		json = TestUtil.objectTojson(hm2);
		ra = this.mockMvc.perform(put(URL_PREFIX + "/save_changes_on_profile").sessionAttrs(sessionAttr).contentType(contentType).content(json))
				.andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		res = TestUtil.jsonToT(returnJson, Integer.class);
		assertEquals(EVERYTHING_IS_RIGHT, res);
		
		user = userRepository.findByUsernameAndPassword(TEST_NEW_USERNAME, TEST_NEW_PASSWORD);
		assertNotNull(user);
	}
	
	@Test
	public void testLogout() throws Exception {
		RegisteredUser ru = new RegisteredUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_CITY, TEST_PHONE_NUMBER);
		ru.setUserStatus(UserStatus.ACTIVATED);
		userRepository.save(ru);
		// prvo smo registrovali nekog korisnika
		
		
		// za rest metodu koju gadjamo koristi se HttpSession
		HashMap<String, Object> sessionAttr = new HashMap<String, Object>();
		sessionAttr.put("loggedUser", new User("-1", "-1", UserType.REGISTERED_USER, UserStatus.PENDING)); // ovaj user nam govori da trenutno nemamo 
																								//ulogovanog korisnika
		
		ResultActions ra = this.mockMvc.perform(get(URL_PREFIX + "/logged_user").sessionAttrs(sessionAttr))
						.andExpect(status().isOk());
		String returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		User user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals("-1", user.getUsername());
		assertEquals("-1", user.getPassword());
		// na pocetku nije niko ulogovan
		
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("username", TEST_USERNAME);
		hm.put("password", TEST_PASSWORD);
		
		String json = TestUtil.objectTojson(hm);
		ra = this.mockMvc.perform(put(URL_PREFIX + "/login").sessionAttrs(sessionAttr).contentType(contentType).content(json))
				.andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_PASSWORD, user.getPassword());
		sessionAttr.put("loggedUser", user);
		// ulogovali smo se
		
		//-----------------------------------------------------------------------------------------------------------------------------------
		
		
		ra = this.mockMvc.perform(get(URL_PREFIX + "/logged_user").sessionAttrs(sessionAttr))
				.andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_PASSWORD, user.getPassword());
		// nasli smo ulogovanog korisnika
		
		//----------------------------------------------------------------------------------------------------------------------------
		
		ra = this.mockMvc.perform(delete(URL_PREFIX + "/logout").sessionAttrs(sessionAttr))
				.andExpect(status().isOk());
		sessionAttr.put("loggedUser", new User("-1", "-1", UserType.REGISTERED_USER, UserStatus.PENDING)); // ovaj user nam govori da trenutno nemamo 
		// zatim smo se izlogovali
		
		
		//-----------------------------------------------------------------------------------------------------------------------------
		
		
		ra = this.mockMvc.perform(get(URL_PREFIX + "/logged_user").sessionAttrs(sessionAttr))
				.andExpect(status().isOk());
		returnJson = ra.andReturn().getResponse().getContentAsString();
		System.out.println( "***************************************"+ returnJson + "******************");
		user = TestUtil.jsonToT(returnJson, RegisteredUser.class);
		assertEquals("-1", user.getUsername());
		assertEquals("-1", user.getPassword());
		// ocekujemo da nema ulogovanog korisnika
		
	}
}
