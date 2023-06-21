package com.example.demo.mongorepo;

import com.example.demo.pojo.Espojo;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticsearchRepo extends ReactiveElasticsearchRepository<Espojo,String> {
    @Query("{\"match\":{\"selftext\":\"?0\"}}")
    Flux<Espojo> findByCustomQuery(String keyword);
    @Query("{\"match\":{\"title\":\"?0\"}}")
    Flux<Espojo> findByCustomQueryTitle(String keyword);


}
