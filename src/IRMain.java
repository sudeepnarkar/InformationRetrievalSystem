import java.io.*;
import java.util.*;

/**
 * Created by sudeep on 9/21/16.
 */
public class IRMain
{

    private static ArrayList<File> fileList = new ArrayList<File>();
    private  int document_frequency;
    private static Map<Integer,Map> mapOfTokenize = new HashMap<>();
    private static List<PostingList> listOfPostingListObjects = new ArrayList<>();
    private static int totalTermCount=0;
    private static int postListCount=0;


    public static String strTokens[];
    public static String tokens[];
    public static File docTable;
    public static File total;
    public static File dictionaryFile;
    public static File postingList;


    public static void listFilesForFolder(String directoryName, ArrayList<File> fileList)
    {
        File directory = new File(directoryName);

        File [] fList = directory.listFiles();

        for (File file:fList)
        {
            if(file.isFile())
            {
                fileList.add(file);
            }
            else if (file.isDirectory())
            {
                listFilesForFolder(file.getAbsolutePath(), fileList);
            }

        }

    }


    public static void main(String[] args) throws Exception
    {
        try
        {
            File file = new File("docsTable.csv");

            String directoryName = args[0];
            listFilesForFolder(directoryName,fileList);

            File fInput;
            String name;

            dictionary dict = new dictionary();

            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw =  new PrintWriter(fos);

            for (int i=0;i<fileList.size();i++)
            {
                name = fileList.get(i).getName();
                //   System.out.println(i+" "+name);
                //     System.out.println(fileList.get(i).getAbsolutePath());
                fInput= fileList.get(i);
                //FileReader file = new FileReader(fInput);
                DocsTable doc = new DocsTable();
                supportingClass1 obj = new supportingClass1();

                TreeMap<String,Integer> map=obj.tokenize(fInput);

                mapOfTokenize.put(i+1,map);

                doc.generate(fInput,obj,i); // populate the docTable

                writeDocFile(doc, file, pw, fos);

                dict.generate(map,i); //generate dictionary table

            }
            fos.close();
            pw.close();

            for(Map.Entry<String,Integer> entry :dictionary.dict_collectionFrequency_Map.entrySet()) {

                totalTermCount+= entry.getValue();


            }

            writeTotalFile(totalTermCount,new File("total.txt"));


            generatePostingList(); // generate Posting List

            writedictionary(dict, new File("dictionary.csv"));

            writetoPostingList(listOfPostingListObjects,new File("postings.csv"));

            // part 3 docrank..

            //Reading query input

            // Declaring files by hardcoding the file paths
            docTable = new File(" path of docsTable.csv");
            total = new File("  path of total.txt");
            dictionaryFile= new File(" path of dictionary.csv");
            postingList = new File(" path of postings.csv");

            Scanner reader = new Scanner(System.in);  // Reading from System.in
            System.out.println("Enter the query :");
            String query = reader.nextLine();

            while(!(query.equals("EXIT")))
            {
                query = query.toLowerCase();


                // tokenizing query input
                tokens = query.split("[-\\s]");


                //Regex to filter out unwanted characters
                for (int i = 0; i < tokens.length; i++) {
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

                    // Stemming single letter if any

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

                String []finalTokens = new String[reviewString.size()];
                finalTokens= reviewString.toArray(finalTokens);
                DocScore docScore = new DocScore();
                docScore.getDocScore(finalTokens, dictionaryFile, docTable, postingList, total);
                System.out.println("Enter the query :");
                query = reader.nextLine();

            }

            System.out.println("EXITING...");

        }

        catch(Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writetoPostingList(List<PostingList> listOfPostingListObjects, File file)
    {

        FileOutputStream fos = null;
        PrintWriter pw= null;
        try
        {
            fos = new FileOutputStream(file);
            pw =  new PrintWriter(fos);


            for (int i = 0; i <listOfPostingListObjects.size() ; i++)
            {

                pw.println((listOfPostingListObjects.get(i).getDocId())+","+listOfPostingListObjects.get(i).getTf());
            }

        }
        catch (Exception e)
        {
            e.getMessage();
            e.printStackTrace();
        }

        finally {
            pw.close();
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private static void writeDocFile(DocsTable doc, File file, PrintWriter pw, FileOutputStream fos) {


        try
        {

            pw.println(doc.getDoc_number()+","+doc.getHeadline()+","+doc.getDoc_length()+","+doc.getSnippet()+","+doc.getDoc_path());

            pw.flush();
        }
        catch (Exception e)
        {
            e.getMessage();
            e.printStackTrace();
        }

    }

    private static void writedictionary(dictionary dict, File file)   {


        FileOutputStream fos = null;
        PrintWriter pw= null;
        try
        {
            fos = new FileOutputStream(file);
            pw =  new PrintWriter(fos);
            //    pw.println("term,cf,df,offset");
            int i = 0;
            for (Map.Entry<String,Integer> entry : dictionary.dict_documentFrequency_Map.entrySet())  {

                String term=entry.getKey();
                int cf= dictionary.dict_collectionFrequency_Map.get(term);
                int df = dictionary.dict_documentFrequency_Map.get(term);
                //int offset=dictionary.dict_offsetMap.get(term);

                pw.println(term+","+cf+","+df+","+i);
                pw.flush();
                i += dictionary.dict_documentFrequency_Map.get(term);
            }

        }
        catch (Exception e)
        {
            e.getMessage();
            e.printStackTrace();
        }

        finally {
            pw.close();
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    private static void generatePostingList() {

        for(Map.Entry<String,Integer> entry : dictionary.dict_collectionFrequency_Map.entrySet()) {

            for (int i = 0; i <entry.getValue() ; i++) {
                PostingList pl = new PostingList();
                int j = 0;
                if(dictionary.dict_docListMap.containsKey(entry.getKey()) && i < dictionary.dict_docListMap.get(entry.getKey()).size())
                    j = dictionary.dict_docListMap.get(entry.getKey()).get(i);


                pl.setDocId(j);
                Map map_temp = null;
                if(dictionary.dict_docListMap.containsKey(entry.getKey()) && i < dictionary.dict_docListMap.get(entry.getKey()).size())
                    map_temp = mapOfTokenize.get(dictionary.dict_docListMap.get(entry.getKey()).get(i));

                int countOfTerms = 0;
                if(map_temp != null)
                    countOfTerms = (int)map_temp.get(entry.getKey());
                if(countOfTerms!=0) {
                    pl.setTf(countOfTerms);
                    //    postListCount += countOfTerms;

                    listOfPostingListObjects.add(pl);
                }
            }

        }
    }

    private static void writeTotalFile(int count, File file ) {

        try
        {

            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);

            pw.println(count);

            pw.flush();
            pw.close();
            fos.close();

        }
        catch (Exception e)
        {
            e.getMessage();
            e.printStackTrace();
        }

    }


    private static void removeStopWords(ArrayList<String> reviewString) {

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

