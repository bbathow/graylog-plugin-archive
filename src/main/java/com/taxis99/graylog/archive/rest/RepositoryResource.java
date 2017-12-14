package com.taxis99.graylog.archive.rest;

import com.codahale.metrics.annotation.Timed;
import com.taxis99.graylog.SnapshotServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.graylog2.plugin.rest.PluginRestResource;
import org.graylog2.shared.rest.resources.RestResource;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "System/Repositories", description = "Management of Graylog Elasticsearch Repositories.")
@Path("/system/repository")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RepositoryResource extends RestResource implements PluginRestResource {

    SnapshotServiceImpl snapshotService;

    @Inject
    public RepositoryResource(SnapshotServiceImpl snapshotService) {
        this.snapshotService = snapshotService;
    }

    @GET
    @Timed
    @ApiOperation(value = "Lists all existing repositories registrations")
    public List<String> getRepositoryList() {
        List<String> repoList;

        repoList = snapshotService.repositoryList();
        return repoList;
    }
}
