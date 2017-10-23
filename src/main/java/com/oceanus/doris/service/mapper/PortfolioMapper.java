package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.PortfolioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Portfolio and its DTO PortfolioDTO.
 */
@Mapper(componentModel = "spring", uses = {AccountsMapper.class, AssetMapper.class, InstitutionMapper.class})
public interface PortfolioMapper extends EntityMapper<PortfolioDTO, Portfolio> {

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.description", target = "accountDescription")
    PortfolioDTO toDto(Portfolio portfolio); 

    @Mapping(source = "accountId", target = "account")
    Portfolio toEntity(PortfolioDTO portfolioDTO);

    default Portfolio fromId(Long id) {
        if (id == null) {
            return null;
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setId(id);
        return portfolio;
    }
}
