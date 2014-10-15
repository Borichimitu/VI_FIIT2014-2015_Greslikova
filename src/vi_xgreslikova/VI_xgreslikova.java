package vi_xgreslikova;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.document.TextField;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TopScoreDocCollector;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;
/**
 *
 * @author Zuzana Greslikova
 */
public class VI_xgreslikova {

    public static void main(String[] args) throws IOException {

        try {

            create_test_sample();

            //if(compare(test_sample,get_result(Autism)) == true) read_data;



            //UNIT TEST
            //String real_result = search("Autism");
            //String expected_result = create_test_sample();
            //if(unit_test(expected_result, real_result)) System.out.println("UNIT TEST PASSED");
            //else System.out.println("UNIT TEST FAILED");            

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void create_test_sample() throws IOException {

        String looking = "Autism";
        String filePath_shortabstract = "input_shortabstracts.ttl";
        String filePath_redirects = "input_redirects.ttl";
        String filePath_categories = "input_categories.ttl";

        try {
            String line;
            Pattern p;
            Matcher matcher;

            //Read and parse input

            //read shortabstract
            FileReader fr_shortabstract = new FileReader(filePath_shortabstract);
            BufferedReader textReader1 = new BufferedReader(fr_shortabstract);

            String REGEX3 = "(?i)/" + looking + ">.*schema#comment>[^\"]*\"([^\"]*)\"";
            String shortabstract = null;// = new ArrayList<String>();

            while ((line = textReader1.readLine()) != null) {

                p = Pattern.compile(REGEX3);
                matcher = p.matcher(line);
                while (matcher.find()) {
                    System.out.println(matcher.group(1).trim());
                    shortabstract = (matcher.group(1).trim());
                }
            }
            textReader1.close();
            fr_shortabstract.close();

            //reading categories
            List<String> categories = new ArrayList<>();
            FileReader fr_categories = new FileReader(filePath_categories);
            BufferedReader textReader2 = new BufferedReader(fr_categories);
            String REGEX1 = "(?i)Category:([^>]*)>";

            while ((line = textReader2.readLine()) != null) {

                p = Pattern.compile(REGEX1);
                matcher = p.matcher(line);
                while (matcher.find()) {
                    System.out.println(matcher.group(1).trim());
                    categories.add(matcher.group(1).trim());
                }
            }
            textReader2.close();
            fr_categories.close();

            //read redirects
            FileReader fr_redirects = new FileReader(filePath_redirects);
            BufferedReader textReader3 = new BufferedReader(fr_redirects);

            String REGEX2 = "(?i)<http://dbpedia.org/resource/([^>]*)>.*wikiPageRedirects[^<]*<http://dbpedia.org/resource/([^>]*)>";
            List<String> redirects = new ArrayList<>();

            while ((line = textReader3.readLine()) != null) {

                p = Pattern.compile(REGEX2);
                matcher = p.matcher(line);
                while (matcher.find()) {
                    System.out.println(matcher.group(1).trim() + "," + matcher.group(2).trim());
                    redirects.add(matcher.group(1).trim() + "," + matcher.group(2).trim());
                }
            }
            textReader3.close();
            fr_redirects.close();

            //OUTPUT
            create_output_xml(looking, shortabstract, categories, redirects);
            //create_output_txt(looking, shortabstract, categories, redirects);

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void create_output_xml(String looking, String shortabstract, List<String> categories, List<String> redirects) throws IOException {

        String fileOut = "output_shortabstract_categories.xml";
        String fileOut_redirects = "output_redirects.xml";

        try {

            FileWriter fw = new FileWriter(fileOut);
            try (BufferedWriter textWriter = new BufferedWriter(fw)) {
                List<String> output = new ArrayList<>();

                output.add("<dbpedia>");
                output.add("<article>");

                output.add("<title>" + looking + "</title>");
                output.add("<short_abstract>" + shortabstract + "</short_abstract>");

                output.add("<categories>");
                output.add("<nmbcat>" + categories.size() + "</nmbcat>");
                for (String l : categories) {
                    output.add("<cat>" + l + "</cat>");
                }
                output.add("</categories>");

//                output.add("<redirects>");
//                output.add("<nmbred>" + redirects.size() + "</nmbred>");
//                for (String l : redirects) {
//                    output.add("<redrct>" + l + "</redrct>");
//                }
//                output.add("</redirects>");

                output.add("</article>");
                output.add("</dbpedia>");

                for (String l : output) {
                    textWriter.write(l);
                    textWriter.newLine();
                }

                textWriter.close();

            }
            fw.close();


            //make redirects output
            FileWriter fw2 = new FileWriter(fileOut_redirects);
            try (BufferedWriter textWriter2 = new BufferedWriter(fw2)) {
                for (String l : redirects) {
                    textWriter2.write(l);
                    textWriter2.newLine();
                }


                textWriter2.close();
            }
            fw2.close();

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }


    }

    public static void create_output_txt(String looking, String shortabstract, List<String> categories, List<String> redirects) throws IOException {

        String fileOut = "output.txt";

        try {

            FileWriter fw = new FileWriter(fileOut);
            try (BufferedWriter textWriter = new BufferedWriter(fw)) {
                List<String> output = new ArrayList<>();

                textWriter.write(looking.hashCode() + "<title:" + looking + ">" + "<shortabstract:" + shortabstract + ">");

                textWriter.write("<categories:");
                for (String l : categories) {
                    textWriter.write(l + ".");
                }
                textWriter.write(">");

                textWriter.write("<redirects:");
                for (String l : redirects) {
                    textWriter.write(l + ".");
                }
                textWriter.write(">");

                textWriter.newLine();
            }

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }


    }

    public static boolean unit_test(String expected_result, String real_result) throws IOException {

        return (expected_result).equals(real_result);

    }
}