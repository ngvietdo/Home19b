package com.home19b.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
@Setter
public class MongodbCfg {
    MongoClient mongoDbClient = null;
    @Value("${spring.data.mongodb.database}")
    private String dbName;
    @Value("${spring.data.mongodb.host}")
    private String dbHost;
    @Value("${spring.data.mongodb.port}")
    private String dbPort;
    @Value("${spring.data.mongodb.username}")
    private String dbUser;
    @Value("${spring.data.mongodb.password}")
    private String dbPass;

    @Bean
    MongoClient mongoClient() {
        if (mongoDbClient != null) {
            return mongoDbClient;
        }

        List<ServerAddress> seeds = new ArrayList<>();
        seeds.add(new ServerAddress(dbHost, Integer.parseInt(dbPort)));
        List<MongoCredential> credentials = new ArrayList<>();

        credentials.add(
                MongoCredential.createScramSha1Credential(
                        dbUser,
                        dbName,
                        dbPass.toCharArray()
                )
        );

        mongoDbClient = new MongoClient(seeds, credentials);

        return mongoDbClient;
    }
}
