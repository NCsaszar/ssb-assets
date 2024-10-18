package com.secure_sentinel.accounts.accounts_service.mapper;

import com.secure_sentinel.accounts.accounts_service.dto.TransactionDTO;
import com.secure_sentinel.accounts.accounts_service.model.Transaction;
import com.secure_sentinel.accounts.accounts_service.utils.AccountUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.accountNumber", target = "accountNumber", qualifiedByName = "maskAccountNumber")
    TransactionDTO transactionToTransactionDTO(Transaction transaction);

    Transaction transactionDTOToTransaction(TransactionDTO transactionDTO);

    @Named("maskAccountNumber")
    default String maskAccountNumber(String accountNumber) {
        return AccountUtils.maskAccountNumber(accountNumber);
    }
}