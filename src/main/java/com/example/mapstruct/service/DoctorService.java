package com.example.mapstruct.service;

import com.example.mapstruct.dto.DoctorRequestDto;
import com.example.mapstruct.entity.Doctor;
import com.example.mapstruct.entity.Profession;
import com.example.mapstruct.mapper.DoctorMapper;
import com.example.mapstruct.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {

	private final DoctorRepository doctorRepository;
	private final DoctorMapper doctorMapper;

	public Doctor findByIdAndDeletedFalse(long id) {
		return doctorRepository.findByIdAndDeletedFalse(id).orElseThrow(RuntimeException::new);
	}

	public Doctor save(DoctorRequestDto doctorRequestDto) {
		return doctorRepository.save(doctorMapper.toEntity(doctorRequestDto));
	}

	public Doctor update(DoctorRequestDto requestDto) {
        var doctor= doctorRepository.findByIdAndDeletedFalse(requestDto.getId()).orElseThrow(RuntimeException::new);
        doctorMapper.updateEntity(doctor,requestDto);
        return doctorRepository.save(doctor);
	}


    public Page<Doctor> findAllPageable(Integer pageNumber, Integer pageSize, String sortingDirection, String sortingName, String name) {
        Sort.Direction direction;
        if (sortingDirection.equals("ASC")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }

        return doctorRepository.findAllByNamePageable(PageRequest.of(pageNumber - 1, pageSize, Sort.by(direction, sortingName)),name);

    }

    public List<Doctor> findAllList(String sortingDirection, String sortingName, String name) {

        Sort.Direction direction;
        if (sortingDirection.equals("ASC")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        return doctorRepository.findAllByNameList(Sort.by(direction, sortingName),name);
    }

}
