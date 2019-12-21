package pl.edu.agh.cs.po.ewolucja;

public enum MapDirection {
    NORTH,
    NORTHWEST,
    WEST,
    SOUTHWEST,
    SOUTH,
    SOUTHEAST,
    EAST,
    NORTHEAST;
    public String toString()
    {
        switch(this)
        {
            case NORTH: return "N";
            case NORTHWEST: return "A";
            case WEST: return "W";
            case SOUTHWEST: return "B";
            case SOUTH: return "S";
            case SOUTHEAST: return "C";
            case EAST: return "E";
            case NORTHEAST: return "D";
        }
        return null;
    }
    public MapDirection next()
    {
        switch(this)
        {
            /*
            case NORTH: return EAST;

            case SOUTH: return WEST;

            case WEST: return NORTH;

            case EAST: return SOUTH;
            */
            case NORTH: return NORTHEAST;
            case NORTHWEST: return NORTH;
            case WEST: return NORTHWEST;
            case SOUTHWEST: return WEST;
            case SOUTH: return SOUTHWEST;
            case SOUTHEAST: return SOUTH;
            case EAST: return SOUTHEAST;
            case NORTHEAST: return EAST;
        }
        return null;
    }
    public MapDirection previous()
    {
        switch(this)
        {
            /*
            case NORTH: return WEST;
            case SOUTH: return EAST;
            case WEST: return SOUTH;
            case EAST: return NORTH;
            */
            case NORTH: return NORTHWEST;
            case NORTHWEST: return WEST;
            case WEST: return SOUTHWEST;
            case SOUTHWEST: return SOUTH;
            case SOUTH: return SOUTHEAST;
            case SOUTHEAST: return EAST;
            case EAST: return NORTHEAST;
            case NORTHEAST: return NORTH;
        }
        return null;
    }
    public Vector2d toUnitVector()
    {
        int x = 0;
        int y = 0;
        switch(this)
        {
            case NORTH: {y = 1; break;}
            case NORTHWEST: {x = -1; y = 1; break;}
            case WEST: {x = -1; break;}
            case SOUTHWEST: {x = -1; y = -1; break;}
            case SOUTH: {y = -1; break;}
            case SOUTHEAST: {x = 1; y = -1; break;}
            case EAST: {x = 1; break;}
            case NORTHEAST: {x = 1; y = 1; break;}
        }
        Vector2d result = new Vector2d(x,y);
        return result;
    }
}
