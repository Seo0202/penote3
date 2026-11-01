package com.example.demo.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateRequest {

    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 100, message = "내용은 100자 이하로 입력하세요.")
    private String content;

    public static CommentUpdateRequest from(CommentResponse response) {
        CommentUpdateRequest request = new CommentUpdateRequest();
        request.content = response.getContent();
        return request;
    }
}
