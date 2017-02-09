import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by sudeep on 11/21/16.
 */
public class dictionary {

    private  String term;
    private static int global_offset=0;
    private  int cf;
    private  int df;
    private  int offset;
    public   static TreeMap<String, Integer> dict_collectionFrequency_Map = new TreeMap<String, Integer>();
    public   static TreeMap<String, Integer> dict_documentFrequency_Map = new TreeMap<String, Integer>();
    public   static TreeMap<String, ArrayList<Integer>> dict_docListMap = new TreeMap<String, ArrayList<Integer>>();
    public   static TreeMap<String, Integer> dict_offsetMap = new TreeMap<String,Integer>();



    public  void generate(TreeMap<String, Integer> collectionFrequency_map,int i) {

        // checking if the entry is present in the dict map
        for(Map.Entry<String,Integer> entry : collectionFrequency_map.entrySet()) {
            String key = entry.getKey();
            if(dict_collectionFrequency_Map.containsKey(key)) {
                updateDictCollectionFrequencyMap(collectionFrequency_map,key);
                dict_documentFrequency_Map.put(key,dict_documentFrequency_Map.get(key)+1);

                if(!dict_docListMap.containsKey(key))
                {
                    ArrayList temp_list = new ArrayList();
                    temp_list.add(i+1);
                    dict_docListMap.put(key,temp_list);
                }
                else {
                    ArrayList temp_list = dict_docListMap.get(key);
                    temp_list.add(i+1);
                    dict_docListMap.put(key, temp_list);
                }
            }
            else {
                insertDictCollectionFrequencyMap(collectionFrequency_map,key);
                dict_documentFrequency_Map.put(key,1);

                if(!dict_docListMap.containsKey(key))
                {
                    ArrayList temp_list = new ArrayList();
                    temp_list.add(i+1);
                    dict_docListMap.put(key,temp_list);
                }
                else {
                    ArrayList temp_list = dict_docListMap.get(key);
                    temp_list.add(i+1);
                    dict_docListMap.put(key, temp_list);
                }
            }
        }

        //generateOffsetMap();

    }

    private void generateOffsetMap() {

        for(Map.Entry<String,Integer> entry : dict_documentFrequency_Map.entrySet()) {
            if(dict_offsetMap.containsKey(entry.getKey()))
                dict_offsetMap.put(entry.getKey(), dict_offsetMap.get(entry.getKey()) + entry.getValue());
            else
                dict_offsetMap.put(entry.getKey(), global_offset);
            global_offset += entry.getValue();
        }

    }

    private void insertDictCollectionFrequencyMap(TreeMap<String,Integer> collectionFrequency_map,String key) {
        if(collectionFrequency_map.get(key)>1)
            dict_collectionFrequency_Map.put(key,collectionFrequency_map.get(key));
        else
            dict_collectionFrequency_Map.put(key,1);
    }

    private void updateDictCollectionFrequencyMap(TreeMap<String, Integer> collectionFrequencyMap,String key) {
        dict_collectionFrequency_Map.put(key,dict_collectionFrequency_Map.get(key)+collectionFrequencyMap.get(key));
        //dict_documentFrequency_Map.put(key)

    }
}
