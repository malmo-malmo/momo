package com.momo.domain.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.momo.common.RepositoryTest;
import com.momo.domain.district.entity.City;
import com.momo.domain.group.entity.Group;
import com.momo.domain.post.entity.Post;
import com.momo.domain.post.entity.PostType;
import com.momo.domain.user.entity.SocialProvider;
import com.momo.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@DisplayName("게시글 레포지토리 테스트")
public class PostRepositoryTest extends RepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Group group;
    private Post post;

    @BeforeEach
    void before() {
        user = save(
            User.builder()
                .provider(SocialProvider.KAKAO)
                .providerId("test")
                .refreshToken("refresh Token")
                .nickname("testMan")
                .imageUrl("이미지 URL")
                .city(City.SEOUL)
                .district("마포구")
                .university("한국대")
                .build()
        );
        group = save(Group.builder()
            .city(City.SEOUL)
            .district("마포")
            .imageUrl("이미지 URL")
            .introduction("안녕하세요")
            .university("한국대")
            .isOffline(false)
            .isEnd(false)
            .startDate(LocalDate.now())
            .name("모임 이름")
            .manager(user)
            .build());
        post = save(
            Post.builder()
                .author(user)
                .group(group)
                .title("테스트 게시글")
                .contents("테스트 내용")
                .type(PostType.NORMAL)
                .build()
        );
    }

    @Test
    void 게시글을_저장한다() {
        Post post = postRepository.findAll().get(0);
        Assertions.assertAll(
            () -> assertThat(post).isEqualTo(this.post),
            () -> assertThat(post.getAuthor()).isEqualTo(user),
            () -> assertThat(post.getGroup()).isEqualTo(group),
            () -> assertThat(post.getTitle()).isEqualTo("테스트 게시글"),
            () -> assertThat(post.getContents()).isEqualTo("테스트 내용"),
            () -> assertThat(post.getType()).isEqualTo(PostType.NORMAL)
        );
    }

    @Test
    void 게시글_번호로_게시글을_조회한다() {
        Long postId = this.post.getId();
        Post post = postRepository.findPostAndAuthorById(postId);
        assertThat(post).isEqualTo(this.post);
    }

    @Test
    void 로그인한_유저가_올린_게시물을_조회한다() {
        List<Post> actual = postRepository
            .findAllWithGroupAndAuthorByUserOrderByCreatedDateDesc(user, PageRequest.of(0, 10));
        Assertions.assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(1),
            () -> assertThat(actual.get(0).getId()).isNotNull(),
            () -> assertThat(actual.get(0).getAuthor().getId()).isEqualTo(user.getId()),
            () -> assertThat(actual.get(0).getGroup().getId()).isEqualTo(group.getId()),
            () -> assertThat(actual.get(0).getTitle()).isEqualTo(post.getTitle()),
            () -> assertThat(actual.get(0).getContents()).isEqualTo(post.getContents())
        );
    }
}
