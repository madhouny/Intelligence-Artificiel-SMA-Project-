package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class ConsumerContainer {

	public static void main(String[] args) throws ControllerException {
		
		Runtime runtime = Runtime.instance();
		ProfileImpl impl = new ProfileImpl();
		impl.setParameter(impl.MAIN_HOST, "localhost");
		AgentContainer container = runtime.createAgentContainer(impl);
		AgentController agentController = container.createNewAgent("Consumer", "agents.ConsumerAgent", new Object [] {"xml"});
		
		agentController.start();
		

	}

}
