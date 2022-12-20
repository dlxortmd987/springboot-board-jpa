package com.prgrms.java;

import com.prgrms.java.dto.CreatePostRequest;
import com.prgrms.java.domain.HobbyType;
import com.prgrms.java.domain.Post;
import com.prgrms.java.domain.User;
import com.prgrms.java.dto.GetPostDetailsResponse;
import com.prgrms.java.dto.GetPostsResponse;
import com.prgrms.java.dto.ModifyPostRequest;
import com.prgrms.java.repository.PostRepository;
import com.prgrms.java.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
    }

    @DisplayName("게시글을 페이징 조회할 수 있다.")
    @Test
    void getPostsTest() {
        // given
        User user = userRepository.save(new User("이택승", 25, HobbyType.MOVIE));
        Post post1 = new Post("데브코스 짱짱", "데브코스 짱짱입니다.", user);
        Post post2 = new Post("데브코스 짱짱2", "데브코스 짱짱2입니다.", user);
        List<Post> posts = postRepository.saveAll(List.of(post1, post2));
        GetPostsResponse expected = new GetPostsResponse(
                posts.stream()
                        .map(GetPostDetailsResponse::from)
                        .toList()
        );

        // when
        GetPostsResponse actual = postService.getPosts(PageRequest.of(0, 10));

        // then
        assertThat(actual.getPostDetails())
                .containsAll(expected.getPostDetails());
    }

    @DisplayName("게시글을 단건 조회할 수 있다.")
    @Test
    void getPostDetailTest() {
        // given
        User user = userRepository.save(new User("이택승", 25, HobbyType.MOVIE));
        Post post = postRepository.save(new Post("데브코스 짱짱2", "데브코스 짱짱2입니다.", user));
        GetPostDetailsResponse expected = GetPostDetailsResponse.from(post);

        // when
        GetPostDetailsResponse actual = postService.getPostDetail(post.getId());

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @DisplayName("게시글을 등록할 수 있다.")
    @Test
    void createPostTest() {
        // given
        User user = userRepository.save(new User("이택승", 25, HobbyType.MOVIE));

        CreatePostRequest createPostRequest = new CreatePostRequest(
                "데브코스 짱짱",
                "데브코스 짱짱입니다.",
                user.getId()
        );

        // when
        long postId = postService.createPost(createPostRequest);

        // then
        Optional<Post> maybePost = postRepository.findById(postId);
        assertThat(maybePost)
                .isPresent();
    }

    @DisplayName("게시글을 수정할 수 있다.")
    @Test
    void modifyPostTest() {
        // given
        User user = userRepository.save(new User("이택승", 25, HobbyType.MOVIE));
        Post post = postRepository.save(new Post("데브코스 짱짱", "데브코스 짱짱입니다.", user));

        ModifyPostRequest request = new ModifyPostRequest("데브코스 좋아", "데브코스 좋아용!");

        // when
        postService.modifyPost(post.getId(), request);

        // then
        Optional<Post> maybePost = postRepository.findById(post.getId());
        assertThat(maybePost.get().getTitle())
                .isEqualTo(request.getTitle());
        assertThat(maybePost.get().getContent())
                .isEqualTo(request.getContent());
    }
}