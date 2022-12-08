package org.fairdatapipeline.api;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import org.fairdatapipeline.netcdf.NetcdfBuilder;
import org.fairdatapipeline.netcdf.NetcdfWriter;
import org.fairdatapipeline.objects.CoordinateVariableDefinition;
import org.fairdatapipeline.objects.NumericalArray;
import org.fairdatapipeline.objects.NumericalArrayImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;

public class Object_component_write_dimension extends Object_component_write {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(Object_component_write_dimension.class);
  CoordinateVariableDefinition dimdef;
  Variable variable;
  int[] write_pointer;
  int[] shape;
  boolean eof = false;

  Object_component_write_dimension(Data_product_write_nc dp, CoordinateVariableDefinition dimdef) {
    // dimdef.getVariableName().toString: /my/path + / + dimensionName = component_name
    super(dp, dimdef.getVariableName().toString());
    this.dimdef = dimdef;
    NetcdfBuilder nBuilder = ((Data_product_write_nc) this.dp).getNetCDFBuilder();
    nBuilder.prepare(dimdef);
  }

  private void getVariable() {
    if (this.variable != null) return;
    this.been_used = true;
    NetcdfWriter nWriter = ((Data_product_write_nc) this.dp).getNetCDFWriter();
    this.variable = nWriter.getVariable(this.dimdef.getVariableName());
    this.shape = this.variable.getShape();
    this.write_pointer = new int[] {0};
  }

  public void writeData() throws IOException {
    if (this.dimdef.getValues() != null) {
      this.writeData(new NumericalArrayImpl(this.dimdef.getValues()));
    }
  }

  /**
   * writes the whole data (if nadat.getShape equals variable.getShape) or write a slice and update
   * a write-pointer.
   *
   * @param nadat the data we want to write.
   * @throws IOException if the write fails.
   */
  public void writeData(NumericalArray nadat) throws /*EOFException, */ IOException {
    if (eof) throw (new EOFException("trying to write beyond end of data"));
    NetcdfWriter nWriter = ((Data_product_write_nc) this.dp).getNetCDFWriter();
    if (this.variable == null) this.getVariable();
    if (Arrays.equals(nadat.getShape(), this.shape)) {
      // nadat contains ALL the data for the variable.. write it from its start.
      try {
        nWriter.writeArrayData(variable, nadat);
      } catch (InvalidRangeException e) {
        // TODO: error handling
      }
      eof = true;
    } else {
      try {
        Array a = Array.makeFromJavaArray(nadat.asObject());
        if (a.getShape().length == 0) a = Array.makeArrayRankPlusOne(a);
        nWriter.writeArrayData(variable, a, this.write_pointer);
        this.write_pointer[0] += 1;
        if (this.shape[0] != 0 && this.write_pointer[0] >= this.shape[0]) {
          eof = true;
        }
      } catch (InvalidRangeException e) {
        // TODO: error handling
      }
    }
  }

  void write_preset_data() {
    try {
      writeData();
    } catch (IOException e) {
      LOGGER.error("failed to write preset data", e);
    }
  }
}
