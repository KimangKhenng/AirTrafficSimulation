package Agents;


import Utilities.AgentMessage;
import Utilities.Airport;
import Utilities.FlightSchedule;
import Utilities.ScheduleFactory;
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
import jade.wrapper.AgentController;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class StationAgent extends BaseAgent{

    /**
     * Inner Class
     */
    private class InitiateAircraft extends OneShotBehaviour{

        @Override
        public void action() {
            List<FlightSchedule> schedules = ScheduleFactory.searchSchedule(airport.getName());
            System.out.println("Number of Schedules: " + myAgent.getLocalName() + " " +schedules.size());
            for(int i = 0 ; i < schedules.size() ; ++i){
                String Prefix = myAgent.getName().substring(0,2).toUpperCase();
                String agentName = Prefix + "0" + i;
                try {
                    AgentController controller = myAgent.getContainerController().createNewAgent(agentName,"Agents.AircraftAgent",
                            new Object[]{ScheduleFactory.searchSchedule(myAgent.getLocalName()).get(i)});
                    aircraftAgent.add(agentName);
                    controller.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
    private class runWayRequestServer extends CyclicBehaviour {
        private MessageTemplate template = MessageTemplate.MatchConversationId(AgentMessage.runWayRequestId);


        /**
         * Handle runway requests from airplane
         */
        public void action() {
            ACLMessage msg = receive(template);
            if(msg != null){
                System.out.println("Received request");
                String content = msg.getContent();
                if(content.equals(AgentMessage.runWayRequestMessage)){
                    ACLMessage reply = msg.createReply();
                    for (int i = 0 ; i < runWay.size() ; ++i)
                        if (runWay.get(i)) {
                            reply.setConversationId(AgentMessage.runWayRequestId);
                            reply.setPerformative(ACLMessage.AGREE);
                            reply.setContent(AgentMessage.runWayOfferMessage + i);
                            //System.out.println("Sent request to: " + runWay.get(i));
                            myAgent.send(reply);
                            runWay.set(0,false);
                        }else {
                            reply.setPerformative(ACLMessage.REFUSE);
                            reply.setContent(AgentMessage.runWayNotAvailable);
                            myAgent.send(reply);
                        }
                }
            }else{
                /**
                 * Block Behavior
                 */
                block();
            }
        }
    }
    private class talkWithOtherAgent extends TickerBehaviour{
        /**
         * Talk with other agent
         */

        public talkWithOtherAgent(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {

        }
    }
    private class AreaProximityBehavior extends TickerBehaviour{

        public AreaProximityBehavior(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {


        }
    }
    /**
     * Member variable
     */
    private Vector<String> aircraftAgent;
    private List<Boolean> runWay;
    private Airport airport;

    @Override
    protected void setup() {
        super.setup();
        /**
         * Initialize members
         */
        aircraftAgent = new Vector<>();
        runWay = new ArrayList<>();
        //System.out.println("Number of run way" + runWay.size());
        for(int i = 0 ; i < 4 ; ++i){
            runWay.add(true);
        }

        /**
         * Retrieve Argument
         */
        airport = (Airport) this.getArguments()[0];


        /**
         * Register with DF
         */
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(AgentMessage.stationAgentType);
        serviceDescription.setName( getLocalName() + AgentMessage.stationAgentType );
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription); }
        catch (FIPAException fe) { fe.printStackTrace();
        }

        // Add Behaviour
        addBehaviour(new InitiateAircraft());
        addBehaviour(new runWayRequestServer());
        //addBehaviour(new AreaProximityBehavior(this,1000));
        //addBehaviour(new informGUIPosition());
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
