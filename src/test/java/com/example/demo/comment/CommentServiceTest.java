package com.example.demo.comment;

import com.example.demo.article.Article;
import com.example.demo.article.ArticleNotFoundException;
import com.example.demo.article.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    CommentService commentService;

    private Article article(Long id) {
        Article article = Article.builder().title("t").content("c").author("a").build();
        ReflectionTestUtils.setField(article, "id", id);
        return article;
    }

    private Comment comment(Article article, String content, String author) {
        return Comment.builder().article(article).content(content).author(author).build();
    }

    private CommentRequest request(String content, String author) {
        CommentRequest request = new CommentRequest();
        request.setContent(content);
        request.setAuthor(author);
        return request;
    }

    private CommentUpdateRequest updateRequest(String content) {
        CommentUpdateRequest request = new CommentUpdateRequest();
        request.setContent(content);
        return request;
    }

    // ---------- create ----------

    @Test
    void create_글에_댓글을_연결해서_저장() {
        Article article = article(1L);
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 10L);
            return saved;
        });

        Long id = commentService.create(1L, request("내용", "작성자"));

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        assertThat(captor.getValue().getArticle()).isSameAs(article);
        assertThat(captor.getValue().getContent()).isEqualTo("내용");
        assertThat(captor.getValue().getAuthor()).isEqualTo("작성자");
        assertThat(id).isEqualTo(10L);
    }

    @Test
    void create_없는_글이면_예외이고_저장하지_않음() {
        given(articleRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.create(1L, request("내용", "작성자")))
                .isInstanceOf(ArticleNotFoundException.class);
        verify(commentRepository, never()).save(any());
    }

    // ---------- findById ----------

    @Test
    void findById_그_글의_댓글이면_조회() {
        given(commentRepository.findById(5L))
                .willReturn(Optional.of(comment(article(1L), "내용", "작성자")));

        CommentResponse response = commentService.findById(1L, 5L);

        assertThat(response.getContent()).isEqualTo("내용");
        assertThat(response.getAuthor()).isEqualTo("작성자");
    }

    @Test
    void findById_없는_댓글이면_예외() {
        given(commentRepository.findById(5L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.findById(1L, 5L))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void findById_다른_글의_댓글이면_예외() {
        given(commentRepository.findById(5L))
                .willReturn(Optional.of(comment(article(2L), "내용", "작성자")));

        assertThatThrownBy(() -> commentService.findById(1L, 5L))
                .isInstanceOf(CommentNotFoundException.class);
    }

    // ---------- findAllByArticleId ----------

    @Test
    void findAllByArticleId_엔티티를_응답으로_변환() {
        Article article = article(1L);
        given(commentRepository.findByArticleIdOrderByIdAsc(1L))
                .willReturn(List.of(comment(article, "c1", "a1"), comment(article, "c2", "a2")));

        List<CommentResponse> result = commentService.findAllByArticleId(1L);

        assertThat(result).extracting(CommentResponse::getContent).containsExactly("c1", "c2");
    }

    // ---------- update ----------

    @Test
    void update_내용만_바뀌고_작성자는_유지() {
        Comment comment = comment(article(1L), "old", "writer");
        given(commentRepository.findById(5L)).willReturn(Optional.of(comment));

        commentService.update(1L, 5L, updateRequest("new"));

        assertThat(comment.getContent()).isEqualTo("new");
        assertThat(comment.getAuthor()).isEqualTo("writer");
    }

    @Test
    void update_다른_글의_댓글이면_예외이고_내용은_그대로() {
        Comment comment = comment(article(2L), "old", "writer");
        given(commentRepository.findById(5L)).willReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.update(1L, 5L, updateRequest("new")))
                .isInstanceOf(CommentNotFoundException.class);
        assertThat(comment.getContent()).isEqualTo("old");
    }

    // ---------- delete ----------

    @Test
    void delete_그_글의_댓글이면_삭제() {
        Comment comment = comment(article(1L), "내용", "작성자");
        given(commentRepository.findById(5L)).willReturn(Optional.of(comment));

        commentService.delete(1L, 5L);

        verify(commentRepository).delete(comment);
    }

    @Test
    void delete_다른_글의_댓글이면_예외이고_삭제하지_않음() {
        given(commentRepository.findById(5L))
                .willReturn(Optional.of(comment(article(2L), "내용", "작성자")));

        assertThatThrownBy(() -> commentService.delete(1L, 5L))
                .isInstanceOf(CommentNotFoundException.class);
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void delete_없는_댓글이면_예외() {
        given(commentRepository.findById(5L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.delete(1L, 5L))
                .isInstanceOf(CommentNotFoundException.class);
        verify(commentRepository, never()).delete(any());
    }
}
