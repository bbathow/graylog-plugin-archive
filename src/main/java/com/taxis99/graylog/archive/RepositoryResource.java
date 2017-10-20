package com.taxis99.graylog.archive;

import com.codahale.metrics.annotation.Timed;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.snapshot.GetSnapshotRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.graylog2.plugin.rest.PluginRestResource;
import org.graylog2.shared.rest.resources.RestResource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Api(value = "System/Repositories", description = "Management of Graylog Elasticsearch Repositories.")
@Path("/system/repository")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RepositoryResource extends RestResource implements PluginRestResource {

    private final JestClient jestClient;

    private static final Logger log = LoggerFactory.getLogger(RepositoryResource.class);


    @Inject
    public RepositoryResource(JestClient jestClient) {
        this.jestClient = jestClient;
    }

    @GET
    @Timed
    @ApiOperation(value = "Lists all existing collector registrations")
    public List<String> getRepositoryCreated() {
        List<String> repoList = new ArrayList<>();

        try {

            //GetSnapshotRepository getSnapshotRepository = new GetSnapshotRepository.Builder().setHeader("Authorization", "Basic ZWxhc3RpYzpjaGFuZ2VtZQ==").build();
            GetSnapshotRepository getSnapshotRepository = new GetSnapshotRepository.Builder().build();

            JestResult jestResult = jestClient.execute(getSnapshotRepository);

            JSONObject mainObject = new JSONObject(jestResult.getJsonString());
            JSONArray tempArray = mainObject.names();

            for(int i=0;i < tempArray.length();i++){
                repoList.add(tempArray.getString(i));
            }

        } catch (Exception ex) {
            log.error("Exception in createSnapshot method: " + ex.toString());
            ex.printStackTrace();
        }

        return repoList;
    }
}
