package io.tenants.saas.schemabased.web.controller;

import io.tenants.saas.schemabased.service.PostService;
import io.tenants.saas.schemabased.web.dto.PostCreationDto;
import io.tenants.saas.schemabased.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@RequestBody PostCreationDto postCreationDto) {
        postService.createPost(postCreationDto);
    }

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

}
