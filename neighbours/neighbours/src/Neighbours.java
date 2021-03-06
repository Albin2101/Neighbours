import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static java.lang.System.*;



/*
 *  Program to simulate segregation.
 *  See : http://nifty.stanford.edu/2014/mccown-schelling-model-segregation/
 *
 * NOTE:
 * - JavaFX first calls method init() and then method start() far below.
 * - To test uncomment call to test() first in init() method!
 *
 */
// Extends Application because of JavaFX (just accept for now)
public class Neighbours extends Application {
    Random rand = new Random();

    class Actor {
        final Color color;        // Color an existing JavaFX class
        boolean isSatisfied;
        int row;// false by default

        Actor(Color color) {      // Constructor to initialize

            this.color = color;
        }
    }

    // Below is the *only* accepted instance variable (i.e. variables outside any method)
    // This variable may *only* be used in methods init() and updateWorld()
    Actor[][] world;              // The world is a square matrix of Actors
    // This is the method called by the timer to update the world
    // (i.e move unsatisfied) approx each 1/60 sec.
    void updateWorld() {
        // % of surrounding neighbours that are like me
        double threshold = 0.7;





        // TODO update world
    }

    // This method initializes the world variable with a random distribution of Actors
    // Method automatically called by JavaFX runtime
    // That's why we must have "@Override" and "public" (just accept for now)
    @Override
    public void init() throws InterruptedException {
        //test();    // <---------------- Uncomment to TEST!
        // %-distribution of RED, BLUE and NONE
        double[] dist = {0.4, 0.4, 0.2};
        // Number of locations (places) in world (must be a square)
        int nLocations = 900;   // Should also try 90 000

        // TODO initialize the world


        // blir problem n??r 2*nLocations*dist[1] = 0, allt blir vitt f??r den g??r fr??n 0 till distlocation lol
        Actor[] distArray = new Actor[nLocations];
        for (int i = 0;                         i < nLocations*dist[0]; i++){
            distArray[i] = new Actor(Color.RED);
        }
        for (int j = (int) (nLocations*dist[0]); j < (2*nLocations)*dist[1]; j++){
            distArray[j] = new Actor(Color.BLUE);
        }
        for (int k = (int) ((2*nLocations)*dist[1]); k < nLocations; k++){
            distArray[k] = new Actor(Color.WHITE);
        }

        shuffle(distArray);
        world = array2Matrix(distArray, nLocations);
            // Should be last
        fixScreenSize(nLocations);


    }

    // ---------------  Methods ------------------------------




    // TODO Many ...

    // Check if inside world
    boolean isValidLocation(int size, int row, int col) {
        return row >= 0 &&
                row <= size &&
                col >= 0 &&
                col <= size
                ;
    }

    // ----------- Utility methods -----------------



    // TODO (general method possible reusable elsewhere)


    Actor[] shuffle(Actor[] arr) {
        int currentIndex = arr.length;                  // Skapar variabler o skit, s??tter currentIndex tilll??ngden av listan.
        int randomIndex;
        while (currentIndex != 0) {                     // g??r igenom hela listan
            randomIndex = rand.nextInt(currentIndex);   // Pick a remaining element...
            currentIndex--;
            Actor temp = arr[currentIndex];             // And swap it with the current element.
            arr[currentIndex] = arr[randomIndex];
            arr[randomIndex] = temp;
        }
        return arr;
    }

    Actor[][] array2Matrix(Actor[] arr, int nLocation) {
        int sideLength = (int)sqrt(nLocation);
        Actor[][] matrix = new Actor[sideLength][sideLength];
        int totalt = 0;
        for (int h = 0; h < sideLength; h++){
            for(int w = 0; w < sideLength;w++){
                matrix[w][h] = arr[totalt++];
            }
        }
        return matrix;
     }

    boolean isActorSatisfied(Actor[][] arr, Actor a, int row, int col, double threshold) { //Funkar inte riktigt som den ska s?? kolla p?? den. Ring mig om du har n??gon fr??ga :)
        int colorCount = 0;
        int surroundingCount = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (!(row == r && col == c) && isValidLocation(arr.length, r, c)) {
                    if ((arr[row][col].color).equals(arr[r][c].color)) {
                        colorCount++;
                    }
                    surroundingCount++;
                }
            }
        }
        if ((colorCount/surroundingCount) > threshold){
            a.isSatisfied = true;
        }
        return a.isSatisfied;
    }

    Boolean allSatisified (Actor[][] arr, Actor a, int row, int col, double threshold, int nLocations) {
            int amountSatisfied=0;
            int amountOfWhites=0;
            boolean allSatisfied=false;
            for (int i= 0; i < sqrt(nLocations); i++){
                for(int j= 0; j < sqrt(nLocations); j++) {
                    if((arr[i][j].color).equals(Color.WHITE)){
                        amountOfWhites++;
                    } else{
                        if(isActorSatisfied(arr,arr[j][i], 2,6, 0.7)==true){
                            amountSatisfied++;
                        }
                    }
                }
            }
            if(amountSatisfied==(nLocations-amountOfWhites)){
                allSatisfied=true;
            }
        return allSatisfied;
    }

    void swapActors(Actor distArray[], int nLocations){

        //1. G?? igenom v??rlden och notera vilka som ??r missn??jda (och n??jda).
        //H??r anv??nder vi att minst 50% av grannarna skall ha samma f??rg.
        //2. G?? igenom v??rlden och spara index till alla tomma platser (null). Byt
        //mellan rad/kolumn och index se Anteckningar/Arrayer.
        //3. Shuffla de tomma platserna (indexen).
        //4. Byt f??rsta tomma plats mot f??rsta missn??jda. N??sta tomma plats mot
        //n??sta missn??jda o.s.v.


        /*
        int r??knare = 0;
        for (int i = 0; i < nLocations ; i++){
            if (!(distArray[i].isSatisfied) ){
                for (int j = 0; j <nLocations ; j++) {
                    if (distArray[j].color == Color.WHITE) {
                        distArray[j] = distArray[i];
                        distArray[j] = new Actor(Color.WHITE);
                        r??knare ++;
                    }
                }
            }
        }
        out.print(r??knare);
        */

    }



    // ------- Testing -------------------------------------

    // Here you run your tests i.e. call your logic methods
    // to see that they really work. Important!!!!
    void test() {
        // A small hard coded world for testing
        Actor[][] testWorld = new Actor[][]{
                {new Actor(Color.RED), new Actor(Color.RED), null},
                {null, new Actor(Color.BLUE), null},
                {new Actor(Color.RED), null, new Actor(Color.BLUE)}
        };
        double th = 0.5;   // Simple threshold used for testing

        int size = testWorld.length;
        out.println(isValidLocation(size, 0, 0));
        out.println(!isValidLocation(size, -1, 0));
        out.println(!isValidLocation(size, 0, 3));

        // TODO

        exit(0);
    }

    // ******************** NOTHING to do below this row, it's JavaFX stuff  **************

    double width = 500;   // Size for window
    double height = 500;
    final double margin = 50;
    double dotSize;

    void fixScreenSize(int nLocations) {
        // Adjust screen window
        dotSize = 9000 / nLocations;
        if (dotSize < 1) {
            dotSize = 2;
        }
        width = sqrt(nLocations) * dotSize + 2 * margin;
        height = width;
    }

    long lastUpdateTime;
    final long INTERVAL = 450_000_000;



    @Override
    public void start(Stage primaryStage) throws Exception {

        // Build a scene graph
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().addAll(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Create a timer
        AnimationTimer timer = new AnimationTimer() {
            // This method called by FX, parameter is the current time
            public void handle(long now) {
                long elapsedNanos = now - lastUpdateTime;
                if (elapsedNanos > INTERVAL) {
                    updateWorld();
                    renderWorld(gc);
                    lastUpdateTime = now;
                }
            }
        };

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation");
        primaryStage.show();

        timer.start();  // Start simulation
    }


    // Render the state of the world to the screen
    public void renderWorld(GraphicsContext g) {
        g.clearRect(0, 0, width, height);
        int size = world.length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = (int) (dotSize * col + margin);
                int y = (int) (dotSize * row + margin);
                if (world[row][col] != null) {
                    g.setFill(world[row][col].color);
                    g.fillOval(x, y, dotSize, dotSize);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
