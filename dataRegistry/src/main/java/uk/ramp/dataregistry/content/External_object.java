package uk.ramp.dataregistry.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class External_object extends FDP_Updateable {
  @XmlElement private String doi_or_unique_name;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  @XmlElement
  private Boolean primary_not_supplement;

  @XmlElement private LocalDateTime release_date;

  @XmlElement private String title;

  @XmlElement private String description;

  @XmlElement private String data_product;

  @XmlElement private String original_store;

  public External_object() {};

  public String getDoi_or_unique_name() {
    return doi_or_unique_name;
  }

  public Boolean isPrimary_not_supplement() {
    return primary_not_supplement;
  }

  public LocalDateTime getRelease_date() {
    return release_date;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getData_product() {
    return data_product;
  }

  public String getOriginal_store() {
    return original_store;
  }

  public void setDoi_or_unique_name(String doi_or_unique_name) {
    this.doi_or_unique_name = doi_or_unique_name;
  }

  public void setPrimary_not_supplement(Boolean primary_not_supplement) {
    this.primary_not_supplement = primary_not_supplement;
  }

  public void setRelease_date(LocalDateTime release_date) {
    this.release_date = release_date;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setData_product(String data_product) {
    this.data_product = data_product;
  }

  public void setOriginal_store(String original_store) {
    this.original_store = original_store;
  }
}
