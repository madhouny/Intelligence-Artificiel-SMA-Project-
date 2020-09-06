package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VendeurGui extends Application {
	protected VendeurAgent vendeurAgent;
	ObservableList<String> list;
	AgentContainer container;
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		startContainer();
		primaryStage.setTitle("Vendeur");
		
		// deployer l'agent vendeur avec un button exemple vendeur 1 , 2 etc  
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10));
		hBox.setSpacing(10);
		Label label = new Label("Agent Name:");
		TextField fieldAgentName = new TextField();
		Button buttonDeployment = new Button("Deployer");
		
		hBox.getChildren().addAll(label,fieldAgentName,buttonDeployment);
		
	
		BorderPane borderPane = new BorderPane();
		VBox vbox = new VBox();
		list = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<>(list);
		vbox.getChildren().add(listView);
		borderPane.setTop(hBox);
		borderPane.setCenter(vbox);
		Scene scene = new Scene(borderPane,400,300);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonDeployment.setOnAction((e)->{
			String name = fieldAgentName.getText();
			AgentController agentController;
			try {
				agentController = container.createNewAgent(name, "agents.VendeurAgent", new Object[] {this});
				agentController.start();
			} catch (StaleProxyException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		
		
	}
	private void startContainer() throws ControllerException {
		// deployer l'agent acheteur
		Runtime runtime = Runtime.instance();
		ProfileImpl impl = new ProfileImpl();
		impl.setParameter(impl.MAIN_HOST, "localhost");
		 container = runtime.createAgentContainer(impl);
		container.start();
	}
	
	public void logMessage(ACLMessage aclMessage) {
		Platform.runLater(()->{
			list.add(aclMessage.getContent()
					+", " +aclMessage.getSender().getName()
					+", "+ aclMessage.getPerformative());
		});
		
	}

}
