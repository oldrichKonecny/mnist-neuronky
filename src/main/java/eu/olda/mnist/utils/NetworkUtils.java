package eu.olda.mnist.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import eu.olda.mnist.exceptions.InvalidArgumentException;
import eu.olda.mnist.exceptions.ParseNetworkSettingsException;
import eu.olda.mnist.network.NetworkSettings;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by olda on 02.12.2018
 */
public class NetworkUtils {

  private NetworkUtils() {}

  public static NetworkSettings parseNetworkSettingsFile() {
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    try {
      return objectMapper.readValue(NetworkUtils.class.getResourceAsStream("/networkSettings.yaml"), NetworkSettings.class);
    } catch (Exception ex) {
      throw new ParseNetworkSettingsException("cannot parse networkSettings.yaml file", ex);
    }
  }

  public static double[] makeRandomArray(int size, double lowerBound, double upperBound) {
    if (size < 1) throw new InvalidArgumentException("size of array must be more then 0");
    double[] array = new double[size];
    for (int i = 0; i < size; ++i) {
      array[i] = randomDouble(lowerBound, upperBound);
    }
    return array;
  }

  public static double[][] makeRandom2DimArray(int layerSize, int prevLayerSize, double lowerBound, double upperBound) {
    if (layerSize < 1 || prevLayerSize < 1) throw new InvalidArgumentException("size of arrays must be more then 0");
    double[][] array = new double[layerSize][prevLayerSize];
    for (int i = 0; i < layerSize; ++i) {
      array[i] = makeRandomArray(prevLayerSize, lowerBound, upperBound);
    }
    return array;
  }

  private static double randomDouble(double lowerBound, double upperBound) {
    return ThreadLocalRandom.current().nextDouble(lowerBound, upperBound);
  }
}
