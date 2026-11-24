package com.example.demo.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository implements IStudentRepository {
    private HashMap<String, Student> MapStudentRepository = new HashMap<>();

    @Override 
    public List<Student> findAll(){
        return new ArrayList<>(MapStudentRepository.values());
    }

    @Override 
    public Optional<Student> findById(String id){
        return Optional.ofNullable(MapStudentRepository.get(id));
    }

    @Override 
    public Student save(Student student){
        MapStudentRepository.put(student.getId(), student);
        return MapStudentRepository.get(student.getId());
    }

    @Override 
    public boolean deleteById(String id){
        if(MapStudentRepository.containsKey(id)){
            MapStudentRepository.remove(id);
            return true;
        }else return false;
    }
}
