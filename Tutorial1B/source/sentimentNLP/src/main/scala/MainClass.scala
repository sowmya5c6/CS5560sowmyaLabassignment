import java.io.{BufferedReader, FileReader}
import scala.io.Source

/**
 * Created by Mayanka on 23-Jul-15.
 */
object MainClass {

  def main(args: Array[String]) {

    /*val br = new BufferedReader(new FileReader("file.txt"))
    val s = s2.next
    val line = br.readLine*/

    val filename = "file.txt"
    for (line <- Source.fromFile(filename).getLines()) {


      val sentimentAnalyzer: SentimentAnalyzer = new SentimentAnalyzer
      val tweetWithSentiment: TweetWithSentiment = sentimentAnalyzer.findSentiment(line)
      System.out.println(tweetWithSentiment)
    }
  }
}
