package com.softstream.apps.base.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.hr.service.KilometricExpenseServiceImpl;
import com.softstream.apps.hr.service.NdfKilometricExpenseServiceImpl;
import com.axelor.apps.hr.service.expense.ExpenseLimitServiceImpl;
import com.softstream.apps.hr.service.expense.NdfExpenseLimitServiceImpl;

public class NdfBaseModule extends AxelorModule {
  @Override
  protected void configure() { 

    bind(KilometricExpenseServiceImpl.class).to(NdfKilometricExpenseServiceImpl.class);
    bind(ExpenseLimitServiceImpl.class).to(NdfExpenseLimitServiceImpl.class);

  }
}
