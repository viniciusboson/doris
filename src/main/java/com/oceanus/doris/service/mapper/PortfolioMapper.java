package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.web.rest.dto.PortfolioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Portfolio and its DTO PortfolioDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PortfolioMapper extends EntityMapper<PortfolioDTO, Portfolio> {



    @Mapping(target = "accounts", ignore = true)
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
