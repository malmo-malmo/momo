package com.momo.post.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.momo.RestDocsControllerTest;
import com.momo.api.post.PostController;
import com.momo.domain.post.dto.PostCardResponse;
import com.momo.domain.post.dto.PostResponse;
import com.momo.domain.post.service.PostService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

@WebMvcTest(PostController.class)
@DisplayName("게시글 문서화 테스트")
public class PostRestDocsTest extends RestDocsControllerTest {

    @InjectMocks
    private PostController postController;
    @MockBean
    private PostService postService;

    @Test
    public void 게시글_작성() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "upload.png", null, new ClassPathResource("upload-test.png").getInputStream());
        super.mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/api/post")
                .file(file)
                .param("groupId", String.valueOf(1L))
                .param("title", "테스트 게시글")
                .param("contents", "테스트 게시글 내용")
                .param("typeName", "테스트 타입")
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(PostDocumentation.create());
    }

    @Test
    public void 게시글_상세_조회() throws Exception {
        when(postService.findById(any(), anyLong())).thenReturn(PostResponse.builder()
            .id(1L)
            .authorId(1L)
            .authorImage("http://~~")
            .authorNickname("테스트맨")
            .title("테스트 게시글 제목")
            .contents("테스트 게시글 내용")
            .imageUrls(List.of("http://~~"))
            .createdDate(LocalDateTime.now())
            .build()
        );

        super.mockMvc.perform(get("/api/post/{id}", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(PostDocumentation.find());
    }

    @Test
    public void 게시글_목록_조회() throws Exception {
        when(postService.findPageByGroupIdAndType(any(), any())).thenReturn(List.of(
            PostCardResponse.builder()
                .id(1L)
                .authorImage("http://~~")
                .authorNickname("테스트맨")
                .title("테스트 게시글")
                .contents("테스트 내용")
                .createdDate(LocalDateTime.now())
                .commentCnt(1L)
                .build()
        ));
        super.mockMvc.perform(get("/api/posts/paging", 1L)
                .param("groupId", String.valueOf(1L))
                .param("type", "테스트 타입")
                .param("page", String.valueOf(1))
                .param("size", String.valueOf(10))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(PostDocumentation.findPageByCardsRequest());

    }
}