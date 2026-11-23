import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.io.IOException;

public class Game {
    public static String[] player = Field.getTeam()[0];
    public static Field feild = new Field();
    private static int selectedPlayerIndex = 0;
    public static String purple = Field.purple;
    public static String blue = Field.blue3[1];

    // Each player keeps the terrain tile underneath them independently.
    // All 15 starting player positions are placed on green turf.
    private static final String[] playerUnderlyingTiles = new String[15];

    static {
        for (int i = 0; i < playerUnderlyingTiles.length; i++) {
            playerUnderlyingTiles[i] = Field.green[1];
        }
    }

    public static void main(String[] args) throws IOException {
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

    public static void move(String direction, int speed, int[] playerPosition) {
        if (playerPosition == null || playerPosition.length < 2 || speed < 1 || direction == null) {
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

        if (team == null || team.length == 0) {
            return;
        }

        selectedPlayerIndex = (selectedPlayerIndex + 1) % team.length;
        Field.bluePlayerTiles.get(selectedPlayerIndex - 1).setColor(blue);
        Field.bluePlayerTiles.get(selectedPlayerIndex).setColor(purple);
        player = team[selectedPlayerIndex];

        // Do not reset terrain tracking: every player keeps their own saved tile.
    }
}