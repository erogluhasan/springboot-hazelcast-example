package com.example.mapstruct.controller;

import com.example.mapstruct.dto.DoctorRequestDto;
import com.example.mapstruct.dto.DoctorResponseDto;
import com.example.mapstruct.dto.ProfessionResponseDto;
import com.example.mapstruct.mapper.DoctorMapper;
import com.example.mapstruct.service.DoctorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorController {
	public static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    DoctorService doctorService;
    DoctorMapper doctorMapper;

    // -------------------Retrieve All Doctors---------------------------------------------

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/list/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> allDoctors(
			@RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(name = "sortingDirection", required = false, defaultValue = "ASC") String sortingDirection,
			@RequestParam(name = "sortingName", required = false, defaultValue = "id") String sortingName,
            @RequestParam(name = "pageable",  required = false, defaultValue = "true") Boolean pageable,
			@RequestParam(name="name",required=false,defaultValue = "") String name
			)
    {
        if (pageable) {
            return new ResponseEntity<Page<DoctorResponseDto>>(doctorService.findAllPageable(pageNumber,pageSize,sortingDirection,sortingName,name).map(doctorMapper::toDto), HttpStatus.OK);
        }else{
            return new ResponseEntity<List<DoctorResponseDto>>(doctorMapper.toDto(doctorService.findAllList(sortingDirection,sortingName,name)) , HttpStatus.OK);
        }
	}

	// -------------------Retrieve Single Doctor------------------------------------------

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DoctorResponseDto> getDoctor(@PathVariable("id") long id) {
        return new ResponseEntity<DoctorResponseDto>(doctorMapper.toDto(doctorService.findByIdAndDeletedFalse(id)), HttpStatus.OK);
	}


	// -------------------Create a  Doctor-------------------------------------------
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DoctorResponseDto> createDoctor(@RequestBody DoctorRequestDto doctorRequestDTO) {
		return new ResponseEntity<DoctorResponseDto>(doctorMapper.toDto(doctorService.save(doctorRequestDTO)), HttpStatus.CREATED);
	}

	// ------------------- Update a Doctor ---------------------------


    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateDoctor(
             @PathVariable("id") long id,
            @RequestBody DoctorRequestDto doctorRequestDTO) {
        return new ResponseEntity<DoctorResponseDto>(doctorMapper.toDto(doctorService.update(doctorRequestDTO)), HttpStatus.OK);
	}
}