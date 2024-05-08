package mskafkaapplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mskafkaapplication.dto.StudentResponse;
import mskafkaapplication.dto.request.CreateStudentRequest;
import mskafkaapplication.dto.request.UpdateStudentRequest;
import mskafkaapplication.error.StudentNotFoundException;
import mskafkaapplication.mapper.StudentMapper;
import mskafkaapplication.repository.StudentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    static Integer dinamicNum = 0;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final KafkaTemplate<String, StudentResponse> kafkaTemplate;

    public void createStudent(CreateStudentRequest request) {
        studentRepository.save(studentMapper.mapRequestToStudent(request));
        var mappedToStudent = studentMapper.mapRequestToStudent(request);
        kafkaTemplate.send("student-topic", studentMapper.mapEntityToResponse(mappedToStudent));
        log.info("Student created {}", request);
    }

    public List<StudentResponse> getAllStudents() {
        dinamicNum++;
        var students = studentRepository.findAll().stream().map(studentMapper::mapEntityToResponse).collect(Collectors.toList());
        log.info("Students: {}", students);
        students.forEach(student -> kafkaTemplate.send("student-topic", 2, "key1" + dinamicNum, student));
        return students;
    }

    public StudentResponse getStudent(Long id) {
        var student = studentRepository.findById(id).orElseThrow(this::exStudentNotFound);
        return studentMapper.mapEntityToResponse(student);
    }

    public void updateStudent(Long id, UpdateStudentRequest request) {
        var student = studentRepository.findById(id).orElseThrow(this::exStudentNotFound);
        studentMapper.mapUpdateRequestToEntity(student, request);
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        var student = studentRepository.findById(id).orElseThrow(this::exStudentNotFound);
        studentRepository.deleteById(student.getId());
    }

    public StudentNotFoundException exStudentNotFound() {
        throw new StudentNotFoundException();
    }
}
