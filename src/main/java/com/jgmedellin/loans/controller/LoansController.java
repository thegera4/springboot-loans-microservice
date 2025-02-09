package com.jgmedellin.loans.controller;

import com.jgmedellin.loans.constants.LoansConstants;
import com.jgmedellin.loans.dto.ErrorResponseDto;
import com.jgmedellin.loans.dto.LoansContactInfoDto;
import com.jgmedellin.loans.dto.LoansDto;
import com.jgmedellin.loans.dto.ResponseDto;
import com.jgmedellin.loans.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Loans API", description = "This API allows to Create, Read, Update and Delete loans in EazyBank")
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated // Perform validation on the request body
public class LoansController {

  private final ILoansService iLoansService;

  @Autowired
  public LoansController(ILoansService iLoansService) {
    this.iLoansService = iLoansService;
  }

  @Value("${build.version}")   // Injecting an env variable from application.properties (Approach 1)
  private String buildVersion;

  @Autowired // Injecting the environment object to get the active profile (Approach 2)
  private Environment environment;

  @Autowired
  private LoansContactInfoDto loansContactInfoDto; // Injecting the LoansContactInfoDto bean (Approach 3)

  @Operation(summary = "Create Loan", description = "Endpoint to create new loan inside EazyBank")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
          @ApiResponse(
                  responseCode = "500",
                  description = LoansConstants.MESSAGE_500,
                  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
          )
  }
  )
  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createLoan(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",
          message = "Mobile number must be 10 digits") String mobileNumber) {
    iLoansService.createLoan(mobileNumber);
    return ResponseEntity
            .status(HttpStatus.CREATED).body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
  }

  @Operation(summary = "Fetch Loan", description = "Endpoint to fetch loan details by mobile number")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
          @ApiResponse(
                  responseCode = "500",
                  description = LoansConstants.MESSAGE_500,
                  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
          )
  })
  @GetMapping("/fetch")
  public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",
          message = "Mobile number must be 10 digits") String mobileNumber) {
    LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);
    return ResponseEntity.status(HttpStatus.OK).body(loansDto);
  }

  @Operation(summary = "Update Loan", description = "Endpoint to update loan details by loan number")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = LoansConstants.MESSAGE_200),
          @ApiResponse(responseCode = "417", description = LoansConstants.MESSAGE_417_UPDATE),
          @ApiResponse(
                  responseCode = "500",
                  description = LoansConstants.MESSAGE_500,
                  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
          )
  })
  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updateLoan(@Valid @RequestBody LoansDto loansDto) {
    boolean isUpdated = iLoansService.updateLoan(loansDto);
    if (isUpdated) {
      return ResponseEntity
              .status(HttpStatus.OK).body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
    } else {
      return ResponseEntity
              .status(HttpStatus.EXPECTATION_FAILED)
              .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
    }
  }

  @Operation(summary = "Delete Loan", description = "Endpoint to delete a loan by mobile number")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = LoansConstants.MESSAGE_200),
          @ApiResponse(responseCode = "417", description = LoansConstants.MESSAGE_417_DELETE),
          @ApiResponse(
                  responseCode = "500",
                  description = LoansConstants.MESSAGE_500,
                  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
          )
  })
  @DeleteMapping("/delete")
  public ResponseEntity<ResponseDto> deleteLoan(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",
          message = "Mobile number must be 10 digits") String mobileNumber) {
    boolean isDeleted = iLoansService.deleteLoan(mobileNumber);
    if (isDeleted) {
      return ResponseEntity
              .status(HttpStatus.OK).body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
    } else {
      return ResponseEntity
              .status(HttpStatus.EXPECTATION_FAILED)
              .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
    }
  }

  @Operation(summary = "Get build information account", description = "Endpoint to check the current API version")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
          @ApiResponse(
                  responseCode = "500",
                  description = LoansConstants.MESSAGE_500,
                  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
          )
  })
  @GetMapping("/build-info")
  public ResponseEntity<String> getBuildInfo() {
    return ResponseEntity.status(HttpStatus.OK).body("Current Build Version: " + buildVersion);
  }

  @Operation(summary = "Get java version", description = "Endpoint to check the Java version installed.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
          @ApiResponse(
                  responseCode = "500",
                  description = LoansConstants.MESSAGE_500,
                  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
          )
  })
  @GetMapping("/java-version")
  public ResponseEntity<String> getJavaVersion() {
    return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
  }

  @Operation(summary = "Get contact information", description = "Endpoint to check the contact information.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
          @ApiResponse(
                  responseCode = "500",
                  description = LoansConstants.MESSAGE_500,
                  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
          )
  })
  @GetMapping("/contact-info")
  public ResponseEntity<LoansContactInfoDto> getContactInfo() {
    return ResponseEntity.status(HttpStatus.OK).body(loansContactInfoDto);
  }

}