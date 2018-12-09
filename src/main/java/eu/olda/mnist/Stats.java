package eu.olda.mnist;

import eu.olda.mnist.network.NetworkSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olda on 09.12.2018
 */
@Getter
@Setter
@ToString
public class Stats {

  private List<TestResult> testResults = new ArrayList<>();
  private List<TestResult> trainResults = new ArrayList<>();

  private MnistSettings mnistSettings;
  private NetworkSettings networkSettings;

  @Getter
  @Setter
  @ToString
  @AllArgsConstructor
  public static class TestResult {
    private int testedCorrectCount;
    private int testedAllCount;

  }

}
