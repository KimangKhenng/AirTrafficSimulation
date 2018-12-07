package Utilities;

public interface AgentMessage {
    String runWayRequestId = "Runway-Request";
    String runWayRequestMessage = "Are there any available runway?";
    String runWayOfferMessage = "This runway is available:";
    String runWayReleaseMessage = "I am done using is way:";
    String runWayNotAvailable = "There are no available runways";
    String stationAgentType ="I am station agent";
    String aircraftAgentType = "I am aircraft agent";
    String guiAgentType = "I am GUI agnet";
    String locationRequestID = "What is your current runway?";
    String routeSwitchingRequestID = "Can you change the route to avoid collision?";
    String cantSwitchRouteID = "I cannot switch my route because I am in special condition";
    String getLocationRequestAnswerID = "This is my coordinates";
}
