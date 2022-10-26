package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.ProfessionRequestDto;
import com.example.mapstruct.dto.ProfessionResponseDto;
import com.example.mapstruct.entity.Profession;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-27T00:48:02+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class ProfessionMapperImpl implements ProfessionMapper {

    @Override
    public ProfessionResponseDto toDto(Profession profession) {
        if ( profession == null ) {
            return null;
        }

        ProfessionResponseDto professionResponseDto = new ProfessionResponseDto();

        professionResponseDto.setParentId( professionParentId( profession ) );
        professionResponseDto.setParentName( professionParentName( profession ) );
        professionResponseDto.setId( profession.getId() );
        professionResponseDto.setName( profession.getName() );
        professionResponseDto.setDescription( profession.getDescription() );

        return professionResponseDto;
    }

    @Override
    public List<ProfessionResponseDto> toDto(List<Profession> profession) {
        if ( profession == null ) {
            return null;
        }

        List<ProfessionResponseDto> list = new ArrayList<ProfessionResponseDto>( profession.size() );
        for ( Profession profession1 : profession ) {
            list.add( toDto( profession1 ) );
        }

        return list;
    }

    @Override
    public Profession toEntity(ProfessionRequestDto professionRequestDTO) {
        if ( professionRequestDTO == null ) {
            return null;
        }

        Profession profession = new Profession();

        doBeforeMapping( profession, professionRequestDTO );

        profession.setParent( professionRequestDtoToProfession( professionRequestDTO ) );
        profession.setName( professionRequestDTO.getName() );
        profession.setDescription( professionRequestDTO.getDescription() );

        profession.setDeleted( false );

        doAfterMapping( profession, professionRequestDTO );

        return profession;
    }

    @Override
    public Profession toEntity(Long id) {
        if ( id == null ) {
            return null;
        }

        Profession profession = new Profession();

        profession.setId( id );

        return profession;
    }

    @Override
    public void updateEntity(Profession profession, ProfessionRequestDto professionRequestDTO) {
        if ( professionRequestDTO == null ) {
            return;
        }

        doBeforeMapping( profession, professionRequestDTO );

        if ( profession.getParent() == null ) {
            profession.setParent( new Profession() );
        }
        professionRequestDtoToProfession1( professionRequestDTO, profession.getParent() );
        profession.setId( professionRequestDTO.getId() );
        profession.setName( professionRequestDTO.getName() );
        profession.setDescription( professionRequestDTO.getDescription() );

        doAfterMapping( profession, professionRequestDTO );
    }

    private Long professionParentId(Profession profession) {
        if ( profession == null ) {
            return null;
        }
        Profession parent = profession.getParent();
        if ( parent == null ) {
            return null;
        }
        Long id = parent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String professionParentName(Profession profession) {
        if ( profession == null ) {
            return null;
        }
        Profession parent = profession.getParent();
        if ( parent == null ) {
            return null;
        }
        String name = parent.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    protected Profession professionRequestDtoToProfession(ProfessionRequestDto professionRequestDto) {
        if ( professionRequestDto == null ) {
            return null;
        }

        Profession profession = new Profession();

        doBeforeMapping( profession, professionRequestDto );

        profession.setId( professionRequestDto.getParentId() );

        doAfterMapping( profession, professionRequestDto );

        return profession;
    }

    protected void professionRequestDtoToProfession1(ProfessionRequestDto professionRequestDto, Profession mappingTarget) {
        if ( professionRequestDto == null ) {
            return;
        }

        doBeforeMapping( mappingTarget, professionRequestDto );

        mappingTarget.setId( professionRequestDto.getParentId() );

        doAfterMapping( mappingTarget, professionRequestDto );
    }
}
