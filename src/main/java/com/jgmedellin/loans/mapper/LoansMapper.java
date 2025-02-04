package com.jgmedellin.loans.mapper;

import com.jgmedellin.loans.dto.LoansDto;
import com.jgmedellin.loans.entity.Loans;

public class LoansMapper {

  /**
   * Maps the Loans entity to the LoansDto object
   * @param loan Loans entity object
   * @param loansDto LoansDto object
   * @return LoansDto object with the loan details already mapped
   */
  public static LoansDto mapToLoansDto(Loans loan, LoansDto loansDto) {
    loansDto.setLoanNumber(loan.getLoanNumber());
    loansDto.setLoanType(loan.getLoanType());
    loansDto.setMobileNumber(loan.getMobileNumber());
    loansDto.setTotalLoan(loan.getTotalLoan());
    loansDto.setAmountPaid(loan.getAmountPaid());
    loansDto.setOutstandingAmount(loan.getOutstandingAmount());
    return loansDto;
  }

  /**
   * Maps the LoansDto object to the Loans entity
   * @param loansDto LoansDto object
   * @param loan Loans entity object
   * @return Loans entity object with the loan details already mapped
   */
  public static Loans mapToLoans(LoansDto loansDto, Loans loan) {
    loan.setLoanNumber(loansDto.getLoanNumber());
    loan.setLoanType(loansDto.getLoanType());
    loan.setMobileNumber(loansDto.getMobileNumber());
    loan.setTotalLoan(loansDto.getTotalLoan());
    loan.setAmountPaid(loansDto.getAmountPaid());
    loan.setOutstandingAmount(loansDto.getOutstandingAmount());
    return loan;
  }

}