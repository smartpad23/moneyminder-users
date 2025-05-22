package com.moneyminder.moneyminderusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@AutoConfiguration
@SpringBootApplication
public class MoneyMinderUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyMinderUsersApplication.class, args);
    }

}
