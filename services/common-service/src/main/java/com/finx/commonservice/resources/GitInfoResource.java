package com.finx.commonservice.resources;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Path("/git-info")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class GitInfoResource {

    @GET
    public Map<String, Object> gitInfo() {
        Properties properties = new Properties();
        try {
            properties.load(GitInfoResource.class.getResourceAsStream("/git.properties"));
            return Map.of(
                    "git",
                    Map.of("commit",
                            Map.of("time", properties.get("git.commit.time"),
                                    "id", properties.get("git.commit.id"),
                                    "user", properties.get("git.commit.user.email")),
                            "branch", properties.get("git.branch"))
            );
        } catch (IOException e) {
            log.warn("Failed to load git.properties", e);
            return Map.of();
        }

    }
}
