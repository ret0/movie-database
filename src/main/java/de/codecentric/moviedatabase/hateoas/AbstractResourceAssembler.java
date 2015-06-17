package de.codecentric.moviedatabase.hateoas;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractResourceAssembler<T, D extends ResourceSupport> implements ResourceAssembler<T, D> {

    public List<D> toResource(List<T> entities) {
        Assert.notNull(entities);
        return entities.stream().
                map(e -> toResource(e)).
                collect(Collectors.toList());
    }

}
