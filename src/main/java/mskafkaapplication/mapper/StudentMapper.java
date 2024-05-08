package mskafkaapplication.mapper;

import mskafkaapplication.dto.StudentResponse;
import mskafkaapplication.dto.request.CreateStudentRequest;
import mskafkaapplication.dto.request.UpdateStudentRequest;
import mskafkaapplication.model.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    Student mapRequestToStudent(CreateStudentRequest request);

    StudentResponse mapEntityToResponse(Student student);

    void mapUpdateRequestToEntity(@MappingTarget Student student, UpdateStudentRequest request);
}