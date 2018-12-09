package eu.olda.mnist;

import eu.olda.mnist.exceptions.InvalidArgumentException;
import eu.olda.mnist.exceptions.MnistGeneralException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by olda on 09.12.2018
 */
@Getter
@Setter
public class DataSet {

  private int size;

  private List<double[]> inputs;
  private List<Integer> expectedResult;

  public DataSet() {
  }

  public DataSet(int size) {
    this.size = size;

    inputs = new ArrayList<>(size);
    expectedResult = new ArrayList<>(size);
  }

  public double[] getInp(int index) {
    if (index < 0 || index > size-1) {
      throw new InvalidArgumentException("wrongIndex for getInp");
    }
    return inputs.get(index);
  }

  public int getExpRes(int index) {
    if (index < 0 || index > size-1) {
      throw new InvalidArgumentException("wrongIndex for getExpRes");
    }
    return expectedResult.get(index);
  }

  public void addData(double[] input, int expResult) {
    if (inputs.size() >= size || expectedResult.size() >= size) throw new MnistGeneralException("dataSet is too big...");
    inputs.add(input);
    expectedResult.add(expResult);
  }

  public DataSet getRandomBatch(int size) {
    if (size < 1 || size > this.size) throw new MnistGeneralException("size of subBatch (subDataSet) is: " + size + " but require to be maximum: " + this.size);
    DataSet subDataSet = new DataSet(size);

    int[] indexes = ThreadLocalRandom.current().ints(0, this.size).distinct().limit(size).toArray();
    for (int i : indexes) {
      subDataSet.addData(inputs.get(i), expectedResult.get(i));
    }
    return subDataSet;
  }

}
