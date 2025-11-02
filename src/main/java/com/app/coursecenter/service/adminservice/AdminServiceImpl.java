package com.app.coursecenter.service.adminservice;

import com.app.coursecenter.dto.UserDto;
import com.app.coursecenter.entity.Authority;
import com.app.coursecenter.entity.User;
import com.app.coursecenter.mapper.UserMapper;
import com.app.coursecenter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // constructor enj
    public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUser(int page, int size) {
         Pageable pageable = PageRequest.of(page, size);
         Page<User> studentPage = userRepository.findAll(pageable);
         return studentPage.map(userMapper::map);
    }

    @Override
    @Transactional
    public UserDto promoteToAdmin(long studentId) {
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

        return userMapper.map(savedUser);
    }

    @Override
    @Transactional
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
