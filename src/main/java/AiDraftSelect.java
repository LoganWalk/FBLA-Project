import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class AiDraftSelect {
    /*
     * Use team.java for the ai team playerRating they chould select players in
     * their range area and one player that is lower than theur range nad one hgiher
     * than their rang ebut alos leave a little wiggle room on the money also they
     * need to select off their stats, playerSalary,ranking, and playerRating
     */

    /* Use dicoantair and lists to store the data */

    private static ArrayList<Team> aiTeams;
    private static Player[] players;
    private static Team team;

    public AiDraftSelect() {

    }

    public static Player getPlayerSelected(Team team, ArrayList<Player> players) {
        int remainingSpots = Math.max(1, 15 - team.getPlayers().size()); // Adjust getPlayers() to your
                                                                         // Team getter
        int averagePlayerBudget = (int) team.getBudget() / remainingSpots;
        int averagePlayerRating = (int) team.getRating();
        int marginSalary = (int) (averagePlayerBudget * 0.25); // 25% margin so players actually match
        int marginRating = Math.max(1, (int) (averagePlayerRating * 0.05)); // At least +/- 3 rating
                                                                            // points

        // Check if the team has already filled every position (0 positions still
        // needed)
        // Passing any player or calling your position check method to see what's still
        // empty
        ArrayList<String> positionsNeeded = players.isEmpty() ? new ArrayList<>()
                : team.checkPlayers(players.get(0));

        // CASE A: Team STILL needs starting positions (first 9 picks)
        if (!positionsNeeded.isEmpty()) {

            // 1. Try strict budget + rating + needed position
            for (Player p : players) {
                int playerRating = p.getRating();
                int playerSalary = p.getSalary();
                String playerPosition = p.getPosition();

                boolean withinSalary = playerSalary <= team.getBudget()
                        && playerSalary >= averagePlayerBudget - marginSalary
                        && playerSalary <= averagePlayerBudget + marginSalary;

                boolean withinRating = playerRating >= averagePlayerRating - marginRating
                        && playerRating <= averagePlayerRating + marginRating;

                if (withinSalary && withinRating && positionsNeeded.contains(playerPosition)) {
                    return p;
                }
            }

            // 2. Fallback: Any affordable player at a needed starting position
            for (Player p : players) {
                if (p.getSalary() <= team.getBudget() && positionsNeeded.contains(p.getPosition())) {
                    return p;
                }
            }
        }

        // CASE B: All positions are filled (or none left) -> Pick a RANDOM affordable
        // backup!
        ArrayList<Player> affordableBackups = new ArrayList<>();
        for (Player p : players) {
            if (p.getSalary() <= team.getBudget()) {
                affordableBackups.add(p);
            }
        }

        if (!affordableBackups.isEmpty()) {
            int randomBackupIndex = (int) (Math.random() * affordableBackups.size());
            return affordableBackups.get(randomBackupIndex);
        }

        // Last resort if over budget: return cheapest/first player so it never returns
        // null
        return players.isEmpty() ? null : players.get(0);
    }

}