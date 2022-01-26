package com.momo.api.schedule;

import static org.springframework.http.HttpStatus.CREATED;

import com.momo.common.CurrentUser;
import com.momo.domain.schedule.dto.AttendanceCreateRequests;
import com.momo.domain.schedule.service.AttendanceService;
import com.momo.domain.user.entity.User;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @ApiOperation(value = "출석 체크")
    @PostMapping
    public ResponseEntity<Void> create(@CurrentUser User user,
        @Valid @RequestBody AttendanceCreateRequests attendanceCreateRequests) {
        attendanceService.create(user, attendanceCreateRequests);
        return ResponseEntity.status(CREATED).build();
    }
}
