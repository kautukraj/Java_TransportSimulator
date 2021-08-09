package demo;

import base.*;

import java.util.ArrayList;

public class NetworkDemo extends Network
{
    ArrayList <Hub> hubs = new ArrayList<>();
    static ArrayList <Highway> highways = new ArrayList<>();
    ArrayList <Truck> trucks = new ArrayList<>();
    //static Graph graph = new Graph();

    @Override
    public void add(Hub hub)
    {
        hubs.add(hub);
        //graph.addNode(hub);
    }

    @Override
    public void add(Highway hwy)
    {
        highways.add(hwy);
        for (Hub hub: hubs)
        {
            if (hwy.getStart() == hub)
                hub.add(hwy);
        }
        //graph.addEdge(hwy.getStart(), hwy.getEnd());
    }

    public static ArrayList<Highway> getAllHighways()
    {
        return highways;
    }

    @Override
    public void add(Truck truck)
    {
        trucks.add(truck);
    }

    @Override
    public void start()
    // derived class calls start on each of the Hubs and Trucks
    {
        //System.out.println("Start of Network called");
        for (Hub hub: hubs)
            hub.start();

        for (Truck truck: trucks)
            truck.start();
    }

    @Override
    public void redisplay(Display disp)
    // derived class calls draw on each hub, highway, and truck
    // passing in display
    {
        //System.out.println("Redisplay of Network called");
        for (Hub hub: hubs)
            hub.draw(disp);

        for (Highway hwy: highways)
            hwy.draw(disp);

        for (Truck truck: trucks)
            truck.draw(disp);
    }

    @Override
    protected Hub findNearestHubForLoc(Location loc)
    {
        double min = Integer.MAX_VALUE;
        Hub nearest = null; 

        for (Hub hub: hubs)
        {
            double distance = Math.sqrt(Math.pow(hub.getLoc().getX() - loc.getX(), 2) + Math.pow(hub.getLoc().getY() - loc.getY(), 2));
            if (distance < min)
            {
                min = distance;
                nearest = hub;
            }
        }
        return nearest;
    }
}

/*class Node
{
    Hub hub;
    Node (Hub hub)
    {
        this.hub = hub;
    }

    // override equals and hashcode
    @Override
    public boolean equals (Object obj)
    {
        Node node = (Node) obj;
        return node.hub.getLoc().getX() == this.hub.getLoc().getX() && node.hub.getLoc().getY() == this.hub.getLoc().getY();
    }

    @Override
    public int hashCode()
    {
        return this.hub.hashCode();
    }
}*/

/*class Graph
{
    private final Map<Node, List<Node>> adjNodes = new HashMap<>();

    void addNode(Hub hub)
    {
        //System.out.println("Call to addNode");
        adjNodes.putIfAbsent(new Node(hub), new ArrayList<>());

        for (Node node: adjNodes.keySet())
            System.out.println(node.hub.getLoc());
    }

    void removeNode(Hub hub)
    {
        Node v = new Node(hub);
        adjNodes.values().forEach(e -> e.remove(v));
        adjNodes.remove(new Node(hub));
    }

    void addEdge(Hub hub1, Hub hub2)
    {
        Node node1 = new Node(hub1);
        Node node2 = new Node(hub2);

        adjNodes.get(node1).add(node2);
        // adjNodes.get(node2).add(node1); // don't add the reverse arrow

        for (List<Node> l: adjNodes.values())
        {
            for (Node n: l)
            {
                System.out.print(n.hub.getLoc());
            }
            System.out.println();
        }

    }*/

    /*void removeEdge(Hub hub1, Hub hub2)
    {
        Node v1 = new Node (hub1);
        Node v2 = new Node (hub2);
        List<Node> eV1 = adjNodes.get(v1);
        List<Node> eV2 = adjNodes.get(v2);
        if (eV1 != null)
            eV1.remove(v2);
        if (eV2 != null)
            eV2.remove(v1);
    }

    List<Node> getAdjNodes(Hub hub)
    {
        return adjNodes.get(new Node(hub));
    }
}*/