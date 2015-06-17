package de.codecentric.moviedatabase.controller;

import de.codecentric.moviedatabase.domain.Tag;
import de.codecentric.moviedatabase.hateoas.AbstractResourceAssembler;
import de.codecentric.moviedatabase.hateoas.ControllerLinkBuilderFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TagResourceAssembler extends AbstractResourceAssembler<Tag, Resource<Tag>> {

    private ControllerLinkBuilderFactory linkBuilderFactory;

    public TagResourceAssembler(ControllerLinkBuilderFactory linkBuilderFactory) {
        this.linkBuilderFactory = linkBuilderFactory;
    }

    @Override
    public Resource<Tag> toResource(Tag tag) {
        Assert.notNull(tag);
        Link searchLink = null;
        searchLink = linkBuilderFactory.linkTo(AbstractMovieController.class)
                .addRequestParam(RequestParameter.SEARCH_STRING.getName(), "tag:'" + tag.getLabel() + "'").withRel(Relation.SEARCH.getName());
        return new Resource<>(tag, searchLink);
    }

    public Resource<Tag> toResource(Tag tag, UUID movieId) {
        Assert.notNull(movieId);
        Link deleteTagFromMovieLink = linkBuilderFactory.linkTo(AbstractMovieController.class).slash(movieId).slash(PathFragment.TAGS.getName()).slash(tag.getLabel()).withSelfRel();
        Resource<Tag> resourceTag = toResource(tag);
        resourceTag.add(deleteTagFromMovieLink);
        return resourceTag;
    }

    public List<Resource<Tag>> toResource(Collection<Tag> tags, UUID movieId) {
        Assert.notNull(tags);
        return tags.stream().
                map(tag -> toResource(tag, movieId)).
                collect(Collectors.toList());
    }
}
