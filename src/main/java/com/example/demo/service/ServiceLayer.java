package com.example.demo.service;


//import org.springframework.data.mongodb.core.query.Query;

import com.example.demo.mongorepo.RepositoryMain;
import com.example.demo.pojo.MainObjective;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServiceLayer {
    @Autowired
private ReactiveMongoTemplate reactiveMongoTemplate;
    @Autowired
    private RepositoryMain repo;

    public void saveMethod(MainObjective object)
    {
      Mono<MainObjective>d=  repo.save(object);
    }


    public Flux<MainObjective> getAllDataMethod() {
//
         Flux<MainObjective> all = repo.findAll();
         return all;
    }

    public Mono<Void> DeleteMethodd(String id)
    {

       return repo.deleteById(id);
    }

    public Mono<Void> DeleteMethodAuthor(String author)
    {

        return repo.deleteByAuthor(author);
    }

    public Flux<MainObjective> getAllByAuthorMethod(String authorName) {

        return repo.findByAuthor(authorName);
    }

    public Flux<MainObjective> getByKeywordMethod(String keyword) {


        Flux<MainObjective> byExactMatch = repo.getMainObjectiveByRegEx(keyword);
        byExactMatch.subscribe(entity -> System.out.println(entity));
        return byExactMatch;
    }

    public Flux<MainObjective> sortAllCreatedMethod()
    {
        return repo.findAll(Sort.by(Sort.Direction.ASC,"createdutc"));
    }
}
