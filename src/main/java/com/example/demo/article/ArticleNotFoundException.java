package com.example.demo.article;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(Long id) {
        super("해당 게시글이 없습니다. id=" + id);
    }
}
