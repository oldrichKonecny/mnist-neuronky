package eu.olda.mnist.network;

import eu.olda.mnist.exceptions.InvalidArgumentException;
import eu.olda.mnist.utils.MathUtils;
import eu.olda.mnist.utils.NetworkUtils;
import lombok.Getter;

import java.util.function.BiConsumer;

/**
 * Created by olda on 02.12.2018
 */
public class NetworkService {

  private Network network;
  @Getter
  private NetworkSettings settings;

  public void initNetwork() {
    settings = NetworkUtils.parseNetworkSettingsFile();
    checkNetworkSettings(settings);
    network = new Network();
    //set size of network
    network.setWeights(new double[settings.getNetworkSize()][][]);
    network.setBias(new double[settings.getNetworkSize()][]);
    network.setCurrentState(new double[settings.getNetworkSize()][]);
    network.setError(new double[settings.getNetworkSize()][]);

    //set size of layers
    for (int i = 0; i < settings.getNetworkSize(); ++i) {
      network.getCurrentState()[i] = new double[settings.getLayersSizes()[i]];
      network.getError()[i] = new double[settings.getLayersSizes()[i]];

      network.getBias()[i] = NetworkUtils.makeRandomArray(settings.getLayersSizes()[i], settings.getBiasLowerBound(), settings.getBiasUpperBound());
      //no weights for input layer neurons
      if (i > 0) network.getWeights()[i] = NetworkUtils.makeRandom2DimArray(settings.getLayersSizes()[i], settings.getLayersSizes()[i-1], settings.getWeightLowerBound(), settings.getWeightUpperBound());
    }
  }

  public double[] getCurrentOutput() {
    return network.getCurrentState()[settings.getNetworkSize()-1];
  }

  /**
   * calculate one walk through network
   */
  public double[] calculateOutput(double[] input) {
    checkInput(input);
    //set input vector as input layer
    network.getCurrentState()[0] = input;

    BiConsumer<Integer, Integer> fce = (layerIndex, neuronIndex) -> {
      double value = calculateSumForSingleNeuron(layerIndex, neuronIndex);
      network.getCurrentState()[layerIndex][neuronIndex] = MathUtils.getActivationFunction(settings.getActivationFunctionType()).apply(value);
    };
    iterateNetwork(1, fce);

    //return output layer
    return getCurrentOutput();
  }

  /**
   * sum previous layer neuron result * their corresponding weights + bias
   */
  private double calculateSumForSingleNeuron(int layerIndex, int neuronIndex) {
    double value = network.getBias()[layerIndex][neuronIndex];
    for (int prevLayerNeuronIndex = 0; prevLayerNeuronIndex < settings.getLayersSizes()[layerIndex-1]; ++prevLayerNeuronIndex) {
      value += network.getWeights()[layerIndex][neuronIndex][prevLayerNeuronIndex] * network.getCurrentState()[layerIndex-1][prevLayerNeuronIndex];
    }
    return value;
  }

  public double[] calculateAndUpdate(double[] input, double[] expectedOutput) {
    checkInput(input);
    checkExpectedOutput(expectedOutput);

    calculateOutput(input);
    backPropagateError(expectedOutput);
    updateWeightsAndBias();

    return getCurrentOutput();
  }

  private void backPropagateError(double[] expectedOutput) {
    BiConsumer<Integer, Integer> fce = (layerIndex, neuronIndex) -> {
      double error = 0;
      //if it is in output layer don't sum next layer weights..
      if (layerIndex == settings.getNetworkSize()-1) {
        error = network.getCurrentState()[layerIndex][neuronIndex] - expectedOutput[neuronIndex];
      } else {
        for (int nextLayerNeuronIndex = 0; nextLayerNeuronIndex < settings.getLayersSizes()[layerIndex+1]; ++nextLayerNeuronIndex) {
          error += network.getWeights()[layerIndex+1][nextLayerNeuronIndex][neuronIndex] * network.getError()[layerIndex+1][nextLayerNeuronIndex];
        }
      }
      network.getError()[layerIndex][neuronIndex] = error * MathUtils.neuronDerivation(network.getCurrentState()[layerIndex][neuronIndex]);
    };

    backIterateNetwork(fce);
  }

  private void updateWeightsAndBias() {
    BiConsumer<Integer, Integer> fce = (layerIndex, neuronIndex) -> {
      double value = (-1) * settings.getLearningRate() * network.getError()[layerIndex][neuronIndex];
      network.getBias()[layerIndex][neuronIndex] += value;
      //iterate weights
      for (int prevNeuronIndex = 0; prevNeuronIndex < settings.getLayersSizes()[layerIndex - 1]; ++prevNeuronIndex) {
        network.getWeights()[layerIndex][neuronIndex][prevNeuronIndex] += value * network.getCurrentState()[layerIndex - 1][prevNeuronIndex];
      }
    };

    iterateNetwork(1, fce);
  }

  /**
   * check input for invalid size or other weird stuff
   */
  private void checkInput(double[] input) {
    if (input.length != settings.getInputSize()) {
      throw new InvalidArgumentException("size of input is: " + input.length + " , but expected: " + settings.getInputSize());
    }
  }

  private void checkExpectedOutput(double[] output) {
    if (output.length != settings.getOutputSize()) {
      throw new InvalidArgumentException("size of expected output is: " + output.length + " , but expected: " + settings.getOutputSize());
    }
  }

  private void checkNetworkSettings(NetworkSettings settings) {
    if (settings.getLayersSizes() == null || settings.getLayersSizes().length < 2) {
      throw new InvalidArgumentException("Network needs to have at least 3 layers");
    }
    for (int layer : settings.getLayersSizes()) {
      if (layer < 1) {
        throw new InvalidArgumentException("Number of neurons in each layer must be more then 0");
      }
    }
  }

  private void iterateNetwork(int startLayer, BiConsumer<Integer, Integer> biConsumer) {
    for (int layerIndex = startLayer; layerIndex < settings.getNetworkSize(); ++layerIndex) {
      for (int neuronIndex = 0; neuronIndex < settings.getLayersSizes()[layerIndex]; ++neuronIndex) {
        biConsumer.accept(layerIndex, neuronIndex);
      }
    }
  }

  private void backIterateNetwork(BiConsumer<Integer, Integer> biConsumer) {
    for (int layerIndex = settings.getNetworkSize()-1; layerIndex > 0; --layerIndex) {
      for (int neuronIndex = 0; neuronIndex < settings.getLayersSizes()[layerIndex]; ++neuronIndex) {
        biConsumer.accept(layerIndex, neuronIndex);
      }
    }
  }
}
