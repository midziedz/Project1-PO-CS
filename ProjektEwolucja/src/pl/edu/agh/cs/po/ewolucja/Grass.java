package pl.edu.agh.cs.po.ewolucja;

public class Grass implements IMapElement {
    private Vector2d position;
    private int feedingEnergy;
    Grass(Vector2d position, int feedingEnergy)
    {
        this.position = position;
        this.feedingEnergy = feedingEnergy;
    }
    public Vector2d getPosition()
    {
        return this.position;
    }
    public int getEnergy()
    {
        return this.feedingEnergy;
    }
    public String toString()
    {
        return "*";
    }
}