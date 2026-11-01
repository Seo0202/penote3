package com.example.demo.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/articles/{articleId}/comments")
    public String list(@PathVariable Long articleId, Model model) {
        model.addAttribute("commentList", commentService.findAllByArticleId(articleId));
        return "comments/list";
    }

    @GetMapping("/articles/{articleId}/comments/new")
    public String newComment(Model model) {
        model.addAttribute("comment", new CommentRequest());
        return "comments/new";
    }

    @PostMapping("/articles/{articleId}/comments")
    public String create(@PathVariable Long articleId,
                         @Valid @ModelAttribute("comment") CommentRequest commentRequest,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "comments/new";
        }
        Long id = commentService.create(articleId, commentRequest);
        return "redirect:/comments/" + id;
    }

    @GetMapping("/articles/{articleId}/comments/{id}")
    public String show(@PathVariable Long articleId, @PathVariable Long id, Model model) {
        model.addAttribute("comment", commentService.findById(articleId, id));
        return "comments/show";
    }

    @GetMapping("/articles/{articleId}/comments/{id}/edit")
    public String edit(@PathVariable Long articleId, @PathVariable Long id, Model model) {
        CommentResponse response = commentService.findById(articleId, id);
        model.addAttribute("comment", CommentUpdateRequest.from(response));
        addEditAttributes(model, response);
        return "comments/edit";
    }

    @PatchMapping("/articles/{articleId}/comments/{id}")
    public String update(@PathVariable Long articleId,
                         @PathVariable Long id,
                         @Valid @ModelAttribute("comment") CommentUpdateRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            addEditAttributes(model, commentService.findById(articleId, id));
            return "comments/edit";
        }
        commentService.update(articleId, id, request);
        return "redirect:/comments/" + id;
    }

    @DeleteMapping("/articles/{articleId}/comments/{id}")
    public String delete(@PathVariable Long articleId, @PathVariable Long id) {
        commentService.delete(articleId, id);
        return "redirect:/comments";
    }

    private void addEditAttributes(Model model, CommentResponse response) {
        model.addAttribute("commentId", response.getId());
        model.addAttribute("author", response.getAuthor());
    }
}
