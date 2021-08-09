package demo;

import base.*;

public class FactoryDemo extends Factory
{
    @Override
    public Network createNetwork()
    {
        //System.out.println("Call to createNetwork");
        return new NetworkDemo();
    }

    @Override
    public Highway createHighway()
    {
        //System.out.println("call to createHighway");
        return new HighwayDemo();
    }

    @Override
    public Hub createHub(Location location)
    {
        //System.out.println("call to createHub");
        return new HubDemo(location);
    }

    @Override
    public Truck createTruck()
    {
        //System.out.println("call to createTruck");
        return new TruckDemo();
    }
}
