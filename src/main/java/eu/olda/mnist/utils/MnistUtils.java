package eu.olda.mnist.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import eu.olda.mnist.MnistSettings;
import eu.olda.mnist.exceptions.MnistGeneralException;
import eu.olda.mnist.exceptions.ParseNetworkSettingsException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by olda on 02.12.2018
 */

public class MnistUtils {
  private MnistUtils() {}

  public static MnistSettings parseMnistSettingsFile() {
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    try {
      return objectMapper.readValue(MnistSettings.class.getResourceAsStream("/mnistSettings.yaml"), MnistSettings.class);
    } catch (Exception ex) {
      throw new ParseNetworkSettingsException("cannot parse mnistSettings.yaml file", ex);
    }
  }

  public static double[] expectedOutputToVector(int outputInt, int vectorSize) {
    if (outputInt > vectorSize) throw new MnistGeneralException("outputInt cannot be bigger then vectorSize");
    double[] result = new double[vectorSize];
    result[outputInt] = 1.0;
    return result;
  }

  public static int indexOfMaxFromVector(double[] vector) {
    int maxAt = 0;
    for (int i = 0; i < vector.length; ++i) {
      maxAt = vector[i] > vector[maxAt] ? i : maxAt;
    }
    return maxAt;
  }

  public static List<Integer> readLabelFile(String fileName) {
    try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      return stream.map(Integer::parseInt)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new MnistGeneralException("cannot read label file: " + fileName, e);
    }
  }

  public static List<double[]> readVectorFile(String fileName) {
    try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      return stream.map(line -> line.split(","))
          .map(values ->
              Arrays.stream(values).mapToDouble(strNum -> (Double.parseDouble(strNum) / 256.0)).toArray())
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new MnistGeneralException("cannot read label file: " + fileName, e);
    }
  }
}
