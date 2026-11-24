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
    // Tracks the last successful movement time separately for each player.
    private static final long[] playerLastMoveTime = new long[15];
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

    public static void move(String direction, int speedRating, int[] playerPosition) throws InterruptedException {
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
        int crossingSteps = Math.max(1, board[row].length - 1);
        int movementDelay = calculateSpeed(speedRating, crossingSteps);
        long currentTime = System.nanoTime();
        long delayNanos = movementDelay * 1_000_000L;
        if (playerLastMoveTime[playerIndex] != 0 && currentTime - playerLastMoveTime[playerIndex] < delayNanos) {
            return;
        }

        int newRow = row;
        int newCol = col;
        if (direction.equals("left")) {
            newCol--;
        } else if (direction.equals("right")) {
            newCol++;
        } else if (direction.equals("up")) {
            newRow--;
        } else if (direction.equals("down")) {
            newRow++;
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
        playerLastMoveTime[playerIndex] = currentTime;
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
                    move("left", player2.getSpeed(), findPlayer(player));
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                } else if (inputValue == 'd') {
                    move("right", player2.getSpeed(), findPlayer(player));
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                } else if (inputValue == 'w') {
                    move("up", player2.getSpeed(), findPlayer(player));
                    feild.printField("BluePlayer" + selectedPlayerIndex);
                } else if (inputValue == 's') {
                    move("down", player2.getSpeed(), findPlayer(player));
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

    // Returns the delay between successful moves, in milliseconds.
    // A rating of 99 crosses the row in approximately 10 seconds.
    public static int calculateSpeed(int speedRating, int crossingSteps) {
        int rating = Math.max(1, Math.min(99, speedRating));
        double crossingTimeMillis = 10_000.0 * (99.0 / rating);
        return Math.max(1, (int) Math.round(crossingTimeMillis / crossingSteps));
    }
}
