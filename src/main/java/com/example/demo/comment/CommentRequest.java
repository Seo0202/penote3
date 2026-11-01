package com.example.demo.comment;

import com.example.demo.article.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {

    @NotBlank(message = "작성자를 입력하세요.")
    @Size(max = 50, message = "작성자는 50자 이하로 입력하세요.")
    private String author;

    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 100, message = "내용은 100자 이하로 입력하세요.")
    private String content;

    public Comment toEntity(Article article) {
        return Comment.builder()
                .article(article)
                .content(content)
                .author(author)
                .build();
    }
}
