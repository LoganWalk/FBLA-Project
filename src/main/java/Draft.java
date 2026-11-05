import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class Draft {
  private ArrayList<Player> players;
  private Player playerSelected;

  public Draft(ArrayList<Player> player, Player playerSelected) {
    this.players = player;
    this.playerSelected = playerSelected;
  }

  public void run() throws IOException {
    selectPlayer();
  }

  private void printOut(int number, int currentSelection) throws IOException {
    clearTerminal();
    String BLACK_TEXT = "\u001B[30m";
    String WHITE_BACKGROUND = "\u001B[47m";
    String RESET = "\u001B[0m";

    if (number == 0) {
      for (int i = 0; i < this.players.size(); i++) {
        if (currentSelection == i) {
          System.out.print(WHITE_BACKGROUND + BLACK_TEXT + (i + 1) + ". " + this.players.get(i)
              + RESET + " <---");
        } else {
          System.out.print((i + 1) + ". " + this.players.get(i));
        }

        System.out.println();
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
}
