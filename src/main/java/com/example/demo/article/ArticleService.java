package com.example.demo.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Long create(ArticleRequest articleRequest) {
        return articleRepository.save(articleRequest.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public ArticleResponse findById(Long id) {
        return ArticleResponse.from(getArticle(id));
    }

    @Transactional
    public Long update(Long id, ArticleRequest articleRequest) {
        Article article = getArticle(id);
        article.update(articleRequest.getTitle(), articleRequest.getContent());
        return article.getId();
    }

    private Article getArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 게시글이 없습니다. id=" + id));
    }

    @Transactional
    public void delete(Long id) {
        Article article = getArticle(id);
        articleRepository.delete(article);
    }


}
