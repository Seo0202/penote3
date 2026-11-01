package com.example.demo.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional(readOnly = true)
    public List<ArticleResponse> findAll() {
        List<ArticleResponse> articleResponses = new ArrayList<>();
        for (Article article : articleRepository.findAllByOrderByIdDesc()) {
            articleResponses.add(ArticleResponse.from(article));
        }
        return articleResponses;
    }

    @Transactional
    public Long update(Long id, ArticleUpdateRequest request) {
        Article article = getArticle(id);
        article.update(request.getTitle(), request.getContent());
        return article.getId();
    }

    @Transactional
    public void delete(Long id) {
        articleRepository.delete(getArticle(id));
    }

    private Article getArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }
}
