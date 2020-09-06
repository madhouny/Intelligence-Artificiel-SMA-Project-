package agents;

import java.awt.List;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AcheteurAgent extends GuiAgent {

	protected AcheteurGui gui;
	protected AID[] vendeurs;

	@Override
	protected void setup() {
		if (getArguments().length == 1) {
			gui = (AcheteurGui) getArguments()[0];
			gui.acheteurAgent = this;
		}

		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);

		// chercher des services dans DF (annuaires)
		parallelBehaviour.addSubBehaviour(new TickerBehaviour(this, 50000) {

			@Override
			protected void onTick() {
				DFAgentDescription agentDescription = new DFAgentDescription();
				ServiceDescription serviceDescription = new ServiceDescription();
				serviceDescription.setType("transaction");
				serviceDescription.setName("vente-livres");
				agentDescription.addServices(serviceDescription);
				try {
					DFAgentDescription[] results = DFService.search(myAgent, agentDescription);
					vendeurs = new AID[results.length];
					for (int i = 0; i < results.length; i++) {
						vendeurs[i] = results[i].getName();
					}
				} catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
			private int counter= 0;
			private java.util.List<ACLMessage> listAclMessages = new ArrayList<>();
			@Override
			public void action() {
				MessageTemplate messageTemplate = MessageTemplate.or(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.or(
								MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
								MessageTemplate.or(
										MessageTemplate.MatchPerformative(ACLMessage.AGREE),
										MessageTemplate.MatchPerformative(ACLMessage.REFUSE)))
						);
				ACLMessage aclMessage = receive(messageTemplate);
				if (aclMessage != null) {
					switch (aclMessage.getPerformative()) {
					case ACLMessage.REQUEST:
						String livre = aclMessage.getContent();
						ACLMessage aclMessage2  = new ACLMessage(ACLMessage.CFP);
						aclMessage2.setContent(livre);
						for (AID aid : vendeurs) {
							aclMessage2.addReceiver(aid);
						}
						send(aclMessage2);
						break;
					case ACLMessage.PROPOSE:
						// Choisir le minimum prix du livre proposés par les vendeurs
						++counter;
						listAclMessages.add(aclMessage);
						
						if(counter == vendeurs.length) {
							ACLMessage meilleurOffre = listAclMessages.get(0);
							double min = Double.parseDouble(listAclMessages.get(0).getContent());
							for(ACLMessage offre : listAclMessages) {
								double price = Double.parseDouble(offre.getContent());
								if(price < min) {
									meilleurOffre = offre;
									min = price;
								}
							}
							
							ACLMessage aclMessageAccept = meilleurOffre.createReply();
							aclMessage.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							send(aclMessageAccept);
							
						}
						break;
					case ACLMessage.AGREE:
						ACLMessage aclMessage3 = new ACLMessage(ACLMessage.CONFIRM);
						aclMessage3.addReceiver(new AID("Consumer",AID.ISLOCALNAME));
						aclMessage3.setContent(aclMessage.getContent());
						send(aclMessage3);
						break;
					case ACLMessage.REFUSE:

						break;

					default:
						break;
					}
					String livre = aclMessage.getContent();
					gui.logMessage(aclMessage);
					ACLMessage reply = aclMessage.createReply();
					reply.setContent("OK pour " + livre);
					send(reply);

					ACLMessage aclMessage3 = new ACLMessage(ACLMessage.CFP);
					aclMessage3.setContent(livre);
					aclMessage3.addReceiver(new AID("Vendeur", AID.ISLOCALNAME));
					send(aclMessage3);
				} else
					block();

			}
		});
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub

	}

}
