import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import models.User;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private final String DEFAULT_USER = "User";
	private final String DEFAULT_PASSWORD = "password#123";
	private final String DEFAULT_EMAIL = "user@mail.com";

	private User user;

	@Before
	public void setUp() throws Exception {
		user = new User(DEFAULT_USER, DEFAULT_EMAIL, DEFAULT_PASSWORD);
	}

	@Test
	public void naoDeveRetornarSenha() {
		assertNotEquals(user.getPassword(), DEFAULT_PASSWORD);
	}

	@Test
	public void naoDevePermitirAlterarSenha() throws Exception {
		String oldPassword = user.getPassword();
		try {
			user.setPassword(DEFAULT_PASSWORD);
			fail();
		} catch (Exception e) {
			assertEquals("Senha já existe.", e.getMessage());
		}
		assertEquals(oldPassword, user.getPassword());

		setUp();
		
		try {
			user.setPassword("wrongpassword", "newPassword123");
			fail();
		} catch (Exception e) {
			assertEquals("Senha inválida.", e.getMessage());
		}
	}
	
	@Test
	public void devePermitirAlterarSenha() {
		String oldPassword = user.getPassword();

		try {
			user.setPassword(DEFAULT_PASSWORD, "newPassword123");
		} catch (Exception e) {
			fail();
		}
		assertNotEquals(oldPassword, user.getPassword());
	}

	@Test
	public void naoDevePermitirAlterarEmail() {
		try {
			user.setEmail("mateus2@mail.com");
			fail();
		} catch (Exception e) {
			assertEquals("E-mail já existe.", e.getMessage());
		}
		assertEquals(user.getEmail(), DEFAULT_EMAIL);
	}

	@Test
	public void devePermitirAlterarNome() {
		assertEquals(DEFAULT_USER, user.getName());
		user.setName("User2");
		assertEquals("User2", user.getName());
	}

	@Test
	public void deveVerificarSeUsuariosSaoIguais() throws Exception {
		User otherUser = new User("User2", DEFAULT_EMAIL, DEFAULT_PASSWORD);
		assertEquals(otherUser, user);

		otherUser = new User("User2", "user2@mail.com", DEFAULT_PASSWORD);
		assertNotEquals(otherUser, user);

		otherUser = new User(DEFAULT_USER, DEFAULT_EMAIL, DEFAULT_PASSWORD);
		assertEquals(otherUser, user);
	}
	
	@Test
	public void deveGerarHashsValidos() throws Exception {
		User otherUser = new User("User2", DEFAULT_EMAIL, DEFAULT_PASSWORD);
		assertEquals(user.hashCode(), user.hashCode());
		
		otherUser = new User("User2", "user2@mail.com", DEFAULT_PASSWORD);
		assertNotEquals(otherUser.hashCode(), user.hashCode());

		otherUser = new User(DEFAULT_USER, DEFAULT_EMAIL, DEFAULT_PASSWORD);
		assertEquals(otherUser.hashCode(), user.hashCode());
	}

}
