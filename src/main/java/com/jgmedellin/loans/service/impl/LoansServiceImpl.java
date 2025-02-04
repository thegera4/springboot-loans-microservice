package com.jgmedellin.loans.service.impl;

import com.jgmedellin.loans.constants.LoansConstants;
import com.jgmedellin.loans.dto.LoansDto;
import com.jgmedellin.loans.entity.Loans;
import com.jgmedellin.loans.exception.LoanAlreadyExistsException;
import com.jgmedellin.loans.exception.ResourceNotFoundException;
import com.jgmedellin.loans.mapper.LoansMapper;
import com.jgmedellin.loans.repository.LoansRepository;
import com.jgmedellin.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

  private LoansRepository loansRepository;

  @Override
  public void createLoan(String mobileNumber) {
    // Fetch the loan details by mobileNumber, if the loan does not exist, throw an exception
    Optional<Loans> optionalLoan = loansRepository.findByMobileNumber(mobileNumber);
    if (optionalLoan.isPresent()) { throw new LoanAlreadyExistsException("Loan already exists for this customer!"); }
    // Create a new loan for the customer
    loansRepository.save(createNewLoan(mobileNumber));
  }

  /**
   * Utility method to create a new loan for the customer
   * @param mobileNumber Mobile number of the customer
   */
  private Loans createNewLoan(String mobileNumber) {
    Loans newLoan = new Loans();
    long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
    newLoan.setLoanNumber(Long.toString(randomLoanNumber));
    newLoan.setMobileNumber(mobileNumber);
    newLoan.setLoanType(LoansConstants.HOME_LOAN);
    newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
    newLoan.setAmountPaid(0);
    newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
    return newLoan;
  }

  @Override
  public LoansDto fetchLoan(String mobileNumber) {
    // Fetch the loan details by mobileNumber, if the loan does not exist, throw an exception
    Optional<Loans> loan = loansRepository.findByMobileNumber(mobileNumber);
    if (loan.isEmpty()) { throw new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber); }
    // Map the loan details to the LoansDto object and return
    return LoansMapper.mapToLoansDto(loan.get(), new LoansDto());
  }

  @Override
  public boolean updateLoan(LoansDto loansDto) {
    // Fetch the loan details by loanNumber, if the loan does not exist, throw an exception
    Loans loan = loansRepository.findByLoanNumber(loansDto.getLoanNumber())
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loansDto.getLoanNumber()));
    // Map the updated loan details to the Loans object, save and return the success status
    LoansMapper.mapToLoans(loansDto, loan);
    loansRepository.save(loan);
    return true;
  }

  @Override
  public boolean deleteLoan(String mobileNumber) {
    // Fetch the loan details by mobileNumber, if the loan does not exist, throw an exception
    Loans loan = loansRepository.findByMobileNumber(mobileNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
    // Delete the loan and return the success status
    loansRepository.deleteById(loan.getLoanId());
    return true;
  }

}