package com.example.demo.article;

import lombok.Getter;

@Getter
public class ArticleResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;

    private ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
    }

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(article);
    }
}
