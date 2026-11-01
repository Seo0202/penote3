package com.example.demo.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    public String create(ArticleRequest articleRequest) {
        Long id = articleService.create(articleRequest);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "articles/show";
    }

    @GetMapping("/articles/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new ArticleRequest());
        return "articles/new";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "articles/edit";
    }

    @PatchMapping("/articles/{id}")
    public String update(@PathVariable Long id, ArticleRequest articleRequest) {
        articleService.update(id, articleRequest);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id) {
        articleService.delete(id);
        return "redirect:/articles";
    }

    @GetMapping("/articles")
    public String list(Model model) {
        model.addAttribute("articleList", articleService.findAll());
        return "articles/list";
    }
}
