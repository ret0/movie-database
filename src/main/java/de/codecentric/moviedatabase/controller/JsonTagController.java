package de.codecentric.moviedatabase.controller;

import de.codecentric.moviedatabase.domain.Tag;
import de.codecentric.moviedatabase.service.MovieService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping(value = "/tags", headers = "Accept=application/json")
public class JsonTagController {

    private MovieService movieService;
    private TagResourceAssembler tagResourceAssembler;

    public JsonTagController(MovieService movieService,
                             TagResourceAssembler tagResourceAssembler) {
        this.movieService = movieService;
        this.tagResourceAssembler = tagResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<Resource<Tag>> getAllTags() {
        return tagResourceAssembler.toResource(movieService.findAllTags());
    }
}
