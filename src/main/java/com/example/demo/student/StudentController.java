package com.example.demo.student;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/students")
@RestController 
public class StudentController {
    private final StudentService studentService;

    StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents(){
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id){
        Optional<Student> data = studentService.findById(id);
        if(data.isEmpty()){ 
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().body(data.get());
        }
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student){
        Student created = studentService.addStudent(student);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable String id){
        Optional<Student> updated = studentService.updateStudent(student, id);
        if(updated.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(updated.get());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteStudent(@PathVariable String id){
        boolean chk_delete = studentService.deleteStudent(id);
        if(chk_delete){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
