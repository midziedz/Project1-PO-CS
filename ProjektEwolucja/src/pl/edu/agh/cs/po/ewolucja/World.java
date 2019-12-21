package pl.edu.agh.cs.po.ewolucja;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class World {
    public static void main(String[] args) throws InterruptedException {
        JSONMapData parameters = new JSONMapData("src/parameters.json");
        CycledMap map = new CycledMap(parameters);
        while(true)
        {
            System.out.println(map.toString());
            Thread.sleep(10000);
            map.dayIsGone();
        }
    }
}
