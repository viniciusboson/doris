package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.web.rest.dto.AccountsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Accounts and its DTO AccountsDTO.
 */
@Mapper(componentModel = "spring", uses = {PortfolioMapper.class, AssetMapper.class, InstitutionMapper.class})
public interface AccountsMapper extends EntityMapper<AccountsDTO, Accounts> {

    @Mapping(source = "portfolio.id", target = "portfolioId")
    @Mapping(source = "portfolio.description", target = "portfolioDescription")
    AccountsDTO toDto(Accounts accounts);

    @Mapping(source = "portfolioId", target = "portfolio")
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
