package com.example.Article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/articles")
    public String create(@ModelAttribute ArticleRequest articleRequest)
    {
        Long id = articleService.create(articleRequest);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);

        return "articles/show";
    }
}
