package pl.antonina.tasks.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class LocationUtils {

    public static URI getLocation(long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQuery("")
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}