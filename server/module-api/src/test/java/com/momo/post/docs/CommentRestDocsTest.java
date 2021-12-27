package com.momo.post.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.momo.RestDocsControllerTest;
import com.momo.api.post.CommentController;
import com.momo.domain.post.domain.model.Comment;
import com.momo.domain.post.dto.CommentCreateRequest;
import com.momo.domain.post.dto.CommentResponse;
import com.momo.domain.post.dto.CommentsResponse;
import com.momo.domain.post.service.CommentService;
import com.momo.domain.user.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(CommentController.class)
@DisplayName("댓글 문서화 테스트")
public class CommentRestDocsTest extends RestDocsControllerTest {

    @InjectMocks
    private CommentController commentController;
    @MockBean
    private CommentService commentService;

    @Test
    public void 게시물_댓글_등록() throws Exception {
        when(commentService.create(any(), any())).thenReturn(CommentResponse.builder()
            .id(1l)
            .authorId(1l)
            .authorImage("http://~~")
            .authorNickname("테스트맨")
            .contents("테스트 댓글")
            .createdDate(LocalDateTime.now())
            .build()
        );

        CommentCreateRequest request = new CommentCreateRequest(1l, "테스트 댓글");
        String content = super.objectMapper.writeValueAsString(request);
        super.mockMvc.perform(post("/api/comment")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(CommentDocumentation.create());
    }

    @Test
    public void 게시물_댓글_목록_조회() throws Exception {
        when(commentService.findPageByPostId(any(), any())).thenReturn(CommentsResponse.of(
            List.of(
                Comment.builder()
                    .id(1l)
                    .user(User.builder().id(1l).imageUrl("http://~~").nickname("테스트맨").build())
                    .contents("테스트 댓글")
                    .createdDate(LocalDateTime.now())
                    .build()
            ),
            1l
        ));

        super.mockMvc.perform(get("/api/comments/paging")
                .param("postId", String.valueOf(1l))
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(10))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(CommentDocumentation.findPageByPost());
    }
}
