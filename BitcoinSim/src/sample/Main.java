package sample;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

    public static GlobalDataHandler gdh;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gdh = new GlobalDataHandler();
        primaryStage.setTitle("Bitcoin Simulator");

        double verticalSpacing = 5;
        double horizontalSpacing = 3;
        StackPane root = new StackPane();
        TabPane tabPane = new TabPane();
        BorderPane viewUsersBorder = new BorderPane();
        BorderPane viewTransactionsBorder = new BorderPane();

        //creating the four tabs
        Tab usersView = new Tab();
        Tab transactionsView = new Tab();
        Tab addUsersView = new Tab();
        Tab moneySendView = new Tab();
        usersView.closableProperty().setValue(false);
        transactionsView.closableProperty().setValue(false);
        addUsersView.closableProperty().setValue(false);
        moneySendView.closableProperty().setValue(false);
        usersView.setText("View Users");
        transactionsView.setText("View Transactions");
        addUsersView.setText("Add Users");
        moneySendView.setText("Send Money");

        //view users tab
        ListView<String> userListView = new ListView();
        VBox userInfoVBox = new VBox();
        HBox nameHBox = new HBox(horizontalSpacing);
        HBox pubKeyHBox = new HBox(horizontalSpacing);
        HBox privKeyHBox = new HBox(horizontalSpacing);
        HBox totalFundsHBox = new HBox(horizontalSpacing);
        HBox unspentOutputsHBox = new HBox(horizontalSpacing);

        Label nameLabel = new Label("Name:");
        Label nameValueLabel = new Label();
        Label pubKeyLabel = new Label("Public Key:");
        Label pubKeyValueLabel = new Label();
        Label privKeyLabel = new Label("Private Key:");
        Label privKeyValueLabel = new Label();
        Label totalFundsLabel = new Label("Total Funds:");
        Label totalFundsValueLabel = new Label();
        Label unspentOutputsLabel = new Label("Total Number of Unspent Ouputs:");
        Label unspentOutputsValueLabel = new Label();

        nameHBox.getChildren().addAll(nameLabel,nameValueLabel);
        pubKeyHBox.getChildren().addAll(pubKeyLabel,pubKeyValueLabel);
        privKeyHBox.getChildren().addAll(privKeyLabel,privKeyValueLabel);
        totalFundsHBox.getChildren().addAll(totalFundsLabel,totalFundsValueLabel);
        unspentOutputsHBox.getChildren().addAll(unspentOutputsLabel,unspentOutputsValueLabel);

        userInfoVBox.getChildren().addAll(nameHBox,pubKeyHBox,privKeyHBox,totalFundsHBox,unspentOutputsHBox);
        viewUsersBorder.setLeft(userListView);
        viewUsersBorder.setCenter(userInfoVBox);

        //view transactions tab
        ListView<String> txListView = new ListView();
        txListView.setPrefWidth(460);
        VBox txInfoVBox = new VBox();
        HBox senderViewHBox = new HBox(horizontalSpacing);
        HBox receiverViewHBox = new HBox(horizontalSpacing);
        HBox amountSentHBox = new HBox(horizontalSpacing);
        HBox amountReceivedHBox = new HBox(horizontalSpacing);
        HBox amountChangeHBox = new HBox(horizontalSpacing);

        Label senderLabel = new Label("Sender:");
        Label senderNameLabel = new Label();
        Label receiverLabel = new Label("Receiver:");
        Label receiverNameLabel = new Label();
        Label amountSentLabel = new Label("Amount Sent:");
        Label amountSentValueLabel = new Label();
        Label amountRecievedLabel = new Label("Amount Received:");
        Label amountReceivedValueLabel = new Label();
        Label changeLabel = new Label("Amount in Change:");
        Label changeValueLabel = new Label();

        senderViewHBox.getChildren().addAll(senderLabel,senderNameLabel);
        receiverViewHBox.getChildren().addAll(receiverLabel,receiverNameLabel);
        amountSentHBox.getChildren().addAll(amountSentLabel,amountSentValueLabel);
        amountReceivedHBox.getChildren().addAll(amountRecievedLabel,amountReceivedValueLabel);
        amountChangeHBox.getChildren().addAll(changeLabel,changeValueLabel);

        txInfoVBox.getChildren().addAll(senderViewHBox,receiverViewHBox,amountSentHBox,amountReceivedHBox,
                amountChangeHBox);
        viewTransactionsBorder.setLeft(txListView);
        viewTransactionsBorder.setCenter(txInfoVBox);

        //send money tab
        VBox moneySendVBox = new VBox(verticalSpacing);
        HBox senderHBox = new HBox(horizontalSpacing);
        HBox receiverHBox = new HBox(horizontalSpacing);
        HBox amountHBox = new HBox(horizontalSpacing);

        CheckBox coinbaseCheckbox = new CheckBox("Coinbase");
        ChoiceBox<String> senderChoice = new ChoiceBox();
        ChoiceBox<String> receiverChoice = new ChoiceBox();
        Button sendButton = new Button("Send");
        Label payerLabel = new Label("Sender:");
        Label payeeLabel = new Label("Receiver:");
        Label amountLabel = new Label("Amount:");
        Slider moneySlide = new Slider();
        moneySlide.setShowTickMarks(true);
        moneySlide.setShowTickLabels(true);
        TextField amountInput = new TextField();
        amountInput.setEditable(false);

        senderHBox.getChildren().addAll(payerLabel,senderChoice);
        receiverHBox.getChildren().addAll(payeeLabel,receiverChoice);
        amountHBox.getChildren().addAll(amountLabel,moneySlide,amountInput);
        moneySendVBox.getChildren().addAll(coinbaseCheckbox,senderHBox,receiverHBox,amountHBox,sendButton);

        //add users tab
        HBox nameInputHBox = new HBox();
        Label addNameLabel = new Label("Name:");
        TextField nameInput = new TextField();
        nameInputHBox.setSpacing(horizontalSpacing);
        nameInputHBox.getChildren().addAll(addNameLabel,nameInput);
        Button addNameButton = new Button("Add");
        VBox createUserVBox = new VBox();
        createUserVBox.setSpacing(verticalSpacing);
        createUserVBox.getChildren().addAll(nameInputHBox,addNameButton);

        //adding layouts to each tab
        moneySendView.setContent(moneySendVBox);
        usersView.setContent(viewUsersBorder);
        transactionsView.setContent(viewTransactionsBorder);
        addUsersView.setContent(createUserVBox);

        tabPane.getTabs().addAll(usersView, transactionsView, moneySendView, addUsersView);
        root.getChildren().add(tabPane);
        primaryStage.setScene(new Scene(root, 700, 200));
        primaryStage.show();

        senderChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
            moneySlide.setMax(gdh.findUserByName(senderChoice.getSelectionModel().getSelectedItem()).getTotalFunds());
        });

        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            User currUser = gdh.findUserByName(userListView.getSelectionModel().getSelectedItem());
            nameValueLabel.setText(currUser.getName());
            pubKeyValueLabel.setText(currUser.getPublicKey());
            privKeyValueLabel.setText(currUser.getPrivateKey());
            totalFundsValueLabel.setText(Integer.toString(currUser.getTotalFunds()));
            unspentOutputsValueLabel.setText(Integer.toString(currUser.getUnspentOutputs().size()));
        });

        txListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //substring to remove the first part
            //int index = Integer.parseInt(txListView.getSelectionModel().getSelectedItem().substring(12));
            Tx currTx = gdh.hashToTx.get(txListView.getSelectionModel().getSelectedItem());
            senderNameLabel.setText(currTx.getSenderName());
            receiverNameLabel.setText(currTx.getReceiverName());
            amountSentValueLabel.setText(Integer.toString(currTx.getAmountSent()));
            amountReceivedValueLabel.setText(Integer.toString(currTx.getAmountReceived()));
            changeValueLabel.setText(Integer.toString(currTx.getChangeAmount()));
        });

        moneySlide.valueProperty().addListener((observable, oldValue, newValue) -> {
            amountInput.setText(Integer.toString((int)moneySlide.getValue()));
        });

        sendButton.setOnAction(event -> {
            if(!receiverChoice.getSelectionModel().isEmpty() &&
                    (coinbaseCheckbox.isSelected()||!senderChoice.getSelectionModel().isEmpty())){
                if(amountInput.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Cannot leave amount field empty");
                    alert.showAndWait();

                }else if(Integer.parseInt(amountInput.getText()) > moneySlide.getMax()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Sender does not have enough funds!");
                    alert.showAndWait();
                }else{
                    if(coinbaseCheckbox.isSelected()){
                        Tx newTx = Tx.createCoinbaseTx(gdh.findUserByName(receiverChoice.getValue()),
                                Integer.parseInt(amountInput.getText()));
                        txListView.getItems().add(String.valueOf(gdh.txToHash.get(newTx)));
                        amountInput.clear();
                    } else{
                        if(senderChoice.getValue().equals(receiverChoice.getValue())){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Cannot send to self");
                            alert.showAndWait();
                        }else{
                            User sender = gdh.findUserByName(senderChoice.getValue());
                            Tx newTx = sender.send(gdh.findUserByName(receiverChoice.getValue()),
                                    Integer.parseInt(amountInput.getText()));
                            txListView.getItems().add(String.valueOf(gdh.txToHash.get(newTx)));
                            amountInput.clear();
                        }
                    }
                }
            }else{
                if(receiverChoice.getSelectionModel().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Cannot leave receiver field empty");
                    alert.showAndWait();
                }else if(senderChoice.getSelectionModel().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Cannot leave sender field empty unless the transaction is from the coinbase");
                    alert.showAndWait();
                }

            }
        });

        coinbaseCheckbox.setOnAction(event -> {
            if(coinbaseCheckbox.isSelected()){
                senderChoice.setDisable(true);
                payerLabel.setDisable(true);
                moneySlide.setMax(100);
            }else{
                senderChoice.setDisable(false);
                payerLabel.setDisable(false);
            }
        });

        addNameButton.setOnAction(event -> {
            if (nameInput.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Field cannot be empty!");
                alert.showAndWait();
            } else if(gdh.checkForDuplicateName(nameInput.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This name has already been used!");
                alert.showAndWait();
            }else{
                User newUser = new User(nameInput.getText());
                nameInput.clear();
                senderChoice.getItems().add(newUser.getName());
                receiverChoice.getItems().add(newUser.getName());
                userListView.getItems().add(newUser.getName());
            }
        });
    }
}