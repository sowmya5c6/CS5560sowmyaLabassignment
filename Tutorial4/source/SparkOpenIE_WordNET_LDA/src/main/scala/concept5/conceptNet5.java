package concept5;

/**
 * Created by Mayanka on 27-Jun-16.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.nio.file.Paths;import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;



import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



import java.io.BufferedInputStream;
import java.io.InputStream;


/**
 * This class is the same as the ApacheHttpRestClient2 class, but with
 * fewer try/catch clauses, and fewer comments.
 */
public class conceptNet5 {

    public final static void main(String[] args) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String line1 = null;
        String fileName = "sample1";
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line1 = bufferedReader.readLine()) != null) {
                System.out.println(line1);

// create an empty Annotation just with the given text
                Annotation document = new Annotation(line1);

// run all Annotators on this text
                pipeline.annotate(document);

                // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
                List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

                for (CoreMap sentence : sentences) {
                    // traversing the words in the current sentence
                    // a CoreLabel is a CoreMap with additional token-specific methods
                    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                        System.out.println("\n" + token);

                        // this is the text of the token
                        String word = token.get(CoreAnnotations.TextAnnotation.class);
                        System.out.println("Text Annotation");
                        System.out.println(token + ":" + word);
                        // this is the POS tag of the token

                        String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                        System.out.println("Lemma Annotation");
                        System.out.println(token + ":" + lemma);

                        // Demo finding parts of speech
                        String word1 = lemma;




                        HttpClient httpClient = new DefaultHttpClient();
        String line = "";
        try {
            HttpGet httpGetRequest = new HttpGet("http://conceptnet5.media.mit.edu/data/5.4/search?rel=/r/PartOf&end=/c/en/tree&limit=10");
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

            HttpEntity entity = httpResponse.getEntity();

            byte[] buffer = new byte[1024];
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                int bytesRead = 0;
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                while ((bytesRead = bis.read(buffer)) != -1) {
                    String chunk = new String(buffer, 0, bytesRead);
                    System.out.println(chunk);
                    line += chunk;
                }

                inputStream.close();
            }
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(line);
            JSONObject b = (JSONObject) obj;
            JSONArray ja = (JSONArray) b.get("edges");
            for (int i = 0; i < ja.size(); i++) {
                JSONObject ob = (JSONObject) ja.get(i);
                System.out.println(ob.get("surfaceText"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

                    }
                }
            }
            bufferedReader.close();
        }

        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }




    }
}