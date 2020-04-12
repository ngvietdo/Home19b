package com.home19b.domain.db;

import com.home19b.config.MongodbCfg;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@Slf4j
public class MongoDbAccess {
    private MongoClient mongo = null;

    @Autowired
    private MongodbCfg mongoCfg;

    public MongoDatabase getDatabase() {
        return getMongo().getDatabase(mongoCfg.getDbName());
    }

    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> clazz) {
        return getDatabase().getCollection(collectionName, clazz);
    }

    public MongoCollection getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

    public MongoClient getMongo() {

        if (mongo == null) {

            int port = Integer.parseInt(mongoCfg.getDbPort());
            String host = mongoCfg.getDbHost();
            String authUser = mongoCfg.getDbUser();
            String authPwd = mongoCfg.getDbPass();
            String dbName = mongoCfg.getDbName();
            int connectionsPerHost = 10;
            int maxConnectionIdleTime = 60;
            int maxConnectionLifeTime = 120;
            String encodedPwd = "";

            log.info("Mongo DB server: {}:{}", host, port);
            log.info("Mongo DB user: {}", authUser);
            log.info("Mongo DB pwd: {}", authPwd);
            log.info("Mongo DB db: {}", dbName);
            log.info("Mongo DB connectionsPerHost: {}", connectionsPerHost);
            log.info("Mongo DB maxConnectionIdleTime: {}", maxConnectionIdleTime);
            log.info("Mongo DB maxConnectionLifeTime: {}", maxConnectionLifeTime);

            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            MongoClientOptions.Builder options = MongoClientOptions.builder()
                    .connectionsPerHost(connectionsPerHost)
                    .writeConcern(WriteConcern.ACKNOWLEDGED)
                    .maxConnectionIdleTime(maxConnectionIdleTime * 1_000)
                    .maxConnectionLifeTime(maxConnectionLifeTime * 1_000)
                    .codecRegistry(pojoCodecRegistry);
            try {
                encodedPwd = URLEncoder.encode(authPwd, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                log.error("Exception ", ex);
            }

            String clientUrl = "mongodb://" + authUser + ":" + encodedPwd + "@" + host + ":" + port + "/" + dbName;
            MongoClientURI uri = new MongoClientURI(clientUrl, options);

            log.info("Connect to MongoDB information: {}", uri.toString());
            try {
                mongo = new MongoClient(uri);
            } catch (Exception ex) {
                log.error("An Exception occoured when connecting to MongoDB", ex);
            }
        }
        return mongo;
    }
}
