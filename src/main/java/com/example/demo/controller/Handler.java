package com.example.demo.controller;

import com.example.demo.pojo.Espojo;
import com.example.demo.pojo.MainObjective;
import com.example.demo.pojo.RedditResponse;
import com.example.demo.service.ServiceLayer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Scanner;


@Component
@RestController
public class Handler {

    @Autowired
    private WebClient client;
    @Autowired
    private ServiceLayer layer;
   String token;
    @GetMapping("/")
    public String help()
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("OQSJpmuc0qecOsezZApf-w", "YNEaPZdhRUnQ8Hq8DsJC-kgLKUag2g");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = "grant_type=password&username=Correct_Jury_3674&password=jaanu@321";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        String authUrl = "https://www.reddit.com/api/v1/access_token";
        ResponseEntity<String> response = restTemplate.postForEntity(authUrl, request, String.class);

        Map<String,String>map;
        try {

            ObjectMapper mapper=new ObjectMapper();
            map=mapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {
          });
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        token=map.get("access_token");




//                client
//                .post()
//                .uri("https://www.reddit.com/api/v1/access_token")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .headers(httpHeaders -> httpHeaders.setBearerAuth())
        return "homepage";
    }


    @GetMapping("/getdata")
    public  Mono<RedditResponse> getData() {
//            WebClient client=WebClient.create();

//        System.out.println(token);
        String token1="Bearer "+token;
        Mono<RedditResponse> response= client
                .get()
                .uri("https://oauth.reddit.com/r/appletv/new?limit=80")
                .header("Authorization", token1)
                .retrieve()
                .bodyToMono(RedditResponse.class);
        
//        response.subscribe(t->t.getData().getChildren().stream().map(g->g.getData().getTitle()).collect(Collectors.toList()).stream().forEach(f-> System.out.println(f)));
//        response.subscribe(t->t.getData().getChildren().stream().forEach(g->layer.saveMethod(g.getData())));

        response.subscribe(t->t.getData().getChildren().stream().forEach(g->layer.saveMethod(g.getData())));
        //Storing data in MongoRepo and ElasticSearchRepo

        return response;
    }

    @GetMapping("/db/findall")
    public Flux<MainObjective>getAllDataMethod()
    {   Flux<MainObjective> result=layer.getAllDataMethod();

       return result;
    }



    @GetMapping("db/find/{AuthorName}")
    public Flux<MainObjective> findByAuthorNameMethod(@PathVariable String AuthorName)
    {
        return layer.getAllByAuthorMethod(AuthorName);
    }

    @GetMapping("/db/find/title/{title}")
    public Flux<Espojo>getByTitleMethod(@PathVariable String title)
    {   Flux<Espojo> result=layer.queryTitleMethod(title);

        return result;
    }
    @GetMapping("db/find/keyword/{keyword}")
    public Flux<Espojo>findByKeywordMethod(@PathVariable String keyword)
    {
        return layer.queryMethod(keyword);
//        return layer.getByKeywordMethod(keyword);
    }

    @DeleteMapping("/db/delete/{id}")
    public Mono<Void> deleteDataMethod(@PathVariable String id)
    {
          return layer.DeleteMethodd(id);

//


    }
    @DeleteMapping("/db/delete/author/{author}")
    public Mono<Void> deleteDataMethodAuthor(@PathVariable String author)
    {
       return   layer.DeleteMethodAuthor(author);
    }
    @GetMapping("db/sort")
    public Flux<MainObjective>sortAllMethod()
    {
        return layer.sortAllCreatedMethod();
    }


    @PostMapping("db/post/create")
    public Mono<String> createPost()
    {
        WebClient client=WebClient.create();

        Scanner sc=new Scanner(System.in);
        System.out.println("enter the subreddit");
        String sr=sc.nextLine();

        System.out.println("enter the title ");
        String title=sc.nextLine();

        System.out.println("enter the text ");
        String text=sc.nextLine();

        String token1="Bearer "+token;
        Mono<String>response= client
                .post()
                .uri("https://oauth.reddit.com/api/submit?sr="+sr+"&kind=self&text="+text+"&title="+title)
                .header("Authorization", token1)
                .retrieve()
                .bodyToMono(String.class);

        return response;

    }



}
