package com.example.demo.controller;

import com.example.demo.pojo.Child;
import com.example.demo.pojo.MainObjective;
import com.example.demo.pojo.RedditResponse;
import com.example.demo.service.ServiceLayer;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@Component
@RestController
public class Handler {

@Autowired
    private ServiceLayer layer;
    @GetMapping("/")
    public String help()
    {
        return "homepage";
    }
    @GetMapping("/getdata")
    public Mono<RedditResponse> getData() {
        WebClient client=WebClient.create();

        Mono<RedditResponse>response= client
                .get()
                .uri("https://oauth.reddit.com/r/appletv/new?limit=50")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IlNIQTI1NjpzS3dsMnlsV0VtMjVmcXhwTU40cWY4MXE2OWFFdWFyMnpLMUdhVGxjdWNZIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjg2ODk0NzY1LjczMTg2OSwiaWF0IjoxNjg2ODA4MzY1LjczMTg2OSwianRpIjoiR21wVjdvTjRIYVZBRlVTd1pUS0FlaXdScVFqc3ZRIiwiY2lkIjoiT1FTSnBtdWMwcWVjT3NlelpBcGYtdyIsImxpZCI6InQyX2FtaHhnNjYyIiwiYWlkIjoidDJfYW1oeGc2NjIiLCJsY2EiOjE2MTQ1MTAxMDkwMDAsInNjcCI6ImVKeUtWdEpTaWdVRUFBRF9fd056QVNjIiwiZmxvIjo5fQ.Ki2gso8rTePHIhJuxHtLn0ZpsecHwe8ywYEJudOqurjbwrlYoDuaZ7ckBLT1kZ8_xzVniSaeSsDFbNUkqClSggZ34qwpOv8dF8WZ4yslCTO0aVl4tlS7sFhU0Ei5Xi_uxbkkOrvENQR1tS-E0YCd9DEN-qeWkCORoU_f34wPJALyWMPo9j6EmtSmK2RVr-gbYocoiYk6wYvleesWVqZFwi_7Yh2W4GlaDxOZySppbotsl6a9R9GAXoi7Cvfdw4w72Zngc0oNzIiDvbsSsauuGW6QLZCqEnOxVQSCHYKdAHvIBjzeKb82TaDj1-FplDtOqt3MskPM-inqCgLYiNCShw")
                   .retrieve()
                .bodyToMono(RedditResponse.class);


//           a.subscribe(t->t.getData().getChildren().stream().map(g->g.getData().getTitle()).collect(Collectors.toList()).stream().forEach(f-> System.out.println(f)));
        response.subscribe(t->t.getData().getChildren().stream().forEach(g->layer.saveMethod(g.getData())));
        return response;
    }

    @GetMapping("/db/findall")
    public Flux<MainObjective>getAllDataMethod()
    {
       return layer.getAllDataMethod();
    }

    @DeleteMapping("/db/delete/{id}")
    public Mono<Void> deleteDataMethod(@PathVariable String id)
    {
        return layer.DeleteMethodd(id);
    }
    @DeleteMapping("/db/delete/author/{author}")
    public Mono<Void> deleteDataMethodAuthor(@PathVariable String author)
    {
        return layer.DeleteMethodAuthor(author);
    }

    @GetMapping("db/find/{AuthorName}")
    public Flux<MainObjective> findByAuthorNameMethod(@PathVariable String AuthorName)
    {
        return layer.getAllByAuthorMethod(AuthorName);
    }


    @GetMapping("db/find/keyword/{keyword}")
    public Flux<MainObjective>findByKeywordMethod(@PathVariable String keyword)
    {
        return layer.getByKeywordMethod(keyword);
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

        Mono<String>response= client
                .post()
                .uri("https://oauth.reddit.com/api/submit?sr="+sr+"&kind=self&text="+text+"&title="+title)
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IlNIQTI1NjpzS3dsMnlsV0VtMjVmcXhwTU40cWY4MXE2OWFFdWFyMnpLMUdhVGxjdWNZIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjg2ODk0NzY1LjczMTg2OSwiaWF0IjoxNjg2ODA4MzY1LjczMTg2OSwianRpIjoiR21wVjdvTjRIYVZBRlVTd1pUS0FlaXdScVFqc3ZRIiwiY2lkIjoiT1FTSnBtdWMwcWVjT3NlelpBcGYtdyIsImxpZCI6InQyX2FtaHhnNjYyIiwiYWlkIjoidDJfYW1oeGc2NjIiLCJsY2EiOjE2MTQ1MTAxMDkwMDAsInNjcCI6ImVKeUtWdEpTaWdVRUFBRF9fd056QVNjIiwiZmxvIjo5fQ.Ki2gso8rTePHIhJuxHtLn0ZpsecHwe8ywYEJudOqurjbwrlYoDuaZ7ckBLT1kZ8_xzVniSaeSsDFbNUkqClSggZ34qwpOv8dF8WZ4yslCTO0aVl4tlS7sFhU0Ei5Xi_uxbkkOrvENQR1tS-E0YCd9DEN-qeWkCORoU_f34wPJALyWMPo9j6EmtSmK2RVr-gbYocoiYk6wYvleesWVqZFwi_7Yh2W4GlaDxOZySppbotsl6a9R9GAXoi7Cvfdw4w72Zngc0oNzIiDvbsSsauuGW6QLZCqEnOxVQSCHYKdAHvIBjzeKb82TaDj1-FplDtOqt3MskPM-inqCgLYiNCShw")
                .retrieve()
                .bodyToMono(String.class);

        return response;

    }



}
