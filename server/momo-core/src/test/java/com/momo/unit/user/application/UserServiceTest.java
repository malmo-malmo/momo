package com.momo.unit.user.application;

import static com.momo.UserFixture.getUserWithId;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.momo.aws.service.S3UploadService;
import com.momo.common.ServiceTest;
import com.momo.common.exception.CustomException;
import com.momo.common.exception.ErrorCode;
import com.momo.district.entity.City;
import com.momo.user.application.UserService;
import com.momo.user.application.UserServiceImpl;
import com.momo.user.application.dto.request.UserUpdateRequestDto;
import com.momo.user.application.dto.response.UserImageUpdateResponseDto;
import com.momo.user.application.dto.response.UserResponseDto;
import com.momo.user.application.dto.response.UserUpdateResponseDto;
import com.momo.user.domain.model.User;
import com.momo.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

@DisplayName("유저 서비스 테스트")
public class UserServiceTest extends ServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3UploadService s3UploadService;

    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, s3UploadService);
        user = getUserWithId();
    }

    @Test
    @DisplayName("유저 정보를 조회한다")
    void findMyInformation_LoginUser_Success() {
        given(userRepository.findById(any())).willReturn(of(user));

        UserResponseDto expected = userService.findMyInformation(user);

        verify(userRepository).findById(any());
        Assertions.assertAll(
            () -> assertThat(expected.getId()).isEqualTo(user.getId()),
            () -> assertThat(expected.getNickname()).isEqualTo(user.getNickname()),
            () -> assertThat(expected.getImageUrl()).isEqualTo(user.getImageUrl()),
            () -> assertThat(expected.getCity()).isEqualTo(user.getLocation().getCity()),
            () -> assertThat(expected.getDistrict()).isEqualTo(user.getLocation().getDistrict()),
            () -> assertThat(expected.getUniversity()).isEqualTo(user.getLocation().getUniversity()),
            () -> assertThat(expected.getFavoriteCategories()).isNotNull()
        );
    }

    @Test
    @DisplayName("유저 정보를 수정한다 - 변경할 닉네임이 다른 경우")
    void updateMyInformation_DifferentNickname_Success() {
        UserUpdateRequestDto expected = UserUpdateRequestDto.builder()
            .nickname("변경할 닉네임")
            .university("변경할 대학교")
            .city(City.SEOUL)
            .district("행정구역")
            .build();

        given(userRepository.findById(any())).willReturn(of(user));
        given(userRepository.existsByNickname(any())).willReturn(false);

        UserUpdateResponseDto actual = userService.updateMyInformation(user, expected);

        verify(userRepository).findById(any());
        verify(userRepository).existsByNickname(any());
        verifyUserUpdateResponseDto(actual, expected);
    }

    @Test
    @DisplayName("유저 정보를 수정한다 - 변경할 닉네임이 같은 경우")
    void updateMyInformation_SameNickname_Success() {
        UserUpdateRequestDto expected = UserUpdateRequestDto.builder()
            .nickname(user.getNickname())
            .university("변경할 대학교")
            .city(City.SEOUL)
            .district("행정구역")
            .build();

        given(userRepository.findById(any())).willReturn(of(user));

        UserUpdateResponseDto actual = userService.updateMyInformation(user, expected);

        verify(userRepository).findById(any());
        verify(userRepository, never()).existsByNickname(any());
        verifyUserUpdateResponseDto(actual, expected);
    }

    private void verifyUserUpdateResponseDto(UserUpdateResponseDto actual, UserUpdateRequestDto expected) {
        Assertions.assertAll(
            () -> assertThat(actual.getNickname()).isEqualTo(expected.getNickname()),
            () -> assertThat(actual.getUniversity()).isEqualTo(expected.getUniversity()),
            () -> assertThat(actual.getCity().getCode()).isEqualTo(expected.getCity().getCode()),
            () -> assertThat(actual.getDistrict()).isEqualTo(expected.getDistrict())
        );
    }

    @Test
    @DisplayName("유저 정보 수정을 실패한다 - 중복된 닉네임")
    void updateMyInformation_DuplicatedNickname_Failure() {
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
            .nickname("중복된 닉네임")
            .build();

        given(userRepository.findById(any())).willReturn(of(user));
        given(userRepository.existsByNickname(any())).willReturn(true);

        assertThatThrownBy(() -> userService.updateMyInformation(user, requestDto))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.DUPLICATED_NICKNAME.getMessage());
    }

    @Test
    @DisplayName("유저 프로필 이미지를 수정한다")
    void updateImage_LoginUser_Success() {
        String imageUrl = "imageUrl";

        given(userRepository.findById(any())).willReturn(of(user));
        given(s3UploadService.upload(any(), any())).willReturn(imageUrl);

        UserImageUpdateResponseDto actual = userService
            .updateImage(user, new MockMultipartFile("image", "image".getBytes()));

        assertThat(actual.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("닉네임 중복 여부를 확인한다 - 성공")
    void validateDuplicateNickname_Success() {
        given(userRepository.existsByNickname(any())).willReturn(false);

        userService.validateDuplicateNickname("닉네임");

        verify(userRepository).existsByNickname(any());
    }

    @Test
    @DisplayName("닉네임 중복 여부를 확인한다 - 실패")
    void validateDuplicateNickname_Failure() {
        given(userRepository.existsByNickname(any())).willReturn(true);

        assertThatThrownBy(() -> userService.validateDuplicateNickname("닉네임"))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.DUPLICATED_NICKNAME.getMessage());
    }
}
