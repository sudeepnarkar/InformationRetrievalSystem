import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * Created by sudeep on 10/19/16.
 */
public class supportingClass1 {


    public supportingClass1() {
        this.collectionFrequency_map = new TreeMap<>();
    }

    public TreeMap<String, Integer> getCollectionFrequency_map() {
        return collectionFrequency_map;
    }

    public void setCollectionFrequency_map(TreeMap<String, Integer> collectionFrequency_map) {
        this.collectionFrequency_map = collectionFrequency_map;
    }

    private TreeMap<String,Integer> collectionFrequency_map;
  //  private static  TreeMap<String,Integer> documentFrequency_map = new TreeMap<String,Integer>();
    private static int offset;





    public void writeToFile(File file, String name)
    {
        try
        {

            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
         //   System.out.println("Printing key values pairs");
            for (String key : collectionFrequency_map.keySet())
            {
                pw.println(key+" "+collectionFrequency_map.get(key));
            //   System.out.println(key+ " " + collectionFrequency_map.get(key));
            }

           // pw.println("Number of terms :" + collectionFrequency_map.keySet().size());

            pw.flush();
            pw.close();
            fos.close();

        }
        catch (Exception e)
        {
            e.getMessage();
        }

    }


    public static String removeParenthesis(String str)
    {
        if (str.contains("(")||str.contains(")")||str.contains("[")||str.contains("]")) // check if parenthesis exist
        {
            if(str.startsWith("(")||str.startsWith(")")||str.startsWith("[")||str.startsWith("]"))
            {
                str = str.substring(1, str.length());
            }
            if (str.endsWith("(")||str.endsWith(")")||str.endsWith("[")||str.endsWith("]"))
            {
                str = str.substring(0, str.length() - 1);

            }
        }

        return str;
    }

    public static String removeQuotes(String str)
    {
        if (str.contains("\"")||str.contains("\'")) // check if double quotes,single quotes
        {
            if(str.startsWith("\"")||str.startsWith("\'"))
            {
                str = str.substring(1, str.length());
            }
            if (str.endsWith("\"")||str.endsWith("\'"))
            {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }


    public  TreeMap tokenize(File file) throws Exception
    {
        try
        {
            FileReader file1 = new FileReader(file);
            BufferedReader br = new BufferedReader(file1);
            String line;
            String[] tokens = new String[200];
            int ctr;


           // System.out.println("Here");

            //  reviews obj= new reviews();


            while ((line = br.readLine()) != null)
            {

                line = line.toLowerCase();
                tokens = line.split("[-\\s]");


                //System.out.println("Line is :"+line);
                for(int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].replaceAll("[^A-Za-z0-9]+", "");
                }
                ArrayList<String> reviewString = new ArrayList<String>(Arrays.asList(tokens));

                //reviewString.removeAll(toRemove);

                removeStopWords(reviewString);
                // removing comma, a period, question mark, colon, semicolon, or an exclamation mark followed by space.
                for (int i = 0; i < reviewString.size(); i++)
                {

                    String str = reviewString.get(i);
                    int len = str.length();


                    //Stemmming involving  plural and third person

                    if (str.endsWith("ies") && !(str.endsWith("eies") || str.endsWith("aies")))
                    {
                        int index = str.lastIndexOf("ies");
                        str = str.substring(0, index) + "y";
                    }

                    if (str.endsWith("es") && !(str.endsWith("aes") || str.endsWith("ees") || str.endsWith("oes")))
                    {
                        int index = str.lastIndexOf("es");
                        str = str.substring(0, index) + "e";
                    }

                    if (str.endsWith("s") && !(str.endsWith("us") || str.endsWith("ss")))
                    {
                        int index = str.lastIndexOf("s");
                        str = str.substring(0, index) + "";
                    }

                    if (str.length() == 1)
                    {
                        reviewString.remove(i);
                        i--;
                    }
                    else
                    {
                        reviewString.set(i, str);

                    }
                }

              //  reviewString.removeAll(toRemove);

                // Inserting the values in the treemap

                for(int i=0;i<reviewString.size();i++)
                {


                    if (collectionFrequency_map.containsKey(reviewString.get(i)))
                    {
                        Integer value = collectionFrequency_map.get(reviewString.get(i));
                        if (value == null)
                            value = 0;
                        value++;
                        collectionFrequency_map.put(reviewString.get(i), value);
                    }
                    else if (!reviewString.get(i).equals(""))
                    {
                        collectionFrequency_map.put(reviewString.get(i), 1);
                    }

                }
            }
        }
        catch (Exception e)
        {
            // System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return collectionFrequency_map;

    }

    private void removeStopWords(ArrayList<String> reviewString) {

        ArrayList<String> toRemove = new ArrayList<String>();
        toRemove.add("and");
        toRemove.add("an");
        toRemove.add("by");
        toRemove.add("from");
        toRemove.add("of");
        toRemove.add("the");
        toRemove.add("with");
        toRemove.add("in");
        toRemove.add("for");
        toRemove.add("hence");
        toRemove.add("of");
        toRemove.add("with");
        toRemove.add("within");
        toRemove.add("who");
        toRemove.add("when");
        toRemove.add("where");
        toRemove.add("was");
        toRemove.add("why");
        toRemove.add("how");
        toRemove.add("whom");
        toRemove.add("have");
        toRemove.add("had");
        toRemove.add("has");
        toRemove.add("not");
        toRemove.add("but");
        toRemove.add("do");
        toRemove.add("does");
        toRemove.add("done");

        reviewString.removeAll(toRemove);

    }
}



