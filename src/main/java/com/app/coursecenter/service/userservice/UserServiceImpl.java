package com.app.coursecenter.service.userservice;

import com.app.coursecenter.dto.UserDto;
import com.app.coursecenter.entity.User;
import com.app.coursecenter.mapper.CourseMapper;
import com.app.coursecenter.mapper.UserMapper;
import com.app.coursecenter.repository.CourseReservationRepository;
import com.app.coursecenter.repository.UserRepository;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.UserCoursesRespond;
import com.app.coursecenter.service.CourseRatingProducer;
import com.app.coursecenter.service.CourseReservationCommandProducer;
import com.app.coursecenter.util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final CourseReservationCommandProducer producer;
    private final CourseRatingProducer ratingProducer;
    private final CourseReservationRepository courseReservationRepository;
    private final CourseMapper courseMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           PasswordEncoder passwordEncoder, FindAuthenticatedUser findAuthenticatedUser,
                           CourseReservationCommandProducer producer, CourseRatingProducer ratingProducer,
                           CourseReservationRepository courseReservationRepository, CourseMapper courseMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.producer = producer;
        this.ratingProducer = ratingProducer;
        this.courseReservationRepository = courseReservationRepository;
        this.courseMapper = courseMapper;
    }

    // ---------------- Course Reservation Commands ----------------

    public void requestCourseReservation(Long courseId) {
        producer.sendReserveCourseCommand(getCurrentUserId(), getCurrentUserEmail(), courseId);
    }

    public void cancelCourseReservation(Long courseId) {
        producer.sendCancelReservationCommand(getCurrentUserId(), getCurrentUserEmail(), courseId);
    }

    @Override
    public void rateCourse(Long courseId, int rate) {
        Long studentId = getCurrentUserId();
        String studentEmail = getCurrentUserEmail();
        ratingProducer.sendCourseRatingCommand(studentId,studentEmail, courseId, rate);
    }

    private Long getCurrentUserId() {
        try {
            return findAuthenticatedUser.getAuthenticatedUser().getId();
        } catch (AccessDeniedException e) {
            throw new RuntimeException("Unauthorized access", e);
        }
    }

    private String getCurrentUserEmail() {
        try {
            return findAuthenticatedUser.getAuthenticatedUser().getEmail();
        } catch (AccessDeniedException e) {
            throw new RuntimeException("Unauthorized access", e);
        }
    }

    // ---------------- Profile Information ----------------

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserInfo() throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        return userMapper.map(user);
    }


    @Transactional
    public List<UserCoursesRespond> getEnrolledCourses() {
        return courseReservationRepository.findCoursesByUserId(getCurrentUserId()).stream().
                map(courseMapper::courseToUserCoursesRespond).collect(Collectors.toList());
    }

    // ---------------- Account Deletion ----------------

    @Override
    public void deleteUser() throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();

        if (isLastAdmin(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin cannot delete itself");
        }

        userRepository.delete(user);
    }

    // ---------------- Password Update ----------------

    @Override
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();

        if (!isOldPasswordCorrect(user.getPassword(), passwordUpdateRequest.getOldPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password incorrect");
        }

        if (!isNewPasswordConfirmed(passwordUpdateRequest.getNewPassword(), passwordUpdateRequest.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New passwords don't match");
        }

        if (!isNewPasswordDifferent(user.getPassword(), passwordUpdateRequest.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old and new passwords must be different");
        }

        user.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        userRepository.save(user);
    }

    private boolean isOldPasswordCorrect(String encodedPassword, String oldRawPassword) {
        // ✅ Fix: first argument is raw, second is encoded
        return passwordEncoder.matches(oldRawPassword, encodedPassword);
    }

    private boolean isNewPasswordConfirmed(String newPassword, String confirmation) {
        return newPassword.equals(confirmation);
    }

    private boolean isNewPasswordDifferent(String oldEncodedPassword, String newRawPassword) {
        // compare using matches to avoid comparing encoded hash to raw
        return !passwordEncoder.matches(newRawPassword, oldEncodedPassword);
    }

    private boolean isLastAdmin(User user) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
        if (isAdmin) {
            long adminCount = userRepository.countAdminUser();
            return adminCount <= 1;
        }
        return false;
    }
}
