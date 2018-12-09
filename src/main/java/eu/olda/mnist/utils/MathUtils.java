package eu.olda.mnist.utils;

import eu.olda.mnist.exceptions.InvalidArgumentException;
import eu.olda.mnist.network.NetworkSettings;

import java.util.function.Function;

/**
 * Created by olda on 05.12.2018
 */
public class MathUtils {

  private MathUtils() {}

  public static Function<Double, Double> getActivationFunction(NetworkSettings.ActivationFunctionType type) {
    switch (type) {
      case SIGMOID: return getSigmoidFunction();
    }
    throw new InvalidArgumentException("Cannot find activation function");
  }

  public static Function<Double, Double> getErrorFunction(NetworkSettings.ErrorFunctionType type) {
    switch (type) {
      case MEAN_SQUARED: return getMeanSquaredFunction();
    }
    throw new InvalidArgumentException("Cannot find error function");
  }

  public static double neuronDerivation(double value) {
    return value * (1.0 - value);
  }

  private static Function<Double,Double> getSigmoidFunction() {
    return (x) -> 1.0 / (1.0 + Math.exp(-x));
  }

  private static Function<Double, Double> getMeanSquaredFunction() {
    return null;
  }


}
