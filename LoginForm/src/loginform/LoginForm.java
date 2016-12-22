package loginform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

/**
 *
 * @author ashmeet
 */
public class LoginForm extends Application {

    Stage window;
    public TextField txtUser;
    public PasswordField pwBox;
    public Scene loginScene,mainWindowScene;
    
    public Connection conn = null;
    public OraclePreparedStatement pst = null;
    public OracleResultSet rs = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Login Form Window");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Welcome");
        welcomeTxt.setFont(Font.font("Tahoma", FontWeight.LIGHT, 25));
        grid.add(welcomeTxt, 0, 0);

        conn = databaseConnection();
        Label lblUser = new Label("Username");
        grid.add(lblUser, 0, 1);

        txtUser = new TextField();
        txtUser.setPromptText("username");
        grid.add(txtUser, 1, 1);

        Label lblPassword = new Label("Password");
        grid.add(lblPassword, 0, 2);

        pwBox = new PasswordField();
        pwBox.setPromptText("password");
        grid.add(pwBox, 1, 2);

        Button loginBtn = new Button("Login");
        grid.add(loginBtn, 1, 3);
        loginBtn.setOnAction(e -> {
            if(loginAction()){
                window.setScene(mainWindow());
                //opening Main window
            }
        });

        loginScene = new Scene(grid, 500, 500);
        window.setScene(loginScene);
        window.show();

    }

    public static Connection databaseConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "Ashmeet", "system");
            System.out.println("Connection succeed");
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public Boolean loginAction() {
        try {
            String query = "select username,password from studentTable where username=? and password = ?";
            pst = (OraclePreparedStatement) conn.prepareStatement(query);
            pst.setString(1, txtUser.getText());
            pst.setString(2, pwBox.getText());
            rs = (OracleResultSet)pst.executeQuery();
            if(rs.next()){
                System.out.println("Login Successful");
                return true;

            }else{
                System.out.println("Invalid login Credentials");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public Scene mainWindow(){
        Button logoutBtn=new Button("Logout");
        logoutBtn.setOnAction(e->{
            window.setScene(loginScene);
        });
        StackPane stackPane=new StackPane();
        stackPane.getChildren().add(logoutBtn);
        mainWindowScene=new Scene(stackPane,600,500);
        return mainWindowScene;
    }
}
