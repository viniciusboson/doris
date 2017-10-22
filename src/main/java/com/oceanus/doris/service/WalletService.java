package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.WalletDTO;
import java.util.List;

/**
 * Service Interface for managing Wallet.
 */
public interface WalletService {

    /**
     * Save a wallet.
     *
     * @param walletDTO the entity to save
     * @return the persisted entity
     */
    WalletDTO save(WalletDTO walletDTO);

    /**
     *  Get all the wallets.
     *
     *  @return the list of entities
     */
    List<WalletDTO> findAll();

    /**
     *  Get the "id" wallet.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WalletDTO findOne(Long id);

    /**
     *  Delete the "id" wallet.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
