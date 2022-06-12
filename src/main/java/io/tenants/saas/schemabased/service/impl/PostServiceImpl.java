package io.tenants.saas.schemabased.service.impl;

import io.tenants.saas.schemabased.database.saas.model.Post;
import io.tenants.saas.schemabased.database.saas.repository.PostRepository;
import io.tenants.saas.schemabased.service.PostService;
import io.tenants.saas.schemabased.web.dto.PostCreationDto;
import io.tenants.saas.schemabased.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public void createPost(PostCreationDto postCreationDto) {
        Post post = new Post();
        post.setContent(postCreationDto.getContent());
        // do not publish with a new post
        post.setPublished(false);
        postRepository.save(post);
    }

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> PostDto.builder()
                        .content(post.getContent())
                        .build())
                .toList();
    }
}
