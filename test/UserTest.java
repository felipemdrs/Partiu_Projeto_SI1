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
	public void naoDevePermitirAlterarSenha() {
		String oldPassword = user.getPassword();
		try {
			user.setPassword(DEFAULT_PASSWORD);
		} catch (Exception e) {
			assertEquals("Password already exists", e.getMessage());
		}
		assertEquals(oldPassword, user.getPassword());
	}

	@Test
	public void naoDevePermitirAlterarEmail() {
		try {
			user.setEmail("mateus2@mail.com");
			fail();
		} catch (Exception e) {
			assertEquals("Email already exists", e.getMessage());
		}
		assertEquals(user.getEmail(), DEFAULT_EMAIL);
	}

	@Test
	public void devePermitirAlterarNome() {
		assertEquals(DEFAULT_USER, user.getNome());
		user.setNome("User2");
		assertEquals("User2", user.getNome());
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
