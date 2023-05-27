package com.github.bookshelf.controller;

import com.github.bookshelf.entities.Book;
import com.github.bookshelf.repository.BookRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BookController {

  public BookRepository bookRepository;

  public BookController(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @GetMapping(path = "/books", produces = "application/json")
  public @ResponseBody CollectionModel<EntityModel<Book>> allBooks() {
    List<Book> books = bookRepository.findAll();

    List<EntityModel<Book>> bookModels = books.stream()
      .map(book -> EntityModel.of(book,
        linkTo(methodOn(BookController.class).allBooks()).withRel("books")))
      .collect(Collectors.toList());

    return CollectionModel.of(bookModels,
      linkTo(methodOn(BookController.class).allBooks()).withSelfRel());
  }
}
