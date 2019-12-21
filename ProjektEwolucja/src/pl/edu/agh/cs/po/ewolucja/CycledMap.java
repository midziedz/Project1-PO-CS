package pl.edu.agh.cs.po.ewolucja;

import java.util.ArrayList;
import java.util.List;

import java.lang.Math;

import java.util.LinkedHashMap;
import java.util.Map;

public class CycledMap {
    private LinkedHashMap<Vector2d,MapCell> cells = new LinkedHashMap<>();
    private List<Animal> animals = new ArrayList<>();
    private List<Grass> jungle = new ArrayList<>();
    private List<Grass> savanna = new ArrayList<>();
    private List<MapCell> jungleFreeCells = new ArrayList<>();
    private List<MapCell> savannaFreeCells = new ArrayList<>();
    private int width;
    private int height;
    private int maxEndurance;
    private Vector2d jungleUpperRight;
    private Vector2d jungleLowerLeft;
    private int startEnergy;
    private int moveEnergy;
    private int minFeedingEnergy;
    private int maxFeedingEnergy;
    private MapVisualizer visualizer;

    public CycledMap(JSONMapData input)
    {
        this.width = input.width;
        this.height = input.height;
        this.maxEndurance = input.maxEndurance;
        int startAnimalNumber = input.startAnimalNumber;
        this.jungleLowerLeft = new Vector2d((int)((this.width/2)-(input.jungleRatio*this.width/2)), (int)((this.height/2)-(input.jungleRatio*this.height/2)));
        this.jungleUpperRight = new Vector2d((int)((this.width/2)+(input.jungleRatio*this.width/2)), (int)((this.height/2)+(input.jungleRatio*this.height/2)));
        this.startEnergy = input.startEnergy;
        this.moveEnergy = input.moveEnergy;
        this.maxFeedingEnergy = input.maxPlantEnergy;
        this.minFeedingEnergy = input.minPlantEnergy;
        this.visualizer = new MapVisualizer(this);
        for(int i = 0; i < this.width; i++)
        {
            for(int j = 0; j < this.height; j++)
            {
                Vector2d pos = new Vector2d(i,j);
                MapCell cell = new MapCell(pos);
                cells.put(pos, cell);
                if(pos.follows(this.jungleLowerLeft) && pos.precedes(this.jungleUpperRight))
                {
                    this.jungleFreeCells.add(cell);
                }
                else
                {
                    this.savannaFreeCells.add(cell);
                }
            }
        }
        for(int i = 0; i < (int)Math.sqrt(this.width*this.height); i++)
        {
            int x = (int) (Math.random()*(width - 1));
            int y = (int) (Math.random()*(height - 1));
            int energy = (int) (Math.random()*10*(this.maxFeedingEnergy - this.minFeedingEnergy) + this.minFeedingEnergy);
            if((new Vector2d(x,y)).follows(this.jungleLowerLeft) && (new Vector2d(x,y)).precedes(this.jungleUpperRight))
            {
                Vector2d pos = new Vector2d(x,y);
                this.jungle.add(new Grass(pos, energy));
                this.cells.get(pos).plantGrass(energy);
                this.jungleFreeCells.remove(this.cells.get(pos));
            }
            else
            {
                Vector2d pos = new Vector2d(x,y);
                this.savanna.add(new Grass(pos, energy));
                this.cells.get(pos).plantGrass(energy);
                this.savannaFreeCells.remove(this.cells.get(pos));
            }
        }
        for(int i = 0; i < startAnimalNumber; i++)
        {
            int type = (int) (Math.random()*20);
            if(type % 2 == 0)
            {
                int index = (int) (Math.random()*(jungleFreeCells.size() - 1));
                MapCell mapCell = jungleFreeCells.get(index);
                mapCell.addAnimal(new Animal(mapCell.getPosition(),this.startEnergy,this.startEnergy, this));
                this.jungleFreeCells.remove(mapCell);
            }
            else
            {
                int index = (int) (Math.random()*(savannaFreeCells.size() - 1));
                MapCell mapCell = savannaFreeCells.get(index);
                mapCell.addAnimal(new Animal(mapCell.getPosition(),this.startEnergy,this.startEnergy, this));
                this.savannaFreeCells.remove(mapCell);
            }
        }
    }
    private boolean animalAt(Vector2d position)
    {
        for(Animal animal: animals)
        {
            if(animal.getPosition().equals(position))
            {
                return true;
            }
        }
        return false;
    }
    private Animal getDifferentAnimalAt(Vector2d position, Animal except)
    {
        for(Animal animal: animals)
        {
            if(animal.getPosition().equals(position) && animal != except)
            {
                return animal;
            }
        }
        return null;
    }
    private Grass getGrassAt(Vector2d position)
    {
        return cells.get(position).getGrass();
    }
    public void place(Animal animal)
    {
        if(!animalAt(animal.getPosition()))
        {
            this.animals.add(animal);
            this.cells.get(animal.getPosition()).addAnimal(animal);
        }
    }
    public Vector2d cutPosition(Vector2d position)
    {
        int x = position.x;
        int y = position.y;
        if(position.x >= this.width)
        {
            x = position.x % this.width;
        }
        if(position.x < 0)
        {
            x = this.width + position.x;
        }
        if(position.y >= this.height)
        {
            y = position.y % this.height;
        }
        if(position.y < 0)
        {
            y = this.height + position.y;
        }
        return new Vector2d(x,y);
    }
    public boolean grassAt(Vector2d position) {
        Grass grass = this.cells.get(position).getGrass();
        return (grass != null);
    }
    private Animal findMostEnergeticByE6At(Vector2d position, E6 e6)
    {
        Animal found = new Animal(position, 0, 0, this);
        for(Animal animal: this.animals)
        {
            if(animal.getE6() == e6 && animal.getEnergy() > found.getEnergy())
            {
                found = animal;
            }
        }
        return found;
    }
    public Vector2d getNeighbour(Vector2d position)
    {
        List<Vector2d> neighbours = new ArrayList<>();
        List<Vector2d> freeNeighbours = new ArrayList<>();
        neighbours.add(cutPosition(new Vector2d(position.x+1, position.y+1)));
        neighbours.add(cutPosition(new Vector2d(position.x+1, position.y)));
        neighbours.add(cutPosition(new Vector2d(position.x+1, position.y-1)));
        neighbours.add(cutPosition(new Vector2d(position.x, position.y-1)));
        neighbours.add(cutPosition(new Vector2d(position.x-1, position.y-1)));
        neighbours.add(cutPosition(new Vector2d(position.x-1, position.y)));
        neighbours.add(cutPosition(new Vector2d(position.x-1, position.y+1)));
        neighbours.add(cutPosition(new Vector2d(position.x, position.y+1)));
        for(Vector2d neighbour: neighbours)
        {
            if(this.cells.get(neighbour).isEmpty())
            {
                freeNeighbours.add(neighbour);
            }
        }
        if(!freeNeighbours.isEmpty())
        {
            int index = (int)(Math.random()*(freeNeighbours.size()-1));
            return freeNeighbours.get(index);
        }
        int index = (int)(Math.random()*(neighbours.size()-1));
        return freeNeighbours.get(index);
    }
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition)
    {
        if(this.cells.get(newPosition).isEmpty())
        {
            if(newPosition.follows(this.jungleLowerLeft) && newPosition.precedes(this.jungleUpperRight))
            {
                this.jungleFreeCells.remove(this.cells.get(newPosition));
            }
            else
            {
                this.savannaFreeCells.remove(this.cells.get(newPosition));
            }
        }
        this.cells.get(oldPosition).removeAnimal(animal);
        this.cells.get(newPosition).addAnimal(animal);
        if(this.cells.get(oldPosition).isEmpty())
        {
            if(oldPosition.follows(this.jungleLowerLeft) && oldPosition.precedes(this.jungleUpperRight))
            {
                this.jungleFreeCells.add(this.cells.get(oldPosition));
            }
            else
            {
                this.savannaFreeCells.add(this.cells.get(oldPosition));
            }
        }
    }
    public int getWidth()
    {
        return this.width;
    }
    public int getHeight()
    {
        return this.height;
    }
    public List<Animal> getAnimals()
    {
        return this.animals;
    }
    public int getStartEnergy()
    {
        return this.startEnergy;
    }
    public LinkedHashMap<Vector2d,MapCell> getCells()
    {
        return this.cells;
    }
    public int getMoveEnergy()
    {
        return this.moveEnergy;
    }
    public MapVisualizer getVisualizer() {
        return this.visualizer;
    }
    public Vector2d getLowerLeft()
    {
        return new Vector2d(0, 0);
    }
    public Vector2d getUpperRight()
    {
        return new Vector2d(this.width - 1, this.height - 1);
    }
    public int getMaxEndurance()
    {
        return this.maxEndurance;
    }
    public void dayIsGone()
    {
        List<Animal> toRemove = new ArrayList<>();
        for(Animal animal: this.animals)
        {
            if(animal.getEnergy() <= 0)
            {
                toRemove.add(animal);
                this.cells.get(animal.getPosition()).removeAnimal(animal);
                if(this.cells.get(animal.getPosition()).isEmpty())
                {
                    if(animal.getPosition().follows(this.jungleLowerLeft) && animal.getPosition().precedes(this.jungleUpperRight))
                    {
                        this.jungleFreeCells.add(this.cells.get(animal.getPosition()));
                    }
                    else
                    {
                        this.savannaFreeCells.add(this.cells.get(animal.getPosition()));
                    }
                }
            }
            else
            {
                animal.reproducedToday = false;
            }
        }
        this.animals.removeAll(toRemove);
        for(Animal animal: this.animals)
        {
            animal.dailyRoutine();
        }
        for(MapCell mapCell: this.cells.values())
        {
            Grass grass = mapCell.getGrass();
            mapCell.feedMostEnergeticAnimals(grass);
            boolean removed = false;
            for(Grass trawa: this.jungle)
            {
                if(trawa == grass)
                {
                    this.jungle.remove(trawa);
                    removed = true;
                    break;
                }
            }
            if(!removed)
            {
                for (Grass trawa : this.savanna) {
                    if (trawa == grass) {
                        this.savanna.remove(trawa);
                        break;
                    }
                }
            }
            //animal.eat();
        }
        List<Animal> children = new ArrayList<>();
        for(MapCell mapCell: this.cells.values())
        {
            Animal mostPowerfulMale = mapCell.getMostPowerfulMale();
            Animal mostPowerfulFemale = mapCell.getMostPowerfulFemale();
            if(mostPowerfulMale != null && mostPowerfulFemale != null && !mostPowerfulMale.reproducedToday && !mostPowerfulFemale.reproducedToday)
            {
                Animal child = mostPowerfulFemale.reproduce(mostPowerfulMale);
                if(child != null)
                {
                    this.cells.get(child.getPosition()).addAnimal(child);
                    children.add(child);
                }
            }
            //animal.reproduce();
        }
        this.animals.addAll(children);
        int energy = (int) (Math.random()*(this.maxFeedingEnergy - this.minFeedingEnergy) + this.minFeedingEnergy);
        int index = (int)(Math.random()*(this.jungleFreeCells.size()-1));
        if(!jungleFreeCells.isEmpty())
        {
            this.jungleFreeCells.get(index).plantGrass(energy);
            this.jungle.add(new Grass(this.jungleFreeCells.get(index).getPosition(), energy));
            this.jungleFreeCells.remove(this.jungleFreeCells.get(index));
        }
        if(!savannaFreeCells.isEmpty())
        {
            energy = (int) (Math.random() * (this.maxFeedingEnergy - this.minFeedingEnergy) + this.minFeedingEnergy);
            index = (int) (Math.random() * (this.savannaFreeCells.size() - 1));
            this.savannaFreeCells.get(index).plantGrass(energy);
            this.savanna.add(new Grass(this.savannaFreeCells.get(index).getPosition(), energy));
            this.savannaFreeCells.remove(this.savannaFreeCells.get(index));
        }
    }
    public String toString()
    {
        return this.visualizer.draw(this.getLowerLeft(), this.getUpperRight());
    }
}