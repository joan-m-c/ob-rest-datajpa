
package com.example.obrestdatajpa;

import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repository.BookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class ObRestDatajpaApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(ObRestDatajpaApplication.class, args);
		BookRepository repository = context.getBean(BookRepository.class);

		//CRUD
		//crear un libro
			Book book1 = new Book(null, "Rey leon","Jose Villa Nueva",125,29.99, LocalDate.of(2018, 12,1),true);
			Book book2 = new Book(null, "Hapinnes","Tito el Bambino",450,69.99, LocalDate.of(2013, 6,15),true);

		//almacenar un libro
			System.out.println("num libros en base de datos: " + repository.findAll().size());

			repository.save(book1);
			repository.save(book2);

		// recuperar todos los  libros
		System.out.println("num libros en base de datos: " + repository.findAll().size());

		;

		// borrar un libro
			//repository.deleteById(1L);

		System.out.println("num libros en base de datos: " + repository.findAll().size());

	}

}
