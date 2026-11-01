package com.example.demo.article;

import com.example.demo.comment.CommentRequest;
import com.example.demo.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/articles")
    public String list(Model model) {
        model.addAttribute("articleList", articleService.findAll());
        return "articles/list";
    }

    @GetMapping("/articles/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new ArticleRequest());
        return "articles/new";
    }

    @PostMapping("/articles")
    public String create(@Valid @ModelAttribute("article") ArticleRequest articleRequest,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "articles/new";
        }
        Long id = articleService.create(articleRequest);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        model.addAttribute("commentList", commentService.findAllByArticleId(id));
        model.addAttribute("comment", new CommentRequest());
        return "articles/show";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        ArticleResponse response = articleService.findById(id);
        model.addAttribute("article", ArticleUpdateRequest.from(response));
        addEditAttributes(model, response);
        return "articles/edit";
    }

    @PatchMapping("/articles/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("article") ArticleUpdateRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            addEditAttributes(model, articleService.findById(id));
            return "articles/edit";
        }
        articleService.update(id, request);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id) {
        articleService.delete(id);
        return "redirect:/articles";
    }

    private void addEditAttributes(Model model, ArticleResponse response) {
        model.addAttribute("articleId", response.getId());
        model.addAttribute("author", response.getAuthor());
    }
}
