package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {OperationMapper.class, AssetMapper.class})
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {

    @Mapping(source = "operation.id", target = "operationId")
    @Mapping(source = "asset.id", target = "assetId")
    @Mapping(source = "asset.code", target = "assetCode")
    TransactionDTO toDto(Transaction transaction); 

    @Mapping(source = "operationId", target = "operation")
    @Mapping(source = "assetId", target = "asset")
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
