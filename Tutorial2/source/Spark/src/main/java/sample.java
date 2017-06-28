
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Scanner;

import java.util.Arrays;
import java.nio.file.Files;
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
import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.String;

import static org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.InputType.file;


public class sample {

    public static void main(String[] args) { Set personSet = new HashSet();
        Set inputs4 = new HashSet();
        Set inputs5 = new HashSet();
        Set inputs6 = new HashSet();
        Set inputs7 = new HashSet();


        SparkConf conf = new SparkConf().setAppName("Sample").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        JavaRDD<String> distFile = sc.textFile("sample.txt");
        JavaRDD<String> lines = sc.textFile("sample.txt");

        String firstLine = lines.first();
        pipeline = new StanfordCoreNLP(props, false);
        String text = "Dollar gains on Greenspan speech.The dollar has hit its highest level against the euro in almost three months after the Federal Reserve head said the US trade deficit is set to stabilise.In late trading in New York, the dollar reached $1.2871 against the euro, from $1.2974 on Thursday. Market concerns about the deficit has hit the greenback in recent months.";
        Annotation document = pipeline.process(text);
//        List<Tuple2<String,String>> inputList = new ArrayList<Tuple2<String,String>>();
        List<String> inputs = new ArrayList<String>();
        List<String> inputs1 = new ArrayList<String>();
        List<String> inputs2 = new ArrayList<String>();
        List<String> inputs3 = new ArrayList<String>();



        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            String lemma = "";

            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                System.out.println("Text Annotation");
                System.out.println(token + ":" + word);
                lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                System.out.println("Lemma Annotation");
                System.out.println(token + ":" + lemma);try {
                    BufferedWriter out = new BufferedWriter(new FileWriter("file.txt",true));
                    out.write(lemma);  //Replace with the string
                    //you are trying to write
                    out.close();
                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }




                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.println("NER");
                System.out.println(token + ":" + ner);

                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                System.out.println("POS");
                System.out.println(token + ":" + pos);
//                ArrayList<String> newOne = " " + lemma;

//                inputList.add(new Tuple2<String,String>(token.toString().substring(0,1), lemma));

                //                System.out.println(lemma);
                inputs.add(lemma);
                inputs1.add(ner);




                   }
        }
        System.out.println(inputs1);

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
        System.out.println("Enter question based on above data");
        Scanner sc1 = new Scanner(System.in);
        String ques = sc1.nextLine();
        while(sc1.hasNext()) {

            if(sc1.nextLine()=="Who will announce the budget on Monday");
            System.out.println("Whitehouse");
            if(sc1.nextLine()=="Where the dollar reached $1.2871 against the euro");
            System.out.println("NEWYORK");


        }


//        System.out.println(inputList);
  }

 }