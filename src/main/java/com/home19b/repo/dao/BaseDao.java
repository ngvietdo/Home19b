package com.home19b.repo.dao;

import com.home19b.domain.db.MongoDbAccess;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseDao {

    @Autowired
    MongoDbAccess mongoDbAccess;

    public <T> MongoCollection<T> getCollection(String collection, Class<T> clazz) {
        return mongoDbAccess.getDatabase().getCollection(collection, clazz);
    }

    public long delete(String collection, Bson condition) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            if (condition == null) {
                condition = new Document();
            }
            DeleteResult result = database.getCollection(collection).deleteMany(condition);
            result.getDeletedCount();
        } catch (Exception ex) {
            log.error("", ex);
        }
        return 0;
    }

    public List<Document> findAll(String collection, Bson params, Bson sort, int start, int limit) {
        List<Document> result = new ArrayList<>();
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            if (params == null) {
                params = new Document();
            }
            if (sort == null) {
                sort = new Document();
            }
            FindIterable<Document> iterable = database.getCollection(collection).find(params).sort(sort).skip(start).limit(limit);
            result = new ArrayList<>();
            for (Document document : iterable) {
                result.add(document);
            }
            return result;
        } catch (Exception ex) {
            log.error("", ex);
        }
        log.debug("result {} - {}", result.size(), result);
        return result;
    }

    public List<Document> findAll(String collection, Bson params, Bson sort, Bson group, int start, int limit) {
        List<Document> result = new ArrayList<>();

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", params),
                new Document()
                        .append("$group", new Document()
                                .append("_id", group
                                )
                                .append("count", new Document()
                                        .append("$sum", 1.0)
                                )
                        ),
                new Document()
                        .append("$sort", sort),
                new Document().append("$skip", start),
                new Document().append("$limit", limit)
        );
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            AggregateIterable aggregateIterable = database.getCollection(collection).aggregate(pipeline)
                    .allowDiskUse(false);
            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    result.add(document);
                }
            };

            aggregateIterable.forEach(processBlock);
        } catch (Exception e) {
            log.error("error", e);
        }
        return result;
    }

    public long countAll(String collection, Bson params) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            if (params == null) {
                params = new Document();
            }
            return database.getCollection(collection).countDocuments(params);
        } catch (Exception ex) {
            log.error("", ex);
        }
        return 0;
    }

    public long countAllGroup(String collection, Bson params, Bson group) {
        List<Document> result = new ArrayList<>();

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", params),
                new Document()
                        .append("$group", new Document()
                                .append("_id", group
                                )
                        ),
                new Document()
                        .append("$count", "count")
        );
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            AggregateIterable aggregateIterable = database.getCollection(collection).aggregate(pipeline)
                    .allowDiskUse(false);
            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    result.add(document);
                }
            };

            aggregateIterable.forEach(processBlock);
        } catch (Exception e) {
            log.error("error", e);
        }
        return result.isEmpty() ? 0 : (Integer) result.get(0).get("count");
    }

    public Document findOne(String collectionName, Map<String, Object> params) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            Document p = getFilter(params);
            return database.getCollection(collectionName).find(p).first();
        } catch (Exception ex) {
            log.error("", ex);
        }
        return null;
    }

    public Document findOne(String collectionName, Bson params) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            return database.getCollection(collectionName).find(params).first();
        } catch (Exception ex) {
            log.error("", ex);
        }
        return null;
    }

    public List<Document> find(String collectionName, Map<String, Object> params) {
        List<Document> result = null;
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            Document p = getFilter(params);
            FindIterable<Document> iterable = database.getCollection(collectionName).find(p);
            result = new ArrayList<>();
            for (Document document : iterable) {
                result.add(document);
            }
        } catch (Exception ex) {
            log.error("", ex);
        }
        return result;
    }

    private Document getFilter(Map<String, Object> params) {
        Document p = new Document();
        if (!params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                p.put(entry.getKey(), entry.getValue());
            }
        }
        return p;
    }

    public List<Document> find(String collectionName, Map<String, Object> params, Bson sort, int start, int limit) {
        List<Document> result = null;
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            Document p = getFilter(params);
            FindIterable<Document> iterable = database.getCollection(collectionName).find(p).sort(sort).skip(start).limit(limit);
            result = new ArrayList<>();
            for (Document document : iterable) {
                result.add(document);
            }
        } catch (Exception ex) {
            log.error("", ex);
        }
        return result;
    }

    public List<Document> find(String collectionName, Map<String, Object> params, int limit) {
        List<Document> result = null;
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            Document p = getFilter(params);
            FindIterable<Document> iterable = database.getCollection(collectionName).find(p).limit(limit);
            result = new ArrayList<>();
            for (Document document : iterable) {
                result.add(document);
            }
        } catch (Exception ex) {
            log.error("", ex);
        }
        return result;
    }

    public long update(String collectionName, Bson condition, Bson values) {
        long count = 0;
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            UpdateResult result = database.getCollection(collectionName).updateMany(condition, values);
            count = result.getModifiedCount();
            log.info("update: {} result: {}", collectionName, result);
        } catch (Exception ex) {
            log.error("Ex: ", ex);
        }
        return count;
    }

    public void update(String collectionName, Bson condition, Bson values, boolean upsert) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            UpdateOptions options = new UpdateOptions().upsert(upsert);
            UpdateResult result = database.getCollection(collectionName).updateMany(condition, values, options);
            log.info("update: {} result: {}", collectionName, result);
        } catch (Exception ex) {
            log.error("Ex: ", ex);
        }
    }

    public void insertOne(String collectionName, Document document) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            database.getCollection(collectionName).insertOne(document);
        } catch (Exception ex) {
            log.error("Ex: ", ex);
        }
    }

    public void insertMany(String collectionName, List<Document> documents) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            database.getCollection(collectionName).insertMany(documents);
        } catch (Exception ex) {
            log.error("Ex: ", ex);
        }
    }

    public <T> void insertOne(T document, Class<T> clazz) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            database.getCollection(clazz.getSimpleName(), clazz).insertOne(document);
        } catch (Exception ex) {
            log.error("Ex: ", ex);
        }
    }

    public <T> void insertMany(List<T> documents, Class<T> clazz) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            database.getCollection(clazz.getSimpleName(), clazz).insertMany(documents);
        } catch (Exception ex) {
            log.error("Ex: ", ex);
        }
    }

    public <T> void replaceOne(Bson filter, T document, Class<T> clazz, boolean isUpsert) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            database.getCollection(clazz.getSimpleName(), clazz).replaceOne(filter, document, new ReplaceOptions().upsert(isUpsert));
        } catch (Exception ex) {
            log.error("Ex: ", ex);
        }
    }


    public <T> Long deleteMany(Bson filter, Class<T> clazz) {
        try {
            MongoDatabase database = mongoDbAccess.getDatabase();
            DeleteResult deleteResult = database.getCollection(clazz.getSimpleName(), clazz).deleteMany(filter);
            return deleteResult.getDeletedCount();
        } catch (Exception ex) {
            log.error("Ex: ", ex);
            return 0L;
        }
    }
}
