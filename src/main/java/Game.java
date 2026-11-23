import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    public static final String[] yellow = { "Purple", "\\u001b[48;5;93m  \\u001b[0m" };
    public static final String[] blue = { "Blue", "\u001b[48;5;18m  \u001b[0m" };
    public static String[] player = Field.getPlayer();
    public static Field feild = new Field();
    public static int count = 0;
    public static final String[] green = { "Green", "\u001b[48;5;22m  \u001b[0m" };
    public static String[] temp = {};

    public static void main(String[] args) throws IOException {
        callMovement();
    }

    public static int[] findPlayer(String[] player) {
        for (int i = 0; i < feild.getField().length; i++) {
            for (int j = 0; j < feild.getField()[i].length; j++) {
                if (feild.getField()[i][j] == player) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    public static void move(String direction, int speed, int[] playerPosition) throws IOException {
        if (count == 0) {
            temp = green;
            count++;
        }

        if (playerPosition == null) {
            return;
        }

        int row = playerPosition[0];
        int col = playerPosition[1];

        int newRow = row;
        int newCol = col;

        if (direction.equals("left")) {
            newCol -= speed;

        } else if (direction.equals("right")) {
            newCol += speed;

        } else if (direction.equals("up")) {
            newRow -= speed;

        } else if (direction.equals("down")) {
            newRow += speed;

        } else {
            return;
        }

        // Check field boundaries
        if (newRow < 4 || newRow >= feild.getField().length - 4) {
            return;
        }

        if (newCol < 4 || newCol >= feild.getField()[newRow].length - 3) {
            return;
        }

        // Store the location and color of the pixel infront of the character, the one
        // the character is on, and the one behidn the cahracter in temp values
        String[] temp2 = feild.getField()[newRow][newCol];

        feild.getField()[newRow][newCol] = feild.getField()[row][col];
        feild.getField()[row][col] = temp;
        temp = temp2;

        // set the one infront of the player befre moving the to yellwo then set the
        // space the player was on to what is was before the player got there.

        playerPosition[0] = newRow;
        playerPosition[1] = newRow;

    }

    public static void clearTerminal() {
        System.out.println("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    public static void callMovement() throws IOException {
        int count = 0;
        try (Terminal terminal = TerminalBuilder.terminal()) {
            terminal.enterRawMode();
            while (true) {
                while (true) {
                    if (count == 0) {
                        feild.printField();
                        count++;
                    }
                    int inputValue = terminal.reader().read();
                    if (inputValue == 'a') {
                        move("left", 1, findPlayer(player));
                        feild.printField();
                        break;
                    } else if (inputValue == 'd') {
                        move("right", 1, findPlayer(player));
                        feild.printField();
                        break;
                    } else if (inputValue == 'w') {
                        move("up", 1, findPlayer(player));
                        feild.printField();
                        break;
                    } else if (inputValue == 's') {
                        move("down", 1, findPlayer(player));
                        feild.printField();
                        break;
                    } else if (inputValue == 'q') {
                        terminal.close();
                        System.exit(0);
                    } else if (inputValue == 'e') {
                        switchPlayer();
                    }

                }
            }
        }

    }

    public static void switchPlayer() {
        String[][] team = Field.getTeam();
        for (int i = 0; i < team.length; i++) {
            if (player == team[i]) {
                if (i == team.length) {
                    String[] temp = player;
                    player = team[0];

                    team[team.length - 1] = temp;
                    count = 0;
                } else {
                    String[] temp = player;
                    player = team[i + 1];
                    player[1] = yellow[1];
                    team[i] = temp;
                    team[i][1] = blue[1];
                    count = 0;
                }

                break;
            }
        }
    }
}
