package demo;

import base.*;

public class TruckDemo extends Truck
{
    private Highway hwy = null; // keep track of the highway on which the truck is
    private int elapsedTime;
    private int speed;
    private static final int count = 0;
    //int state = 0;
    private String state = "srcStation"; // initially the truck is at the srcStation
    private Hub lastHub = null; // last hub the truck was on
    private Hub nextHub = null; // next hub on which the truck will be

    // 0: starting
    // 1: at a hub
    // 2: on a highway
    // 3: moving to dest


    // derived classes should generate unique name for each instance
    @Override
    public String getTruckName()
    {
        return super.getTruckName() + 19043;
    }


    @Override
    public Hub getLastHub()
    {
        return this.lastHub;
    }


    @Override
    // the truck is notified that is has entered the highway through a call to its enter(Highway) method
    public void enter(Highway hwy)
    {
        if (hwy == null)
        {
            this.hwy = hwy;
            state = "destStation";
        }
        else
        {
            this.hwy = hwy;
            this.lastHub = hwy.getStart();
            this.nextHub = hwy.getEnd();
            this.speed = hwy.getMaxSpeed();
            this.elapsedTime = 0;
            state = "highway";
        }
    }



    @Override
    protected void update(int deltaT)
    {
        if (state.equals("srcStation"))
        {
            this.nextHub = Network.getNearestHub(this.getLoc());
            this.setLoc(nextHub.getLoc());
            state = "hub";
        }

        else if (state.equals("hub")) // on hub
        {
            if (this.getLoc().equals(this.getDest()) || nextHub.equals(Network.getNearestHub(getDest())))
                // the hub is the destination itself OR Truck at dest hub
                this.setLoc(getDest());

            else
            {
                if (nextHub.add(this))
                    state = "wait"; // wait stage
            }
        }

        else if (state.equals("highway")) // on a highway
        {
            elapsedTime += deltaT;

            double totalDistance = Math.sqrt(lastHub.getLoc().distSqrd(nextHub.getLoc()));
            double distanceCovered = (elapsedTime/1000.0) * speed; // time is in milisec
            double fraction = distanceCovered / totalDistance;

            if (fraction >= 1) // fraction of distance travelled; > as it might cover a little more due to rounding
            {
                setLoc(new Location(nextHub.getLoc()));
                this.hwy.remove(this);

                if (this.getLoc().equals(this.getDest())) // reached the dest
                {
                    state = "destStation";
                    return;
                }
                state = "hub";
            }

            else // more distance is left to cover
            {
                int x;
                int cos = Math.abs(lastHub.getLoc().getX() - nextHub.getLoc().getX());

                if (lastHub.getLoc().getX() <= nextHub.getLoc().getX())
                    x = (int) (lastHub.getLoc().getX() + cos * fraction);
                else
                    x = (int) (nextHub.getLoc().getX() + cos * (1 - fraction));

                int y;
                int sin = Math.abs(lastHub.getLoc().getY() - nextHub.getLoc().getY());

                if (lastHub.getLoc().getY() <= nextHub.getLoc().getY())
                    y = (int) (lastHub.getLoc().getY() + sin * fraction);
                else
                    y = (int) (nextHub.getLoc().getY() + sin * (1 - fraction));

                setLoc(new Location(x, y));
            }
        }

        else  if (state.equals("destStation")) // on the way to destStation
            this.setLoc(getDest()); // jump to the dest station

    }



    /*@Override
    // called every deltaT time to update its status/position
    // If less than startTime, does nothing
    // If at a hub, tries to move on to next highway in its route; don't do anything, Hub will do.
    // If on a road/highway, moves towards next Hub
    // If at dest Hub, moves towards dest; but truck need not handle this right?
    protected void update(int deltaT)
    {
        // update its location
        System.out.println("Call to update");

        //Case 1: if currentTime < this.startTime then do nothing
        //Case 2: If at a hub, tries to move on to next highway in its route: no need to handle this
        //Case 3: If on a road/highway, moves towards next Hub: needs to be handled
        //Case 4: If at dest Hub, moves towards dest station
        //Case 5: If at src station, then move towards nearest hub

        //increment currentTime by deltaT on each call
        //update position as x = speed * deltaT * some trig stuff

    }*/

    /*@Override
    protected synchronized void update(int deltaT)
    {
        currentTime += deltaT; // increment the time
        //if before start time
        if (currentTime < this.getStartTime()) return;

        Location startloc, endloc;

        //if location is getHubNearestToLoc(dest) - set highway to null
        int v;
        double m, sin, cos;
        //if inside the network
        if(highway != null)
        {
            //stationary
            //if(this.getLoc() == highway.getEnd().getLoc())
            if (sameSpot(this.getLoc(), highway.getEnd().getLoc()))
            {
                //if(this.getLoc() == Network.getNearestHub(this.getDest()).getLoc())
                if (sameSpot(this.getLoc(), Network.getNearestHub(this.getDest()).getLoc()))
                highway = null;

            return;
            }
            //on highway or exiting hub
            else
                {
                v = highway.getMaxSpeed();
                m = (highway.getStart().getLoc().getY() - highway.getEnd().getLoc().getY()) / (double)(highway.getStart().getLoc().getX() - highway.getEnd().getLoc().getX());
                startloc = highway.getStart().getLoc();
                    //m = (this.getLoc().getY() - highway.getEnd().getLoc().getY()) / (double)(this.getLoc().getX() - highway.getEnd().getLoc().getX());
                    //startloc = this.getLoc();
                endloc = highway.getEnd().getLoc();
                // check
            }
        }

        //network-station link
        else
            {
            //set some speed for network-station link
            v = 50;
            //start to network
            if(!flag)
            {
                m = (this.getSource().getY() - Network.getNearestHub(this.getSource()).getLoc().getY()) / (double) (this.getSource().getX() - Network.getNearestHub(this.getSource()).getLoc().getX());
                startloc = this.getSource();
                endloc = Network.getNearestHub(this.getSource()).getLoc();

            }
            //network to end
            else
                {
                //if(this.getLoc() == this.getDest())
                    if (sameSpot(this.getLoc(), this.getDest()))
                    return;

                m = (this.getDest().getY() - Network.getNearestHub(this.getDest()).getLoc().getY()) / (double) (this.getDest().getX() - Network.getNearestHub(this.getDest()).getLoc().getX());
                startloc = Network.getNearestHub(this.getDest()).getLoc();
                endloc = this.getDest();
            }
        }
        sin = (endloc.getY() < startloc.getY() ? -1 : 1) * (m / Math.pow(1 + (m * m),0.5));
        cos = (endloc.getX() < startloc.getX() ? -1 : 1) * (1 / Math.pow(1 + (m * m),0.5));
        this.setLoc(new Location(this.getLoc().getX() + (int)(v * (deltaT/300) * cos),this.getLoc().getY() + (int)(v * (deltaT/300) * sin)));
    }


    private boolean sameSpot(Location l1, Location l2)
    {
        if((l1.getY() >= l2.getY() + margin) && (l1.getY() <= l2.getY() + margin) && (l1.getX() >= l2.getX() + margin) && (l1.getX() <= l2.getX() + margin))
            return true;

        return false;
    }
        @Override
    public Hub getLastHub()
    {
        if(highway != null)
            return highway.getStart();
            //if on source-network link
        else if(!flag)
            return null;
            //from last hub to dest
        else
            return Network.getNearestHub(this.getDest());

        return this.lastHub;
}*/

        /*@Override
    // the truck is notified that is has entered the highway through a call to its enter(Highway) method
    public void enter(Highway hwy)
    {
        this.hwy = hwy;
        if (this.hwy == null)
        {
            state = 3;
            return;
        }

        state = 2;
        this.lastHub = hwy.getStart();
        this.nextHub = hwy.getEnd();
        this.speed = hwy.getMaxSpeed();
        this.elapsedTime = 0;
    }*/


     /*@Override
    protected void update(int deltaT)
    {
        if (state == 0)
        {
            this.nextHub = Network.getNearestHub(this.getLoc());

            this.setLoc(new Location(nextHub.getLoc()));
            state = 1;
        }

        else if (state == 1)
        {
            if (this.getLoc().equals(this.getDest())) // the hub is the destination itself
            {
                state = 3; // can be 4 too
                return;
            }

            if (nextHub.equals(Network.getNearestHub(getDest()))) // at dest hub
            {
                state = 3;
                return;
            }

            boolean added = nextHub.add(this);
            if (added)
                state = 5; // wait stage
        }

        else if (state == 2) // on a highway
        {
            Location lastHubLoc = lastHub.getLoc();
            Location nextHubLoc = nextHub.getLoc();

            elapsedTime += deltaT;

            double dist = Math.sqrt(lastHubLoc.distSqrd(nextHubLoc));
            double fraction = (elapsedTime/1000.0) * speed / dist; // time is in milisec

            if (fraction >= 1) // fraction of distance travelled
            {
                setLoc(new Location(nextHubLoc));
                this.hwy.remove(this);

                if (this.getLoc().equals(this.getDest())) // reached the dest
                {
                    state = 3;
                    return;
                }
                state = 1;
            }
            else
            {
                int x1 = lastHubLoc.getX();
                int x2 = nextHubLoc.getX();
                int x;

                if (x1 <= x2)
                    x = (int) (x1 + Math.abs(x1 - x2) * fraction);
                else
                    x = (int) (x2 + Math.abs(x1 - x2) * (1 - fraction));

                int y1 = lastHubLoc.getY();
                int y2 = nextHubLoc.getY();
                int y;

                if (y1 <= y2)
                    y = (int) (y1 + Math.abs(y1 - y2) * fraction);
                else
                    y = (int) (y2 + Math.abs(y1 - y2) * (1 - fraction));

                setLoc(new Location(x, y));
            }
        }

        else  if (state == 3)
        {
            this.setLoc(getDest());

            state = 4;
            // travelling at infinite speed
        }

        else  if (state == 4)
        {
            state = 5;
        }

        else
        {
            // wait time
        }
    }*/
}
