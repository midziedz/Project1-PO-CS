package pl.edu.agh.cs.po.ewolucja;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONMapData {
    public int width;
    public int height;
    public int startAnimalNumber;
    public int startEnergy;
    public int maxEndurance;
    public int moveEnergy;
    public int minPlantEnergy;
    public int maxPlantEnergy;
    public double jungleRatio;
    public JSONMapData(String fileName)
    {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(fileName))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject parameters = (JSONObject) obj;
            this.width = (int) (long) parameters.get("width");
            this.height = (int) (long) parameters.get("height");
            this.maxEndurance = (int) (long) parameters.get("maxEndurance");
            this.startAnimalNumber = (int) (long) parameters.get("startAnimalNumber");
            this.startEnergy = (int) (long) parameters.get("startEnergy");
            this.moveEnergy = (int) (long) parameters.get("moveEnergy");
            this.minPlantEnergy = (int) (long) parameters.get("minPlantEnergy");
            this.maxPlantEnergy = (int) (long) parameters.get("maxPlantEnergy");
            this.jungleRatio = (double) parameters.get("jungleRatio");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /*private void parseEmployeeObject(JSONObject parameters) {
        JSONObject parametersObject = (JSONObject) parameters.get("parameters");

        //Get employee first name
        this.width = (int) parametersObject.get("width");
        this.height = (int) parametersObject.get("height");
        this.startEnergy = (int) parametersObject.get("startEnergy");
        this.moveEnergy = (int) parametersObject.get("moveEnergy");
        this.plantEnergy = (int) parametersObject.get("plantEnergy");
        this.jungleRatio = (double) parametersObject.get("jungleRatio");

    }*/
}