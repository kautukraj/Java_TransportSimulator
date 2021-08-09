package demo;
import base.Highway;
import base.Truck;

import java.util.ArrayList;

public class HighwayDemo extends Highway
{
    int current = 0;
    ArrayList <Truck> trucks = new ArrayList<>();

    @Override
    public synchronized boolean hasCapacity()
    {
        return current < getCapacity();
    }

    @Override
    public synchronized boolean add(Truck truck)
    {
        if (current < getCapacity())
        {
            trucks.add(truck);
            current += 1;
            return true;
        }
        else
            return false;
    }

    @Override
    public synchronized void remove(Truck truck)
    {
        trucks.remove(truck);
        current -= 1;
    }
}
