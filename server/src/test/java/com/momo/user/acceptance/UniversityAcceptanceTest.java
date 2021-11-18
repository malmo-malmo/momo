package com.momo.user.acceptance;

import static com.momo.fixture.UserFixture.USER1;

import com.momo.common.acceptance.AcceptanceTest;
import com.momo.common.acceptance.step.AcceptanceStep;
import com.momo.user.acceptance.step.UniversityAcceptanceStep;
import com.momo.user.controller.dto.UniversityResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("커리어넷 오픈 API 통합/인수 테스트")
public class UniversityAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("커리어넷 오픈 API로 대학교 이름을 조회한다.")
    public void update_success() {
        String universityName = "한국";
        String token = getAccessToken(USER1);
        ExtractableResponse<Response> res = UniversityAcceptanceStep.requestToFind(token, universityName);
        AcceptanceStep.assertThatStatusIsOk(res);
        Assertions.assertThat(getObjects(res, UniversityResponse.class).size()).isGreaterThan(0);
    }
}
