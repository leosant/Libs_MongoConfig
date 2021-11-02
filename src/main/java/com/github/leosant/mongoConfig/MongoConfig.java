package com.github.leosant.mongoConfig;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Class responsible in configuration of connection with database mongodb
 * @author Leonardo Santos
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.github.leosant.mongoConfig")
public abstract class MongoConfig extends AbstractMongoClientConfiguration implements IMongoConfig{

    private static MongoClient mongoConnection = null;
    private static String mongoDatabase = null;

    /**
     * Configure in connection with database local and parse the responsible for methods
     * {@link #mongoClient(String, String)}
     * @param port of database
     * @param database String of connection with database expected name of database
     */
    public static void mongoClientLocal(Integer port, String database) {
        String fullPathUrl = "mongodb://localhost:" + port +
                String.format("/%1$s", database);
        mongoClient(fullPathUrl, database);
    }

    /**
     * Configure in connection with database to cluster in mongodb
     * responsible for establish are connection methods {@link #mongoClient(String, String)}
     * @param mongoSrv String of connection with database (mongoSrv start with "mongodb+srv://", expected of string after "mongodb+srv://")
     * @param database String of connection with database expected name of database
     */
    public static void mongoClientCluster(String mongoSrv, String database) {
        mongoClient("mongodb+srv://"+mongoSrv, database);
    }

    /**
     * Configure in connection with database method also create configuration of mongoTemplate
     * @param connection String of connection with database
     * @param database String with the name of database
     */
    protected static void mongoClient(String connection, String database) {
        ConnectionString connectionString = new ConnectionString(connection);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoConnection = MongoClients.create(settings);
        mongoDatabase = database;
    }

    /**
     * Allows transaction with database from of a connection pre-established
     * @return MongoTemplate for possible transaction with database
     */
    public static MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoConnection, mongoDatabase);
    }

    /**
     * method no used
     * @return string "test"
     */
    @Override
    protected String getDatabaseName() {
        return "test";
    }
}
