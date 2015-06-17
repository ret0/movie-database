package de.codecentric.moviedatabase.controller;

import de.codecentric.moviedatabase.domain.Comment;
import de.codecentric.moviedatabase.domain.Movie;
import de.codecentric.moviedatabase.domain.Tag;
import de.codecentric.moviedatabase.model.MovieForm;
import de.codecentric.moviedatabase.service.MovieService;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping(value = "/movies", produces = {"application/json", "application/hal+json"})
public class JsonMovieController {

    private MovieService movieService;
    private MovieResourceAssembler movieResourceAssembler;

    public JsonMovieController(MovieService movieService,
                               MovieResourceAssembler movieResourceAssembler,
                               TagResourceAssembler tagResourceAssembler) {
        this.movieService = movieService;
        this.movieResourceAssembler = movieResourceAssembler;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<Resource<Movie>> getMovie(@PathVariable UUID id) {
        return enableCorsRequests(movieResourceAssembler.toResource(movieService.findMovieById(id)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<List<Resource<Movie>>> getMovies(@RequestParam(required = false) String searchString) {
        Set<Tag> tags = new HashSet<Tag>();
        Set<String> searchWords = new HashSet<String>();
        MovieUtil.convertSearchStringToTagsAndSearchWords(searchString, tags, searchWords);
        List<Movie> movies = movieService.findMovieByTagsAndSearchString(tags, searchWords);
        return enableCorsRequests(movieResourceAssembler.toResource(movies), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json", "application/hal+json"})
    public
    @ResponseBody
    ResponseEntity<Resource<Movie>> addMovieForSenchaTouch(@RequestBody Movie movie) {
        return addMovie(movie);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST, consumes = {"application/json", "application/hal+json"})
    public
    @ResponseBody
    ResponseEntity<Resource<Movie>> addMovie(@RequestBody Movie movie) {
        // recreate the movie to make sure that the client is not sending too much information, e.g., an ID.
        Movie result = new Movie(movie.getTitle(), movie.getDescription(), movie.getStartDate());
        movieService.createMovie(result);
        return getMovie(result.getId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json", "application/hal+json"})
    public
    @ResponseBody
    ResponseEntity<Resource<Movie>> editMovie(@PathVariable UUID id, @RequestBody MovieForm movieForm) {
        Movie movie = movieService.findMovieById(id);
        movie.setDescription(movieForm.getDescription());
        movie.setStartDate(movieForm.getStartDate());
        movie.setTitle(movieForm.getTitle());
        movieService.updateMovie(movie);
        return getMovie(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    ResponseEntity<Object> deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<Object>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST, consumes = {"text/plain"})
    public
    @ResponseBody
    ResponseEntity<Resource<Movie>> addComment(@PathVariable UUID id, @RequestBody String content) {
        Movie movie = movieService.findMovieById(id);
        movie.getComments().add(new Comment(new Date(), content));
        return enableCorsRequests(movieResourceAssembler.toResource(movie), HttpStatus.CREATED);
    }

    private <T> ResponseEntity<T> enableCorsRequests(T entity, HttpStatus statusCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<T>(entity, headers, statusCode);
    }
}
