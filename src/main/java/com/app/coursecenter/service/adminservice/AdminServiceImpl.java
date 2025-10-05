package com.app.coursecenter.service.adminservice;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.entity.Authority;
import com.app.coursecenter.entity.Student;
import com.app.coursecenter.mapper.StudentMapper;
import com.app.coursecenter.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




@Service
public class AdminServiceImpl implements AdminService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    // constructor enj
    public AdminServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        return studentMapper.map(studentRepository.findAll());
    }

    @Override
    @Transactional
    public StudentDto promoteToAdmin(long studentId) {
        // TODO promote to teacher who in the security filter chain will have access to some apis (or different set)

        Optional<Student> user = studentRepository.findById(studentId);

        if (user.isEmpty() || user.get().getAuthorities().stream().anyMatch((authority) ->
                "ROLE_ADMIN".equals(authority.getAuthority()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already an admin");
        }


        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_ADMIN"));
        authorities.add(new Authority("ROLE_STUDENT"));

        user.get().setAuthorities(authorities);

        Student savedStudent = studentRepository.save(user.get());

        return studentMapper.map(savedStudent);
    }

    @Override
    @Transactional
    public void deleteNonAdminUser(long id) {

        // ? bullshitest way of doing this function but anyway

        Optional<Student> user = studentRepository.findById(id);
        if (user.isEmpty() || user.get().getAuthorities().stream().anyMatch((authority) ->
                "ROLE_ADMIN".equals(authority.getAuthority()))) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already an admin");
        }
        studentRepository.delete(user.get());
    }
}
