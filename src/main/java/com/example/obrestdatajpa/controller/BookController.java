package com.example.obrestdatajpa.controller;

import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class); // logger para errores

    //atributos
    private BookRepository bookRepository;

    // constructores

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }




//CRUD SOBRE LA ENTIDAD BOOK


    /**
     * Buscar todos los libros que hay en base de datos (Array List de libros)
     * http://localhost:8081/api/books
     * @return
     */
    @GetMapping("/api/books")
    public List<Book> findAll(){
        //recuperar y devolver los libros de base de datos
        return bookRepository.findAll();
    }




    /**
     * buscar un solo libro en base de datos segun su id
     * http://localhost:8081/api/books/1
     * http://localhost:8081/api/books/2
     * @param id
     * @return
     */
    @GetMapping("/api/books/{id}")
    public ResponseEntity<Book> findOneById(@PathVariable Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        //Optional es un elvoltorio para manejar datos nulos
        //option 1
        if(bookOpt.isPresent()) //  podemos veriricar si hay datos o no
            //return bookOpt.get();
          return ResponseEntity.ok(bookOpt.get());
        else
            //return null;
             return ResponseEntity.notFound().build();

        //option 2
        //return bookOpt.orElse(null);

        //option 3
    }

    // crear un nuevo libro en base de datos


    /**
     * Metodo Post, no coliciona con findall porque son diferentes metotos http: GET VS POST
     * @param book
     * @param headers
     * @return
     */

    @PostMapping("/api/books")
    public ResponseEntity <Book> create(@RequestBody Book book, @RequestHeader HttpHeaders headers ){
        System.out.println(headers.get("User-Agent"));

        if (book.getId() != null ){ // quiere decir que existe el id y por tanton no es una creacion
           log.warn("triying to create a bock  with id");
            System.out.println("triying to create a bock  with id");
            return ResponseEntity.badRequest().build();
        }
        //guardar el libro recibido por parametros en la base de datos
        Book result = bookRepository.save(book);
       return ResponseEntity.ok(result);
    }

    /**
     * Actualizar un libro existe en la base de datos
     */
    @PutMapping("/api/books")
    public ResponseEntity<Book>  update(@RequestBody Book book){

     //   Optional<Book> bookOpt = bookRepository.findById(id);
//        if(bookOpt.isPresent())
//            return ResponseEntity.ok(bookOpt.get());
//        else
//            return ResponseEntity.notFound().build();

        if (book.getId() == null) { // si no tiene id quiere decir que si es una creacion
            log.warn("Trying to update a non existent book");
            return ResponseEntity.badRequest().build();
        }

        if (!bookRepository.existsById(book.getId())){ // comprobar si existe el libro con ese id
            log.warn("Trying to update a non existent book");
            return ResponseEntity.notFound().build();

        }

        //proceso de actualizacion
        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result);


    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Book> delete (@PathVariable Long id){

        if (!bookRepository.existsById(id)){ // comprobar si existe el libro con ese id
            log.warn("Trying to delete a non existent book");
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/api/books")
    public ResponseEntity<Book> deleteAll(){

        log.info("REST request for deleting all books");
        bookRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }



}
