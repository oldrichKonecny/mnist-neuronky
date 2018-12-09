package eu.olda.mnist;

import eu.olda.mnist.exceptions.MnistGeneralException;
import eu.olda.mnist.network.NetworkService;
import eu.olda.mnist.utils.MnistUtils;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.List;
import java.util.logging.Level;

/**
 * Created by olda on 09.12.2018
 */
@Log
public class MnistService {

  private NetworkService networkService;
  private MnistSettings mnistSettings;
  @Getter
  private Stats stats;

  public MnistService() {
    initMnistService();
  }

  private void initMnistService() {
    networkService = new NetworkService();
    networkService.initNetwork();
    mnistSettings = MnistUtils.parseMnistSettingsFile();

    stats = new Stats();
    stats.setMnistSettings(mnistSettings);
    stats.setNetworkSettings(networkService.getSettings());
  }

  public void trainMnistNetwork() {
    DataSet fullDataSet = getTrainDataSet();
    double[] firstVec = fullDataSet.getInp(0);
    int firstLab = fullDataSet.getExpRes(0);

    for (int loop = 0; loop < mnistSettings.getTrainLoops(); ++loop) {
      int correctResults = 0;
      for (int trainData = 0; trainData < fullDataSet.getSize(); ++trainData) {
        int expected = fullDataSet.getExpRes(trainData);
        double[] resVec = networkService.calculateAndUpdate(fullDataSet.getInp(trainData), MnistUtils.expectedOutputToVector(expected, 10));
        int result = MnistUtils.indexOfMaxFromVector(resVec);
        if (expected == result) ++correctResults;
      }
      stats.getTrainResults().add(new Stats.TestResult(correctResults, fullDataSet.getSize()));
      log.log(Level.INFO, "finished batch number :" + loop + " with correctResults: " + correctResults + " out of: " + fullDataSet.getSize());
    }
  }

  public void testMnistNetwork() {
    DataSet testDataSet = getTestDataSet();

    int correctResults = 0;
    for (int i = 0; i < testDataSet.getSize(); ++i) {
      int result = MnistUtils.indexOfMaxFromVector(networkService.calculateOutput(testDataSet.getInp(i)));
      int expectedResult = testDataSet.getExpRes(i);
      if (result == expectedResult) ++correctResults;
    }
    stats.getTestResults().add(new Stats.TestResult(correctResults, testDataSet.getSize()));
  }

  private DataSet getTrainDataSet() {
    return parseCsvToDataSet(mnistSettings.getTrainSetVectorFile(), mnistSettings.getTrainSetLabelFile());
  }

  private DataSet getTestDataSet() {
    return parseCsvToDataSet(mnistSettings.getTestVectorFile(), mnistSettings.getTestLabelFile());
  }

  private DataSet parseCsvToDataSet(String inputVectorFile, String labelsFile) {
    log.info("Start creating DataSet");
    DataSet dataSet = new DataSet();
    log.info("getting label file: " + labelsFile);
    List<Integer> labels = MnistUtils.readLabelFile(labelsFile);

    log.info("getting vector file: " + inputVectorFile);
    List<double[]> vectors = MnistUtils.readVectorFile(inputVectorFile);
    if (labels.size() != vectors.size()) throw new MnistGeneralException("labelFile.size != vectorFile.size");
    dataSet.setExpectedResult(labels);
    dataSet.setInputs(vectors);
    dataSet.setSize(labels.size());
    log.info("finished creating DataSet with size: " + dataSet.getSize());
    return dataSet;
  }
}
