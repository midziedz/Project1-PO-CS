package pl.edu.agh.cs.po.ewolucja;

//import java.util.ArrayList;
//import java.util.List;

import java.util.*;

public class MapCell {
    private Vector2d position;
    private ArrayList<Animal> animals = new ArrayList<>();
    Grass grass = null;
    public MapCell(Vector2d position)
    {
        this.position = position;
    }
    public void addAnimal(Animal animal)
    {
        this.animals.add(animal);
        Collections.sort(this.animals);
    }
    public void removeAnimal(Animal animal)
    {
        this.animals.remove(animal);
        Collections.sort(this.animals);
    }
    public Animal getMostPowerfulMale()
    {
        for(int i = 0; i < this.animals.size(); i++)
        {
            if(this.animals.get(i).getE6() == E6.MALE)
            {
                return this.animals.get(i);
            }
        }
        return null;
    }
    public Animal getMostPowerfulFemale()
    {
        for(int i = 0; i < this.animals.size(); i++)
        {
            if(this.animals.get(i).getE6() == E6.FEMALE)
            {
                return this.animals.get(i);
            }
        }
        return null;
    }
    public Grass getGrass()
    {
        Grass grass = this.grass;
        this.grass = null;
        return grass;
    }
    public boolean plantGrass(int energy)
    {
        if(this.grass == null) {
            this.grass = new Grass(this.position, energy);
            return true;
        }
        return false;
    }
    public boolean isEmpty()
    {
        return (this.animals.isEmpty() && grass == null);
    }
    public Vector2d getPosition()
    {
        return this.position;
    }
    public void feedMostEnergeticAnimals(Grass grass)
    {
        if(!this.animals.isEmpty()) {
            int maxEnergy = this.animals.get(0).getEnergy();
            int counter = 0;
            for (int i = 0; i < this.animals.size(); i++) {
                if (animals.get(i).getEnergy() == maxEnergy) {
                    counter = counter + 1;
                } else {
                    break;
                }
            }
            if(grass != null) {
                Grass podzial = new Grass(grass.getPosition(), grass.getEnergy() / counter);
                for (int i = 0; i < counter; i++) {
                    animals.get(i).eat(podzial);
                }
            }
        }
    }
    public Object getObject()
    {
        if(!this.animals.isEmpty())
        {
            return this.animals.get(0);
        }
        return this.grass;
    }
    public int getMaxEnergy()
    {
        return this.animals.get(0).getEnergy();
    }
    public int countAnimals()
    {
        return this.animals.size();
    }
}