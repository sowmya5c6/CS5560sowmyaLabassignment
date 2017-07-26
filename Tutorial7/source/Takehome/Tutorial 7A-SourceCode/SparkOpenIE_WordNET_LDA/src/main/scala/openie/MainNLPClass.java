package openie;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Quadruple;
import rita.RiWordNet;

import java.io.*;
import java.util.*;

/**
 * Created by Megha Nagabhushan on 7/17/2017.
 */
public class MainNLPClass {

    public static String returnTriplets(String sentence) throws IOException {

        Document doc = new Document(sentence);
        String triplet = "";
        FileWriter fileWriter = new FileWriter("fileWriter", true);
        FileWriter fileSubject = new FileWriter("myClass", true);
        FileWriter fileTriplets = new FileWriter("TripletsNew", true);
        FileWriter fileData = new FileWriter("DataProperties", true);
        FileWriter fileIndividual = new FileWriter("Individuals", true);
        FileWriter temp = new FileWriter("temp", true);
        FileWriter myClass = new FileWriter("Class", true);
        FileWriter objectFile = new FileWriter("ObjectProperties", true);

        ArrayList<String> subjectList = new ArrayList<>();
        ArrayList<String> objectList = new ArrayList<>();
        ArrayList<String> predicateList = new ArrayList<>();
        HashSet<String> predicateValues = new HashSet();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> objects = new ArrayList<>();

        //creating a hash set for Class file
        HashSet<String> classSet = new HashSet<>();
        //adding our predefined classes to define the schema
        if (classSet.size() == 0) {
            classSet.add("EDUCATION");
            classSet.add("LEVEL");
        }


        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            Collection<Quadruple<String, String, String, Double>> l = sent.openie();//.iterator();

            for (Quadruple x : l) {

                //retrieving subject from the quadruple and saving it in a list
                String subject = (String) x.first();
                subjectList.add(subject);

                //retrieving object from the quadruple and saving it in a list
                String object = (String) x.third();
                objectList.add(object);

                //retrieving predicates from the quadruple
                String predicate = (String) x.second();

                //checking if a subject has NER and adding the NER values into the class set and adding them as intance in Indiviaduals File
                String subjectNER = returnNER(subject);
                if (!subjectNER.equals("O")) {
                    fileIndividual.write(subjectNER + "," + subject.replaceAll(" .*$", "").replaceAll(" .*$", "") + "\n");
                    classSet.add(subjectNER);
                } else {
                    if (subject.contains("schools") || subject.contains("universities") || subject.contains("institutions")) {
                        fileIndividual.write("EDUCATION," + subject.replaceAll(" .*$", "") + "\n");
                    } else if (subject.contains("levels")) {
                        fileIndividual.write("LEVEL," + subject.replaceAll(" .*$", "") + "\n");
                    }
                }

                //checking if a object has NER and adding the NER values into the class set and adding them as intance in Indiviaduals File
                String objectNER = returnNER(object);
                if (!objectNER.equals("O")) {
                    fileIndividual.write(objectNER + "," + object.replaceAll(" .*$", "") + "\n");
                    classSet.add(objectNER);
                } else {
                    if (object.contains("schools") || object.contains("universities") || object.contains("institutions")) {
                        fileIndividual.write("EDUCATION," + object.replaceAll(" .*$", "") + "\n");
                    } else if (object.contains("levels")) {
                        fileIndividual.write("LEVEL," + object.replaceAll(" .*$", "") + "\n");
                    }
                }


                //writing out the Class file
                for (String s : classSet) {
                    myClass.write(s + "\n");
                }


                //populating objectProperties file
                if (!subjectNER.equals("O") && !objectNER.equals("O")) {
                    objectFile.write(predicate + "," + subjectNER + "," + objectNER + ",Func\n");

                }

                if ((subject.contains("schools") || subject.contains("universities") || subject.contains("institutions")) &&
                        (object.contains("schools") || object.contains("universities") || object.contains("institutions"))) {

                    objectFile.write(predicate + ",EDUCATION,EDUCATION,Func\n");

                }
                if ((subject.contains("schools") || subject.contains("universities") || subject.contains("institutions")) &&
                        (object.contains("levels"))) {

                    objectFile.write(predicate + ",EDUCATION,LEVEL,Func\n");

                }
                if ((subject.contains("levels")) &&
                        (object.contains("schools") || object.contains("universities") || object.contains("institutions"))) {

                    objectFile.write(predicate + ",LEVEL,EDUCATION,Func\n");

                }
                if ((subject.contains("levels")) &&
                        (object.contains("levels"))) {

                    objectFile.write(predicate + ",LEVEL,LEVEL,Func\n");

                }
                //end of the objectProperty File

                //adding realtedTo triplets


                //creating dataproperties
                for (String str : classSet) {
                    fileData.write("realtedTo," + str + ",string\n");
                }

                if (!subjectNER.equals("O")) {
                    subjectList.add(subject);
                }

                if (!subjectNER.equals("O")) {
                    subjects.add(subject);
                }

                if (!objectNER.equals("O")) {
                    objects.add(object);
                }

                tripletGeneration(subject, predicate, object);


                triplet = subject + predicate + object;

            }


            //removing stopwords and duplicates for the subject
            HashSet<String> subjectSet = stopWordRemoving(subjectList);


            HashSet<String> synonymSet = new HashSet<>();
            for (String str : subjectSet) {
                synonymSet = getSynonyms(str);
                for (String syn : synonymSet) {
                    fileWriter.write("relatedTo" + "," + str + "," + syn + ",Func\n");
                }
            }

            HashSet<String> subjectsSet = stopWordRemoving(subjects);


            HashSet<String> synonymSubjectSet = new HashSet<>();
            for (String str : subjectsSet) {
                synonymSubjectSet = getSynonyms(str);
                for (String syn : synonymSubjectSet) {
                    fileTriplets.write(str + ",relatedTo" + syn + ",Data\n");
                }
            }

            HashSet<String> objectsSet = stopWordRemoving(objects);


            HashSet<String> synonymObjectSet = new HashSet<>();
            for (String str : objectsSet) {
                synonymObjectSet = getSynonyms(str);
                for (String syn : synonymObjectSet) {
                    fileWriter.write(str + ",relatedTo" + syn + ",Data\n");
                }
            }


            //creating data properties
            /*for(String str : subjectSet){
                fileData.write("hasNer,"+str+",string\n");
            }*/


        }
        temp.close();
        fileData.close();
        fileIndividual.close();
        fileSubject.close();
        fileTriplets.close();
        fileWriter.close();
        myClass.close();
        objectFile.close();
        stripDuplicatesFromFile("ObjectProperties");
        stripDuplicatesFromFile("myClass");
        stripDuplicatesFromFile("Individuals");
        stripDuplicatesFromFile("DataProperties");
        stripDuplicatesFromFile("Class");
        return triplet;

    }

    public static String returnNER(String word) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(word);
        pipeline.annotate(document);
        String stringNER = "";
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                stringNER = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            }
        }
        return stringNER;
    }

    public static HashSet<String> stopWordRemoving(ArrayList arrayList) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("data/stopwords.txt"));

        for (String line = br.readLine(); line != null; line = br.readLine()) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).equals(line)) {
                    arrayList.remove(i);
                }
            }
        }
        HashSet<String> subjectSet = new HashSet<String>(arrayList);
        return subjectSet;
    }

    public static HashSet<String> getSynonyms(String word) {
        RiWordNet wordnet = new RiWordNet("/home/sowmya/Downloads/WordNet-3.0");
        String[] poss = wordnet.getPos(word);
        HashSet<String> synonym = new HashSet<>();
        for (int j = 0; j < poss.length; j++) {
            System.out.println("\n\nSynonyms for " + word + " (pos: " + poss[j] + ")");
            String[] synonyms = wordnet.getAllSynonyms(word, poss[j], 10);
            for (int i = 0; i < synonyms.length; i++) {
                synonym.add(synonyms[i]);
            }
        }
        return synonym;
    }

    public static void stripDuplicatesFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Set<String> lines = new HashSet<String>(10000); // maybe should be bigger
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String unique : lines) {
            writer.write(unique);
            writer.newLine();
        }
        writer.close();
    }

    public static void tripletGeneration(String subject, String predicate, String object) throws IOException {

        FileWriter fileWriter = new FileWriter("TripletGenerated", true);
        String subjectNER = returnNER(subject);
        String objectNER = returnNER(object);
        HashSet<String> subjectSynonym = new HashSet<>();
        HashSet<String> objectSynonym = new HashSet<>();
        if (subject.contains("school")) {
            String x = subject.replaceAll(".+", "schools");
            subject = x;
        }
        if (subject.contains("institution")) {
            String x = subject.replaceAll(".+", "institutions");
            subject = x;
        }
        if (subject.contains("university")) {
            subject = subject.replaceAll(".+", "universities");
        }
        if (object.contains("school")) {
            object = object.replaceAll(".+", "schools");
        }
        if (object.contains("institution")) {
            object = object.replaceAll(".+", "institutions");
        }
        if (object.contains("country")) {
            object = object.replaceAll(".+", "countries");
        }
        if (object.contains("academy")) {
            object = object.replaceAll(".+", "academies");
        }


        if (!subjectNER.equals("O") && !objectNER.equals("O")) {
            if (predicate.contains("was") || predicate.contains("at") || predicate.contains("would")) {
                fileWriter.write(subject + "," + predicate + "," + object + ",Obj\n");
            }
        }


        if (!subjectNER.equals("O") && !objectNER.equals("O")) {
            if (predicate.contains("was") || predicate.contains("at") || predicate.contains("would")) {
                fileWriter.write(subject + "," + predicate + "," + object + ",Obj\n");
            }
        }


        if ((subject.contains("schools") || subject.contains("institutions") || subject.contains("institutions")) &&
                (object.contains("schools") || object.contains("universities") || object.contains("institutions"))) {
            if (predicate.contains("was") || predicate.contains("at") || predicate.contains("would")) {
                fileWriter.write(subject + "," + predicate + "," + object + ",Obj\n");
            }
        }
        if ((subject.contains("schools") || subject.contains("institutions") || subject.contains("universities")) &&
                (object.contains("levels"))) {
            if (predicate.contains("was") || predicate.contains("at") || predicate.contains("would")) {
                fileWriter.write(subject + "," + predicate + "," + object + ",Obj\n");
            }
        }
        if ((subject.contains("levels")) &&
                (object.contains("schools") || object.contains("institutions") || object.contains("universities"))) {
            if (predicate.contains("was") || predicate.contains("at") || predicate.contains("would")) {
                fileWriter.write(subject + "," + predicate + "," + object + ",Obj\n");
            }
        }
        if ((subject.contains("levels")) &&
                (object.contains("levels"))) {
            if (predicate.contains("was") || predicate.contains("at") || predicate.contains("would")) {
                fileWriter.write(subject + "," + predicate + "," + object + ",Obj\n");
            }
        }

        if (predicate.contains("emphasiz")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",emphasizes," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("feature")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",features," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("determine")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",determines," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("defeat")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",defeated," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("play")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",plays," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("finish")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",finished," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("in")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",isIn," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("with")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",isWith," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("join")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",join," + object.replaceAll(" .*$", "") + ",Data\n");
        } else if (predicate.contains("found")) {
            fileWriter.write(subject.replaceAll(" .*$", "") + ",found," + object.replaceAll(" .*$", "") + ",Data\n");
        }


        subjectSynonym = getSynonyms(subject);
        System.out.println("printing sub synonyms0");
        for (String str : subjectSynonym) {
            fileWriter.write(subject + ",relatedTo," + str.replaceAll(" .*$", "") + ",Data\n");
        }


        System.out.println("inside obj ner");
        objectSynonym = getSynonyms(object);
        for (String str : objectSynonym) {
            fileWriter.write(subject + ",relatedTo," + str.replaceAll(" .*$", "") + ",Data\n");
        }

        stripDuplicatesFromFile("TripletGenerated");
        fileWriter.close();
    }

}