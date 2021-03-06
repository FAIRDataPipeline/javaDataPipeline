package org.fairdatapipeline.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.fairdatapipeline.dataregistry.content.RegistryObject_component;
import org.fairdatapipeline.file.CleanableFileChannel;

/**
 * This represents an object_component to read from or write to (or raise issues with) An
 * object_component without a name is the 'whole_object' component.
 */
public abstract class Object_component {
  final String component_name;
  final boolean whole_object;
  final Data_product dp;
  RegistryObject_component registryObject_component;
  boolean been_used = false;

  Object_component(Data_product dp, String component_name) {
    this(dp, component_name, false);
  }

  Object_component(Data_product dp) {
    this(dp, "whole_object", true);
  }

  Object_component(Data_product dp, String component_name, boolean whole_object) {
    this.dp = dp;
    this.whole_object = whole_object;
    this.component_name = component_name;
    this.populate_component();
  }

  /**
   * raise an issue with this component
   *
   * @param description the text description of this issue
   * @param severity Integer - higher means more severe
   */
  public void raise_issue(String description, Integer severity) {
    Issue i = this.dp.coderun.raise_issue(description, severity);
    i.add_components(this);
  }

  CleanableFileChannel getFileChannel() throws IOException {
    this.been_used = true;
    return this.dp.getFilechannel();
  }

  abstract void populate_component();

  RegistryObject_component retrieveObject_component() {
    Map<String, String> objcompmap = new HashMap<>();
    objcompmap.put("object", dp.registryObject.get_id().toString());
    if (this.whole_object) objcompmap.put("whole_object", "true");
    else objcompmap.put("name", component_name);
    return (RegistryObject_component)
        dp.coderun.restClient.getFirst(RegistryObject_component.class, objcompmap);
  }

  /** POST this registryObject_component to the registry, unless it's not been used */
  abstract void register_me_in_registry();

  /** Add this registryObject_component to the Coderun inputs/outputs. */
  abstract void register_me_in_code_run();
}
