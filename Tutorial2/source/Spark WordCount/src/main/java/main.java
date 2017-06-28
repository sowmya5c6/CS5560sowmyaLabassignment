

import java.util.Arrays;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.SparkContext;

import org.apache.spark.SparkConf;

import static edu.stanford.nlp.util.logging.RedwoodConfiguration.Handlers.output;


public class main {
    public static void main(String[] args) throws Exception {

        SparkConf conf = new SparkConf().setAppName("Simple");
        conf.setMaster("local");
        conf.setAppName("my app");
        JavaSparkContext sc = new JavaSparkContext(conf);


        // Parallelized with 2 partitions
        JavaRDD<String> rddX = sc.parallelize(
                Arrays.asList("Joseph", "Jimmy", "Tina",
                        "Thomas", "James", "Cory",
                        "Christine", "Jackeline", "Juan"), 3);

        JavaPairRDD<Character, Iterable<String>> rddY = rddX.groupBy(word -> word.charAt(0));

        System.out.println(rddY.collect());

    }
}


