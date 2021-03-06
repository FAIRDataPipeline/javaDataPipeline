package org.fairdatapipeline.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.fairdatapipeline.api.Coderun;
import org.immutables.value.Value.Immutable;

/**
 * config.yaml configuration file - this specifies the file as rewritten by the FAIR-CLI command.
 *
 * <p>The config file MUST contain the run_metadata section, and MAY contain the {@link #readItems()
 * read} and {@link #writeItems() write} section(s).
 */
@Immutable
@JsonSerialize
@JsonDeserialize
public interface Config {

  /**
   * Ignored at the moment; I think the FAIR-CLI should deal with hash-checking for input files.
   *
   * @return boolean fail_on_hash_mismatch
   */
  @JsonProperty("fail_on_hash_mismatch")
  Optional<Boolean> failOnHashMisMatch();

  /**
   * The config file MUST contain a run_metadata section to specify the coderun details
   *
   * @return {@link Config.ConfigRunMetadata ImmutableConfigRunMetadata} the run metadata
   */
  @JsonProperty("run_metadata")
  ImmutableConfigRunMetadata run_metadata();

  /**
   * The config file MAY contain the read section to specify the read data products
   *
   * @return List a list of {@link Config.ConfigItem ImmutableConfigItem} read items
   */
  @JsonProperty("read")
  List<ImmutableConfigItem> readItems();

  /**
   * The config file MAY contain the write section to specify the write data products
   *
   * @return List a list of {@link Config.ConfigItem ImmutableConfigItem} write items
   */
  @JsonProperty("write")
  List<ImmutableConfigItem> writeItems();

  /**
   * Read or Write data product items, specified by name, or (only for write items) possibly ending
   * in * for simple globbing
   */
  @Immutable
  @JsonSerialize
  @JsonDeserialize
  interface ConfigItem {
    /**
     * data_product name (for write items this may end in * for simple globbing)
     *
     * @return String data product name
     */
    @JsonProperty
    String data_product();

    /**
     * For write items only: please set the description to get stored in the registry with this data
     * product
     *
     * @return The description of this write data product, if given
     */
    Optional<String> description();

    /**
     * Config (read or write) items MUST have a use section, which MUST specify a version.
     *
     * @return The {@link Config.ConfigUseItem ImmutableConfigUseItem} use section for this data
     *     product.
     */
    @JsonProperty
    ImmutableConfigUseItem use();

    /**
     * Config write items may specify the file_type (such as csv, h5, or toml) but this can also be
     * specified using {@link Coderun#get_dp_for_write(String, String)} method.
     *
     * @return String the file_type file extension, if given.
     */
    @JsonProperty
    Optional<String> file_type();

    /**
     * This is ignored. But added by CLI
     *
     * @return Boolean 'public' - this is ignored.
     */
    @JsonProperty("public")
    Optional<Boolean> isPublic();
  }

  /**
   * Use-section for a data product. This MUST specify a version. It may override data product name
   * or namespace.
   */
  @Immutable
  @JsonSerialize
  @JsonDeserialize
  interface ConfigUseItem {
    /**
     * Override the default namespace for this item
     *
     * @return String namespace name, if given.
     */
    @JsonProperty
    Optional<String> namespace();

    /**
     * Override the data_product name for this item
     *
     * @return String the actual data product name to be used, if given.
     */
    @JsonProperty
    Optional<String> data_product();

    /**
     * Set the version for this item
     *
     * @return String a semver version to be used for this data product.
     */
    @JsonProperty
    String version();
  }

  /** Defaults and settings for this coderun. */
  @Immutable
  @JsonSerialize
  @JsonDeserialize
  interface ConfigRunMetadata {
    /**
     * Set the description field for this code run. (optional, but it really shouldn't be)
     *
     * @return String the description for this code run, if given
     */
    @JsonProperty
    Optional<String> description();

    /**
     * this is usually http://localhost:8000/api/ (optional, but it probably shouldn't be)
     *
     * @return String the URL of the local data registry, if given.
     */
    @JsonProperty
    Optional<String> local_data_registry_url();

    /**
     * I don't know why we would specify this. It is ignored.
     *
     * @return String remote data registry URL, if given.
     */
    @JsonProperty
    Optional<String> remote_data_registry_url();

    /**
     * In which namespace to search for READ data products. this can be overridden in the {@link
     * Config.ConfigUseItem ConfigUseItem}. Optional.
     *
     * @return String default input namespace, if given.
     */
    @JsonProperty
    Optional<String> default_input_namespace();

    /**
     * In which namespace to place the WRITE data products. this can be overridden in the {@link
     * Config.ConfigUseItem ConfigUseItem}. Optional.
     *
     * @return String the default output namespace, if given.
     */
    @JsonProperty
    Optional<String> default_output_namespace();

    /**
     * The file system root of the local data store for new write objects.
     *
     * @return String the write data store Filesystem path on your local machine.
     */
    @JsonProperty
    Optional<String> write_data_store();

    /**
     * This is ignored.
     *
     * @return String local_repo - this is ignored.
     */
    @JsonProperty
    Optional<String> local_repo();

    /**
     * This is the location of the repository of the analysis code. For example:
     * https://github.com/FAIRDataPipeline/javaExampleModel
     *
     * @return String remote_repo - this must be a valid URL.
     */
    @JsonProperty
    Optional<String> remote_repo();

    /**
     * This is ignored.
     *
     * @return String script - this is ignored.
     */
    @JsonProperty
    Optional<String> script();

    /**
     * you can give the path to the submission script (to be stored in the code run) here, or in
     * {@link Coderun#Coderun(Path, Path)} FileApi constructor. Constructor will override the
     * config.
     *
     * @return String script_path - the location of the 'submission script', if given.
     */
    @JsonProperty
    Optional<String> script_path();

    /**
     * The commit hash of the remote_repo, in order to store the exact version of the analysis code.
     *
     * @return String latest_commit - the commit hash of the remote_repo, in order to store the
     *     exact version of the analysis code.
     */
    @JsonProperty
    Optional<String> latest_commit();

    /**
     * The authentication token for the registry.
     *
     * @return The authentication token for the registry.
     */
    @JsonProperty
    Optional<String> token();

    /**
     * This is ignored.
     *
     * @return Boolean 'public' - this is ignored.
     */
    @JsonProperty("public")
    Optional<Boolean> isPublic();

    /**
     * Default write version - Not Used
     *
     * @return The default write version - not used.
     */
    @JsonProperty
    Optional<String> default_read_version();

    /**
     * Default read version - Not Used
     *
     * @return The default write version - not used.
     */
    @JsonProperty
    Optional<String> default_write_version();
  }
}
