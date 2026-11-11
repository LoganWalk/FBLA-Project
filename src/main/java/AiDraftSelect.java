import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class AiDraftSelect {
  /*
   * Use team.java for the ai team rating they chould select players in their
   * range area and one player that is lower than theur range nad one hgiher than
   * their rang ebut alos leave a little wiggle room on the money also they need
   * to select off their stats, salary,ranking, and rating
   */

  /* Use dicoantair and lists to store the data */

  private static ArrayList<Team> aiTeams;
  private static Player[] players;
  private static Team team;

  public AiDraftSelect() {
    this.team = team;
    this.players = players;
  }

  public static Player getPlayerSelected(Team team, ArrayList<Player> players) {
    /*
     * use the players to find their stats and salary then find the teams budget and
     * rating and find out what player the ai needs to select
     */
    return null;
  }

}
