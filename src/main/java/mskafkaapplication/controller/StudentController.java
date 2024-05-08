package mskafkaapplication.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import mskafkaapplication.dto.StudentResponse;
import mskafkaapplication.dto.request.CreateStudentRequest;
import mskafkaapplication.dto.request.UpdateStudentRequest;
import mskafkaapplication.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public void createStudent(@RequestBody @Valid CreateStudentRequest request) {
        studentService.createStudent(request);
    }

    @CircuitBreaker(name = "getAllStudents")
    @GetMapping
    public List<StudentResponse> getAllStudents() {
//        throw new RuntimeException(); ~ for circuitbreaker to work
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable Long id, @RequestBody UpdateStudentRequest request) {
        studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}