package com.example.foodordering.mapper;
import com.example.foodordering.dto.request.AccountCreationRequest;
import com.example.foodordering.dto.request.AccountUpdateRequest;
import com.example.foodordering.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(AccountCreationRequest request);

    void updateAccount(@MappingTarget Account account, AccountUpdateRequest request);

}
