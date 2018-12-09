package eu.olda.mnist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by olda on 09.12.2018
 */
@Setter
@Getter
@ToString
public class MnistSettings {

  private int trainLoops;
  private int batchSize;

  private String trainSetVectorFile;
  private String trainSetLabelFile;

  private String testVectorFile;
  private String testLabelFile;
}
