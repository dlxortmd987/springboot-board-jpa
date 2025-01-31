package com.prgrms.java.controller;

import com.prgrms.java.domain.Email;
import com.prgrms.java.dto.post.*;
import com.prgrms.java.service.PostService;
import com.prgrms.java.service.UserService;
import com.prgrms.java.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<GetPostsResponse> getPosts(Pageable pageable) {
        final GetPostsResponse posts = postService.getPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPostDetailsResponse> getPostDetails(@PathVariable Long id) {
        final GetPostDetailsResponse postDetail = postService.getPostDetail(id);
        return ResponseEntity.ok(postDetail);
    }

    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest request, HttpServletRequest httpServletRequest) {
        final Email authenticate = authenticate(httpServletRequest);
        final long userId = userService.getUserId(authenticate);

        final long postId = postService.createPost(request, userId);
        return ResponseEntity.ok(new CreatePostResponse(postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModifyPostResponse> modifyPost(@PathVariable Long id, @Valid @RequestBody ModifyPostRequest request, HttpServletRequest httpServletRequest) {
        authenticate(httpServletRequest);

        postService.modifyPost(id, request);
        return ResponseEntity.ok(new ModifyPostResponse(id));
    }

    private Email authenticate(HttpServletRequest httpServletRequest) {
        final String identifier = CookieUtil.getValue(httpServletRequest, CookieUtil.LOGIN_TOKEN);
        return userService.validMember(new Email(identifier));
    }
}
