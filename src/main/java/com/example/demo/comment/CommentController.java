package com.example.demo.comment;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public String create(@PathVariable Long articleId,
                         @Valid @ModelAttribute("comment") CommentRequest commentRequest,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addErrorMessage(redirectAttributes, bindingResult);
            return "redirect:/articles/" + articleId;
        }
        commentService.create(articleId, commentRequest);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}/comments/{id}/edit")
    public String edit(@PathVariable Long articleId, @PathVariable Long id, Model model) {
        CommentResponse response = commentService.findById(articleId, id);
        model.addAttribute("comment", CommentUpdateRequest.from(response));
        model.addAttribute("articleId", articleId);
        model.addAttribute("commentId", response.getId());
        model.addAttribute("author", response.getAuthor());
        return "comments/edit";
    }

    @PatchMapping("/articles/{articleId}/comments/{id}")
    public String update(@PathVariable Long articleId,
                         @PathVariable Long id,
                         @Valid @ModelAttribute("comment") CommentUpdateRequest request,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addErrorMessage(redirectAttributes, bindingResult);
            return "redirect:/articles/" + articleId + "/comments/" + id + "/edit";
        }
        commentService.update(articleId, id, request);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}/comments/{id}")
    public String delete(@PathVariable Long articleId, @PathVariable Long id) {
        commentService.delete(articleId, id);
        return "redirect:/articles/" + articleId;
    }

    private void addErrorMessage(RedirectAttributes redirectAttributes, BindingResult bindingResult) {
        String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        redirectAttributes.addFlashAttribute("commentError", message);
    }
}
