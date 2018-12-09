package eu.olda.mnist.network;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by olda on 05.12.2018
 */
@Getter
@Setter
@ToString
public class NetworkSettings {
  private int[] layersSizes;

  private double biasUpperBound;
  private double biasLowerBound;
  private double weightUpperBound;
  private double weightLowerBound;

  private double learningRate;

  private ActivationFunctionType activationFunctionType;
  private ErrorFunctionType errorFunctionType;


  public int getNetworkSize() {
    if (layersSizes != null) return layersSizes.length;
    else return 0;
  }

  public int getInputSize() {
    if (layersSizes != null) return layersSizes[0];
    else return 0;
  }

  public int getOutputSize() {
    if (layersSizes != null) return layersSizes[getNetworkSize()-1];
    else return 0;
  }

  public enum ActivationFunctionType {
    SIGMOID,
  }

  public enum ErrorFunctionType {
    MEAN_SQUARED,
  }
}
