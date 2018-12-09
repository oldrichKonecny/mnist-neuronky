import eu.olda.mnist.MnistService;

/**
 * Created by olda on 01.11.2018
 */
public class Initializer {

  public static void main(String[] args) {
    MnistService mnistService = new MnistService();

    mnistService.trainMnistNetwork();

    mnistService.testMnistNetwork();

    System.out.println(mnistService.getStats());
    System.out.println("Hello World.");
  }

}
