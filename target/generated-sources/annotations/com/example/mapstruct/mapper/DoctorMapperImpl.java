package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.DoctorRequestDto;
import com.example.mapstruct.dto.DoctorResponseDto;
import com.example.mapstruct.entity.Doctor;
import com.example.mapstruct.entity.Profession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-27T00:48:03+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class DoctorMapperImpl implements DoctorMapper {

    @Autowired
    private ProfessionMapper professionMapper;

    @Override
    public DoctorResponseDto toDto(Doctor doctor) {
        if ( doctor == null ) {
            return null;
        }

        DoctorResponseDto doctorResponseDto = new DoctorResponseDto();

        doctorResponseDto.setDoctorName( doctor.getName() );
        doctorResponseDto.setDoctorSurName( doctor.getSurname() );
        doctorResponseDto.setProfessionResponseDtos( professionMapper.toDto( doctor.getProfessionList() ) );
        doctorResponseDto.setNumProfessions( getProfessionsSize( doctor.getProfessionList() ) );
        doctorResponseDto.setId( doctor.getId() );
        doctorResponseDto.setPhone( doctor.getPhone() );

        doctorResponseDto.setExternalId( UUID.randomUUID().toString() );

        return doctorResponseDto;
    }

    @Override
    public List<DoctorResponseDto> toDto(List<Doctor> doctor) {
        if ( doctor == null ) {
            return null;
        }

        List<DoctorResponseDto> list = new ArrayList<DoctorResponseDto>( doctor.size() );
        for ( Doctor doctor1 : doctor ) {
            list.add( toDto( doctor1 ) );
        }

        return list;
    }

    @Override
    public Doctor toEntity(DoctorRequestDto doctorRequestDTO) {
        if ( doctorRequestDTO == null ) {
            return null;
        }

        Doctor doctor = new Doctor();

        doBeforeMapping( doctor, doctorRequestDTO );

        doctor.setName( doctorRequestDTO.getDoctorName() );
        doctor.setSurname( doctorRequestDTO.getDoctorSurName() );
        doctor.setProfessionList( longListToProfessionList( doctorRequestDTO.getProfessionIdList() ) );
        doctor.setPhone( doctorRequestDTO.getPhone() );

        doctor.setDeleted( false );

        doAfterMapping( doctor, doctorRequestDTO );

        return doctor;
    }

    @Override
    public void updateEntity(Doctor doctor, DoctorRequestDto doctorRequestDTO) {
        if ( doctorRequestDTO == null ) {
            return;
        }

        doBeforeMapping( doctor, doctorRequestDTO );

        doctor.setName( doctorRequestDTO.getDoctorName() );
        doctor.setSurname( doctorRequestDTO.getDoctorSurName() );
        if ( doctor.getProfessionList() != null ) {
            List<Profession> list = longListToProfessionList( doctorRequestDTO.getProfessionIdList() );
            if ( list != null ) {
                doctor.getProfessionList().clear();
                doctor.getProfessionList().addAll( list );
            }
            else {
                doctor.setProfessionList( null );
            }
        }
        else {
            List<Profession> list = longListToProfessionList( doctorRequestDTO.getProfessionIdList() );
            if ( list != null ) {
                doctor.setProfessionList( list );
            }
        }
        doctor.setId( doctorRequestDTO.getId() );
        doctor.setPhone( doctorRequestDTO.getPhone() );

        doctor.setDeleted( false );

        doAfterMapping( doctor, doctorRequestDTO );
    }

    protected List<Profession> longListToProfessionList(List<Long> list) {
        if ( list == null ) {
            return null;
        }

        List<Profession> list1 = new ArrayList<Profession>( list.size() );
        for ( Long long1 : list ) {
            list1.add( professionMapper.toEntity( long1 ) );
        }

        return list1;
    }
}
