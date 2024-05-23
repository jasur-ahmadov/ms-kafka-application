package mskafkaapplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mskafkaapplication.dto.StudentResponse;
import mskafkaapplication.dto.request.CreateStudentRequest;
import mskafkaapplication.dto.request.UpdateStudentRequest;
import mskafkaapplication.error.StudentNotFoundException;
import mskafkaapplication.kafka.KafkaTopicConfig;
import mskafkaapplication.mapper.StudentMapper;
import mskafkaapplication.repository.StudentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final KafkaTemplate<String, StudentResponse> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    public void createStudent(CreateStudentRequest request) {
        var student = studentMapper.mapRequestToStudent(request);
        studentRepository.save(student);
        log.info("Student created {}", student);
    }

    public List<StudentResponse> getAllStudents() {
        var students = studentRepository.findAll().stream()
                .map(studentMapper::mapEntityToResponse)
                .collect(Collectors.toList());
        log.info("Students: {}", students);
        students.forEach(student -> kafkaTemplate.send(kafkaTopicConfig.topicName, UUID.randomUUID().toString(), student));
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