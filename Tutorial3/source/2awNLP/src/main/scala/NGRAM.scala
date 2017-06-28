import java.io.{File, FileOutputStream, PrintWriter}
import java.nio.file._
import java.io.OutputStreamWriter
import java.io.BufferedWriter

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source
/**
  * Created by Mayanka on 19-06-2017.
  */
object NGRAM {

  def main(args: Array[String]): Unit = {

    val bufferedSource = Source.fromFile("data/sample1")
    for (line <- bufferedSource.getLines) {
      println(line.toUpperCase)


      val a = getNGrams(line, 2)

      a.foreach(f => println(f.mkString(" ")))




         }
    bufferedSource.close



  def getNGrams(sentence: String, n:Int): Array[Array[String]] = {

    val words = sentence
    val ngrams = words.split(' ').sliding(n)
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("file2",true)))
    for (x <- ngrams) {
      writer.write(x + "\n")  // however you want to format it
    }
    writer.close()

    ngrams.toArray


  }

  }

}


