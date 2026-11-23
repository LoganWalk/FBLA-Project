import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.io.IOException;

public class Game {
    public static String[] player = Field.getPlayer();
    public static Field feild = new Field();
    public static int count = 0;
    public static String temp = "";

    public static void main(String[] args) throws IOException {
        callMovement();
    }

    // Each blue player has a unique board value: BluePlayer0, BluePlayer1, etc.
    public static int getPlayerIndex(String[] selectedPlayer) {
        String[][] team = Field.getTeam();

        for (int i = 0; i < team.length; i++) {
            if (team[i] == selectedPlayer) {
                return i;
            }
        }

        return -1;
    }

    public static int[] findPlayer(String[] selectedPlayer) {
        int playerIndex = getPlayerIndex(selectedPlayer);

        if (playerIndex == -1) {
            return null;
        }

        String playerTile = "BluePlayer" + playerIndex;
        String[][] board = feild.getField();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (playerTile.equals(board[row][col])) {
                    return new int[] { row, col };
                }
            }
        }

        return null;
    }

    public static void move(String direction, int speed, int[] playerPosition) {
        if (playerPosition == null || speed < 1) {
            return;
        }

        int row = playerPosition[0];
        int col = playerPosition[1];
        int newRow = row;
        int newCol = col;
        String[][] board = feild.getField();
        if (count == 0) {
            temp = board[playerPosition[0]][playerPosition[1]];
            count++;
        }

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

        // Keep the existing three/four-tile field boundaries.
        if (newRow < 4 || newRow >= board.length - 4) {
            return;
        }

        if (newCol < 4 || newCol >= board[newRow].length - 3) {
            return;
        }

        String destination = board[newRow][newCol];

        // Do not move onto another blue player.
        if (destination.startsWith("BluePlayer")) {
            return;
        }

        int playerIndex = getPlayerIndex(player);
        if (playerIndex == -1) {
            return;
        }

        String temp2 = board[newRow][newCol];

        // Move the player's identifier and preserve the tile underneath it.
        board[newRow][newCol] = board[row][col];
        board[row][col] = temp;
        temp = temp2;

        playerPosition[0] = newRow;
        playerPosition[1] = newCol;

    }

    public static void clearTerminal() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    public static void callMovement() throws IOException {
        try (Terminal terminal = TerminalBuilder.terminal()) {
            terminal.enterRawMode();
            feild.printField();

            while (true) {
                int inputValue = terminal.reader().read();

                if (inputValue == 'a') {
                    move("left", 1, findPlayer(player));
                    feild.printField();
                } else if (inputValue == 'd') {
                    move("right", 1, findPlayer(player));
                    feild.printField();
                } else if (inputValue == 'w') {
                    move("up", 1, findPlayer(player));
                    feild.printField();
                } else if (inputValue == 's') {
                    move("down", 1, findPlayer(player));
                    feild.printField();
                } else if (inputValue == 'e') {
                    switchPlayer();
                    feild.printField();
                } else if (inputValue == 'q' || inputValue == -1) {
                    break;
                }
            }
        }
    }

    public static void switchPlayer() {
        String[][] team = Field.getTeam();

        for (int i = 0; i < team.length; i++) {
            if (player == team[i]) {
                player = team[(i + 1) % team.length];
                return;
            }
        }
    }
}
