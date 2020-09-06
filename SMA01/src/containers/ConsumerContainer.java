package containers;



import com.sun.glass.ui.Application;

import agents.ConsumerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
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

public class ConsumerContainer  extends javafx.application.Application {
	ObservableList<String> observableList;
	protected ConsumerAgent consumerAgent;
	public static void main(String[] args) throws ControllerException {
		launch(args);
	}
	
	public void startContainer() throws Exception {
		Runtime runtime = Runtime.instance();
		ProfileImpl impl = new ProfileImpl();
		impl.setParameter(impl.MAIN_HOST, "localhost");
		AgentContainer container = runtime.createAgentContainer(impl);
		AgentController agentController = container.createNewAgent("Consumer", "agents.ConsumerAgent", new Object [] {this});
		agentController.start();
	}

	public ConsumerAgent getConsumerAgent() {
		return consumerAgent;
	}

	public void setConsumerAgent(ConsumerAgent consumerAgent) {
		this.consumerAgent = consumerAgent;
	}
	public void logMessage(ACLMessage aclMessage) {
		Platform.runLater(()->{
		observableList.add(aclMessage.getContent()
		+", " +aclMessage.getSender().getName()
		+", "+ aclMessage.getPerformative());
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		startContainer();
		primaryStage.setTitle("Consumer");
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10));
		hBox.setSpacing(10);
		Label label = new Label("Livres:");
		TextField textFieldlivre = new TextField();
		Button buttonAcheter = new Button("Acheter");
		hBox.getChildren().addAll(label,textFieldlivre,buttonAcheter);
		
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10));
		 observableList = FXCollections.observableArrayList();
		
		ListView<String> listViewMessages = new ListView<>(observableList);
		vBox.getChildren().add(listViewMessages);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(hBox);
		borderPane.setCenter(vBox);
		
		Scene scene = new Scene(borderPane,600,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonAcheter.setOnAction(e->{
			String livre = textFieldlivre.getText();
			//observableList.add(livre);
			//textFieldlivre.clear();
			GuiEvent event = new GuiEvent(this, 1);
			event.addParameter(livre);
			consumerAgent.onGuiEvent(event);
			
		});
	}

}
