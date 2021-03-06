package org.fairdatapipeline.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.math3.random.RandomGenerator;
import org.fairdatapipeline.distribution.Distribution.DistributionType;
import org.fairdatapipeline.distribution.ImmutableDistribution;
import org.fairdatapipeline.estimate.ImmutableEstimate;
import org.fairdatapipeline.mapper.DataPipelineMapper;
import org.fairdatapipeline.samples.ImmutableSamples;
import org.json.JSONException;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComponentsSerializerTest {
  private final String expectedJson =
      "{\n"
          + "  \"example-distribution\": {\n"
          + "    \"distribution\": \"gamma\",\n"
          + "    \"scale\": 2,\n"
          + "    \"shape\": 1,\n"
          + "    \"type\": \"distribution\"\n"
          + "  },\n"
          + "  \"example-estimate\": {\n"
          + "    \"type\": \"point-estimate\",\n"
          + "    \"value\": 1.0\n"
          + "  },\n"
          + "  \"example-samples\": {\n"
          + "    \"samples\": [\n"
          + "      1,\n"
          + "      2,\n"
          + "      3\n"
          + "    ],\n"
          + "    \"type\": \"samples\"\n"
          + "  }\n"
          + "}";

  private ObjectMapper objectMapper;
  private RandomGenerator rng;

  @BeforeAll
  public void setUp() {
    this.rng = mock(RandomGenerator.class);
    when(rng.nextDouble()).thenReturn(0D);
    objectMapper = new DataPipelineMapper(rng);
  }

  @Test
  void serialize() throws IOException, JSONException {
    var writer = new StringWriter();
    var estimate = ImmutableEstimate.builder().internalValue(1.0).rng(rng).build();
    var distribution =
        ImmutableDistribution.builder()
            .internalType(DistributionType.gamma)
            .internalShape(1)
            .internalScale(2)
            .rng(rng)
            .build();
    var samples = ImmutableSamples.builder().addSamples(1, 2, 3).rng(rng).build();
    Components components =
        ImmutableComponents.builder()
            .putComponents("example-estimate", estimate)
            .putComponents("example-distribution", distribution)
            .putComponents("example-samples", samples)
            .build();

    objectMapper.writeValue(writer, components);

    var actualJson = writer.toString();
    assertThat(actualJson).isNotBlank();
    assertEquals(actualJson, expectedJson, true);
  }
}
