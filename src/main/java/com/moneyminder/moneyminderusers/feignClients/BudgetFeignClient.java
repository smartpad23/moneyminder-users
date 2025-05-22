package com.moneyminder.moneyminderusers.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "budgetService", url = "${app.budget-service-url}", configuration = FeignConfig.class)
public interface BudgetFeignClient {

    @GetMapping("/budget-name/{id}")
    String getBudgetNameByGroupId(@PathVariable String id);

}
