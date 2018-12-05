package Agents;

import GUI.AgentInterface;
import Utilities.AgentMessage;
import Utilities.FlightSchedule;
import Utilities.FlightStatus;
import Utilities.ScheduleFactory;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.time.LocalDateTime;
import java.util.List;


public class AircraftAgent extends BaseAgent {
    // Behavior Instance
    positionUpdateBehaviour positionUpdateBehaviour;
    /**
     * Private Members
     */

    // Aircraft GPS Information

    private LatLng origin;
    private LatLng destination;
    private int currentWayPoint;
    private double speed;
    private double altitude;
    private LocalDateTime departTime;
    private LocalDateTime ArrivalTime;
    // Aircraft Flight Schedule
    //private List<FlightSchedule> schedule;
    private FlightSchedule currentSchedule;
    private double remainingDistance;

    // Aircraft Actual Arrive/Departure Information

    @Override
    protected void setup() {
        super.setup();
        /**
         * Agent initializations
         */
        currentWayPoint = 0;
        alertZone = 3.0;
        dangerZone = 0.5;

        /**
         * Initialize Flights Schedule
         */
        Object[] obj = getArguments();
        if (obj != null) {
            currentSchedule = (FlightSchedule) obj[0];
            speed = currentSchedule.getSpeed();
            altitude = currentSchedule.getAltitude();
            position = currentSchedule.getOrigin().getLocation();
        }

        remainingDistance = currentSchedule.getDistance();

        /**
         * Register with DF
         */
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(AgentMessage.aircraftAgentType);
        serviceDescription.setName(getLocalName() + AgentMessage.stationAgentType);
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        /**
         * Add behavior
         */
        addBehaviour(new statusChecker());
        addBehaviour(new updateStatus(this, 3000));
        //addBehaviour(new AreaProximityBehavior(this,1000));
        addBehaviour(new informAgentInterface(this, 1000));

    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }


    /**
     * Inner Class
     */
    private class informAgentInterface extends TickerBehaviour {
        public informAgentInterface(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            AgentInterface.getInstance().setData(myAgent.getLocalName(), position, speed,
                    currentSchedule.getStatus().toString(), currentWayPoint, remainingDistance);
        }
    }

        private class positionUpdateBehaviour extends TickerBehaviour {
        /**
         * positionUpdateBehavior
         */
        LatLng nextPos;

        public positionUpdateBehaviour(Agent a, long period) {
            super(a, period);
            nextPos = currentSchedule.getFlightTrajectories().get(currentWayPoint + 1);
        }

        protected void onTick() {
            if (LatLngTool.distance(position, nextPos, LengthUnit.KILOMETER) <= 1.0) {
                //currentWayPoint += 1;
                nextPos = currentSchedule.getFlightTrajectories().get(++currentWayPoint);
            }
            double dx = (nextPos.getLatitude() - position.getLatitude())/ speed;
            double dy = (nextPos.getLongitude() - position.getLongitude()) / speed;
            position.setLatitudeLongitude(position.getLatitude() + dx, position.getLongitude() + dy);
            remainingDistance = remainingDistance(position);
        }
    }
//    private class positionUpdateBehaviour extends CyclicBehaviour {
//        /**
//         * positionUpdateBehavior
//         */
//        LatLng nextPos;
//
//        public void action() {
//            if (LatLngTool.distance(position, nextPos, LengthUnit.KILOMETER) <= 1.0) {
//                //currentWayPoint += 1;
//                nextPos = currentSchedule.getFlightTrajectories().get(++currentWayPoint);
//            }
//            double dx = (nextPos.getLatitude() - position.getLatitude()) / speed;
//            double dy = (nextPos.getLongitude() - position.getLongitude()) / speed;
//            position.setLatitudeLongitude(position.getLatitude() + dx, position.getLongitude() + dy);
//        }
//    }
    private class walkWithOtherAgents extends TickerBehaviour{

    public walkWithOtherAgents(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {

    }
}

    private class statusChecker extends CyclicBehaviour {
        private int runway;
        private int delayTime;
        private boolean runwayRequested;
        private boolean boarding;
        private boolean departed;
        private boolean arrived;

        @Override
        public void action() {
            /**
             * Update Status
             */
            if (!departed) {
                int remainingTime = currentSchedule.getDepartTime().getSecond() - LocalDateTime.now().getSecond() + delayTime;
                int checkInTime = ScheduleFactory.agentStartTime - 2;
                int boardingTime = ScheduleFactory.agentStartTime - 5;
                //System.out.println("Remaining Time: " + remainingTime);
                //System.out.println("Checking Time: " + checkInTime);
                //System.out.println("Boarding Time: " + boardingTime);
                if (remainingTime >= checkInTime) {
                    /**
                     * Call for check-in
                     */
                    currentSchedule.setStatus(FlightStatus.CHECKINGIN);
                }
                if (remainingTime < checkInTime)
                    if (remainingTime == boardingTime) {
                        currentSchedule.setStatus(FlightStatus.BOARDING);
                        if (!runwayRequested) {
                            addBehaviour(new runWayRequest());
                            runwayRequested = true;
                        }

                    }
                if (remainingTime < boardingTime) {
                    /**
                     * Prepare to fly
                     */
                    if (!boarding) {
                        MessageTemplate template = MessageTemplate.MatchConversationId(AgentMessage.runWayRequestId);
                        ACLMessage msg = receive(template);
                        if (msg != null) {
                            System.out.println("Receive Reply");
                            if (msg.getPerformative() == ACLMessage.AGREE) {
                                String runWay = msg.getContent().split(":")[1];
                                runway = Integer.parseInt(runWay);
                                System.out.println("Will depart on Runway: " + runway);
                                boarding = true;
                            } else if (msg.getPerformative() == ACLMessage.REFUSE) {
                                /**
                                 * will wait?
                                 */
                                currentSchedule.setStatus(FlightStatus.DELAYED);
                                delayTime += 1;
                            }
                        } else {
                            block();
                        }
                    }

                }
                if (remainingTime == 0) {
                    currentSchedule.setStatus(FlightStatus.DEPARTED);
                    positionUpdateBehaviour = new positionUpdateBehaviour(myAgent, TIME_STEP);
                    //positionUpdateBehaviour = new positionUpdateBehaviour();
                    addBehaviour(positionUpdateBehaviour);
                    departed = true;
                    /**
                     * Release runway
                     */
//                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//                msg.setContent(AgentMessage.runWayReleaseMessage+runway);
//                msg.addReceiver(new AID(currentSchedule.getOrigin().getName(),AID.ISLOCALNAME));
//                myAgent.send(msg);
                    //System.out.println("Flying");
                    departTime = LocalDateTime.now();

                }
            }


            /**
             * Request runway again because the plane is close to the airport
             */

            if (currentWayPoint == currentSchedule.getFlightTrajectories().size() - 1) {
                if(!arrived){
                    addBehaviour(new runWayRequest(currentSchedule.getDestination().getName()));
                    arrived = true;
                }
            }
            if (currentWayPoint == currentSchedule.getWayPoints()) {
                myAgent.removeBehaviour(positionUpdateBehaviour);
                currentSchedule.setStatus(FlightStatus.ARRIVED);
                ArrivalTime = LocalDateTime.now();
                myAgent.removeBehaviour(this);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            delayTime = 0;
            runway = 0;
        }
    }

    private class runWayRequest extends OneShotBehaviour {
        /**
         * runWayRequest
         */
        private String stationName;

        public runWayRequest(String stationName) {
            this.stationName = stationName;
        }

        public runWayRequest() {
            stationName = currentSchedule.getOrigin().getName();
        }

        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

            msg.setContent(AgentMessage.runWayReleaseMessage);
            msg.setLanguage("English");
            msg.setConversationId(AgentMessage.runWayRequestId);
            /**
             * Search for station agent
             */
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(AgentMessage.stationAgentType);
            sd.setName(stationName + AgentMessage.stationAgentType);
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                if (result.length > 0) {
                    msg.addReceiver(result[0].getName());
                } else {
                    System.out.println("No Agent");
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
            //AID station = new AID(stationName,AID.ISLOCALNAME);

            myAgent.send(msg);
        }
    }
    private double remainingDistance(LatLng position){
        double totalDistance = 0.0;
        List<LatLng> flightSchedules = currentSchedule.getFlightTrajectories();
        flightSchedules.set(currentWayPoint,position);
        for(int i = 0 ; i < flightSchedules.size() && i != flightSchedules.size() - 2 ; ++i){
            totalDistance += LatLngTool.distance(flightSchedules.get(i),flightSchedules.get(i+1), LengthUnit.KILOMETER);
        }
        return totalDistance;
    }

    private class AreaProximityBehavior extends TickerBehaviour {

        public AreaProximityBehavior(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {


        }
    }

    protected class updateStatus extends TickerBehaviour {


        public updateStatus(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            /**
             * Update Info
             */
            System.out.println(myAgent.getLocalName());
            //System.out.println("Status: " + currentSchedule.getStatus());
            System.out.println(myAgent.getLocalName() + "Lat: " + position.getLatitude() + "Long: " + position.getLongitude());
            System.out.println("My current way point: " + currentWayPoint);
        }
    }


}
