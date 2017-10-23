package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.AccountsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Accounts and its DTO AccountsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountsMapper extends EntityMapper<AccountsDTO, Accounts> {

    

    @Mapping(target = "portfolios", ignore = true)
    Accounts toEntity(AccountsDTO accountsDTO);

    default Accounts fromId(Long id) {
        if (id == null) {
            return null;
        }
        Accounts accounts = new Accounts();
        accounts.setId(id);
        return accounts;
    }
}
