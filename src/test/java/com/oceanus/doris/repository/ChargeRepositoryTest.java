package com.oceanus.doris.repository;

import com.oceanus.doris.DorisApp;
import com.oceanus.doris.domain.Asset;
import com.oceanus.doris.domain.Charge;
import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.repository.util.EntityCreation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the ChargeRepository repository
 *
 * @see com.oceanus.doris.repository.ChargeRepository
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DorisApp.class)
public class ChargeRepositoryTest {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private EntityManager em;

    private Charge charge;

    private Institution anotherInstitution;

    private Asset anotherAsset;

    private OperationType anotherOperationType;

    @Before
    public void initTest() {
        charge = EntityCreation.Charge.createEntity(em);
        Asset assets = EntityCreation.Asset.createEntity(em, true);
        charge.getAssets().add(assets);
        em.persist(charge);
        em.flush();

        //Entities not related to the persisted charge
        anotherInstitution = EntityCreation.Institution.createEntity(em, true);
        anotherAsset = EntityCreation.Asset.createEntity(em, true);
        anotherOperationType = Arrays
            .stream(OperationType.values())
            .filter(o -> !o.equals(charge.getOperationType()))
            .findFirst()
            .get();
    }

    @Transactional
    @Test
    public void findAllByInstitutionAndAssetsContainsAndOperationTypeOK() {
        Institution validInstitution = charge.getInstitution();
        Asset validAsset = charge.getAssets().stream().findFirst().get();
        OperationType validOperationType = charge.getOperationType();

        List<Charge> charges = chargeRepository.findAllByInstitutionAndAssetsContainsAndOperationType(
            validInstitution, validAsset, validOperationType);

        assertThat(charges).isNotEmpty();
        Charge testCharge = charges.get(charges.size() - 1);
        assertThat(testCharge.getInstitution()).isEqualTo(validInstitution);
        assertThat(testCharge.getAssets()).contains(validAsset);
        assertThat(testCharge.getOperationType()).isEqualByComparingTo(validOperationType);
    }

    @Transactional
    @Test
    public void findAllByInstitutionAndAssetsContainsAndOperationTypeWithInvalidInstitution() {
        Institution invalidInstitution = anotherInstitution;
        Asset validAsset = charge.getAssets().stream().findFirst().get();
        OperationType validOperationType = charge.getOperationType();

        List<Charge> charges = chargeRepository.findAllByInstitutionAndAssetsContainsAndOperationType(
            invalidInstitution, validAsset, validOperationType);

        assertThat(charges).isEmpty();
    }

    @Transactional
    @Test
    public void findAllByInstitutionAndAssetsContainsAndOperationTypeWithInvalidAsset() {
        Institution validInstitution = charge.getInstitution();
        Asset invalidAsset = anotherAsset;
        OperationType validOperationType = charge.getOperationType();

        List<Charge> charges = chargeRepository.findAllByInstitutionAndAssetsContainsAndOperationType(
            validInstitution, invalidAsset, validOperationType);

        assertThat(charges).isEmpty();
    }

    @Transactional
    @Test
    public void findAllByInstitutionAndAssetsContainsAndOperationTypeWithInvalidOperationType() {
        Institution validInstitution = charge.getInstitution();
        Asset validAsset = charge.getAssets().stream().findFirst().get();
        OperationType invalidOperationType = anotherOperationType;

        List<Charge> charges = chargeRepository.findAllByInstitutionAndAssetsContainsAndOperationType(
            validInstitution, validAsset, invalidOperationType);

        assertThat(charges).isEmpty();
    }
}
