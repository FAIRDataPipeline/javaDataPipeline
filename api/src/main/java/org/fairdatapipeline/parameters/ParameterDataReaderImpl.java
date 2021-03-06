package org.fairdatapipeline.parameters;

import static java.nio.channels.Channels.newReader;

import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.charset.StandardCharsets;
import org.fairdatapipeline.file.CleanableFileChannel;
import org.fairdatapipeline.toml.TomlReader;

public class ParameterDataReaderImpl implements ParameterDataReader {
  private final TomlReader tomlReader;

  public ParameterDataReaderImpl(TomlReader tomlReader) {
    this.tomlReader = tomlReader;
  }

  @Override
  public ReadComponent read(CleanableFileChannel fileChannel, String component) {
    return tomlReader
        .read(newReader(fileChannel, StandardCharsets.UTF_8), new TypeReference<Components>() {})
        .components()
        .get(component);
  }
}
