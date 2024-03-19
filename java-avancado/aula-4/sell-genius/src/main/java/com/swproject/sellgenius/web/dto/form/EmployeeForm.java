package com.swproject.sellgenius.web.dto.form;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeForm {

    private String name;
    @Column(unique = true)
    private String cpf;
    private Date birthDate;
}
