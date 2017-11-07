package com.oceanus.doris.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PositionMetricTest {

    @Test
    public void calculateWeightedAvgPriceWithoutPreviousAvg(){
        PositionMetric positionMetric = new PositionMetric();

        Double weightedAvgPrice = positionMetric.calculateWeightedAvgPrice(null, null,
            40.0, 50.0, null);

        assertThat(weightedAvgPrice).isEqualTo(40.0);
    }

    @Test
    public void calculateWeightedAvgPriceForRepeatedValues(){
        PositionMetric positionMetric = new PositionMetric();

        Double weightedAvgPrice = positionMetric.calculateWeightedAvgPrice(10.0, 100.0,
            10.0, 100.0, null);

        assertThat(weightedAvgPrice).isEqualTo(10.0);
    }

    @Test
    public void calculateWeightedAvgPriceWithoutCost(){
        PositionMetric positionMetric = new PositionMetric();

        Double weightedAvgPrice = positionMetric.calculateWeightedAvgPrice(10.0, 100.0,
            40.0, 50.0, null);

        assertThat(weightedAvgPrice).isEqualTo(20.0);
    }

    @Test
    public void calculateWeightedAvgPriceWithCost(){
        PositionMetric positionMetric = new PositionMetric();

        Double weightedAvgPrice = positionMetric.calculateWeightedAvgPrice(10.0, 100.0,
            40.0, 50.0, 30.0);

        assertThat(weightedAvgPrice).isEqualTo(20.2);
    }

    @Test
    public void increasePositionWithoutCosts(){
        PositionMetric positionMetric = new PositionMetric();

        positionMetric.increasePosition(10.0, 100.0, 0.0);
        positionMetric.increasePosition(40.0, 50.0, 0.0);

        assertThat(positionMetric.getEntryAvgPrice()).isEqualTo(20.0);
        assertThat(positionMetric.getEntryAmount()).isEqualTo(150.0);
    }

    @Test
    public void increasePositionWithCosts(){
        PositionMetric positionMetric = new PositionMetric();

        positionMetric.increasePosition(9.0, 100.0, 21.5);
        positionMetric.increasePosition(12.0, 100.0, 21.7);
        positionMetric.increasePosition(15.0, 200.0, 23.6);

        assertThat(positionMetric.getEntryAvgPrice()).isEqualTo(12.917);
        assertThat(positionMetric.getEntryAmount()).isEqualTo(400.0);
    }

    @Test
    public void decreasePositionWithoutCosts(){
        PositionMetric positionMetric = new PositionMetric();

        positionMetric.decreasePosition(10.0, 100.0, 0.0);
        positionMetric.decreasePosition(40.0, 50.0, 0.0);

        assertThat(positionMetric.getExitAvgPrice()).isEqualTo(20.0);
        assertThat(positionMetric.getExitAmount()).isEqualTo(150.0);
    }


    @Test
    public void decreasePositionWithCosts(){
        PositionMetric positionMetric = new PositionMetric();

        positionMetric.decreasePosition(9.0, 100.0, 21.5);
        positionMetric.decreasePosition(12.0, 100.0, 21.7);
        positionMetric.decreasePosition(15.0, 200.0, 23.6);

        assertThat(positionMetric.getExitAvgPrice()).isEqualTo(12.917);
        assertThat(positionMetric.getExitAmount()).isEqualTo(400.0);
    }

}
