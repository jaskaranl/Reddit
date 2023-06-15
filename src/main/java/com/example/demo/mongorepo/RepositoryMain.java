package com.example.demo.mongorepo;

import com.example.demo.pojo.MainObjective;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryMain extends ReactiveMongoRepository<MainObjective,String> {
Flux<MainObjective>findByAuthor(String AuthorName);
Mono<Void> deleteByAuthor(String authorName);
@Query("{'title': {$regex: '?0'}}")
Flux<MainObjective>getMainObjectiveByRegEx(String name);


}
