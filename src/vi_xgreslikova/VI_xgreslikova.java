package vi_xgreslikova;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zuzana Greslikova
 */
public class VI_xgreslikova {

    public static void main(String[] args) throws IOException {

        try {
                   
            //MAIN CODE comming soon :)            
                        
            //String real_result = search("Autism");
                        
            //UNIT TEST
            create_test_sample();
            //String expected_result = create_test_sample();
            //if(unit_test(expected_result, real_result)) System.out.println("UNIT TEST PASSED");
            //else System.out.println("UNIT TEST FAILED");            
            
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void create_test_sample() throws IOException {

        String looking = "Autism";
        String filePath = "input.ttl";

        try {

            FileReader fr = new FileReader(filePath);
            BufferedReader textReader = new BufferedReader(fr);

            String line;
            String REGEX1 = "(?i)Category:([^>]*)>";
            String REGEX2 = "(?i)(<http[^>]*>).*wikiPageRedirects[^<]*(<http[^>]*>)";
            String REGEX3 = "(?i)/" + looking + ">.*schema#comment>[^\"]*\"([^\"]*)\"";

            List<String> categories = new ArrayList<>();
            List<String> redirects = new ArrayList<>();
            String shortabstract = null;// = new ArrayList<String>();

            //Read and parse input
            while ((line = textReader.readLine()) != null) {

                Pattern p = Pattern.compile(REGEX1);
                Matcher matcher = p.matcher(line);
                while (matcher.find()) {
                    System.out.println(matcher.group(1).trim());
                    categories.add(matcher.group(1).trim());
                }

                p = Pattern.compile(REGEX2);
                matcher = p.matcher(line);
                while (matcher.find()) {
                    System.out.println(matcher.group(1).trim());
                    redirects.add(matcher.group(1).trim());
                }

                p = Pattern.compile(REGEX3);
                matcher = p.matcher(line);
                while (matcher.find()) {
                    System.out.println(matcher.group(1).trim());
                    shortabstract = (matcher.group(1).trim());
                }
            }

            //OUTPUT
            create_output_xml(looking, shortabstract, categories, redirects);
            create_output_txt(looking, shortabstract, categories, redirects);

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void create_output_xml(String looking, String shortabstract, List<String> categories, List<String> redirects) throws IOException {

        String fileOut = "output.xml";

        try {

            FileWriter fw = new FileWriter(fileOut);
            BufferedWriter textWriter = new BufferedWriter(fw);
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

            output.add("<redirects>");
            output.add("<nmbred>" + redirects.size() + "</nmbred>");
            for (String l : redirects) {
                output.add("<redrct>" + l + "</redrct>");
            }
            output.add("</redirects>");

            output.add("</article>");
            output.add("</dbpedia>");

            for (String l : output) {
                textWriter.write(l);
                textWriter.newLine();

            }
            
            textWriter.close();

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }


    }
    
    public static void create_output_txt(String looking, String shortabstract, List<String> categories, List<String> redirects) throws IOException {

        String fileOut = "output.txt";

        try {

            FileWriter fw = new FileWriter(fileOut);
            BufferedWriter textWriter = new BufferedWriter(fw);
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
            textWriter.close();

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }


    }
    
     public static boolean unit_test(String expected_result, String real_result) throws IOException {
         
         return (expected_result).equals(real_result); 
         
     }
}