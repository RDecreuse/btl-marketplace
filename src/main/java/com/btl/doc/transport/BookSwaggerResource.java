package com.btl.doc.transport;

import com.btl.doc.business.Book;
import com.btl.doc.service.BookService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RestController
@Api(value = "Resource for books", description = "Resource for managing books")
@RequestMapping("/api/books")
public class BookSwaggerResource {

    @Autowired
    private BookService service;

    @GET
    @Path("/")
    @RequestMapping(method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Return a list of book", notes = "The list may be empty.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return results"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public List<Book> findAll() {
        return this.service.getBooks();
    }

    @GET
    @Path("/{bookId}")
    @RequestMapping(path = "/{bookId}", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Return a book for given id", notes = "The result can't be null.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return results"),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 404, message = "No book found for given id."),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public Book findOne(@ApiParam(value = "Book id", required = true) @PathVariable final Long bookId) {
        return this.service.findOne(bookId);
    }

    @POST
    @Path("/")
    @RequestMapping(method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a book and return it.", notes = "The result can't be null.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return new book"),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public Book create(@ApiParam(value = "Fresh book", required = true) @RequestBody Book book) {
        return service.create(book);
    }

    @PUT
    @Path("/{bookId}")
    @RequestMapping(path = "/{bookId}", method = RequestMethod.PUT)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a book and return it.", notes = "The result can't be null.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, return updated book"),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public Book update(@ApiParam(value = "Updated book", required = true) @RequestBody Book book) {
        return service.update(book);
    }

    @DELETE
    @Path("/{bookId}")
    @RequestMapping(path = "/{bookId}", method = RequestMethod.DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a book.", notes = "No result will be sent.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok, book deleted."),
            @ApiResponse(code = 400, message = "Wrong parameters"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    public void delete(@PathVariable Long bookId) {
        service.delete(bookId);
    }

}
