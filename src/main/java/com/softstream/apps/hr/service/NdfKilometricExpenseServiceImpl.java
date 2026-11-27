package com.softstream.apps.hr.service;

import com.axelor.apps.base.AxelorException;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.repo.TraceBackRepository;
import com.axelor.apps.hr.db.Employee;
import com.axelor.apps.hr.db.EmploymentContract;
import com.axelor.apps.hr.db.Expense;
import com.axelor.apps.hr.db.ExpenseLine;
import com.axelor.apps.hr.db.KilometricAllowanceRate;
import com.axelor.apps.hr.db.KilometricAllowanceRule;
import com.axelor.apps.hr.db.KilometricLog;
import com.axelor.apps.hr.db.repo.KilometricAllowanceRateRepository;
import com.axelor.apps.hr.exception.HumanResourceExceptionMessage;
import com.axelor.apps.hr.service.config.HRConfigService;
import com.axelor.apps.hr.service.expense.ExpenseComputationService;
import com.axelor.apps.hr.service.expense.ExpenseLineComputeService;
import com.axelor.common.ObjectUtils;
import com.axelor.i18n.I18n;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.axelor.apps.hr.service.KilometricExpenseServiceImpl;
import com.axelor.apps.hr.service.KilometricLogService;
 
public class NdfKilometricExpenseServiceImpl extends KilometricExpenseServiceImpl {

  @Inject
  public NdfKilometricExpenseServiceImpl(
      KilometricLogService kilometricLogService,
      KilometricAllowanceRateRepository kilometricAllowanceRateRepository,
      HRConfigService hrConfigService,
      ExpenseComputationService expenseComputationService,ExpenseLineComputeService expenseLineComputeService) {
    super(  kilometricLogService,
        kilometricAllowanceRateRepository,
        hrConfigService,
        expenseComputationService,expenseLineComputeService);
  }

  @Override
  public BigDecimal computeKilometricExpense(ExpenseLine expenseLine, Employee employee)
      throws AxelorException {
 
    EmploymentContract mainEmploymentContract = employee.getMainEmploymentContract();
    if (mainEmploymentContract == null || mainEmploymentContract.getPayCompany() == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR,
          I18n.get(HumanResourceExceptionMessage.EMPLOYEE_CONTRACT_OF_EMPLOYMENT),
          employee.getName());
    }
    Company company = mainEmploymentContract.getPayCompany();

   
    KilometricAllowanceRate allowance =
        expenseLine.getKilometricAllowParam() != null
            ? kilometricAllowanceRateRepository
                .all()
                .filter(
                    "self.kilometricAllowParam.id = :_kilometricAllowParamId "
                        + "and self.hrConfig.id = :_hrConfigId")
                .bind("_kilometricAllowParamId", expenseLine.getKilometricAllowParam().getId())
                .bind("_hrConfigId", hrConfigService.getHRConfig(company).getId())
                .fetchOne()
            : null;
    if (allowance == null) {
      return BigDecimal.ZERO;
    }

return super.computeKilometricExpense(expenseLine, employee);
  
  }
 
}
