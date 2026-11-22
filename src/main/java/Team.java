import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class Team {
    public static final String[] positions = { "Quarterback", "Running Back", "Wide Receiver",
            "Tight End", "Offensive Lineman", "Kicker", "Defensive Lineman", "Linebacker",
            "Defensive Back" };
    /*
     * For all the teams in the selection that werent picked by the suer generate
     * them a random team voerall range Then generate them a random overall rating
     * in that range this will be used to help figure out how the ai selects their
     * player
     */

    /* The users or players rating should be made after teh draft */

    /*
     * Before the draft give the teams a budget and amount of money they can spend
     * on players Then use that to calculate a projected team overall, the range
     * should be bewteen the amount to buy all the cheapest and the the most
     * expensive players
     * 
     * 
     * 
     * For ai teams give them a budget based off their rating instead of a projected
     * rating based off their budget
     */

    /* Use dicotnarys and list to store values */

    /* you should also create and set default team levels like morale and stuff */

    private ArrayList<Player> players;
    private String name;
    private int budget;
    private int rating;
    private boolean isAi;
    public static ArrayList<Team> aiTeams = new ArrayList<>();

    public Team(ArrayList<Player> players, String name, boolean isAi) {
        this.players = players;
        this.name = name;
        this.isAi = isAi;
        this.budget = generateBudget();
        this.rating = generateRating();
    }

    private int generateBudget() {
        int min = 12000000;
        int max = 1125000000;
        if (!this.isAi) {
            return (int) (Math.random() * (max - min + 1) + min);
        } else {

            int clampedRating = Math.max(50, Math.min(99, this.rating));

            double percentage = (clampedRating - 50) / 49.0;

            long baseBudget = min + (long) ((max - min) * percentage);

            double variance = 0.95 + (Math.random() * 0.10);

            return (int) (baseBudget * variance);
        }
    }

    private int generateRating() {
        if (!this.isAi) {
            int number = 0;
            for (int i = 0; i < this.players.size(); i++) {
                number += this.players.get(i).getRating();
            }
            int division = (this.players.size() == 0) ? 0 : (int) (number / this.players.size());
            return division;
        } else {
            int randomRange1 = (int) (Math.random() * (99 - 50 + 1) + 50);
            int randomRange2 = (int) (Math.random() * (99 - 50 + 1) + 50);

            int min = (randomRange1 > randomRange2) ? randomRange1 : randomRange2;
            int max = (randomRange1 > randomRange2) ? randomRange1 : randomRange2;

            int aiRating = (randomRange1 == randomRange2) ? randomRange1
                    : (int) (Math.random() * (max - min + 1) + min);

            return aiRating;

        }
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public int getRating() {
        return this.rating;
    }

    public static void addAiTeams(Team team) {
        aiTeams.add(team);
    }

    public ArrayList<Team> getAiTeams() {
        return aiTeams;
    }

    public String getName() {
        return this.name;
    }

    public int getBudget() {
        return this.budget;
    }
    /*
     * Make a getBudget public methof
     */

    public ArrayList<String> checkPlayers(Player player) {
        /*
         * Check to make sure the team has at least one of every position if they dont
         * return a list of positions that they need to pick up if they do then return
         * an empty list
         */
        ArrayList<String> result = new ArrayList<>();
        String[] position = { "Quarterback", "Running Back", "Wide Receiver", "Tight End",
                "Offensive Lineman", "Kicker", "Defensive Lineman", "Linebacker", "Defensive Back" };

        for (int i = 0; i < position.length; i++) {
            if (player.getPosition().equals(position[i])) {
                position[i] = "";
            }
        }
        for (int i = 0; i < position.length; i++) {
            result.add(position[i]);
        }
        return result;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

}
