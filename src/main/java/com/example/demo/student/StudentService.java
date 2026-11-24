package com.example.demo.student;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final IStudentRepository IStudentRepository;
    private final IdGenerator IdGenerator;

    StudentService(IStudentRepository IStudentRepository, IdGenerator IdGenerator){
        this.IStudentRepository = IStudentRepository;
        this.IdGenerator = IdGenerator;
    }

    public List<Student> findAll(){
        return IStudentRepository.findAll();
    }

    public Optional<Student> findById(String id){
        return IStudentRepository.findById(id);
    }

    public Student addStudent(Student student){
        student.setId(IdGenerator.generateId());
        return IStudentRepository.save(student);
    }

    public Optional<Student> updateStudent(Student student, String id){
        if(IStudentRepository.deleteById(id) == true){
            return Optional.ofNullable( IStudentRepository.save(student) );
        }else{
            return null;
        }
    }

    public boolean deleteStudent(String id){
        return IStudentRepository.deleteById(id);
    }
}
