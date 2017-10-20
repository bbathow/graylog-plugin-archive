package com.taxis99.graylog.es.snapshots;

import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.snapshot.CreateSnapshot;
import io.searchbox.snapshot.CreateSnapshotRepository;
import io.searchbox.snapshot.DeleteSnapshot;
import io.searchbox.snapshot.DeleteSnapshotRepository;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotRequest;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by drsantos on 9/29/17.
 */

//TODO change author
public class SnapshotServiceImpl implements SnapshotService {

    private static final Logger log = LoggerFactory.getLogger(SnapshotServiceImpl.class);

    private final JestClient jestClient;

    public SnapshotServiceImpl() {
        JestClientFactory factory = new JestClientFactory();

        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://localhost:9200")
                .multiThreaded(true)
                //Per default this implementation will create no more than 2 concurrent connections per given route
                .defaultMaxTotalConnectionPerRoute(2)
                // and no more 20 connections in total
                .maxTotalConnection(2)
                        .build());

        jestClient = factory.getObject();
    }


    /*

    @Inject
    public SnapshotServiceImpl(JestClient jestClient) {
        this.jestClient = jestClient;
    }
    */

    @Override
    public void createRepository(String repositoryName, String path, boolean compress) {

        String repository = "repo02";

        try {
            System.out.println("SnapshotServiceImpl::createRepositoryAAAAAAAAAAA");
            final Settings.Builder registerRepositorySettings = Settings.builder();
            registerRepositorySettings.put("compress", "true");
            registerRepositorySettings.put("location", "/dev/shm");
            registerRepositorySettings.put("chunk_size", "10m");
            registerRepositorySettings.put("max_restore_bytes_per_sec", "40mb");
            registerRepositorySettings.put("max_snapshot_bytes_per_sec", "40mb");
            registerRepositorySettings.put("readonly", "false");
            registerRepositorySettings.put("type", "fs");


            String test = new Gson().toJson(registerRepositorySettings.build());
            //String temp = test.substring()
            //CreateSnapshotRepository createSnapshotRepository = new CreateSnapshotRepository.Builder("repo01").setHeader("type", "fs").settings(test).build();

            CreateSnapshotRepository createSnapshotRepository = new CreateSnapshotRepository.Builder(repository).setHeader("Authorization", "Basic ZWxhc3RpYzpjaGFuZ2VtZQ==").settings(registerRepositorySettings.build().getAsMap()).build();


            /*
            System.out.println("before !!!!!!");
            CreateSnapshotRepository testa = new CreateSnapshotRepository.Builder(repository).settings(registerRepositorySettings.build().getAsMap()).build();
            System.out.println("after " + testa);
            */

            //System.out.println("json " + createSnapshotRepository.getData(new Gson()));
            //JestResult jestResult = jestClient.execute(createSnapshotRepository);

            //CreateSnapshotRepository createSnapshotRepository = new CreateSnapshotRepository.Builder("repo01").settings(registerRepositorySettings.build()).build();
            //String settings = new Gson().toJson(createSnapshotRepository.getData(new Gson()));
            //System.out.println("json " + settings);
            //JestResult jestResult = jestClient.execute(createSnapshotRepository);

            //jestClient.execute(new CreateSnapshotRepository.Builder("articles").settings(Settings.builder().loadFromSource(settings).build().getAsMap()).build());

            //CreateSnapshotRepository createSnapshotRepository = new CreateSnapshotRepository.Builder(repositoryName).settings(registerRepositorySettings.build().getAsMap()).build();

            /*
            String settings = new Gson().toJson(createSnapshotRepository.getData(new Gson()));

            System.out.println("json object " + settings);

            String script = "{\n" +
                            "    \"type\" : \"fs\",\n" +
                            "    \"settings\" : {\n" +
                            "        \"location\" : \"/dev/shm\",\n" +
                            "        \"compress\" : true\n" +
                            "    }\n" +
                            "}";


            JestResult jestResult = jestClient.execute(new CreateSnapshotRepository.Builder(repositoryName)
                    .settings(registerRepositorySettings.build()).setHeader("Content-Type", "application/json")
                    .build());

                         String script = "{\n" +
                    "    \"type\" : \"fs\",\n" +
                    "    \"settings\" : {\n" +
                    "        \"location\" : \"/dev/shm\",\n" +
                    "        \"compress\" : true\n" +
                    "    }\n" +
                    "}";

                    String script = "{\n" +
                    "    \"type\" : \"fs\",\n" +
                    "    \"settings\" : {\n" +
                    "        \"location\" : \"/dev/shm\",\n" +
                    "        \"compress\" : true\n" +
                    "    }\n" +
                    "}";

            System.out.println(new CreateSnapshotRepository.Builder("repo02")
                    .settings(registerRepositorySettings).setHeader("Authorization", "Basic ZWxhc3RpYzpjaGFuZ2VtZQ==").build());

            jestClient.execute(new CreateSnapshotRepository.Builder("repo02")
                    .settings(registerRepositorySettings).setHeader("Authorization", "Basic ZWxhc3RpYzpjaGFuZ2VtZQ==").build()).setJsonString(script);
            */

            System.out.println("test " + test);

            System.out.println("json " + createSnapshotRepository.getData(new Gson()));
            JestResult jestResult = jestClient.execute(createSnapshotRepository);

            System.out.println("result: " + jestResult);

            if(jestResult.isSucceeded()) {
                System.out.println("Repository was created.");
                log.info("Repository was created.");
            } else {
                System.out.println("Repository was not created." + jestResult.getErrorMessage());
            }



        } catch(Exception ex){
            log.error("Exception in createRepository method: " + ex.toString());
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteRepository(String repositoryName) {

        try {

            jestClient.execute(new DeleteSnapshotRepository.Builder(repositoryName).build());
            log.info(repositoryName + " repository has been deleted.");

        } catch (Exception ex){
            log.error("Exception in deleteRepository method: " + ex.toString());

        }
    }

    @Override
    public void createSnapshot(String repositoryName, String index) {

        final Settings.Builder registerRepositorySettings = Settings.builder();
        registerRepositorySettings.put("indices", index);
        registerRepositorySettings.put("ignore_unavailable", "true");
        registerRepositorySettings.put("include_global_state", "false");
        String snapshotName;

        try {
            snapshotName = new SimpleDateFormat("yyyy-MM-DD:HH:mm:ss").format(new Date());

            JestResult jestResult = jestClient.execute(new CreateSnapshot.Builder(repositoryName, snapshotName).setHeader("Authorization", "Basic ZWxhc3RpYzpjaGFuZ2VtZQ==")
                    .settings(registerRepositorySettings.build())
                    .build());
            log.info("Snapshot was created.");


            if(jestResult.isSucceeded()) {
                log.info("Repository was created.");
            } else {
                log.error("Repository was created.", jestResult.getErrorMessage());
            }


        } catch (Exception ex){
            log.error("Exception in createSnapshot method: " + ex.toString());
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteSnapshot(String repositoryName, String snapshotName) {

        try {

            jestClient.execute(new DeleteSnapshot.Builder(repositoryName, snapshotName).build());
            log.info(snapshotName + " snapshot has been deleted.");


        } catch (Exception ex){
            log.error("Exception in deleteSnapshot method: " + ex.toString());

        }
    }

    @Override
    public void restoreSnapshot(String repositoryName, String snapshotName) {
        try {

            RestoreSnapshotRequest restoreSnapshotRequest = new RestoreSnapshotRequest(repositoryName, snapshotName);
            log.info("Snapshot was restored.");


        } catch (Exception ex){
            log.error("Exception in restoreSnapshot method: " + ex.toString());

        }
    }

    public static void main(String [] args) {

        SnapshotServiceImpl snapshotServiceImpl = new SnapshotServiceImpl();

        snapshotServiceImpl.createSnapshot("repo03", "index");

    }
}
