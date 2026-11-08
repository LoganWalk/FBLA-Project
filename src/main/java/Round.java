import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class Round {
    private String teamName;
    private String[] teams;
    private int teamIndex;
    public int count = 0;
    public int count2 = 0;

    public Round(String teamName, String[] teamsSelection, int teamIndex) {
        this.teamName = teamName;
        this.teams = teamsSelection;
        this.teamIndex = teamIndex;
    }

    public void run() throws IOException {
        rounds();
    }

    private int getFirstTeam() {
        count++;
        if (count == 1) {
            return this.teamIndex;
        } else {
            return teamIndex;
        }
    }

    private void rounds() throws IOException {
        int firstTeam = getFirstTeam();
        int userTurn = 0;
        if (firstTeam < this.teamIndex) {
            userTurn = this.teamIndex - firstTeam + 1;
        } else if (firstTeam > this.teamIndex) {
            userTurn = teams.length - firstTeam + this.teamIndex + 1;
        } else if (firstTeam == this.teamIndex) {
            userTurn = firstTeam;
        }
        for (int i = firstTeam - 1; i < teams.length; i++) {
            if (i == teams.length - 1) {
                i = 0;
            }
            Boolean isUserTurn = (i == userTurn - 1);
            if (isUserTurn) {
                System.out.println("It is your teams turn to select a player.");
                Main.runDraft(isUserTurn);
            } else {
                System.out.println("The team selecting is: " + teams[i]);
                Main.runDraft(isUserTurn);
            }

            if (count2 == 150) {
                System.out.println("THE DRAFT HAS ENDED");
                break;
            }
            count2++;
        }
    }
}
