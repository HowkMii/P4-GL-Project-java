package application;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.*;



import static javafx.scene.shape.StrokeLineCap.SQUARE;

public class Main extends Application {

	private static int precedent;
	private static int nbTour;

	private designe [][] cases;

	public void start(Stage primaryStage) {
		try {


			int N = 4; int C = 7; int L = 6;

			Color couleurFond = Color.WHITE;
			Group root = new Group();
			Scene scene = new Scene(root,700,600 + 50);
			scene.setFill(couleurFond);


			When w = Bindings.when((scene.widthProperty().divide(scene.heightProperty())).greaterThan(7.0/6.0));


			Rectangle r = new Rectangle(0, 0, 	700, 600);
			LinearGradient lg = new LinearGradient(0,0,1,1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.RED), new Stop(0.5, Color.SKYBLUE), new Stop(1, Color.GOLD));
			r.setFill(lg);
			root.getChildren().addAll(r);

			r.heightProperty().bind(w.then(scene.heightProperty().subtract(100)).otherwise(r.widthProperty().multiply(6.0/7.0)));
			r.widthProperty().bind(w.then(r.heightProperty().multiply(7.0/6.0)).otherwise(scene.widthProperty()));

			for(int i = 0 ; i < L ; i++){
				for (int j = 0 ; j < C ; j++){
					Circle c = new Circle(10 +45 + 100*j, 10+45 + 100*i, 45);
					c.setFill(couleurFond);
					c.radiusProperty().bind(r.heightProperty().divide(10).subtract(0));
					c.centerXProperty().bind(r.widthProperty().divide(7).multiply(j+0.5));
					c.centerYProperty().bind(r.heightProperty().divide(6).multiply(i+0.5));
					root.getChildren().add(c);



				}
			}

			primaryStage.setTitle("mini-project GL BY AZZOUZ Abdelhakim");
			primaryStage.setScene(scene);
			primaryStage.show();

			//creatiion
			cases = new designe [7][6];


			for(int i = 0 ; i < L ; i++){
				for (int j = 0 ; j < C ; j++){
					cases[j][i] = new designe ();
					cases[j][i].layoutXProperty().bind(r.widthProperty().divide(7).multiply(j));
					cases[j][i].layoutYProperty().bind(r.heightProperty().divide(6).multiply(i));
					cases[j][i].fitHeightProperty().bind(r.heightProperty().divide(6));
					cases[j][i].fitWidthProperty().bind(r.widthProperty().divide(7));


					root.getChildren().add(cases[j][i]);
				}
			}




			Label tour = new Label("");
			tour.setTextFill(Color.BLACK);
			tour.setFont(Font.font("Comic Sans MS", scene.getHeight() / 20));
			tour.setLayoutX(00);
			tour.layoutYProperty().bind(r.heightProperty());


			Label joueur = new Label(" ");
			joueur.setTextFill(Color.SKYBLUE);
			joueur.setFont(Font.font("Comic Sans MS", scene.getHeight() / 20));
			joueur.setLayoutX(200);
			joueur.layoutYProperty().bind(r.heightProperty());

			Label victoire = new Label("");
			victoire.setTextFill(Color.MAGENTA);
			victoire.setFont(Font.font("Comic Sans MS", scene.getHeight() / 20));
			victoire.setLayoutX(100);
			victoire.layoutYProperty().bind(r.heightProperty());
			victoire.setVisible(false);

			scene.heightProperty().addListener(e->{
				tour.setFont(Font.font("Comic Sans MS", scene.getHeight() / 20));
				joueur.setFont(Font.font("Comic Sans MS", scene.getHeight() / 20));
				victoire.setFont(Font.font("Comic Sans MS", scene.getHeight() / 20));
			});



			root.getChildren().addAll(tour, joueur, victoire);


			//cadres de s�lections:
			Rectangle[] rects = new Rectangle[C];
			for(int i = 0 ; i < C ; i++){
				rects[i] = new Rectangle(0, 0, 10, 10);
				rects[i].layoutXProperty().bind(r.widthProperty().divide(C).multiply(i));
				rects[i].heightProperty().bind(r.heightProperty());
				rects[i].widthProperty().bind(r.widthProperty().divide(C));
				rects[i].setFill(Color.TRANSPARENT);
				rects[i].setStroke(Color.GREENYELLOW);
				rects[i].setStrokeType(StrokeType.INSIDE);
				rects[i].setStrokeWidth(12);
				rects[i].setVisible(false);

				root.getChildren().addAll(rects[i]);
			}


			//selections:

			Rectangle r2 = new Rectangle(0,0,10,10);
			r2.heightProperty().bind(r.heightProperty());
			r2.widthProperty().bind(r.widthProperty());
			r2.setFill(Color.TRANSPARENT);
			root.getChildren().addAll(r2);

			precedent = -1;
			r2.setOnMouseMoved(e -> {

				int val = (int)(e.getX() / (r.getWidth() / C));
				if(val != precedent){
					rects[val].setVisible(true);
					if(precedent > -1)
						rects[precedent].setVisible(false);
				}
				precedent= val;

			});



			nbTour = 1;

			//clique:
			r2.setOnMouseClicked(e -> {


				int colonne = (int)(e.getX() / (r.getWidth() / C));

				//placement du jeton:
				if(cases[colonne][0].getStatut() == 0 && !victoire.isVisible()){

					int rang = L-1;
					while(cases[colonne][rang].getStatut() != 0){
						rang--;
					}
					cases[colonne][rang].set(nbTour%2==1 ? 1 : 2);


					//condiiton de victoire:


					//couleur en cours:
					int couleur = (nbTour%2==1 ? 1 : 2);
					//nombre align�s maximal:
					int max = 0;
					int x; int y;
					int somme;

					//-->  diagonale HG-BD
					x = colonne; y = rang; somme=-1;
					while(y >= 0 && x >= 0 && cases[x][y].getStatut() == couleur){ y--; x--; somme++;}
					x = colonne; y = rang;
					while(y < L && x < C && cases[x][y].getStatut() == couleur){ y++; x++; somme++;}
					if(somme > max) max= somme;

					//-->  diagonale HD-BG
					x = colonne; y = rang; somme=-1;
					while(y >= 0 && x < C && cases[x][y].getStatut() == couleur){ y--; x++; somme++;}
					x = colonne; y = rang;
					while(y < L && x >= 0 && cases[x][y].getStatut() == couleur){ y++; x--; somme++;}
					if(somme > max) max= somme;

					//-->  verticale:
					x = colonne; y = rang; somme=-1;
					while(y >= 0 && cases[x][y].getStatut() == couleur){ y--; somme++;}
					y = rang;
					while(y < L && cases[x][y].getStatut() == couleur){ y++; somme++;}
					if(somme > max) max= somme;

					//-->  horizontale:
					x = colonne; y = rang; somme=-1;
					while(x >= 0 && cases[x][y].getStatut() == couleur){ x--; somme++;}
					x = colonne;
					while(x < C && cases[x][y].getStatut() == couleur){ x++; somme++;}
					if(somme > max) max= somme;


					if(max >= N){
						joueur.setVisible(false);
						victoire.setVisible(true);
						victoire.setTextFill(couleur == 1 ? Color.MAROON: Color.GOLD);
						victoire.setText("jour est gangier (°o°) " + (couleur == 1 ? "1" : "2" )+ "  Bravo");
						nbTour--;
					}







					nbTour++;



					if(nbTour > C*L && max < N){
						joueur.setVisible(false);
						victoire.setVisible(true);
						victoire.setText("EGALITE !");
						nbTour--;
					}


					//tour.setText("Tour " + nbTour);
					joueur.setText("JOUEUR QUI  jouer   " + (nbTour%2 == 1 ? "1" : "2"));

				}


			});






































		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
