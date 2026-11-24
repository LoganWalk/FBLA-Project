import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Game {
    public static String[] player = Field.getTeam()[0];
    public static Field feild = new Field();
    private static int selectedPlayerIndex = 0;
    public static String purple = Field.orange;
    public static String blue = Field.blue[1];
    public static int count = 0;
    public static int count2 = 0;
    public static Player player2 = new Player();

    // Each player keeps the terrain tile underneath them independently.
    // All 15 starting player positions are placed on green turf.
    private static final String[] playerUnderlyingTiles = new String[15];

    static {
        for (int i = 0; i < playerUnderlyingTiles.length; i++) {
            playerUnderlyingTiles[i] = Field.green[1];
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        player2.setSpeed(99);
        callMovement();
    }

    // The active team contains players 0-10. Players 11-14 remain on the bench.
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

    public static void move(String direction, int speed, int[] playerPosition) throws InterruptedException {
        if (playerPosition == null || playerPosition.length < 2 || direction == null) {
            return;
        }

        int playerIndex = selectedPlayerIndex;
        if (playerIndex < 0 || playerIndex >= playerUnderlyingTiles.length) {
            return;
        }

        String[][] board = feild.getField();
        int row = playerPosition[0];
        int col = playerPosition[1];

        if (row < 0 || row >= board.length || board[row] == null
                || col < 0 || col >= board[row].length) {
            return;
        }

        String playerTile = "BluePlayer" + playerIndex;

        // Ignore stale positions or positions that do not contain the selected player.
        if (!playerTile.equals(board[row][col])) {
            return;
        }
        int newRow = row;
        int newCol = col;
        boolean delay = false;
        if (direction.equals("left")) {
            if (count < speed) {
                newCol -= 1;
                count++;
            } else {
                count = 0;
            }
        } else if (direction.equals("right")) {
            if (count2 < speed) {
                newCol += 1;
                count2++;
            } else {
                count2 = 0;
            }
        } else if (direction.equals("up")) {
            newRow -= 1;
        } else if (direction.equals("down")) {
            newRow += 1;
        } else {
            return;
        }

        // Only prevent movement outside the actual board.
        if (newRow < 0 || newRow >= board.length || board[newRow] == null
                || newCol < 0 || newCol >= board[newRow].length) {
            return;
        }

        String destination = board[newRow][newCol];

        // Do not move onto another blue player.
        if (destination == null) {
            return;
        }

        // Restore this player's previous terrain and remember the destination terrain.
        board[row][col] = playerUnderlyingTiles[playerIndex];
        playerUnderlyingTiles[playerIndex] = destination;
        board[newRow][newCol] = playerTile;

        playerPosition[0] = newRow;
        playerPosition[1] = newCol;

    }

    public static void clearTerminal() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    public static void callMovement() throws IOException, InterruptedException {
        try (Terminal terminal = TerminalBuilder.terminal()) {
            terminal.enterRawMode();
            feild.printField("BluePlayer" + selectedPlayerIndex);

            while (true) {
                int inputValue = terminal.reader().read();

                if (inputValue == 'a') {
                    move("left", calculateSpeed(player2), findPlayer(player));
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                } else if (inputValue == 'd') {
                    move("right", calculateSpeed(player2), findPlayer(player));
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                } else if (inputValue == 'w') {
                    move("up", calculateSpeed(player2), findPlayer(player));
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                } else if (inputValue == 's') {
                    move("down", calculateSpeed(player2), findPlayer(player));
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                } else if (inputValue == 'e') {
                    switchPlayer();
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                }
            }
        }
    }

    public static void switchPlayer() throws InterruptedException {
        Thread.sleep(50);
        String[][] team = Field.getTeam();

        if (team == null || team.length == 0) {
            return;
        }

        selectedPlayerIndex = (selectedPlayerIndex + 1) % team.length;
        player = team[selectedPlayerIndex];

        // Do not reset terrain tracking: every player keeps their own saved tile.
        Thread.sleep(50);
    }

    public static int calculateSpeed(Player player) {
        int skip = 0;
        int playerSpeedRating = player.getSpeed();
        skip = (int) Math.ceil((playerSpeedRating / 10) + 1);
        return skip;
    }
}
