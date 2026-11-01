package com.example.demo.article;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    ArticleService articleService;

    private Article article(String title, String content, String author) {
        return Article.builder().title(title).content(content).author(author).build();
    }

    @Test
    void findById_없으면_예외() {
        given(articleRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.findById(1L))
                .isInstanceOf(ArticleNotFoundException.class);
    }

    @Test
    void findAll_엔티티를_응답으로_변환() {
        given(articleRepository.findAllByOrderByIdDesc())
                .willReturn(List.of(article("t1", "c1", "a1"), article("t2", "c2", "a2")));

        List<ArticleResponse> result = articleService.findAll();

        assertThat(result).extracting(ArticleResponse::getTitle).containsExactly("t1", "t2");
    }

    @Test
    void update_제목과_내용만_바뀌고_작성자는_유지() {
        Article article = article("old", "oldContent", "writer");
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        ArticleUpdateRequest request = new ArticleUpdateRequest();
        request.setTitle("new");
        request.setContent("newContent");

        articleService.update(1L, request);

        assertThat(article.getTitle()).isEqualTo("new");
        assertThat(article.getContent()).isEqualTo("newContent");
        assertThat(article.getAuthor()).isEqualTo("writer");
    }

    @Test
    void update_없으면_예외() {
        given(articleRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.update(1L, new ArticleUpdateRequest()))
                .isInstanceOf(ArticleNotFoundException.class);
    }

    @Test
    void delete_조회한_글을_삭제() {
        Article article = article("t", "c", "a");
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));

        articleService.delete(1L);

        verify(articleRepository).delete(article);
    }

    @Test
    void delete_없으면_삭제하지_않고_예외() {
        given(articleRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.delete(1L))
                .isInstanceOf(ArticleNotFoundException.class);
        verify(articleRepository, never()).delete(any());
    }
}
