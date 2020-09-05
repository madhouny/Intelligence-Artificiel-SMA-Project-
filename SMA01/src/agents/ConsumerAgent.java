package agents;

import containers.ConsumerContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.proto.states.ReplySender;
import jade.tools.gui.ACLAIDDialog;
import jade.lang.acl.ACLMessage;

public class ConsumerAgent extends GuiAgent {

	private transient ConsumerContainer gui;

	@Override
	protected void setup() {
		if (getArguments().length == 1) {
			gui = (ConsumerContainer) getArguments()[0];
			gui.setConsumerAgent(this);
		}
			ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
			addBehaviour(parallelBehaviour);

			parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

				@Override
				public void action() {

					jade.lang.acl.ACLMessage aclMsg = receive();
					if (aclMsg != null) {
						gui.logMessage(aclMsg);
						
					} else
						block();
				}
			});

		
		/*
		 * parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,1000) {
		 * 
		 * @Override protected void onTick() { System.out.println("tic");
		 * 
		 * } });
		 * 
		 * 
		 * parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
		 * 
		 * @Override public void action() { System.out.println("Action ....");
		 * 
		 * } });
		 * 
		 * 
		 * 
		 * parallelBehaviour.addSubBehaviour(new Behaviour() { private int compteur = 0;
		 * 
		 * @Override public boolean done() { if(compteur == 10) return true; else return
		 * false; }
		 * 
		 * @Override public void action() { ++compteur;
		 * System.out.println("Etape "+compteur);
		 * 
		 * } });
		 */

	}

	@Override
	protected void beforeMove() {
		System.out.println("......");
		System.out.println("Avant Migration");
		System.out.println("......");
	}

	@Override
	protected void afterMove() {
		System.out.println("......");
		System.out.println("Après Migration");
		System.out.println("......");
	}

	@Override
	protected void takeDown() {
		System.out.println("......");
		System.out.println("i'm going to die");
		System.out.println("......");
	}

	@Override
	public void onGuiEvent(GuiEvent params) {
		if(params.getType()==1) {
			String livre =  params.getParameter(0).toString();
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			aclMessage.setContent(livre);
			aclMessage.addReceiver(new AID("Acheteur",AID.ISLOCALNAME));
			send(aclMessage);
			
		}
	}
}
