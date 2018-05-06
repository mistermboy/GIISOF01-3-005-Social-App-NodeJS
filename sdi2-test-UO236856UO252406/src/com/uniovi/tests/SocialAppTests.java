package com.uniovi.tests;

import static com.uniovi.tests.utils.MLabAPI.removeCollection;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;

/**
 * Instance of NotaneitorTests.java
 * 
 * @author
 * @version
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SocialAppTests {

	// static String PathFirefox =
	// "/Applications/FirefoxSDI.app/Contents/MacOS/firefox-bin";
	// static WebDriver driver = getDriver( PathFirefox );
	// static String URL = "http://localhost:8081";

	static String PathFirefox = "D:\\Escritorio\\Firefox46.win\\FirefoxPortable.exe";

	static WebDriver driver = getDriver(PathFirefox);
	static String URL = "http://localhost:8081";

	public static WebDriver getDriver(String PathFirefox) {
		// Firefox (Versi�n 46.0) sin geckodriver para Selenium 2.x.
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	private static void restoreDB() {
		removeCollection("redsocial", "amigos");
		removeCollection("redsocial", "mensajes");
		removeCollection("redsocial", "peticiones");
		removeCollection("redsocial", "usuarios");
	}

	private void registrarUsuario(String name, String email, String pass) {
		// Vamos a la p�gina de registro (signup)
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");

		// Rellenamos el formulario con datos v�lidos.
		PO_RegisterView.fillForm(driver, email, name, pass, pass);

		// Comprobamos que se ha registrado el usuario.
		PO_View.checkElement(driver, "text", "Nuevo usuario registrado");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		driver.navigate().to(URL);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	public static void begin() {
		restoreDB();
	}

	@AfterClass
	public static void end() {
		driver.quit();
		// restoreDB();
	}

	/**
	 * Registro de usuarios con datos v�lidos.
	 */
	@Test

	public void PR01_1() {
		// Vamos a la p�gina de registro (signup)
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");

		// Rellenamos el formulario con datos v�lidos.
		PO_RegisterView.fillForm(driver, "pepe@mail.com", "Pepe", "1234", "1234");

		// Comprobamos que se ha registrado el usuario.
		PO_View.checkElement(driver, "text", "Nuevo usuario registrado");
	}

	/**
	 * Registro de usuarios con datos inv�lidos (repetici�n de contrase�a inv�lida y
	 * usuario ya registrado).
	 */
	@Test
	@Ignore
	public void PR01_2() {

		// PROBAMOS LA VALIDACI�N DE CONTRASE�A
		// Vamos a la p�gina de registro (signup)
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");

		// Rellenamos el formulario con datos v�lidos.
		PO_RegisterView.fillForm(driver, "pepe@mail.com", "Pepe", "1234", "4321");

		// Comprobamos que se ha registrado el usuario.
		PO_View.checkElement(driver, "text", "Las contrase�as no coinciden");

		// PROBAMOS LA VALIDACI�N DE EMAIL UNICO EN EL SISTEMA CON PEPE QUE YA
		// EST� REGISTRADO

		// Vamos a la p�gina de registro (signup)
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");

		// Rellenamos el formulario con datos v�lidos.
		PO_RegisterView.fillForm(driver, "pepe@mail.com", "Pepe", "1234", "1234");

		// Comprobamos que se ha registrado el usuario.
		PO_View.checkElement(driver, "text", "Email ya registrado en el sistema");
	}

	/**
	 * Inicio de sesi�n con datos v�lidos.
	 */
	@Test
	@Ignore
	public void PR02_1() {
		// Vamos a la p�gina de autenticaci�n (login)
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");

		// Rellenamos el formulario de autenticaci�n con datos v�lidos.
		PO_LoginView.fillForm(driver, "pepe@mail.com", "1234");

		// Comprobamos que el login fue correcto.
		PO_View.checkElement(driver, "text", "Usuarios de la aplicaci�n");
	}

	/**
	 * Inicio de sesi�n con datos inv�lidos (usuario no existe en la aplicaci�n).
	 */
	@Test
	@Ignore
	public void PR02_2() {
		// Vamos a la p�gina de autenticaci�n (login)
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");

		// Rellenamos el formulario de autenticaci�n con datos NO v�lidos.
		PO_LoginView.fillForm(driver, "clara@oswin.oswald", "tardis");

		// Comprobamos que el login no fue correcto.
		PO_View.checkElement(driver, "text", "Email o password incorrecto");
	}

	/**
	 * Acceso al listado de usuarios desde el ususario en sesi�n.
	 */
	@Test
	@Ignore
	public void PR03_1() {
		// Vamos a la p�gina de autenticaci�n (login)
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");

		// Rellenamos el formulario de autenticaci�n con datos v�lidos.
		PO_LoginView.fillForm(driver, "pepe@mail.com", "1234");

		// Comprobamos que el login fue correcto y que en efecto se visualiza la
		// lista de usuarios de la aplicaci�n.
		PO_View.checkElement(driver, "h2", "Usuarios de la aplicaci�n");

		// Nos movemos fuera de la ruta /usuarios.
		PO_HomeView.clickOption(driver, "invitaciones", "free", "//h2[contains(text(), 'Invitaciones')]");

		// Clicamos en listado de todos sus peticiones.
		PO_HomeView.clickOption(driver, "usuarios", "free", "//h2[contains(text(), 'Usuarios de la aplicaci�n')]");
	}

	/**
	 * Intento de acceso con URL desde un usuario no identificado al listado de
	 * usuarios de un usuario en sesi�n.
	 */
	@Test
	@Ignore
	public void PR03_2() {

		// Intentamos acceder a la p�gina de listado sin estar autenticados.
		driver.navigate().to("http://localhost:8081/usuarios");

		// Comprobamos que nos redirige a la p�gina de logeo.
		PO_View.checkElement(driver, "text", "Identificaci�n de usuario");
	}

	/**
	 * Realizar una b�squeda v�lida en el listado de usuarios desde un usuario en
	 * sesion.
	 */
	@Test
	@Ignore
	public void PR04_1() {

		// Registramos un usuario que podamos buscar en la lista de usuarios
		registrarUsuario("ana", "ana@email.com", "1234");

		// Accedemos como un usuario en sesi�n a la aplicaci�n
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pepe@mail.com", "1234");

		// En el campo de b�squeda introducimos el criterio a buscar
		WebElement searchField = driver.findElement(By.name("busqueda"));
		searchField.click();
		searchField.clear();
		searchField.sendKeys("ana");

		// Clicamos el bot�n de enviar query
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}

	/**
	 * Intento de acceso con url a la busqueda de usuarios desde un usuario no
	 * identificado.
	 */
	@Test
	@Ignore
	public void PR04_2() {
		// Intentamos acceder a la p�gina de b�squeda sin estar autenticados.
		driver.navigate().to("http://localhost:8081/usuarios?busqueda=ana");

		// Comprobamos que nos redirige a la p�gina de logeo.
		PO_View.checkElement(driver, "text", "Identificaci�n de usuario");
	}

	/**
	 * Enviar una petici�n de amistad a un usario de forma valida.
	 */
	@Test
	@Ignore
	public void PR05_1() {
		// Accedemos como un usuario en sesi�n a la aplicaci�n
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pepe@mail.com", "1234");

		// Vamos al usuario con email p6@hotmail.com y le enviamos una petici�n
		// de
		// amistad.
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"/html/body/div/div[2]/table/tbody/tr[2]/td[3]/a");
		elementos.get(0).click();

		PO_View.checkElement(driver, "free", "/html/body/div/div[2]/table/tbody/tr[2]/td[3]");
	}

	/**
	 * Enviar una petici�n de amistad a un usuario al que le habiamos enviado la
	 * invitaci�n previamente. No deber�a dejarnos enviar la invitaci�n.
	 */
	@Test
	@Ignore
	public void PR05_2() {
		// Accedemos como un usuario en sesi�n a la aplicaci�n
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "pepe@mail.com", "1234");

		// Checkeamos que ya le hemos enviado una peticon de amistad al usuario
		// .
		PO_View.checkElement(driver, "free", "/html/body/div/div[2]/table/tbody/tr[2]/td[3]");
		try {
			// Comprobamos que ya no est� el bot�n para enviar la petici�n de
			// amistad.
			PO_View.checkElement(driver, "free", "/html/body/div/div[2]/table/tbody/tr[2]/td[3]/a");
			fail();
		} catch (TimeoutException e) {
		}
		;

	}

	/**
	 * Listar las invitaciones recividas por un usuario, realizar la comprobaci�n
	 * con una lista que al menos tenga un usaurio.
	 */
	@Test
	@Ignore
	public void PR06_1() {
		// Accedemos como un usuario en sesi�n a la aplicaci�n
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "ana@email.com", "1234");

		// Clicamos en listado de todos sus peticiones.
		PO_HomeView.clickOption(driver, "invitaciones", "free", "//h2[contains(text(), 'Invitaciones')]");

		// Checkear que existe al menos un usuario en la lista y que es el
		// correcto.
		PO_View.checkElement(driver, "free", "//table/tbody/tr/td[contains(text(), 'Pepe')]");
	}

	/**
	 * Acceptar una invitaci�n recivida.
	 */
	@Test
	@Ignore
	public void PR07_1() {
		// Accedemos como un usuario en sesi�n a la aplicaci�n
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "ana@email.com", "1234");

		// Clicamos en listado de todos sus peticiones.
		PO_HomeView.clickOption(driver, "invitaciones", "free", "//h2[contains(text(), 'Invitaciones')]");

		// Vamos al usuario con email p6@hotmail.com y le enviamos una petici�n
		// de
		// amistad. A este usuario le acabamos de mandar una petici�n.
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"/html/body/div/div[1]/table/tbody/tr/td[2]/a");
		elementos.get(0).click();

		try {
			// Comprobamos que ya no est� el bot�n para aceptar la petici�n de
			// amistad.
			PO_View.checkElement(driver, "free", "/html/body/div/div[1]/table/tbody/tr/td[2]/a");
			fail();
		} catch (TimeoutException e) {
		}
		;
	}

	/**
	 * Listar los amigos de un usario. Realizar la comprobaci�n con al menos un
	 * amigo.
	 */
	@Test
	@Ignore
	public void PR08_1() {
		// Accedemos como un usuario en sesi�n a la aplicaci�n
		PO_HomeView.clickOption(driver, "identificarse", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "ana@email.com", "1234");

		// Clicamos en listado de todos sus peticiones.
		PO_HomeView.clickOption(driver, "amigos", "free", "//h2[contains(text(), 'Amistades')]");

		// Checkear que existen usuarios en la lista
		PO_View.checkElement(driver, "free", "/html/body/div/div[1]/table/tbody/tr/td[1]");

		// Checkeamos que adem�s dicho usuario es pepe, la amistad que acabamos
		// de crear para ana.
		PO_View.checkElement(driver, "free", "//table/tbody/tr/td[contains(text(), 'Pepe')]");
	}

	/**
	 * Inicio de sesi�n con datos v�lidos.
	 */
	@Test
	public void PR0C1_1() {

		// Accedemos a la p�gina de inicio de sesi�n de la aplicaci�n JQUERY
		driver.navigate().to("http://localhost:8081/cliente.html");

		// Rellenamos el campo email y password y nos logueamos
		PO_LoginView.fillForm(driver, "pepe@mail.com", "1234");

		// Checkear que se ha logueado correctamente
		PO_View.checkElement(driver, "id", "tablaCuerpo");

	}
	
	/**
	 * Inicio de sesi�n con datos inv�lidos.
	 */
	@Test
	public void PR0C1_2() {

		// Accedemos a la p�gina de inicio de sesi�n de la aplicaci�n JQUERY
		driver.navigate().to("http://localhost:8081/cliente.html");

		// Rellenamos el campo email y password y nos logueamos
		PO_LoginView.fillForm(driver, "pepe@mail.com", "12345");

		// Checkear que no se ha logueado correctamente
		PO_View.checkElement(driver, "id", "widget-login");

	}

}