package com.momo.schedule.service;

import com.momo.common.exception.CustomException;
import com.momo.common.exception.ErrorCode;
import com.momo.group.domain.model.Groups;
import com.momo.group.domain.repository.GroupRepository;
import com.momo.group.domain.repository.ParticipantRepository;
import com.momo.schedule.controller.dto.GroupScheduleResponse;
import com.momo.schedule.controller.dto.GroupSchedulesRequest;
import com.momo.schedule.controller.dto.GroupSchedulesResponse;
import com.momo.schedule.controller.dto.ScheduleCreateRequest;
import com.momo.schedule.controller.dto.UserScheduleResponse;
import com.momo.schedule.controller.dto.UserSchedulesRequest;
import com.momo.schedule.domain.model.Schedule;
import com.momo.schedule.domain.repository.ScheduleRepository;
import com.momo.user.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final GroupRepository groupRepository;

    private final ParticipantRepository participantRepository;

    public Long create(User user, ScheduleCreateRequest request) {
        Groups group = getGroupById(request.getGroupId());
        if (!group.isManager(user)) {
            throw new CustomException(ErrorCode.GROUP_SCHEDULE_UNAUTHORIZED);
        }
        return scheduleRepository.save(Schedule.create(request.toEntity(), group, user)).getId();
    }

    @Transactional(readOnly = true)
    public GroupSchedulesResponse findPageByGroupAndUser(User user, GroupSchedulesRequest request) {
        Groups group = getGroupById(request.getGroupId());
        validateIsGroupParticipant(user, group);
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());
        List<GroupScheduleResponse> responses = scheduleRepository
            .findAllByGroupAndUserOrderByCreatedDateDesc(group, user, pageRequest);
        return GroupSchedulesResponse.of(responses, group.isManager(user));
    }

    @Transactional(readOnly = true)
    public List<UserScheduleResponse> findPageByUser(User user, UserSchedulesRequest request) {
        LocalDateTime searchStartDateTime = request.getSearchStartDate().atStartOfDay();
        LocalDateTime searchEndDateTime = request.getSearchEndDate().plusDays(1).atStartOfDay();
        List<Schedule> schedules = scheduleRepository
            .findAllByStartDateTimeBetween(searchStartDateTime, searchEndDateTime, user);
        return UserScheduleResponse.listOf(schedules);
    }

    public Groups getGroupById(Long groupId) {
        return groupRepository.findById(groupId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INDEX_NUMBER));
    }

    public void validateIsGroupParticipant(User user, Groups group) {
        if (!participantRepository.existsByUserAndGroup(user, group)) {
            throw new CustomException(ErrorCode.GROUP_PARTICIPANT_UNAUTHORIZED);
        }
    }
}
