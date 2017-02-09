import java.io.*;
import java.util.*;

/**
 * Created by sudeep on 12/1/16.
 */
public class DocScore {

    private static TreeMap<Integer, Double> Scores = new TreeMap<>();

    //private static String str_tokens[];
    private static int offset;
    private static int collection_frequency;
    private static int document_frequency;
    public static List<Integer> DocList = new ArrayList<>();
    //private static double w = 0.1;

    public void getDocScore(String[] queryTokens, File dictionary, File docTable, File postingList, File total) {


        createList(docTable);

        try {

            FileReader fileReader = new FileReader(dictionary);
            BufferedReader br = new BufferedReader(fileReader);
            String str;
            String str_tokens[];


            for (int i = 0; i < queryTokens.length; i++) {

                //br.close();
                fileReader = new FileReader(dictionary);
                br = new BufferedReader(fileReader);

                while ((str = br.readLine()) != null) {

                    str_tokens = str.split(",");

                    if (str_tokens[0].equalsIgnoreCase(queryTokens[i])) {

                  //      System.out.println("Term = " + str_tokens[0]);
                        collection_frequency = Integer.parseInt(str_tokens[1]);
                        document_frequency = Integer.parseInt(str_tokens[2]);
                        offset = Integer.parseInt(str_tokens[3]);

                        calculateDocScore(postingList, total, docTable);
                        br.close();
                        break;
                    }
                }

            }

            Double[] sortedValues= Scores.values().toArray(new Double[0]);

            Arrays.sort(sortedValues, Collections.reverseOrder());

            LinkedHashMap<Integer,Double> sortedMap = new LinkedHashMap<>();


            for(int i =0;i<sortedValues.length;i++){

                Double valueOfHashmap =sortedValues[i];

                 for(Map.Entry<Integer ,Double> map :Scores.entrySet())
                 {
                     if(map.getValue()==valueOfHashmap)
                     {
                         sortedMap.put(map.getKey(),map.getValue());
                         break;
                     }
                 }
            }

            // printing the doc information on console
            if(Scores.isEmpty())
            {
                System.out.println("NO RESULT!");
            }
            else
                getDocInformation(sortedMap,docTable,queryTokens);

            Scores.clear();

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }

    }







 void getDocInformation(LinkedHashMap<Integer,Double> sortedMap, File docTable,String [] queryTokens) throws IOException {

/*
     File file = new File("output.txt");
     if (file.exists()) {
         file.delete();

     }
*/

     File output = new File("result.txt");
     FileOutputStream fos = null;
     PrintWriter pw= null;

    // System.out.print("the query is:");

     for (int i = 0; i <queryTokens.length ; i++)
     {
    //     System.out.print(" "+queryTokens[i]);
     }

             fos = new FileOutputStream(output, true);
             pw = new PrintWriter(fos);

            int no_of_docs_written=1;
            for(Map.Entry<Integer,Double> entry :sortedMap.entrySet()) {

                FileReader fileReader = new FileReader(docTable);
                BufferedReader br = new BufferedReader(fileReader);
                String str;
                String strTokens[];

                if(no_of_docs_written<=5) {

                    while ((str = br.readLine()) != null) {

                        strTokens = str.split(",");

                        if (Integer.parseInt(strTokens[0]) == entry.getKey()) {
                            //System.out.println(" ");
                            //System.out.println(strTokens[1]);
                            //System.out.println(strTokens[4]);
                            //System.out.println("Computed Probability:"+entry.getValue());
                            //System.out.println("The document is " + strTokens[4]);
                            //System.out.println(strTokens[3]);

                            try {

                                pw.println(" ");
                                //pw.println("New Document");
                                pw.println(strTokens[1]);
                                pw.println(strTokens[4]);
                                pw.println("Computed probability : " + entry.getValue());
                                pw.println(strTokens[3]);
                                no_of_docs_written++;
                                br.close();
                                break;

                            } catch (Exception e) {
                                e.getMessage();
                                e.printStackTrace();
                            }

                        }

                    }
                }
            }

         pw.close();
         try
         {
             fos.close();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

}



    void writeToOutput(String []strTokens, String []queryTokens, File file) {

        FileOutputStream fos = null;
        PrintWriter pw= null;
        try
        {
            fos = new FileOutputStream(file);
            pw =  new PrintWriter(fos);



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


    private void calculateDocScore(File postingList, File total, File docTable) throws IOException {

        double score;
       // Scores=null;

        try {

            FileReader fileReader = new FileReader(postingList);
            BufferedReader br = new BufferedReader(fileReader);
          //  int term_frequency;
           // int docId;
            int docLength;
            String str;
            int totalTerms;
            String str_tokens[];
            int lineNumber = 0;
           // boolean flag = false;
            int count=0;

          //  System.out.println("DocList");
            for (int i = 0; i <DocList.size() ; i++) {
            //    System.out.println(DocList.get(i));
            }

            while ((str = br.readLine()) != null) {

                List<Integer> temp = new ArrayList<>();
                temp.addAll(DocList);
                lineNumber++;
               // str_tokens = str.split(",");

                if (lineNumber == offset+1) {
                    //  count++;


                    do {
                        str_tokens = str.split(",");
                        int term_frequency = Integer.parseInt(str_tokens[1]);
                         int docId= Integer.parseInt(str_tokens[0]);
                        temp.remove(new Integer(docId));
                        count = traversePostingList(total, docTable, count,docId,term_frequency);

                      }
                        while ((str = br.readLine()) != null && count < document_frequency) ;



                    for (int i = 0; i <temp.size() ; i++)
                    {
                      //  str_tokens = str.split(",");
                        int docId = temp.get(i);
                        count = traversePostingList(total, docTable, count, docId, 0);

                    }

                }


            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int traversePostingList(File total, File docTable,int count,int docId,int term_frequency) {
        String[] str_tokens;
        //int term_frequency;
        //int docId;
        int docLength;
        int totalTerms;
        double score;
        count++;
        //str_tokens = str.split(",");
        //term_frequency = Integer.parseInt(str_tokens[1]);
        //docId = Integer.parseInt(str_tokens[0]);

        //    System.out.println("Doc ID = " + docId);
        docLength = getDocLength(docId, docTable);
        totalTerms = getTotal(total);
      //  System.out.println("DOCID"+docId);
        score = generateDocScore(term_frequency, docLength, totalTerms);

        //System.out.println("Scores =" + score);
        if (Scores.containsKey(docId)) {
            Scores.put(docId, Scores.get(docId) + score);
  //          System.out.println("Current score :" + Scores.get(docId));
        }
        else
        {
            Scores.put(docId, score);
//            System.out.println("Current score :" + Scores.get(docId));
        }
        return count;
    }

    private double generateDocScore(int term_frequency, int docLength, int totalTerms)
    {
     //   System.out.println("TF"+term_frequency);
        double score;
        if(term_frequency!=0) {
            double a =  (double)term_frequency / docLength;
            double b =  (double)collection_frequency /totalTerms;
           // score = Math.log(0.9 * a) + Math.log(0.1 * b) ;
            //return ((0.9) * (term_frequency / docLength)) + ((0.1) * (collection_frequency / totalTerms));
           // score = score/ Math.log(2);

            score = log2((0.9*a)+(0.1*b));

        }
        else
        {
          //  double a = (float) term_frequency / docLength;
          //  double a=0;
            double b =  (double) collection_frequency / totalTerms;
            //score =  Math.log(0.1 * b)/Math.log(2);
            score = log2(0.1*b);
        }


       // score =   calculateRank(term_frequency,docLength,collection_frequency,totalTerms);

        return score;
    }

    public double log2(double v) {

        return Math.log(v)/Math.log(2);
    }


    private int getTotal(File total) {

        int totalTerms=0;
        try {
            FileReader fileReader = new FileReader(total);
            BufferedReader br = new BufferedReader(fileReader);
            String str;

            totalTerms = Integer.parseInt(br.readLine());

        }
        catch(Exception e){

            e.printStackTrace();
        }
        return totalTerms;

    }

    private static void createList(File docTable)
    {

        try {
            FileReader fileReader = new FileReader(docTable);
            BufferedReader br = new BufferedReader(fileReader);
            String str;
            String []strTokens;

            while((str=br.readLine())!=null)
            {
                strTokens=str.split(",");
                DocList.add(Integer.parseInt(strTokens[0]));


            }

        }
        catch(Exception e){

            e.printStackTrace();
        }

    }

    private int getDocLength(int docId, File docTable) {


        try {

            FileReader fileReader = new FileReader(docTable);
            BufferedReader br = new BufferedReader(fileReader);
            String str;
            String str_tokens[];
            int lineNumber=0;
            int docLength;
            while ((str = br.readLine()) != null) {

                lineNumber++;
                str_tokens = str.split(",");

                if(lineNumber==docId){
                    docLength = Integer.parseInt(str_tokens[2]);
                    return docLength;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
