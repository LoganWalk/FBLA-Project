import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import org.jline.utils.InfoCmp;
import java.util.*;

/* You need to make a skip round section that allows them to skip the round but they
 * have to end up with at least 11 players form the draft then all the players they need
 * to fill for a 15 player team will be filled with players within the rating range of 50 to 60 */
public class Draft {
    private ArrayList<Player> players;
    private ArrayList<Player> displayedPlayers = new ArrayList<>();
    private String teamSelected;
    private Player playerSelected;
    private String[] teamsSelection;
    private Boolean isUserTurn;
    private static int count = 0;
    private static int count2 = 0;
    private static int count3 = 0;
    private ArrayList<String> recentPicks = new ArrayList<>();
    private static Integer firstTeam = 0;
    public static final String[] positions = { "Quarterback", "Running Back", "Wide Receiver",
            "Tight End", "Offensive Lineman", "Kicker", "Defensive Lineman", "Linebacker",
            "Defensive Back" };
    public ArrayList<String> positionsNeeded = new ArrayList<>(
            Arrays.asList("Quarterback", "Running Back", "Wide Receiver", "Tight End",
                    "Offensive Lineman", "Kicker", "Defensive Lineman", "Linebacker", "Defensive Back"));
    public static ArrayList<Player> group = new ArrayList<Player>();
    public static int counter = 0;
    private Map<String, Integer> teamIndexMap = new HashMap<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Team> aiTeams = new ArrayList<>();
    private static int turn = 0;
    public ArrayList<Player> playersSelected = new ArrayList<>();
    public int counter2 = 1;
    public int counter3 = 0;
    public int logStart = 0;
    public int counter4 = 0;

    public Draft(ArrayList<Player> player, Player playerSelected, String teamSelected,
            String[] teamsSelection) {
        this.players = player;
        this.playerSelected = playerSelected;
        this.teamSelected = teamSelected;
        this.teamsSelection = teamsSelection;
    }

    public void run() throws IOException {
        if (counter == 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("TO START THE DRAFT...");
            System.out.println("Press ENTER to continue...");
            scanner.nextLine();
            counter++;
        }
        for (String t : this.teamsSelection) {
            Team team;
            ArrayList<Player> teamPlayers = new ArrayList<>();
            if (!t.equals(this.teamSelected)) {
                team = new Team(teamPlayers, t, true);
                Team.addAiTeams(team);
                aiTeams.add(team);
            } else {
                team = new Team(teamPlayers, t, false);
            }
            if (teams.size() < 10) {
                teams.add(team);
            } else {
                break;
            }
        }
        selectPlayer();
    }

    private void printOut(int number, int currentSelection) throws IOException {
        clearTerminal();
        displayedPlayers.clear();
        String BLACK_TEXT = "\u001B[30m";
        String WHITE_BACKGROUND = "\u001B[47m";
        String RESET = "\u001B[0m";

        if (number == 0) {
            System.out.print("""
                    =========================
                    ----------DRAFT----------
                    =========================
                    """);
            if (!recentPicks.isEmpty()) {
                System.out.println("--- RECENT PICKS ---");
                logStart = Math.max(0, recentPicks.size() - 10);
                for (int k = logStart; k < recentPicks.size(); k++) {
                    System.out.println("  " + recentPicks.get(k));
                }
                System.out.println("--------------------");
            }
            Team t1 = getTeamByName(teamSelected);
            System.out.println("Your budget is: $" + t1.getBudget());
            int maxVisiblePlayers = 30;
            int totalPlayers = players.size();
            int startWindow = 0;
            if (currentSelection >= maxVisiblePlayers) {
                startWindow = currentSelection - ((maxVisiblePlayers) / 3);
            }
            int endWindow = Math.min(startWindow + maxVisiblePlayers, totalPlayers);
            int globalIndex = 0;
            int printedCount = 0;
            if (currentSelection == -4) {
                System.out.println(WHITE_BACKGROUND + BLACK_TEXT + "OUT OF MONEY" + RESET);
                globalIndex++;
            } else {
                System.out.println("OUT OF MONEY");
            }
            if (currentSelection == -3) {
                System.out.println(WHITE_BACKGROUND + BLACK_TEXT + "SKIP ROUND" + RESET);
                globalIndex++;
            } else {
                System.out.println("SKIP ROUND");
            }
            if (currentSelection == -2) {
                System.out.println(WHITE_BACKGROUND + BLACK_TEXT + "SKIP DRAFT" + RESET);
                globalIndex++;
            } else {
                System.out.println("SKIP DRAFT");
            }

            for (int j = 0; j < positions.length; j++) {
                group = new ArrayList<Player>();
                for (int e = 0; e < this.players.size(); e++) {
                    if (this.players.get(e).getPosition().equals(positions[j])) {
                        group.add(this.players.get(e));
                    }
                }
                Collections.sort(group, (p1, p2) -> Integer.compare(p2.getRating(), p1.getRating()));

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
                    System.out.println("NONE");
                } else {
                    for (int i = 0; i < group.size(); i++) {
                        displayedPlayers.add(group.get(i));
                        if (globalIndex >= startWindow && printedCount < maxVisiblePlayers) {
                            if (currentSelection == globalIndex) {
                                System.out.print(WHITE_BACKGROUND + BLACK_TEXT + (i + 1) + ". " + group.get(i)
                                        + " <---" + RESET);
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
        }

        else if (number == 1) {
            for (Team t : teams) {
                if (t.getName().equals(teamSelected)) {
                    t.addPlayer(playerSelected);
                    playersSelected.add(playerSelected);
                    int budget = t.getBudget() - playerSelected.getSalary();
                    t.setBudget(budget);
                    break;
                }
            }
            players.remove(playerSelected);
            System.out.println(getKeyByValue(teamIndexMap, turn) + " Selected: " + playerSelected);
            recentPicks.add("Pick #" + count + " - " + getKeyByValue(teamIndexMap, turn) + "(You)" + ": "
                    + playerSelected);
        } else if (number == 2) {

            for (Team t : teams) {
                Team t1 = getTeamByName(teamSelected);
                if (t.equals(t1)) {
                    while (t.getPlayers().size() < 15) {
                        if (players.isEmpty()) {
                            break;
                        }
                        int random = (int) (Math.random() * (players.size()));
                        boolean hasAllPostitions = false;
                        if (positionsNeeded.isEmpty()) {
                            hasAllPostitions = true;
                        }
                        if (!hasAllPostitions) {
                            if (positionsNeeded.contains(players.get(random).getPosition())) {
                                if (players.get(random).getSalary() <= t.getBudget()) {
                                    int budget = t.getBudget() - players.get(random).getSalary();
                                    t.setBudget(budget);
                                    t.addPlayer(players.get(random));
                                    positionsNeeded.remove(players.get(random).getPosition());
                                    players.remove(random);

                                }
                                // Add the player to your team roster here
                            } else {
                                // Position was already filled or invalid, so skip and ask again
                                continue;
                            }
                        } else {
                            if (players.get(random).getSalary() <= t.getBudget()) {
                                int budget = t.getBudget() - players.get(random).getSalary();
                                t.setBudget(budget);
                                t.addPlayer(players.get(random));
                                players.remove(random);
                            }
                        }
                    }
                } else {
                    for (int i = t.getPlayers().size(); i < 15; i++) {
                        playerSelected = AiDraftSelect.getPlayerSelected(t, players);
                        int budget = t.getBudget() - playerSelected.getSalary();
                        t.setBudget(budget);
                        t.addPlayer(playerSelected);
                        players.remove(playerSelected);
                    }
                }
            }
            Player generator = new Player();

            for (Team t : teams) {

                // Make sure every position is filled
                for (String position : positions) {

                    boolean hasPosition = false;

                    for (Player p : t.getPlayers()) {
                        if (p.getPosition().equals(position)) {
                            hasPosition = true;
                            break;
                        }
                    }

                    if (!hasPosition) {

                        Player lowestRated = null;

                        // Find a player at a position with multiple players
                        for (Player p : t.getPlayers()) {

                            int positionCount = 0;

                            for (Player p2 : t.getPlayers()) {
                                if (p.getPosition().equals(p2.getPosition())) {
                                    positionCount++;
                                }
                            }

                            if (positionCount > 1) {

                                if (lowestRated == null ||
                                        p.getRating() < lowestRated.getRating()) {

                                    lowestRated = p;
                                }
                            }
                        }

                        // Replace the lowest-rated extra player
                        if (lowestRated != null) {
                            t.getPlayers().remove(lowestRated);
                        }

                        t.addPlayer(generator.makeBaseCharacter(position));
                    }
                }

                // Fill remaining roster spots to 15
                while (t.getPlayers().size() < 15) {

                    String position = positions[(int) (Math.random() * positions.length)];

                    t.addPlayer(generator.makeBaseCharacter(position));
                }
            }

            results();
        } else if (number == 3) {

        } else if (number == 4) {

        }
    }

    private void clearTerminal() {
        System.out.println("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    public Team getTeamByName(String Name) {
        Team t1 = teams.get(0);
        for (Team t : teams) {
            if (t.getName().equals(Name)) {
                t1 = t;
                break;
            }
        }
        return t1;

    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V targetValue) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(targetValue, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void selectPlayer() throws IOException {
        teamSelect();
        mainloop: while (true) {
            count++;
            turn = round(count);
            if (turn == -1) {
                System.out.println("THE DRAFT HAS ENDED");
                break;
            }
            if (turn != teamIndexMap.get(teamSelected)) {
                String currentTeamName = getKeyByValue(teamIndexMap, turn);
                System.out.println(currentTeamName + " is selecting their player from the draft");
                System.out.println("Looking for AI team: [" + getKeyByValue(teamIndexMap, turn)
                        + "] | aiTeams size: " + aiTeams.size());
                for (Team t : aiTeams) {
                    if (t.getName().equals(currentTeamName)) {
                        playerSelected = AiDraftSelect.getPlayerSelected(t, players);
                        t.addPlayer(playerSelected);
                        players.remove(playerSelected);
                        int budget = t.getBudget() - playerSelected.getSalary();
                        t.setBudget(budget);
                        System.out.println(getKeyByValue(teamIndexMap, turn) + " Selected: " + playerSelected);
                        recentPicks.add("Pick #" + count + " - " + getKeyByValue(teamIndexMap, turn) + ": "
                                + playerSelected);
                        break;
                    }
                }
            } else {
                System.out.println("Your team is selecting");
                try (Terminal terminal = TerminalBuilder.terminal()) {
                    terminal.enterRawMode();
                    int currentSelection = 0;
                    while (true) {
                        printOut(0, currentSelection);
                        int inputValue = terminal.reader().read();
                        if (inputValue == 'w' || inputValue == 'W') {
                            if (currentSelection == 0) {
                                currentSelection = -2;
                            } else {
                                if (currentSelection > -4) {
                                    currentSelection--;
                                }
                            }
                        } else if (inputValue == 's' || inputValue == 'S') {
                            if (currentSelection == -2) {
                                currentSelection = 0;
                            } else {
                                if (currentSelection < displayedPlayers.size() - 1) {
                                    currentSelection++;
                                }
                            }
                        } else if (inputValue == 27) {
                            int next1 = terminal.reader().read();
                            int next2 = terminal.reader().read();
                            if (next1 == 91 || next1 == 79) {
                                if (next2 == 65) {
                                    if (currentSelection == 0) {
                                        currentSelection = -2;
                                    } else {
                                        if (currentSelection > -4) {
                                            currentSelection--;
                                        }
                                    }
                                } else if (next2 == 66) {
                                    if (currentSelection == -2) {
                                        currentSelection = 0;
                                    } else {
                                        if (currentSelection < displayedPlayers.size() - 1) {
                                            currentSelection++;
                                        }
                                    }
                                }
                            }
                        } else if (inputValue == 13 || inputValue == 10) {
                            if (currentSelection == -2) {
                                // Skip Draft
                                printOut(2, 0);
                                break mainloop;
                            } else if (currentSelection == -3) {
                                // Skip Round
                                printOut(3, 0);
                                break;
                            } else if (currentSelection == -4) {
                                // Out of Money
                                printOut(4, 0);
                                break mainloop;
                            } else {
                                playerSelected = displayedPlayers.get(currentSelection);
                                Team t1 = getTeamByName(teamSelected);
                                if (playerSelected.getSalary() > t1.getBudget()) {
                                    System.out.println("You cant afford this player, Pick Again");
                                    continue;
                                }
                                boolean hasAllPostitions = false;
                                for (String position : positionsNeeded) {
                                    if (position.equals("")) {
                                        counter3++;
                                    }
                                    if (counter3 == 9) {
                                        hasAllPostitions = true;
                                        break;
                                    }
                                }
                                if (!hasAllPostitions) {
                                    if (positionsNeeded.contains(playerSelected.getPosition())) {
                                        positionsNeeded.remove(playerSelected.getPosition());
                                        // Add the player to your team roster here
                                    } else {
                                        // Position was already filled or invalid, so skip and ask again
                                        continue;
                                    }
                                }

                                printOut(1, 0);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void teamSelect() throws IOException {
        for (int i = 0; i < this.teamsSelection.length; i++) {
            teamIndexMap.put(this.teamsSelection[i], i);
        }
        List<String> keys = new ArrayList<>(teamIndexMap.keySet());
        int random = (int) (Math.random() * this.teamsSelection.length);
        String teamName = keys.get(random);
        if (teamName.equals(this.teamSelected)) {
            firstTeam = teamIndexMap.get(teamSelected);
        } else {
            firstTeam = teamIndexMap.get(teamName);
        }
    }

    private int round(int pickNumber) throws IOException {
        int totalPicks = this.teamsSelection.length * 15;
        if (pickNumber > totalPicks) {
            return -1;
        }
        return (firstTeam + pickNumber - 1) % this.teamsSelection.length;
    }

    private void results() {
        for (Team t : teams) {

            System.out.printf("""
                    =============================
                    %s    %d Players
                    =============================

                    """, t.getName().toUpperCase(), t.getPlayers().size());

            for (String position : positions) {

                System.out.printf("""
                        ---------------------
                        %s
                        ---------------------
                        """, position);

                boolean foundPlayer = false;

                for (Player p : t.getPlayers()) {

                    if (p.getPosition().equals(position)) {
                        System.out.println(p);
                        foundPlayer = true;
                    }
                }

                if (!foundPlayer) {
                    System.out.println("No players drafted.");
                }

                System.out.println();
            }

            System.out.println("\n");
        }

    }
}
