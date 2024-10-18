package com.secure_sentinel.accounts.accounts_service.mapper;

import com.secure_sentinel.accounts.accounts_service.dto.AccountDTO;
import com.secure_sentinel.accounts.accounts_service.dto.DetailedAccountDTO;
import com.secure_sentinel.accounts.accounts_service.model.Account;
import com.secure_sentinel.accounts.accounts_service.utils.AccountUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "accountNumber", target = "accountNumber", qualifiedByName = "maskAccountNumber")
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "accountProgram.programName", target = "programName")
    AccountDTO accountToAccountDTO(Account account);

    @Mapping(source = "accountProgram.programName", target = "programName")
    DetailedAccountDTO accountToDetailedAccountDTO(Account account);

    @Named("maskAccountNumber")
    default String maskAccountNumber(String accountNumber) {
        return AccountUtils.maskAccountNumber(accountNumber);
    }
}