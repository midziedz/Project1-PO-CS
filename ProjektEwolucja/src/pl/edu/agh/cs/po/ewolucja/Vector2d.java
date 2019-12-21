package pl.edu.agh.cs.po.ewolucja;

public class Vector2d {
    public final int x;
    public final int y;
    public Vector2d(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public String toString()
    {
        return "("+this.x+","+this.y+")";
    }
    public boolean precedes(Vector2d other)
    {
        if(this.x  <= other.x && this.y  <= other.y)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean follows(Vector2d other)
    {
        if(this.x  >= other.x && this.y  >= other.y)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public Vector2d upperRight(Vector2d other)
    {
        int x = 0;
        int y = 0;
        if(this.x > other.x)
        {
            x = this.x;
        }
        else
        {
            x = other.x;
        }
        if(this.y > other.y)
        {
            y = this.y;
        }
        else
        {
            y = other.y;
        }
        Vector2d result = new Vector2d(x,y);
        return result;
    }
    public Vector2d lowerLeft(Vector2d other)
    {
        int x = 0;
        int y = 0;
        if(this.x < other.x)
        {
            x = this.x;
        }
        else
        {
            x = other.x;
        }
        if(this.y < other.y)
        {
            y = this.y;
        }
        else
        {
            y = other.y;
        }
        Vector2d result = new Vector2d(x,y);
        return result;
    }
    public Vector2d add(Vector2d other)
    {
        int x = 0;
        int y = 0;
        x = this.x+other.x;
        y = this.y+other.y;
        Vector2d result = new Vector2d(x,y);
        return result;
    }
    public Vector2d subtract(Vector2d other)
    {
        int x = 0;
        int y = 0;
        x = this.x-other.x;
        y = this.y-other.y;
        Vector2d result = new Vector2d(x,y);
        return result;
    }
    public Vector2d multiply(int number)
    {
        int x = 0;
        int y = 0;
        x = this.x*number;
        y = this.y*number;
        Vector2d result = new Vector2d(x,y);
        return result;
    }
    public boolean equals(Object other)
    {
        if(other instanceof Vector2d)
        {
            Vector2d o = (Vector2d)other;
            if (this.x == o.x && this.y == o.y)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    public Vector2d opposite()
    {
        int x = -1*(this.x);
        int y = -1*(this.y);
        Vector2d result = new Vector2d(x,y);
        return result;
    }
    @Override
    public int hashCode() {
        int hash = 11;
        hash += this.x * this.x * 2;
        hash += this.y * this.y * 3;
        return hash;
    }
}