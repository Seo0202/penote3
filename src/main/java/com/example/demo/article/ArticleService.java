package com.example.demo.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Long create(ArticleRequest articleRequest) {
        return articleRepository.save(articleRequest.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }
}
