package Utilities;



import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;

public class AgentContainer {
    Runtime rt;
    ContainerController container;

    public AgentContainer() {
        initMainContainerInPlatform("localhost", "9888", "MainContainer");
        //a.startAgentInPlatform("HM16","Agents.AircraftAgent",xmlReader.flightSchedules().toArray());
        for(int i = 0 ; i < ScheduleFactory.getAllAirport().size() ; ++i){
            startAgentInPlatform(ScheduleFactory.getAllAirport().get(i).getName(),"Agents.StationAgent",new Object[]{ScheduleFactory.getAllAirport().get(i)});
        }
        //startAgentInPlatform(GuiAgent.name,"Agents.GuiAgent",new Object[0]);
    }

    public ContainerController initContainerInPlatform(String host, String port, String containerName) {
        // Get the JADE runtime interface (singleton)
        this.rt = Runtime.instance();

        // Create a Profile, where the launch arguments are stored
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, containerName);
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        // create a non-main agent container
        ContainerController container = rt.createAgentContainer(profile);
        return container;
    }

    public void initMainContainerInPlatform(String host, String port, String containerName) {

        // Get the JADE runtime interface (singleton)
        this.rt = Runtime.instance();

        // Create a Profile, where the launch arguments are stored
        Profile prof = new ProfileImpl();
        prof.setParameter(Profile.CONTAINER_NAME, containerName);
        prof.setParameter(Profile.MAIN_HOST, host);
        prof.setParameter(Profile.MAIN_PORT, port);
        prof.setParameter(Profile.MAIN, "true");
        prof.setParameter(Profile.GUI, "true");

        // create a main agent container
        this.container = rt.createMainContainer(prof);
        rt.setCloseVM(true);

    }

    public void startAgentInPlatform(String name, String classpath, Object arg[]) {
        try {

            AgentController ac = container.createNewAgent(name, classpath, arg);
            ac.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        AgentContainer c = new AgentContainer();
    }
}
