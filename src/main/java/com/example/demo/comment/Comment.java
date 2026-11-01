package com.example.demo.comment;

import com.example.demo.article.Article;
import com.example.demo.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false, length = 50)
    private String author;

    @Builder
    public Comment(Article article, String content, String author) {
        this.article = article;
        this.content = content;
        this.author = author;
    }

    public void update(String content) {
        this.content = content;
    }
}
