package org.fairdatapipeline.api;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.fairdatapipeline.distribution.Distribution;
import org.fairdatapipeline.file.CleanableFileChannel;
import org.fairdatapipeline.parameters.ReadComponent;

/**
 * This represents an object_component to read from (or raise issues with) An object_component
 * without a name is the 'whole_object' component. Ideally the user should only read from named
 * components on toml and h5 files, and only read from the 'whole_object' on any other files. This
 * is not enforced at the moment.
 */
public class Object_component_read extends Object_component {

  Object_component_read(Data_product_read dp, String component_name) {
    super(dp, component_name);
  }

  Object_component_read(Data_product_read dp) {
    super(dp);
  }

  protected void populate_component() {
    this.registryObject_component = this.retrieveObject_component();
    if (this.registryObject_component == null) {
      throw (new RegistryObjectNotfoundException(
          "Object Component '"
              + this.component_name
              + "' for Object "
              + this.dp.registryObject.get_id().toString()
              + " not found in registry."));
    }
  }

  /**
   * get the filePath to read from; only for whole_object component
   *
   * @return Path the Path of the data object.
   */
  public Path readLink() {
    if (!this.whole_object) {
      throw (new IllegalActionException(
          "You shouldn't try to read directly from a Data Product with named components."));
    }
    this.been_used = true;
    return this.dp.getFilePath();
  }

  /**
   * get the CleanableFileChannel to read directly from the file. only for whole_object component.
   *
   * @return CleanableFileChannel the filechannel to read from.
   * @throws IOException if the file can't be opened.
   */
  public CleanableFileChannel readFileChannel() throws IOException {
    if (!this.whole_object) {
      throw (new IllegalActionException(
          "You shouldn't try to read directly from a Data Product with named components."));
    }
    this.been_used = true;
    return this.getFileChannel();
  }

  /**
   * read the Estimate that was stored as this component in a TOML file.
   *
   * @return the estimate as Number
   */
  public Number readEstimate() {
    ReadComponent data;
    try (CleanableFileChannel fileChannel = this.getFileChannel()) {
      data =
          dp.coderun.parameterDataReader.read(fileChannel, this.registryObject_component.getName());
    } catch (IOException e) {
      throw (new RuntimeException("readEstimate() -- IOException trying to read from file", e));
    }
    return data.getEstimate();
  }

  /**
   * read the Distribution that was stored as this component in a TOML file.
   *
   * @return the Distribution
   */
  public Distribution readDistribution() {
    ReadComponent data;
    try (CleanableFileChannel fileChannel = this.getFileChannel()) {
      data = this.dp.coderun.parameterDataReader.read(fileChannel, this.component_name);
    } catch (IOException e) {
      throw (new RuntimeException(
          "readDistribution() -- IOException trying to read from file.", e));
    }
    return data.getDistribution();
  }

  /**
   * read the Samples that were stored as this component in a TOML file.
   *
   * @return the Samples object
   */
  public List<Number> readSamples() {
    ReadComponent data;
    try (CleanableFileChannel fileChannel = this.getFileChannel()) {
      data = this.dp.coderun.parameterDataReader.read(fileChannel, this.component_name);
    } catch (IOException e) {
      throw (new RuntimeException("readSamples() -- IOException trying to read from file.", e));
    }
    return data.getSamples();
  }

  void register_me_in_registry() {
    // I am a read component, so I am already registered.
  }

  void register_me_in_code_run() {
    if (this.been_used) this.dp.coderun.addInput(this.registryObject_component.getUrl());
  }
}
