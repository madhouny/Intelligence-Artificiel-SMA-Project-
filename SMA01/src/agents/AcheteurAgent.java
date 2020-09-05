package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class AcheteurAgent extends GuiAgent  {

	protected AcheteurGui gui;
	@Override
	protected void setup() {
		if(getArguments().length == 1) {
			gui = (AcheteurGui) getArguments()[0];
			gui.acheteurAgent=this;
		}
		
		ParallelBehaviour parallelBehaviour  = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				
				ACLMessage aclMessage = receive();
				if(aclMessage != null) {
					String livre  = aclMessage.getContent();
					gui.logMessage(aclMessage);
					ACLMessage reply = aclMessage.createReply();
					reply.setContent("OK pour "+ livre);
					send(reply);
					
					ACLMessage aclMessage3 = new ACLMessage(ACLMessage.CFP);
					aclMessage3.setContent(livre);
					aclMessage3.addReceiver(new AID("Vendeur",AID.ISLOCALNAME));
					send(aclMessage3);
				}
				else block();
				
			}
		});
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
