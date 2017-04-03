package overlay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatrixReader 
{
	public int[][] readFile(String filename) throws Exception
	{
		File file = new File(filename);
		if(!file.exists())
			throw new FileNotFoundException("Fichier " + filename + " non existant");
	
		try(BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String json = sb.toString();
		    JSONObject obj = new JSONObject(json);
		    
		    JSONArray rows = new JSONArray(obj.get("matrix").toString());
		    int count = rows.length();
		    int[][] result = new int[count][count];
		    JSONArray row;
		    for(int i = 0; i < count; i++)
		    {
		    	row = rows.getJSONArray(i);
		    	
		    	for(int index = 0; index < count; index++)
			    {
		    		result[i][index] = row.getInt(index);
			    }
		    }
		    
		    return result;
		    
		} catch (IOException | JSONException e) {
			
			throw new Exception("Erreur lors de la lecture du fichier\n" + e.getMessage());
		}
	}
}
