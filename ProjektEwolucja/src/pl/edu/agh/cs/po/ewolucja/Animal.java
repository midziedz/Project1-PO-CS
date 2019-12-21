package pl.edu.agh.cs.po.ewolucja;

import java.lang.Math;

import pl.edu.agh.cs.po.ewolucja.Vector2d;

public class Animal implements IMapElement , Comparable<Animal> {
    private Vector2d position;
    private MapDirection direction;
    private int energy;
    final public int maxEnergy;
    private E6 e6;
    private int[] genes = new int[32];
    private int endurance;
    private CycledMap map;
    public boolean reproducedToday = false;

    private void countingSort(int[] genes, int amount) {
        int[] counters = new int[amount];
        for (int i = 0; i < amount; i++) {
            counters[i] = 0;
        }
        for (int i = 0; i < genes.length; i++) {
            counters[genes[i]] = counters[genes[i]] + 1;
        }
        for (int i = amount - 1; i > 0; i--) {
            if (counters[i] == 0) {
                counters[i - 1] = counters[i - 1] - 1;
                counters[i] = 1;
            }
        }
        if (counters[0] == 0) {
            counters[0] = 1;
            for (int i = 0; i < amount; i++) {
                if (counters[i] > 1) {
                    counters[i] = counters[i] - 1;
                    break;
                }
            }
        }
        int iterator = 0;
        while (iterator < genes.length) {
            int val = 0;
            for (int i = amount - 1; i >= 0; i--) {
                if (iterator > genes.length - 1 - counters[i]) {
                    val = i;
                }
            }
            genes[iterator] = val;
            iterator = iterator + 1;
        }
    }

    public Animal(Vector2d position, int energy, int maxEnergy, CycledMap map) {
        this.position = position;
        this.maxEnergy = maxEnergy;
        this.energy = energy;
        this.map = map;
        this.endurance = (int) (Math.random() * 2 * this.map.getMaxEndurance()) - this.map.getMaxEndurance();
        int random = (int) (Math.random() * 10);
        if (random % 2 == 0) {
            this.e6 = E6.MALE;
        } else {
            this.e6 = E6.FEMALE;
        }
        this.map.place(this);
        int limit = 0;
        for (int i = 0; i < 32; i++) {
            this.genes[i] = (int) (Math.random() * (7 - limit) + limit);
            if (this.genes[i] > limit) limit = this.genes[i];
        }
        countingSort(this.genes, 8);
        int pick = (int) (Math.random()*7);
        switch(pick)
        {
            case 7: this.direction = MapDirection.NORTHEAST;
            case 0: this.direction = MapDirection.NORTH;
            case 1: this.direction = MapDirection.NORTHWEST;
            case 2: this.direction = MapDirection.WEST;
            case 3: this.direction = MapDirection.SOUTHWEST;
            case 4: this.direction = MapDirection.SOUTH;
            case 5: this.direction = MapDirection.SOUTHEAST;
            case 6: this.direction = MapDirection.EAST;
        }
    }
    private Animal(Vector2d position, Animal Mom, Animal Dad) {
        this.position = position;
        this.maxEnergy = Mom.maxEnergy;
        this.map = Mom.map;
        this.endurance = (int) (Math.random() * 2 * this.map.getMaxEndurance()) - this.map.getMaxEndurance();
        int random = (int) (Math.random() * 2);
        if (random % 2 == 0) {
            this.e6 = E6.MALE;
        } else {
            this.e6 = E6.FEMALE;
        }
        this.energy = Mom.energy / 4 + Dad.energy / 4;
        Mom.energy = Mom.energy * 3 / 4;
        Dad.energy = Dad.energy * 3 / 4;
        Mom.reproducedToday = true;
        Dad.reproducedToday = true;
        int div1 = (int) (Math.random() * 10 * 31);
        int div2 = (int) (Math.random() * 10 * (31 - div1) + div1);
        for (int i = 0; i < 32; i++) {
            if (i < div1 || i >= div2) {
                this.genes[i] = Mom.genes[i];
            } else {
                this.genes[i] = Dad.genes[i];
            }
        }
        countingSort(this.genes, 8);
        int pick = (int) (Math.random()*7);
        switch(pick)
        {
            case 7: this.direction = MapDirection.NORTHEAST;
            case 0: this.direction = MapDirection.NORTH;
            case 1: this.direction = MapDirection.NORTHWEST;
            case 2: this.direction = MapDirection.WEST;
            case 3: this.direction = MapDirection.SOUTHWEST;
            case 4: this.direction = MapDirection.SOUTH;
            case 5: this.direction = MapDirection.SOUTHEAST;
            case 6: this.direction = MapDirection.EAST;
        }
        this.reproducedToday = true;
    }

    private void nextDirection() {
        this.direction = this.direction.next();
    }

    private Vector2d alternateLocateFood(int direction) {
        return new Vector2d(0, 0);
    }

    private boolean foundFood(Vector2d position) {
        return this.map.grassAt(position);
    }

    /*private boolean isNotOccupied(Vector2d position)
    {
        return true;
    }*/
    private Vector2d locateFood() {
        int distance = 1;
        while (distance * distance < this.energy + this.endurance) {
            if (foundFood(this.map.cutPosition(this.position.add(this.direction.toUnitVector().multiply(distance))))) {
                this.energy = this.energy - distance * distance * this.map.getMoveEnergy();
                return this.map.cutPosition(this.position.add(this.direction.toUnitVector().multiply(distance)));
            }
            distance = distance + 1;
        }
        this.energy = this.energy - this.map.getMoveEnergy();
        return this.map.cutPosition(this.position.add(this.direction.toUnitVector()));
        //return null;
    }

    private void orientate() {
        int pick = (int) (Math.random() * 31);
        int direction = this.genes[pick];
        for (int i = 0; i < direction; i++) {
            this.nextDirection();
        }
    }

    private boolean move() {
        Vector2d food = this.locateFood();
        if (food != null) {
            Vector2d oldPosition = this.position;
            this.position = food;
            this.map.positionChanged(this, oldPosition, food);
            return true;
        } else {
            return false;
        }
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void eat(Grass trawa) {
        this.energy = this.energy + trawa.getEnergy();
    }

    public Animal reproduce(Animal other) {
        if (this.energy >= this.maxEnergy / 2 && other.energy >= other.maxEnergy / 2) {
            return new Animal(this.map.getNeighbour(this.position), this, other);
        }
        return null;
    }

    public int getEnergy() {
        return this.energy;
    }

    public E6 getE6() {
        return this.e6;
    }

    public void dailyRoutine() {
        this.orientate();
        boolean moved = this.move();
        //boolean ate = this.eat();
        //boolean reproduced = this.reproduce(other);
    }

    @Override

    public int compareTo(Animal animal) {

        if(this.getEnergy() > animal.getEnergy()) return 1;

        else if (this.getEnergy() < animal.getEnergy()) return -1;

        else return 0;
    }
    public String toString()
    {
        return "O";
    }
}