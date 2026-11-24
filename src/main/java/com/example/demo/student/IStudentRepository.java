package com.example.demo.student;

import java.util.List;
import java.util.Optional;

public interface IStudentRepository {
    List<Student> findAll();
    Optional<Student> findById(String id);
    Student save(Student student);
    boolean deleteById(String id);
}
