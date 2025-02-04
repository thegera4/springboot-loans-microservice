package com.jgmedellin.loans.repository;

import com.jgmedellin.loans.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {

  /**
   * Fetches the loan details given the mobile number.
   * @param mobileNumber Mobile number of the customer
   * @return Loans object with the loan details
   */
  Optional<Loans> findByMobileNumber(String mobileNumber);

  /**
   * Fetches the loan details given the loan number.
   * @param loanNumber Loan number of the customer
   * @return Loans object with the loan details
   */
  Optional<Loans> findByLoanNumber(String loanNumber);

}