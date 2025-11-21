package com.app.coursecenter.service.userservice;

import com.app.coursecenter.entity.Authority;
import com.app.coursecenter.entity.User;
import com.app.coursecenter.exception.AuthenticationException;
import com.app.coursecenter.mapper.UserMapper;
import com.app.coursecenter.repository.UserRepository;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.UserDetailResponse;
import com.app.coursecenter.service.CourseRatingProducer;
import com.app.coursecenter.util.FindAuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final CourseRatingProducer ratingProducer;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           PasswordEncoder passwordEncoder, FindAuthenticatedUser findAuthenticatedUser,
                           CourseRatingProducer ratingProducer) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.ratingProducer = ratingProducer;
    }

    // ---------------- Course Reservation Commands ----------------



    @Override
    public void rateCourse(Long courseId, int rate) {
        Long studentId = getCurrentUserId();
        String studentEmail = getCurrentUserEmail();
        ratingProducer.sendCourseRatingCommand(studentId, studentEmail, courseId, rate);
    }

    private Long getCurrentUserId() {
        try {
            return findAuthenticatedUser.getAuthenticatedUser().getId();
        } catch (AccessDeniedException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }


    private String getCurrentUserEmail() {
        try {
            return findAuthenticatedUser.getAuthenticatedUser().getEmail();
        } catch (AccessDeniedException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    // ---------------- Profile Information ----------------

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse getUserInfo() throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        return userMapper.userToUserDetailsResponse(user);
    }



    @Override
    public void deleteUser() throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();

        if (isLastAdmin(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin cannot delete itself");
        }

        userRepository.delete(user);
    }


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

    @Transactional(readOnly = true)
    @Override
    public Page<UserDetailResponse> getAllUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> studentPage = userRepository.findAll(pageable);

        return studentPage.map(userMapper::userToUserDetailsResponse);
    }

    @Transactional
    @Override
    public UserDetailResponse promoteToAdmin(long studentId) {
        // TODO promote to teacher who in the security filter chain will have access to some apis (or different set)

        Optional<User> user = userRepository.findById(studentId);

        if (user.isEmpty() || user.get().getAuthorities().stream().anyMatch((authority) ->
                "ROLE_ADMIN".equals(authority.getAuthority()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already an admin");
        }

        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_ADMIN"));
        authorities.add(new Authority("ROLE_STUDENT"));

        user.get().setAuthorities(authorities);

        User savedUser = userRepository.save(user.get());

        return userMapper.userToUserDetailsResponse(savedUser);
    }

    @Transactional
    @Override
    public void deleteNonAdminUser(long id) {

        // ? bullshitest way of doing this function but anyway

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty() || user.get().getAuthorities().stream().anyMatch((authority) ->
                "ROLE_ADMIN".equals(authority.getAuthority()))) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already an admin");
        }
        userRepository.delete(user.get());
    }
}
