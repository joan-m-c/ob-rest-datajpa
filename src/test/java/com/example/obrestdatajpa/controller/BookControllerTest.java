package com.example.obrestdatajpa.controller;

import com.example.obrestdatajpa.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }


    @DisplayName("Comprobar hola mundo desde controladores Spring REST")
    @Test
    void hello() {
    ResponseEntity<String> response =
            testRestTemplate.getForEntity("/hola",String.class);


    assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals(200 ,response.getStatusCode());

        assertEquals("Hola Mundo que tal mundo", response.getBody());

    }



    @Test
    void findAll() {
        ResponseEntity<Book[]> response =
            testRestTemplate.getForEntity("/api/books", Book[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Book> books = Arrays.asList(response.getBody());
        System.out.println(books.size());


    }

    @Test
    void findOneById() {

        ResponseEntity<Book> response =
                testRestTemplate.getForEntity("/api/books/1",Book.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void create() {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                {                   
                    "title": "Libro creado desde spring test",
                    "author": "Juan Marichal",
                    "pages": 225,
                    "price": 69.99,
                    "releaseDate": "2015-11-05",
                    "online": false
                }
                """;

        HttpEntity<String> request = new HttpEntity<String>(json,header);
        ResponseEntity<Book> response =  testRestTemplate.exchange("/api/books",HttpMethod.POST,request,Book.class);



        Book result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L,result.getId());
        assertEquals("Libro creado desde spring test",result.getTitle());

    }
}