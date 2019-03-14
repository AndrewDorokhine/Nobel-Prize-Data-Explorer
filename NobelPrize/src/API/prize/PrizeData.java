package API.prize;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Data structure for the information describing prizes (physics, chemistry,
 * medicine, peace, literature or economics), and the years the laureates 
 * received them. Some basic information about the laureates is also stored such
 * as name and id.
 * 
 * @author Nemi R, Andrew D, Jad A, Seth T, Sitharthan E
 */
public class PrizeData {
    private final HashMap<String, Category> data;
    private final String name;
    private final String info;
    /**
     * Constructor. Gets information from the Nobel Prize API and parses it.
     * @throws java.io.IOException
     */
    public PrizeData() throws IOException {
        name = "Prize data.\n";
        info = "Hashmap of prizes, years, and their laureates.\n";
        data = new HashMap();
        parseData();
    }
    
    /***************************************************************************
     * GETTERS 
     **************************************************************************/
    
    /**
     * Gets a copy of the data hashmap.
     * @return HashMap
     */
    public HashMap getData() {
        HashMap<String, Category> copy = new HashMap();
        for (String key : data.keySet()) {
            copy.put(key, new Category(data.get(key)));
        }
        return copy;
    }
    /**
     * Gets the objects name.
     * @return String
     */
    public String getName() {
        return name + "";
    }
    /**
     * Gets information on the object.
     * @return String
     */
    public String getInfo() {
        return info + "";
    }
    
    /***************************************************************************
     * PARSING FUNCTIONS
     **************************************************************************/ 
    
    /**
     * Gets prize information from the Nobel Prize API and uses GSON
     * to parse the JSON into the PrizeData object.
     * @return
     * @throws IOException 
     */
    private void parseData() throws IOException {
        // Get prize JSON from the API
        String url = "http://api.nobelprize.org/v1/prize.json?";
        String json = getJson(url);
        // Parse with GSON
        Gson gson = new Gson();
        PrizeResult result = gson.fromJson(json, PrizeResult.class);
        // Put the list into the data hashmap
        for (Object prize : result.getPrizes()) {
            addPrize((Prize)prize);
        }

    }
    /**
     * Adds a new prize category to the data hashmap, or adds to the list 
     * contained within the key if it already exists.
     * @param p Prize to add
     */
    private void addPrize(Prize p) {
        if (!data.containsKey(p.getCategory())) {
            data.put(p.getCategory(), new Category());
        }
        Category current = data.get(p.getCategory());
        current.add(p.getYear(), p.getLaureates()); 
    }
    /**
     * Gets JSON string from a URL string that is passed in.
     * @param u the URL as a string
     * @return JSON string
     * @throws IOException 
     */
    private static String getJson (String u) throws IOException {
        URL url            = new URL(u);
        BufferedReader br  = new BufferedReader
                                (new InputStreamReader(url.openStream()));
        StringBuilder json = new StringBuilder();
        String line        = "";
        // Read each line from the buffer and append to the JSON string.
        while ((line = br.readLine()) != null) {
            json.append(line);
        }
        br.close();
        return json.toString();
    }
    /**
     * For getting a string representation of the data.
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName());
        builder.append(getInfo());
        for (Object c : getData().keySet()) {
            String category = (String) c;
            builder.append(category);
            builder.append("\n");
            builder.append(data.get(category).toString());
        }
        return builder.toString();
    }
}