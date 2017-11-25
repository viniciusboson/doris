package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.web.rest.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {OperationMapper.class, PositionMapper.class})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(source = "operation.id", target = "operationId")
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.description", target = "positionDescription")
    TransactionDTO toDto(Transaction transaction);

    @Mapping(source = "operationId", target = "operation")
    @Mapping(source = "positionId", target = "position")
    Transaction toEntity(TransactionDTO transactionDTO);

    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
