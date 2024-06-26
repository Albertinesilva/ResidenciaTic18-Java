package com.swproject.shopall.web.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.swproject.shopall.entities.Employee;
import com.swproject.shopall.entities.UserSystem;
import com.swproject.shopall.services.EmployeeService;
import com.swproject.shopall.services.UserSystemService;
import com.swproject.shopall.web.dto.EmployeeResponseDto;
import com.swproject.shopall.web.dto.form.EmployeeForm;
import com.swproject.shopall.web.dto.mapper.EmployeeMapper;
import com.swproject.shopall.web.exceptions.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private UserSystemService usuarioService;

  @Operation(summary = "Cria um novo funcionário", description = "Recurso para criar um novo funcionário no sistema.", responses = {
      @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDto.class))),
      @ApiResponse(responseCode = "409", description = "Funcionario cpf já cadastrado no sistema.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Recursos não processados por dados de entrada invalidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PostMapping
  public ResponseEntity<EmployeeResponseDto> create(@RequestBody EmployeeForm createDto) {

    Long userId = createDto.getUsuarioId();

    if (userId == null) {
      throw new IllegalArgumentException("O parâmetro 'usuarioId' não pode ser nulo.");
    }

    UserSystem usuario = usuarioService.findById(userId);
    Employee employee = employeeService.save(EmployeeMapper.toEmployee(createDto, usuario));
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId()).toUri();
    return ResponseEntity.created(uri).body(EmployeeMapper.toDto(employee));

  }

  @Operation(summary = "Recuperar um funcionário pelo id", description = "Recurso para recuperar um funcionário pelo id.", responses = {
      @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponseDto> getById(@PathVariable @NonNull Long id) {
    Employee employee = employeeService.findById(id);
    return ResponseEntity.ok(EmployeeMapper.toDto(employee));
  }

  @GetMapping("/")
  public ResponseEntity<List<EmployeeResponseDto>> getAll() {
    List<EmployeeResponseDto> employees = EmployeeMapper.toListDto(employeeService.findAll());
    return ResponseEntity.ok().body(employees);
  }
}
