package Agents;

import Utilities.AgentMessage;
import com.javadocmd.simplelatlng.LatLng;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.text.DateFormat;

public abstract class BaseAgent extends Agent {
    /**
     * Static Variable
     */
    public static int TIME_STEP;


    static {
        TIME_STEP = 16;
    }

    public String getServiceName() {
        return serviceName;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public LatLng getPosition() {
        return position;
    }

    public double getAlertZone() {
        return alertZone;
    }

    public double getDangerZone() {
        return dangerZone;
    }

    // Agent Identification
    protected String serviceName;

    protected DateFormat dateFormat;
    protected LatLng position;

    // Diameter (in Kilometer) used to determined surrounding area for calculating area proximity
    protected double alertZone = 0;
    protected double dangerZone = 0;
    MessageTemplate LocationReqestTemplate;
    MessageTemplate routeSwitchingTemplate;
    MessageTemplate refuseRouteSwitchingTemplate;
    MessageTemplate locationAnswer;
    @Override
    protected void setup() {
        super.setup();
        setEnabledO2ACommunication(true,0);
        LocationReqestTemplate = MessageTemplate.MatchConversationId(AgentMessage.locationRequestID);
        routeSwitchingTemplate = MessageTemplate.MatchConversationId(AgentMessage.routeSwitchingRequestID);
        refuseRouteSwitchingTemplate = MessageTemplate.MatchConversationId(AgentMessage.cantSwitchRouteID);
        locationAnswer = MessageTemplate.MatchConversationId(AgentMessage.getLocationRequestAnswerID);

    }

}
