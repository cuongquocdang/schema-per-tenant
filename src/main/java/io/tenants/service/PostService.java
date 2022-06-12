package io.tenants.service;

import io.tenants.web.dto.PostCreationDto;
import io.tenants.web.dto.PostDto;

import java.util.List;

public interface PostService {

    void createPost(PostCreationDto postCreationDto);

    List<PostDto> getAllPosts();
}
