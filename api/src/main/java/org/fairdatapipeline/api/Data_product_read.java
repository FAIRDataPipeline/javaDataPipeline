package org.fairdatapipeline.api;

import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.List;
import org.fairdatapipeline.config.ConfigException;
import org.fairdatapipeline.config.ImmutableConfigItem;
import org.fairdatapipeline.dataregistry.content.RegistryNamespace;
import org.fairdatapipeline.dataregistry.content.RegistryObject;
import org.fairdatapipeline.dataregistry.content.RegistryStorage_location;
import org.fairdatapipeline.dataregistry.content.RegistryStorage_root;
import org.fairdatapipeline.file.CleanableFileChannel;

/**
 * Data_product_read is created by Coderun: {@link Coderun#get_dp_for_read(String)}
 *
 * <p>Upon {@link Coderun#close()} it will register its components in the coderun.
 */
public class Data_product_read extends Data_product {
  private boolean hash_checked = false;

  Data_product_read(String dataProduct_name, Coderun coderun) {
    super(dataProduct_name, coderun);
  }

  void populate_data_product() {
    // called from the constructor
    this.registryData_product = this.getRegistryData_product();
    if (this.registryData_product == null) {
      throw (new RegistryObjectNotfoundException(
          "Trying to read from non-existing data_product "
              + this.actualDataProduct_name
              + "; NS "
              + this.registryNamespace.getName()
              + "; version "
              + version));
    }
    this.registryObject =
        (RegistryObject)
            coderun.restClient.get(RegistryObject.class, this.registryData_product.getObject());
    if (this.registryObject == null) {
      throw (new RegistryObjectNotfoundException(
          "couldn't retrieve the fdpObject for this READ dp "
              + this.givenDataProduct_name
              + " ("
              + this.actualDataProduct_name
              + ")"));
    }
    this.registryStorage_location =
        (RegistryStorage_location)
            coderun.restClient.get(
                RegistryStorage_location.class, this.registryObject.getStorage_location());
    if (this.registryStorage_location == null) {
      throw (new RegistryObjectNotfoundException(
          "Couldn't retrieve the StorageLocation for this READ dp "
              + this.givenDataProduct_name
              + " ("
              + this.actualDataProduct_name
              + ")"));
    }
    this.registryStorage_root =
        (RegistryStorage_root)
            coderun.restClient.get(
                RegistryStorage_root.class, this.registryStorage_location.getStorage_root());
    if (this.registryStorage_root == null) {
      throw (new RegistryObjectNotfoundException(
          "Couldn't retrieve the StorageRoot for this READ dp "
              + this.givenDataProduct_name
              + " ("
              + this.actualDataProduct_name
              + ")"));
    }
    this.filePath =
        this.registryStorage_root
            .getPath()
            .resolve(Path.of(this.registryStorage_location.getPath()));
  }

  List<ImmutableConfigItem> getConfigItems() {
    return coderun.config.readItems();
  }

  String getDefaultNamespace_name() {
    return this.coderun.config.run_metadata().default_input_namespace().orElse("");
  }

  @Override
  RegistryNamespace getRegistryNamespace(String namespace_name) {
    // for a READ dp we must have the namespace from the config or we will have to give up
    RegistryNamespace ns = super.getRegistryNamespace(namespace_name);
    if (ns == null) {
      throw (new RegistryObjectNotfoundException("Can't find the namespace " + namespace_name));
    }
    return ns;
  }

  @Override
  ImmutableConfigItem getConfigItem(String dataProduct_name) {
    ImmutableConfigItem configItem = super.getConfigItem(dataProduct_name);
    if (configItem == null) {
      throw (new ConfigException("dataProduct " + dataProduct_name + " not found in config"));
    }
    return configItem;
  }

  private void check_hash() {
    // Do nothing for now. Unsure if we will implement hash checking.
  }

  private void executeOnCloseFileHandleDP() {
    // don't need to Hash READ objects
  }

  protected Path getFilePath() {
    this.been_used = true;
    if (!hash_checked) this.check_hash();
    return this.filePath;
  }

  CleanableFileChannel getFilechannel() throws IOException {
    this.been_used = true;
    Runnable onClose = this::executeOnCloseFileHandleDP;
    if (!hash_checked) this.check_hash();
    if (this.filechannel == null) {
      this.filechannel = new CleanableFileChannel(FileChannel.open(this.filePath, READ), onClose);
    } else {
      if (!this.filechannel.isOpen()) {
        this.filechannel = new CleanableFileChannel(FileChannel.open(this.filePath, READ), onClose);
      }
    }
    return this.filechannel;
  }

  /**
   * Obtain an Object_component for reading.
   *
   * @param component_name the name of the object component to read.
   * @return the Object_component class
   */
  public Object_component_read getComponent(String component_name) {
    if (componentMap.containsKey(component_name))
      return (Object_component_read) componentMap.get(component_name);
    Object_component_read dc = new Object_component_read(this, component_name);
    componentMap.put(component_name, dc);
    return dc;
  }

  /**
   * Obtain an Object_component (whole_object) for reading.
   *
   * @return the Object_component class
   */
  public Object_component_read getComponent() {
    if (this.whole_obj_oc == null) this.whole_obj_oc = new Object_component_read(this);
    return (Object_component_read) this.whole_obj_oc;
  }

  void components_to_registry() {
    // this is just to make sure the components can register their issues
    this.componentMap.entrySet().stream()
        .filter(c -> c.getValue().been_used)
        .forEach(component -> component.getValue().register_me_in_registry());
  }

  void objects_to_registry() {
    // this is called upon close; but since this is a READ dp, it won't have a DP, Stolo, fdpObj, or
    // any new components to register
    this.components_to_registry();
  }
}
