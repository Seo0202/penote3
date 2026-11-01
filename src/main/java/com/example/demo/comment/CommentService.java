package com.example.demo.comment;

import com.example.demo.article.Article;
import com.example.demo.article.ArticleNotFoundException;
import com.example.demo.article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Long create(Long articleId, CommentRequest commentRequest) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        return commentRepository.save(commentRequest.toEntity(article)).getId();
    }

    @Transactional(readOnly = true)
    public CommentResponse findById(Long articleId, Long commentId) {
        return CommentResponse.from(getCommentOf(articleId, commentId));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findAllByArticleId(Long articleId) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : commentRepository.findByArticleIdOrderByIdAsc(articleId)) {
            commentResponses.add(CommentResponse.from(comment));
        }
        return commentResponses;
    }

    @Transactional
    public Long update(Long articleId, Long commentId, CommentUpdateRequest request) {
        Comment comment = getCommentOf(articleId, commentId);
        comment.update(request.getContent());
        return comment.getId();
    }

    @Transactional
    public void delete(Long articleId, Long commentId) {
        commentRepository.delete(getCommentOf(articleId, commentId));
    }

    // 댓글이 존재하고, 그 글(articleId)의 댓글일 때만 돌려준다.
    private Comment getCommentOf(Long articleId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        if (!comment.getArticle().getId().equals(articleId)) {
            throw new CommentNotFoundException(commentId);
        }
        return comment;
    }
}
