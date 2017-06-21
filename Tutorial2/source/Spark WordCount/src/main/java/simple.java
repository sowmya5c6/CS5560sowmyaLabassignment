import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Package;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.*;
import java.lang.StringBuilder;

/**
 * Created by sowmya on 6/20/17.
 */
public class simple {






        public static void main(String[] args)
        {
            Properties props = new Properties();
            props.put("annotators", "tokenize, ssplit, pos, lemma");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

            pipeline = new StanfordCoreNLP(props, false);
            String text = "This is a question answering system. The question is from Quora";
            Annotation document = pipeline.process(text);

            for(CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class))
            {
                for(CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class))
                {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                    System.out.println(lemma);
                }
            }
        }
    }



