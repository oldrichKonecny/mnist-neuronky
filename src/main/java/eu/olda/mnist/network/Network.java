package eu.olda.mnist.network;

import lombok.*;

/**
 * Class represents one fully connected feed forward neural network
 */
@Setter
@Getter
public class Network {
  private double[][][] weights;
  private double[][] bias;
  private double[][] currentState;

  private double[][] error;
}
