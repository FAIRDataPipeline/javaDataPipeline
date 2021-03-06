package org.fairdatapipeline.api;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import org.fairdatapipeline.dataregistry.content.RegistryStorage_root;
import org.fairdatapipeline.dataregistry.restclient.APIURL;
import org.fairdatapipeline.dataregistry.restclient.RestClient;

/** Retrieve or create the RegistryStorage_root with a given 'root'. */
class Storage_root {
  RegistryStorage_root registryStorage_root;

  /**
   * Retrieve or create the RegistryStorage_root with the given storageRootURI.
   *
   * @param storageRootURI the path/root for this Storage_root
   * @param restClient link to the restClient to access the registry.
   */
  Storage_root(URI storageRootURI, RestClient restClient) {
    this.registryStorage_root =
        (RegistryStorage_root)
            restClient.getFirst(
                RegistryStorage_root.class,
                Collections.singletonMap("root", storageRootURI.toString()));
    if (this.registryStorage_root == null) {
      this.registryStorage_root =
          (RegistryStorage_root) restClient.post(new RegistryStorage_root(storageRootURI));
      if (this.registryStorage_root == null) {
        throw (new RegistryException(
            "Failed to create in registry:  Storage_root " + storageRootURI));
      }
    }
  }

  APIURL getUrl() {
    return this.registryStorage_root.getUrl();
  }

  URI getRoot() {
    return this.registryStorage_root.getRoot();
  }

  Path getPath() {
    return this.registryStorage_root.getPath();
  }

  /**
   * split the repository URL into a storage root (proto://authority/ part) and path (/xxx/xxx) part
   *
   * @param url the URL to split up into scheme/authority and path.
   * @return string array of length 2.
   */
  static String[] url_to_root(URL url) {
    String path = url.getPath().substring(1);
    String scheme_and_authority_part =
        url.toString().substring(0, url.toString().length() - path.length());
    return new String[] {scheme_and_authority_part, path};
  }
}
