package com.jgmedellin.loans.service;

import com.jgmedellin.loans.dto.LoansDto;

public interface ILoansService {

  /**
   * Creates a new loan given the customer mobile number.
   * @param mobileNumber Mobile number of the customer
   */
  void createLoan(String mobileNumber);

  /**
   * Fetches the loan details given the mobile number.
   * @param mobileNumber Mobile number of the customer
   * @return LoansDto object with the loan details
   */
  LoansDto fetchLoan(String mobileNumber);

  /**
   * Updates loan details.
   * @param loansDto Customer details (name, email, mobile number, account details)
   * @return boolean value indicating the success of the update operation
   */
  boolean updateLoan(LoansDto loansDto);

  /**
   * Deletes a loan given the mobile number.
   * @param mobileNumber Mobile number of the customer
   * @return boolean value indicating the success of the delete operation
   */
  boolean deleteLoan(String mobileNumber);

}