package io.tenants.saas.schemabased.service;

import io.tenants.saas.schemabased.web.dto.PostCreationDto;
import io.tenants.saas.schemabased.web.dto.PostDto;

import java.util.List;

public interface PostService {

    void createPost(PostCreationDto postCreationDto);

    List<PostDto> getAllPosts();
}
