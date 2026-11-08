
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

class Main {
  public static Scanner scanner = new Scanner(System.in);
  public static String[] teams = { "Wall Street Bulls", "Silicon Valley Bears", "Corporate Raiders",
      "Blue Chip Ballers", "Penny Stock Pumas", "Day Trader Dynamos", "Executive Elites",
      "Boardroom Blitz", "Synergy Sharks", "Monopoly Monarchs", "Venture Capitalists",
      "Startup Underdogs", "Algorithmic Aces", "Crypto Crusaders", "Cyber Security Centurions",
      "Fintech Phantoms", "Data Crunchers", "AI Overlords", "Supply Chain Steers",
      "Logistics Legends", "Assembly Line Aces", "Free Market Mavericks", "Fiscal Furies",
      "Liquid Assets", "Future Leaders", "Network Ninjas", "Resume Rebels", "Budget Bruins",
      "Deadline Dominators", "Trademark Titans" };
  public static String[] teamSelection = new String[10];
  public static String teamSelected = "";
  public static ArrayList<Player> players;

  public static void main() throws IOException {
    greet();
    players = new ArrayList<>();

    for (int i = 0; i < 250; i++) {
      players.add(new Player());
    }
  }

  public static void runDraft(Boolean isUserTurn) throws IOException {
    Draft draft = new Draft(players, players.get(0), teamSelected, teamSelection, isUserTurn);

    draft.run();
  }

  public static void greet() throws IOException {
    System.out.println("Hello and Welcome to The Future Business Cup!");
    System.out.print("Enter your name here: ");
    String name = scanner.nextLine();
    System.out.println("Nice to meet you, " + name + "!");
    randomTeam(name);

  }

  public static void randomTeam(String name) throws IOException {
    for (int i = 0; i < 10; i++) {
      int random = (int) (Math.random() * (30));
      if (Arrays.asList(teamSelection).contains(teams[random])) {
        i--;
        continue;
      }
      teamSelection[i] = teams[random];
    }
    selectTeam();
  }

  public static void printInfo(int number, int currentSelection) {
    clearTerminal();
    String BLACK_TEXT = "\u001B[30m";
    String WHITE_BACKGROUND = "\u001B[47m";
    String RESET = "\u001B[0m";

    if (number == 0) {
      for (int i = 0; i < 10; i++) {
        if (currentSelection == i) {
          System.out.print(
              WHITE_BACKGROUND + BLACK_TEXT + (i + 1) + ". " + teamSelection[i] + RESET + " <---");
        } else {
          System.out.print((i + 1) + ". " + teamSelection[i]);
        }

        System.out.println();
      }
    }
    if (number == 1) {
      System.out.println("The team you have selected is: " + teamSelected);
    }
  }

  public static void clearTerminal() {
    System.out.println("\033[H\033[2J\033[3J");
    System.out.flush();
  }

  public static void selectTeam() throws IOException {
    // 1. Use try-with-resources to guarantee terminal.close() is called
    try (Terminal terminal = TerminalBuilder.terminal()) {
      terminal.enterRawMode();

      // 2. Move state outside of the while loop so it persists between keystrokes
      int currentSelection = 0;

      while (true) {
        printInfo(0, currentSelection);

        int inputValue = terminal.reader().read();

        // 3. Handle WASD keys (Case-insensitive)
        if (inputValue == 'w' || inputValue == 'W') {
          if (currentSelection > 0)
            currentSelection--;
        } else if (inputValue == 's' || inputValue == 'S') {
          if (currentSelection < 9)
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
              if (currentSelection < 9)
                currentSelection++;
            }
          }
        }
        // 5. Handle Enter Key (Carriage return '\r' or Newline '\n')
        else if (inputValue == 13 || inputValue == 10) {
          teamSelected = teamSelection[currentSelection];
          printInfo(1, 0);
          break;
        }
      }
    }
  }
}
