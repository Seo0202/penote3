package com.example.demo.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequest {

    private String author;
    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
