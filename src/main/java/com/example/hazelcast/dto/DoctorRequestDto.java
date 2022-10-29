package com.example.hazelcast.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DoctorRequestDto implements Serializable {
    private Long id;
    private String doctorName;
    private String doctorSurName;
    private String phone;
    private List<Long> professionIdList;

}
