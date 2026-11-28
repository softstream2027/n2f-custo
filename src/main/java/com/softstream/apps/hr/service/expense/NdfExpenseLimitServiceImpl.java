package com.softstream.apps.hr.service.expense;

import com.axelor.apps.base.AxelorException;
import com.axelor.apps.base.db.repo.TraceBackRepository;
import com.axelor.apps.hr.db.Expense;
import com.axelor.apps.hr.db.ExpenseLimit;
import com.axelor.apps.hr.db.ExpenseLine;
import com.axelor.apps.hr.db.repo.ExpenseLimitRepository;
import com.axelor.apps.hr.db.repo.ExpenseLineRepository;
import com.axelor.apps.hr.db.repo.ExpenseRepository;
import com.axelor.apps.hr.exception.HumanResourceExceptionMessage;
import com.axelor.i18n.I18n;
import com.axelor.i18n.L10n;
import com.axelor.utils.helpers.date.LocalDateHelper;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import com.axelor.apps.hr.service.expense.ExpenseLimitServiceImpl;
import com.axelor.apps.base.db.Period;

public class NdfExpenseLimitServiceImpl extends ExpenseLimitServiceImpl {
 
  @Inject
  public NdfExpenseLimitServiceImpl(
      ExpenseLimitRepository expenseLimitRepository, ExpenseLineRepository expenseLineRepository) {
    super(expenseLimitRepository, expenseLineRepository);
  }

  @Transactional(rollbackOn = {Exception.class})
  @Override
  public void checkExpenseLimit(Expense expense) throws AxelorException {

    List<ExpenseLimit> expenseLimitList =
        expenseLimitRepository
            .all()
            .filter("self.employee = :employee")
            .bind("employee", expense.getEmployee())
            .fetch();
    if (CollectionUtils.isEmpty(expenseLimitList)) {

      BigDecimal maxAmount = expense.getCompany().getMaxAmount();

    Period period = expense.getPeriod();

    LocalDate fromDate = period.getFromDate();
    LocalDate toDate = period.getToDate();
    L10n dateFormat = L10n.getInstance();

    if(expense.getInTaxTotal() != null && maxAmount != null && maxAmount.compareTo(BigDecimal.ZERO) > 0
    && expense.getInTaxTotal().compareTo(maxAmount) > 0){
     throw new AxelorException(
            TraceBackRepository.CATEGORY_INCONSISTENCY,
            String.format(
                I18n.get(HumanResourceExceptionMessage.EXPENSE_LIMIT_EXCEEDED),
                dateFormat.format(fromDate),
                dateFormat.format(toDate)));
    }

    }
    
    else 
    super.checkExpenseLimit(expense);
  }
 
 
 }
