package com.BankAHT.accounts.dto;

import com.BankAHT.accounts.entity.AccountStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @NotEmpty(message = "CustomerId không thể là null hoặc empty")
    @Pattern(regexp = "^[0-9]{16}$", message = "CustomerId phải là một số gồm 16 chữ số")
    private Long customerId;

    @NotEmpty(message = "AccountsNumber không thể là null hoặc empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "AccountNumber phải là một số gồm 10 chữ số")
    private Long accountId;

    @NotEmpty(message = "AccountType không thể là null hoặc empty")
    private String accountType;

    @NotEmpty(message = "Balance không thể là null hoặc empty")
    private Long balance;


    private Timestamp createdAt;
    public String toString(){
        return customerId+" "+accountId+" "+accountType+" "+balance;
    }

    private AccountStatus accountStatus;
}
