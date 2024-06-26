package com.swprojects.salesofeverything.web.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.swprojects.salesofeverything.entities.Employee;
import com.swprojects.salesofeverything.entities.UserSystem;
import com.swprojects.salesofeverything.services.EmployeeServiceV1;
import com.swprojects.salesofeverything.services.UserSystemService;
import com.swprojects.salesofeverything.web.dto.EmployeeResponseDto;
import com.swprojects.salesofeverything.web.dto.form.EmployeeForm;
import com.swprojects.salesofeverything.web.dto.mapper.EmployeeMapper;
import com.swprojects.salesofeverything.web.exceptions.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Employees", description = "Contém todas as operações aos recursos para cadastro, edição e leitura de um funcionário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeControllerV1 {
  
  @Autowired
  @Qualifier("v1")
  private EmployeeServiceV1 employeeService;

  @Autowired
  private UserSystemService usuarioService;

  @Operation(summary = "Cria um novo funcionário", description = "Recurso para criar um novo funcionário no sistema.", responses = {
      @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDto.class))),
      @ApiResponse(responseCode = "409", description = "Funcionario cpf já cadastrado no sistema.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Recursos não processados por dados de entrada invalidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @PostMapping
  public ResponseEntity<EmployeeResponseDto> create(@RequestBody EmployeeForm createDto) {
    UserSystem usuario = usuarioService.findById(createDto.getUsuarioId());
    Employee employee = employeeService.create(EmployeeMapper.toEmployee(createDto, usuario));
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId()).toUri();
    return ResponseEntity.created(uri).body(EmployeeMapper.toDto(employee));
  }

  @Operation(summary = "Recuperar um funcionário pelo id", description = "Recurso para recuperar um funcionário pelo id.", responses = {
      @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponseDto> getById(@PathVariable @NonNull Long id) {
    return employeeService.findById(id)
        .map(employee -> ResponseEntity.ok(EmployeeMapper.toDto(employee)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Listar todos os funcionários", description = "Listar todos os funcionários cadastrados", responses = {
      @ApiResponse(responseCode = "200", description = "Lista com todos os funcionarios cadastrados", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmployeeResponseDto.class))))
  })
  @GetMapping("/")
  public ResponseEntity<List<EmployeeResponseDto>> getAll() {
    List<EmployeeResponseDto> employees = EmployeeMapper.toListDto(employeeService.findAll());
    return ResponseEntity.ok().body(employees);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EmployeeResponseDto> update(@PathVariable @NonNull Long id,
      @RequestBody EmployeeForm createDto) {
    return employeeService.update(id, createDto)
        .map(employee -> ResponseEntity.ok(EmployeeMapper.toDto(employee)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable @NonNull Long id) {
    employeeService.delete(id);
    return ResponseEntity.noContent().build();
  }

}