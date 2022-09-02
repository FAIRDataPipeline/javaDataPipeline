package org.fairdatapipeline.dataregistry.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.fairdatapipeline.dataregistry.restclient.APIURL;

/** A data product that is used by or generated by a model. */
@XmlRootElement
public class RegistryData_product extends Registry_Updateable {

  @XmlElement private String name;

  @XmlElement private String version;

  @XmlElement private APIURL object;

  @XmlElement private APIURL namespace;

  @XmlElement private APIURL external_object;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  @XmlElement
  private boolean internal_format;

  @XmlElement private APIURL prov_report;

  @XmlElement private APIURL ro_crate;

  /** Empty constructor. */
  public RegistryData_product() {
    this.methods_allowed = List.of("GET", "PUT", "PATCH", "HEAD", "OPTIONS");
  }

  /**
   * Name of the DataProduct, unique in the context of the triple (name, version, namespace)
   *
   * @return Name of the DataProduct.
   */
  public String getName() {
    return name;
  }

  /**
   * Version identifier of the DataProduct, must conform to semantic versioning syntax.
   *
   * @return Version identifier of the DataProduct, must conform to semantic versioning syntax.
   */
  public String getVersion() {
    return version;
  }

  /**
   * APIURL of the associated Object.
   *
   * @return APIURL of the associated Object.
   */
  public APIURL getObject() {
    return object;
  }

  /**
   * API URL of the Namespace of the DataProduct.
   *
   * @return API URL of the Namespace of the DataProduct.
   */
  public APIURL getNamespace() {
    return namespace;
  }

  /**
   * read-only: ExternalObject API URL associated with this DataProduct.
   *
   * @return ExternalObject API URL associated with this DataProduct.
   */
  public APIURL getExternal_object() {
    return external_object;
  }

  /**
   * Whether this data product is in TOML/H5 format managed/specified by the data pipeline, I think.
   *
   * @return Whether this data product is in TOML/H5 format managed/specified by the data pipeline,
   *     I think.
   */
  public boolean isInternal_format() {
    return this.internal_format;
  }

  /**
   * APIURL reference to the PROV report for this Data product
   *
   * @return APIURL reference to the PROV report for this Data product
   */
  public APIURL getProv_report() {
    return this.prov_report;
  }

  /**
   * APIURL reference to the ro_crate for this Data product
   *
   * @return APIURL reference to the ro_crate report for this Data product
   */
  public APIURL getRo_crate() {
    return this.ro_crate;
  }

  /**
   * @param name Name of the DataProduct, unique in the context of the triple (name, version,
   *     namespace)
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @param version Version identifier of the DataProduct, must conform to semantic versioning
   *     syntax.
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /** @param object APIURL of the associated Object. */
  public void setObject(APIURL object) {
    this.object = object;
  }

  /** @param namespace API URL of the Namespace of the DataProduct. */
  public void setNamespace(APIURL namespace) {
    this.namespace = namespace;
  }
}
