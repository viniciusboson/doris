package com.oceanus.doris.repository;

import com.oceanus.doris.domain.Asset;
import com.oceanus.doris.domain.Position;
import com.oceanus.doris.domain.PositionMetric;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PositionMetric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PositionMetricRepository extends JpaRepository<PositionMetric, Long> {
    PositionMetric findOneByPositionAndAssetComparison(Position position, Asset assetComparison);
}
