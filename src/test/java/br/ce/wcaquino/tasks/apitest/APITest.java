package br.ce.wcaquino.tasks.apitest;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	@Test
	public void deveRetornarListaTarefas() {
		RestAssured.given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		String dataDeHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		JSONObject requestParams = new JSONObject();
		requestParams.put("task", "Teste API");
		requestParams.put("dueDate",dataDeHoje );
		
		
		RestAssured.given()
			.body(requestParams.toString())
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaComDataPassada() {
		RestAssured.given()
			.body("{\"task\": \"Teste API\",\"dueDate\": \"2010-07-24\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaSemData() {
		RestAssured.given()
		.body("{\"task\": \"Teste API\",\"dueDate\": null}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Fill the due date"))
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaSemDescricao() {
		RestAssured.given()
			.body("{\"task\": null ,\"dueDate\": \"2010-07-24\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Fill the task description"))
		;
	}
	
}






