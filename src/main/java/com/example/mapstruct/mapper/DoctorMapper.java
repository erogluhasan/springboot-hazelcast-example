package com.example.mapstruct.mapper;


import com.example.mapstruct.dto.DoctorRequestDto;
import com.example.mapstruct.dto.DoctorResponseDto;
import com.example.mapstruct.dto.ProfessionRequestDto;
import com.example.mapstruct.entity.Doctor;
import com.example.mapstruct.entity.Profession;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class, uses = {ProfessionMapper.class})
public interface DoctorMapper {
    @Mapping(source = "name", target = "doctorName")
    @Mapping(source = "surname", target = "doctorSurName")
    @Mapping(source = "professionList", target = "professionResponseDtos")
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID().toString())")
    @Mapping(
            source = "professionList",
            target = "numProfessions",
            qualifiedByName = "countProfessions")
    DoctorResponseDto toDto(Doctor doctor);

    List<DoctorResponseDto> toDto(List<Doctor> doctor);

    @Mapping(source = "doctorName", target = "name")
    @Mapping(source = "doctorSurName", target = "surname")
    @Mapping(source = "professionIdList", target = "professionList")
    @Mapping(target = "deleted",constant = "false")
    @Mapping(target = "id",ignore = true)
    Doctor toEntity(DoctorRequestDto doctorRequestDTO);

    @Mapping(source = "doctorName", target = "name")
    @Mapping(source = "doctorSurName", target = "surname")
    @Mapping(source = "professionIdList", target = "professionList")
    @Mapping(target = "deleted",constant = "false")
    void updateEntity(@MappingTarget Doctor doctor,DoctorRequestDto doctorRequestDTO);

    @BeforeMapping
    default void doBeforeMapping(@MappingTarget Doctor entity, DoctorRequestDto dto) {
        entity.setProfessionList(null);
    }

    @AfterMapping
    default void doAfterMapping(@MappingTarget Doctor entity, DoctorRequestDto dto) {
        if (dto.getProfessionIdList() == null) {
            entity.setProfessionList(null);
        }
    }
    @Named("countProfessions")
    default int getProfessionsSize(List<Profession> professions) {
        if(professions == null) {
            return 0;
        }
        return professions.size();
    }
}
