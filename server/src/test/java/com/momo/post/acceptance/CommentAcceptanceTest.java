package com.momo.post.acceptance;

import static com.momo.fixture.GroupFixture.GROUP_CREATE_REQUEST1;
import static com.momo.fixture.PostFixture.NOTICE_CREATE_REQUEST1;
import static com.momo.fixture.UserFixture.USER1;
import static com.momo.group.acceptance.step.GroupAcceptanceStep.requestToCreateGroup;
import static com.momo.post.acceptance.step.CommentAcceptanceStep.assertThatCreateComment;
import static com.momo.post.acceptance.step.CommentAcceptanceStep.assertThatFindComments;
import static com.momo.post.acceptance.step.CommentAcceptanceStep.requestToCreateComment;
import static com.momo.post.acceptance.step.CommentAcceptanceStep.requestToFindComments;
import static com.momo.post.acceptance.step.PostAcceptanceStep.requestToCreatePost;

import com.momo.common.acceptance.AcceptanceTest;
import com.momo.common.acceptance.step.AcceptanceStep;
import com.momo.post.controller.dto.CommentCreateRequest;
import com.momo.post.controller.dto.CommentResponse;
import com.momo.post.controller.dto.CommentsRequest;
import com.momo.post.controller.dto.CommentsResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("게시물 댓글 통합/인수 테스트")
public class CommentAcceptanceTest extends AcceptanceTest {

    @Test
    public void 모임_참여자가_게시물에_댓글을_등록한다() {
        String token = getAccessToken(USER1);
        Long groupId = extractId(requestToCreateGroup(token, GROUP_CREATE_REQUEST1));
        Long postId = extractId(requestToCreatePost(token, NOTICE_CREATE_REQUEST1, groupId));

        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(postId, "댓글 내용");
        ExtractableResponse<Response> response = requestToCreateComment(token, commentCreateRequest);

        AcceptanceStep.assertThatStatusIsCreated(response);
        assertThatCreateComment(USER1, commentCreateRequest, getObject(response, CommentResponse.class));
    }

    @Test
    public void 모임_참여자가_게시물_댓글_목록을_조회한다() {
        String token = getAccessToken(USER1);
        Long groupId = extractId(requestToCreateGroup(token, GROUP_CREATE_REQUEST1));
        Long postId = extractId(requestToCreatePost(token, NOTICE_CREATE_REQUEST1, groupId));

        List<CommentCreateRequest> commentCreateRequests =
            List.of(new CommentCreateRequest(postId, "댓글 내용1"), new CommentCreateRequest(postId, "댓글 내용2"));
        requestToCreateComment(token, commentCreateRequests.get(0));
        requestToCreateComment(token, commentCreateRequests.get(1));

        ExtractableResponse<Response> response = requestToFindComments(token, new CommentsRequest(postId, 0, 10));
        AcceptanceStep.assertThatStatusIsOk(response);
        assertThatFindComments(commentCreateRequests, getObject(response, CommentsResponse.class));
    }
}
