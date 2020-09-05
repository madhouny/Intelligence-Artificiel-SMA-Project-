package agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.introspection.ACLMessage;
import jade.proto.states.ReplySender;
import jade.tools.gui.ACLAIDDialog;


public class ConsumerAgent  extends Agent{

	@Override
	protected void setup() {
		System.out.println(".......");
		System.out.println("Agent Initialisation..."+ this.getAID().getName());
		if(this.getArguments().length >=1) {
			System.out.println("je vais tenter d'acheter le livre "+getArguments()[0]);
		}
		
		System.out.println(".......");
		
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {

				jade.lang.acl.ACLMessage aclMsg = receive();
				if(aclMsg != null) {
					System.out.println("**********");
					System.out.println("Reception du message");
					System.out.println(aclMsg.getSender().getName());
					System.out.println(aclMsg.getContent());
					System.out.println(aclMsg.getPerformative());
					System.out.println(aclMsg.getLanguage());
					System.out.println(aclMsg.getOntology());
					System.out.println("**********");
					
					jade.lang.acl.ACLMessage reply = aclMsg.createReply();
					reply.setContent("Bien fait !!");
					reply.addReceiver(aclMsg.getSender());
					send(reply);
				}
				else block();
			}
		});
		
		
		/*parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,1000) {
			
			@Override
			protected void onTick() {
				System.out.println("tic");
				
			}
		});
		
		
		parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				System.out.println("Action ....");
				
			}
		});
		
		
		
		parallelBehaviour.addSubBehaviour(new Behaviour() {
			private int compteur = 0;
			
			@Override
			public boolean done() {
				if(compteur == 10) return true;
				else return false;
			}
			
			@Override
			public void action() {
				++compteur;
				System.out.println("Etape "+compteur);
				
			}
		});
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
}
