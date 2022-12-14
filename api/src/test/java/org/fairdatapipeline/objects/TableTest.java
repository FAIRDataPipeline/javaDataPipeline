package org.fairdatapipeline.objects;

import java.util.Collections;
import org.fairdatapipeline.netcdf.NetcdfDataType;
import org.fairdatapipeline.netcdf.NetcdfGroupName;
import org.fairdatapipeline.netcdf.NetcdfName;
import org.junit.jupiter.api.Test;

class TableTest {
  @Test
  void table() {
    LocalVariableDefinition[] columns =
        new LocalVariableDefinition[] {
          new LocalVariableDefinition(
              new NetcdfName("bla"), NetcdfDataType.STRING, "item", "", "", Collections.emptyMap()),
          new LocalVariableDefinition(
              new NetcdfName("price"),
              NetcdfDataType.FLOAT,
              "price",
              "GBP",
              "the price per item",
              Collections.emptyMap())
        };

    TableDefinition td =
        new TableDefinition(
            new NetcdfGroupName("my/group"),
            0,
            "A test table",
            "Bram's beautiful test table",
            Collections.emptyMap(),
            columns);
  }
}