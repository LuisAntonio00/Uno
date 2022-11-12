import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
class Unocard{
    public String color;
    public int value;
    private Random rand;
    private String face;

    public Unocard(int v, String c){
        value = v;
        color = c;
    }
    public Unocard(){
        rand = new Random();
        value = rand.nextInt(28);
        if (value >= 14)
            value -= 14;
        rand = new Random();
        switch(rand.nextInt(4) ){
            case 0: color = "Red";
                break;
            case 1: color = "Green";
                break;
            case 2: color = "Blue";
                break;
            case 3: color = "Yellow";
                break;
        }
        if (value >= 13)
            color = "none";
    }

    public String getFace(){
        face = "[";
        if (color != "none"){
            face += this.color + " ";
        }

        switch(this.value){
            default: face += String.valueOf(this.value);
                break;
            case 10: face += "Skip";
                break;
            case 11: face += "Reverse";
                break;
            case 12: face += "Draw 2";
                break;
            case 13: face += "Change color";
                break;
            case 14: face += "Draw 4";
                break;
        }
        face += "]";
        return face;
    }
    public boolean canPlace(Unocard o, String c) {
        if (this.color == c)
            return true;
        else if (this.value == o.value)
            return true;
        else if (this.color == "none")
            return true;
        return false;
    }
}

public class Main{
    public static void main(String[] args){
        ArrayList<Unocard> playerdeck = new ArrayList<Unocard>();
        ArrayList<Unocard> compdeck = new ArrayList<Unocard>();
        int win;
        Scanner input;
        Unocard topCard;
        int choiceIndex;
        String currentColor;

        gameLoop:
        while (true){
            playerdeck.clear();
            compdeck.clear();
            win = 0;
            topCard = new Unocard();
            currentColor = topCard.color;

            System.out.println("\nIniciando partida! Repartiendo cartas...");
            draw(7, playerdeck);
            draw(7, compdeck);


            for (boolean playersTurn = true; win == 0; playersTurn ^= true){
                choiceIndex = 0;
                System.out.println("\nLa ultima carta jugada es: " + topCard.getFace());

                if (playersTurn){
                    System.out.println("Es tu turno! Elije que jugar: ");
                    for (int i = 0; i < playerdeck.size(); i++) {
                        System.out.print(String.valueOf(i + 1) + ". " +
                                ((Unocard) playerdeck.get(i) ).getFace() + "\n");
                    }
                    System.out.println(String.valueOf(playerdeck.size() + 1 ) + ". " + "Draw card" + "\n" +
                            String.valueOf(playerdeck.size() + 2) + ". " + "Salir");
                    do{
                        System.out.print("\nPor favor infresa un numero de tu elección: ");
                        input = new Scanner(System.in);
                    } while (!input.hasNextInt() );
                    choiceIndex = input.nextInt() - 1;

                    if (choiceIndex == playerdeck.size())
                        draw(1, playerdeck);
                    else if (choiceIndex == playerdeck.size() + 1)
                        break gameLoop;
                    else if ( ((Unocard) playerdeck.get(choiceIndex)).canPlace(topCard, currentColor)){
                        topCard = (Unocard) playerdeck.get(choiceIndex);
                        playerdeck.remove(choiceIndex);
                        currentColor = topCard.color;
                        if (topCard.value >= 10){
                            playersTurn = false;

                            switch (topCard.value){
                                case 12:
                                    System.out.println("Robando 2 cartas...");
                                    draw(2,compdeck);
                                    break;

                                case 13: case 14:
                                do{
                                    System.out.print("\nElige el nuevo color : ");
                                    input = new Scanner(System.in);
                                } while (!input.hasNext("R..|r..|G....|g....|B...|b...|Y.....|y.....") );
                                if (input.hasNext("R..|r..") )
                                    currentColor = "Red";
                                else if (input.hasNext("G....|g....") )
                                    currentColor = "Green";
                                else if (input.hasNext("B...|b...") )
                                    currentColor = "Blue";
                                else if (input.hasNext("Y.....|y.....") )
                                    currentColor = "Yellow";

                                System.out.println("Tu elegiste: " + currentColor);
                                if (topCard.value == 14){
                                    System.out.println("Robando 4 cartas...");
                                    draw(4,compdeck);
                                }
                                break;
                            }
                        }
                    } else System.out.println("Eleccion no valida... Perdiste tu turno.");


                } else{
                    System.out.println("Mi turno... Me quedan: " + String.valueOf(compdeck.size())
                            + " cartas " + ((compdeck.size() == 1) ? "...Uno!":""));
                    for (choiceIndex = 0; choiceIndex < compdeck.size(); choiceIndex++){
                        if ( ((Unocard) compdeck.get(choiceIndex)).canPlace(topCard, currentColor))
                            break;
                    }

                    if (choiceIndex == compdeck.size() ){
                        System.out.println("No tengo nada para jugar!");
                        System.out.println("Robando cartas...");
                        draw(1,compdeck);
                    }
                    else{
                        topCard = (Unocard) compdeck.get(choiceIndex);
                        compdeck.remove(choiceIndex);
                        currentColor = topCard.color;
                        System.out.println("Mi elección es " + topCard.getFace() + "!");

                        if (topCard.value >= 10) {
                            playersTurn = true;

                            switch (topCard.value) {
                                case 12:
                                    System.out.println("Tu robas 2 cartas...");
                                    draw(2,playerdeck);
                                    break;

                                case 13: case 14:
                                do{
                                    currentColor = new Unocard().color;
                                }
                                while (currentColor == "none");

                                System.out.println("El nuevo color es: " + currentColor);
                                if (topCard.value == 14){
                                    System.out.println("Tu robas 4 cartas...");
                                    draw(4,playerdeck);
                                }
                                break;
                            }
                        }
                    }

                    if (playerdeck.size() == 0)
                        win = 1;
                    else if (compdeck.size() == 0)
                        win = -1;
                }
            }
            if (win == 1)
                System.out.println("Ganaste... :)");
            else
                System.out.println("Perdiste :(");

            System.out.print("\nQuieres jugar de nuevo? ");
            input = new Scanner(System.in);

            if (input.next().toLowerCase().contains("n") )
                break;
        }

        System.out.println("Hasta la proxima");
    }
    public static void draw(int cards, ArrayList<Unocard> deck)
    {
        for (int i = 0; i < cards; i++)
            deck.add(new Unocard() );
    }
}
