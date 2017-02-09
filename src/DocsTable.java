import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sudeep on 11/20/16.
 */
public class DocsTable
{

    private int doc_number;
    private String headline;
    private int doc_length;
    private StringBuffer snippet;
    private String doc_path;
   // private static int doc_number_global=0;


    public StringBuffer getSnippet() {
        return snippet;
    }

    public void setSnippet(StringBuffer snippet) {
        this.snippet = snippet;
    }


    public int getDoc_number() {
        return doc_number;
    }

    public void setDoc_number(int doc_number) {
        this.doc_number = doc_number;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getDoc_length() {
        return doc_length;
    }

    public void setDoc_length(int doc_length) {
        this.doc_length = doc_length;
    }



    public void generate(File file, supportingClass1 obj, int j) throws IOException {


        //setting the doc number
        this.setDoc_number(j+1);
      //  System.out.println(" doc number:"+getDoc_number()+" File Name:"+file.getName().toString());
        int word_count=0;
        FileReader file1 = new FileReader(file);
        BufferedReader br = new BufferedReader(file1);

        snippet = new StringBuffer();
        String line ="";
        // setting headline and snippet of the file
        while ((line = br.readLine()) != null) {

            if(line.equalsIgnoreCase("<HEADLINE>"))
            {
                this.setHeadline(br.readLine().replaceAll(",",""));
            }

            if (line.equalsIgnoreCase("<TEXT>")) {
                while(word_count<40) {
                    String str = new String(br.readLine());
                    //String str_split[] = str.trim().split("\\s+");
                    String str_split[] = str.split("\\s+");

                    for(int i=0;i<str_split.length;i++)
                    {
                        str_split[i] = str_split[i].replaceAll("[^A-Za-z0-9]+", "");
                    }

                        for(int i=0;i<str_split.length;i++) {
                          //  System.out.println("word_count=" + word_count + " " +str_split[i]);
                            word_count++;
                            this.snippet.append(str_split[i]+" ");
                            if(word_count==40)
                                break;
                        }
                    }
                }
            }

        //setting doc path of the file.
        this.setDoc_path(file.getAbsolutePath());
        int count = 0;
        for(String key: obj.getCollectionFrequency_map().keySet())
            count += obj.getCollectionFrequency_map().get(key);
        this.setDoc_length(count);

    //setting doc length of the file
    //this.setDoc_length(obj.getCollectionFrequency_map().size());

    //setting snippet of the file


}

    public String getDoc_path() {
        return doc_path;
    }

    public void setDoc_path(String doc_path) {
        this.doc_path = doc_path;
    }



}
