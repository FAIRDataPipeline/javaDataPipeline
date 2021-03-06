package org.fairdatapipeline.estimate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EstimateTest {
  private RandomGenerator rng;

  @BeforeAll
  public void setUp() {
    rng = mock(RandomGenerator.class);
    when(rng.nextDouble()).thenReturn(0D);
  }

  @Test
  void derivedEstimateFromEstimate() {
    var data = ImmutableEstimate.builder().internalValue(5).rng(rng).build();
    assertThat(data.getEstimate()).isEqualTo(5);
  }

  @Test
  void derivedSampleFromEstimate() {
    var data = ImmutableEstimate.builder().internalValue(5).rng(rng).build();
    assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(data::getSamples);
  }

  @Test
  void derivedDistributionFromEstimate() {
    var data = ImmutableEstimate.builder().internalValue(5).rng(rng).build();
    assertThatExceptionOfType(UnsupportedOperationException.class)
        .isThrownBy(data::getDistribution);
  }
}
