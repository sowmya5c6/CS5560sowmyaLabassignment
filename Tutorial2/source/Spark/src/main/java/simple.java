import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;



import java.util.Arrays;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.SparkContext;



import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class simple {

    public static void main(String[] args)
    {
                SparkConf conf = new SparkConf().setAppName("Sample").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        pipeline = new StanfordCoreNLP(props, false);
        String text = "This is a question answering system. The question is from quora";
        Annotation document = pipeline.process(text);
//        List<Tuple2<String,String>> inputList = new ArrayList<Tuple2<String,String>>();
        List<String>inputs= new ArrayList<String>();
        for(CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class))
        {
            String lemma="";
            for(CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class))
            {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
//                ArrayList<String> newOne = " " + lemma;

//                inputList.add(new Tuple2<String,String>(token.toString().substring(0,1), lemma));

                //                System.out.println(lemma);
                inputs.add(lemma);

            }
        }
        String[] newArray = new String[inputs.size()];
        newArray = inputs.toArray(newArray);


//for(int i=0;i<inputList.toArray().length;i++){
        JavaRDD<String> rddX = sc.parallelize(
                Arrays.asList(
                        newArray
                ), 6);
//    JavaPairRDD<String,String> pairRDD = sc.parallelizePairs(inputList);
//    pairRDD.mapToPair(tuple -> tuple);


        JavaPairRDD<Character, Iterable<String>> rddY = rddX.groupBy(word -> word.charAt(0));

        System.out.println(rddY.collect());

    }
//        System.out.println(inputList);


}