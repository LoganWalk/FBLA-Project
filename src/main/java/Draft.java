import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import org.jline.utils.InfoCmp;
import java.util.HashMap;
import java.util.Map;

public class Draft {
    private ArrayList<Player> players;
    private String teamSelected;
    private Player playerSelected;
    private String[] teamsSelection;
    public static final String[] positions = { "Quarterback", "Running Back", "Wide Receiver",
            "Tight End", "Offensive Lineman", "Kicker", "Defensive Lineman", "Linebacker",
            "Defensive Back" };
    public static ArrayList<Player> group = new ArrayList<Player>();
    public static int counter = 0;
    private Map<String, Integer> teamIndexMap = new HashMap<>();

    public Draft(ArrayList<Player> player, Player playerSelected, String teamSelected, String[] teamsSelection) {
        this.players = player;
        this.playerSelected = playerSelected;
        this.teamSelected = teamSelected;
        this.teamsSelection = teamsSelection;

    }

    public void run() throws IOException {
        if (counter == 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("TO START THE DRAFT...");

            // 1. Prompt the user
            System.out.println("Press ENTER to continue...");

            // 2. Pause the execution until Enter is pressed
            scanner.nextLine();
            counter++;
        }
        selectPlayer();
    }

    private void printOut(int number, int currentSelection) throws IOException {
        clearTerminal();
        String BLACK_TEXT = "\u001B[30m";
        String WHITE_BACKGROUND = "\u001B[47m";
        String RESET = "\u001B[0m";

        if (number == 0) {
            System.out.print("""
                    =========================
                    ----------DRAFT----------
                    =========================
                    """);

        }
        int maxVisiblePlayers = 70;
        int totalPlayers = players.size();

        // 1. Keep the start window at 0 initially, but push it forward if selection
        // goes past the bottom
        int startWindow = 0;
        if (currentSelection >= maxVisiblePlayers) {
            // Keeps the selected item exactly at the bottom of the 50-line view
            startWindow = currentSelection - maxVisiblePlayers + 1;
        }

        // 2. Establish the end boundary
        int endWindow = Math.min(startWindow + maxVisiblePlayers, totalPlayers);

        int globalIndex = 0;
        int printedCount = 0;

        for (int j = 0; j < positions.length; j++) {
            group = new ArrayList<Player>();
            for (int e = 0; e < this.players.size(); e++) {
                if (this.players.get(e).getPosition() == positions[j]) {
                    group.add(this.players.get(e));
                }
            }

            // Sort ascending
            Collections.sort(group, (p1, p2) -> Integer.compare(p1.getRating(), p2.getRating()));

            // Sort descending
            Collections.sort(group, (p1, p2) -> Integer.compare(p2.getRating(), p1.getRating()));

            // Only print headers if we are near the items being displayed
            // To keep it simple, we track if any player in this group falls in our window
            boolean groupHasVisiblePlayers = false;
            for (int i = 0; i < group.size(); i++) {
                int itemIndex = globalIndex + i;
                if (itemIndex >= startWindow && printedCount < maxVisiblePlayers) {
                    groupHasVisiblePlayers = true;
                    break;
                }
            }

            if (groupHasVisiblePlayers || group.size() == 0) {
                System.out.printf("""
                        ----------------------
                        %-6s
                        ----------------------
                        """, positions[j]);
            }

            if (group.size() == 0) {
                // Only print NONE if it fits the view scope
                System.out.println("NONE");
            } else {
                for (int i = 0; i < group.size(); i++) {
                    // 3. Only print the player if they fall within our window limits
                    if (globalIndex >= startWindow && printedCount < maxVisiblePlayers) {
                        if (currentSelection == globalIndex) {
                            System.out.print(
                                    WHITE_BACKGROUND + BLACK_TEXT + (i + 1) + ". " + group.get(i) + " <---" + RESET);
                        } else {
                            System.out.print((i + 1) + ". " + group.get(i));
                        }
                        System.out.println();
                        printedCount++;
                    }
                    globalIndex++;
                }
            }

        }
        if (number == 1) {
            System.out.println("The team you have selected is: " + playerSelected);
        }
    }

    private void clearTerminal() {
        System.out.println("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    private void selectPlayer() throws IOException {
        try (Terminal terminal = TerminalBuilder.terminal()) {
            terminal.enterRawMode();

            // 2. Move state outside of the while loop so it persists between keystrokes
            int currentSelection = 0;

            while (true) {
                printOut(0, currentSelection);

                int inputValue = terminal.reader().read();

                // 3. Handle WASD keys (Case-insensitive)
                if (inputValue == 'w' || inputValue == 'W') {
                    if (currentSelection > 0)
                        currentSelection--;
                } else if (inputValue == 's' || inputValue == 'S') {
                    if (currentSelection < this.players.size() - 1)
                        currentSelection++;
                }
                // 4. Handle Arrow Keys (detecting the escape sequence prefix 27)
                else if (inputValue == 27) {
                    int next1 = terminal.reader().read();
                    int next2 = terminal.reader().read();

                    if (next1 == 91 || next1 == 79) { // '[' or 'O'
                        if (next2 == 65) { // Up Arrow ('A')
                            if (currentSelection > 0)
                                currentSelection--;
                        } else if (next2 == 66) { // Down Arrow ('B')
                            if (currentSelection < this.players.size() - 1)
                                currentSelection++;
                        }
                    }
                }
                // 5. Handle Enter Key (Carriage return '\r' or Newline '\n')
                else if (inputValue == 13 || inputValue == 10) {
                    playerSelected = players.get(currentSelection);
                    printOut(1, 0);
                    break;
                }
            }
        }
    }

    private int teamSelect() {
        /*
         * Loop through for every team adding them to a dicoanry and their index
         * randomize who goes first
         * then if it is the user they select a player if it is not the user you need to
         * make a new file that
         * stores teams the losers and the bots where if it is not the user then the bot
         * selects a player
         * you need to make a ai file that selects players for bot team logically
         * in the team file you need to generate teams their budgets and money they can
         * spend on players
         * you need to ensure that the team budgets are respected when selecting players
         * and the highest will be the highest possible amount for every 15 players for
         * each team
         * and the lowest wil be the lowest possivle amount for every 15 plyer for eahc
         * team
         * then you need to make sure 150 players are in teh draft and 100 will be free
         * agents
         */
        for (int i = 0; i < this.teamsSelection.length; i++) {
            teamIndexMap.put(this.teamsSelection[i], i);
        }
        return 0;
    }
}
