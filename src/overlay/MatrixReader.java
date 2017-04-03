package overlay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

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
		    int[][] result = (int[][])obj.get("matrix");
		    return result;
		    
		} catch (IOException | JSONException e) {
			throw new Exception("Erreur lors de la lecture du fichier");
		}
	}
}
