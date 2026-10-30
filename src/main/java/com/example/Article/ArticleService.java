package com.example.Article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Long create(ArticleRequest articleRequest) {
        Article article = articleRequest.toEntity();
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 게시글이 없습니다. id=" + id
                ));
    }
}