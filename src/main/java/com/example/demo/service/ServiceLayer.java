package com.example.demo.service;


import com.example.demo.mongorepo.ElasticsearchRepo;
import com.example.demo.mongorepo.RepositoryMain;
import com.example.demo.pojo.Espojo;
import com.example.demo.pojo.MainObjective;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


@Service
public class ServiceLayer {
@Autowired
MongoClient mongoClient;
    @Autowired
private ReactiveMongoTemplate reactiveMongoTemplate;
    @Autowired
    private RepositoryMain repo;

    @Autowired
    private ElasticsearchRepo esRepo;

    public void saveMethod(MainObjective object)
    {
        Espojo eObject=new Espojo(object.getSelftext(),object.getTitle(),object.getId());

        Mono<Espojo> save = esRepo.save(eObject);

      Mono<MainObjective>d=  repo.save(object);
      d.subscribe(r-> System.out.println(r.getAuthor()));

    }

    public Flux<Espojo> queryMethod(String keyword)
    {
        return esRepo.findByCustomQuery(keyword);
    }
    public Flux<Espojo> queryTitleMethod(String title)
    {
        return esRepo.findByCustomQueryTitle(title);
    }
    public Flux<MainObjective> getAllDataMethod() {
//

         Flux<MainObjective> all = repo.findAll();

         return all;
    }

    public Mono<Void> DeleteMethodd(String id)
    {
        System.out.println(id);
       Mono<Void>a= esRepo.deleteById(id).then();
        Mono<Void>b= repo.deleteById(id).then();
return Mono.when(a,b).then();
         //            Mono<Void> a=repo.deleteById(id);



    }


    public Mono<Void> DeleteMethodAuthor(String author)
    {

        Flux<MainObjective> responseFromMongo = repo.findByAuthor(author);
        Mono<Void> deleteitem = repo.deleteByAuthor(author).then();
        Flux<Espojo> mapp = responseFromMongo.map(e -> {
            Espojo esobject = new Espojo();
            esobject.setId(e.getId());
            esobject.setSelftext(e.getSelftext());
            esobject.setTitle(e.getTitle());
            return esobject;
        });

        Mono<Void> thens = mapp.flatMap(document -> esRepo.deleteById(document.getId())).then();


        return Mono.when(thens,deleteitem).then();

//        System.out.println("s");





    }

    public Flux<MainObjective> getAllByAuthorMethod(String authorName) {

        return repo.findByAuthor(authorName);
    }

    public Flux<MainObjective> getByKeywordMethod(String keyword) {


        Flux<MainObjective> byExactMatch = repo.getMainObjectiveByRegEx(keyword);

        return byExactMatch;
    }

    public Flux<MainObjective> sortAllCreatedMethod()
    {

        MongoDatabase database = mongoClient.getDatabase("Subreddit");
        MongoCollection<Document> collection = database.getCollection("mainObjective");
        List<Document> aggregationPipeline = Arrays.asList(
                new Document("$sort", new Document("createdutc", 1L))
        ); //sorted using aggregation in mongodb

        Flux<Document> resultM= Flux.from(collection.aggregate(aggregationPipeline));


        Flux<MainObjective> resultFLux=resultM.map(document->{
            MainObjective a=new MainObjective();
            a.setSubreddit(document.getString("subreddit"));
            a.setAuthor(document.getString("author"));
            a.setAuthorfullname(document.getString("authorfullname"));
            a.setSelftext(document.getString("selftext"));
            a.setTitle(document.getString("title"));
            a.setCreated_utc(document.getInteger("createdutc"));
            a.setId(document.getString("_id"));
            return a;
        });
        return resultFLux;
//        return repo.findAll(Sort.by(Sort.Direction.ASC,"createdutc"));
    }
}
