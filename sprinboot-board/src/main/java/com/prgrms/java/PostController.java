package com.prgrms.java;

import com.prgrms.java.dto.CreatePostRequest;
import com.prgrms.java.dto.GetPostDetailsResponse;
import com.prgrms.java.dto.GetPostsResponse;
import com.prgrms.java.dto.ModifyPostRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<GetPostsResponse> getPosts(Pageable pageable) {
        GetPostsResponse posts = postService.getPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPostDetailsResponse> modifyPost(@PathVariable(value = "id") Long id) {
        GetPostDetailsResponse postDetail = postService.getPostDetail(id);
        return ResponseEntity.ok(postDetail);
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody CreatePostRequest request) {
        long postId = postService.createPost(request);
        return ResponseEntity.ok(MessageFormat.format("Create Post Success! [Created Post ID]: {0}", postId));
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> modifyPost(@PathVariable(value = "id") Long id, @RequestBody ModifyPostRequest request) {
        postService.modifyPost(id, request);
        return ResponseEntity.ok(MessageFormat.format("Modify Post Success! [Modified Post ID]: {0}", id));
    }
}
