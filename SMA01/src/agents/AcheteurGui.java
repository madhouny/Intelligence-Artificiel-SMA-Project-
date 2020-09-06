package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AcheteurGui extends Application {
	protected AcheteurAgent acheteurAgent;
	ObservableList<String> list;
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		startContainer();
		primaryStage.setTitle("Acheteur");
		BorderPane borderPane = new BorderPane();
		VBox vbox = new VBox();
		list = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<>(list);
		vbox.getChildren().add(listView);
		borderPane.setCenter(vbox);
		Scene scene = new Scene(borderPane,400,300);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	private void startContainer() throws StaleProxyException {
		// deployer l'agent acheteur
		Runtime runtime = Runtime.instance();
		ProfileImpl impl = new ProfileImpl();
		impl.setParameter(impl.MAIN_HOST, "localhost");
		AgentContainer container = runtime.createAgentContainer(impl);
		AgentController agentController = container.createNewAgent("Acheteur", "agents.AcheteurAgent", new Object[] {this});
		agentController.start();
	}
	
	public void logMessage(ACLMessage aclMessage) {
		Platform.runLater(()->{
			list.add(aclMessage.getContent()
					+", " +aclMessage.getSender().getName()
					+", "+ aclMessage.getPerformative());
		});
		
	}

}
