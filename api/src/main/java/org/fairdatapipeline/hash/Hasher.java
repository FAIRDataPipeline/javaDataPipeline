package org.fairdatapipeline.hash;

import java.time.Instant;
import org.fairdatapipeline.file.FileReader;

/** Create the hash for a given file. Currently only implements sha1 hash. */
public class Hasher {
  public String fileHash(String fileName) {
    return new Sha1Hasher().hash(new FileReader().read(fileName));
  }

  public String fileHash(String fileName, Instant openTimestamp) {
    return new Sha1Hasher().hash(new FileReader().read(fileName) + openTimestamp.toString());
  }
}
