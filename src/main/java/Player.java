import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;

class Player {
  // possibly introduce more posistions for more complexity
  public static final String[] positions = { "Quarterback", "Running Back", "Wide Receiver",
      "Tight End", "Offensive Lineman", "Kicker", "Defensive Lineman", "Linebacker",
      "Defensive Back" };
  public static final Faker faker = new Faker();
  private String name;
  private String position;
  private int rating = 0;
  private int salary = 0;
  private int ranking = 0;
  private int level = 0;

  private int passing = 0;
  private int blocking = 0;
  private int catching = 0;
  private int tackling = 0;
  private int kickPower = 0;
  private int strength = 0;
  private int speed = 0;
  private int stamina = 0;
  private int agility = 0;
  private int throwPower = 0;
  private int throwAccuracy = 0;
  private int carrying = 0;
  private int acceleration = 0;
  private int routeTracking = 0;
  private int toughness = 0;
  private int stiffArm = 0;
  private int kickAccuracy = 0;
  private int hitPower = 0;
  private int blitzing = 0;
  private int zoneCoverage = 0;
  private int manCoverage = 0;
  private int throwUnderPressurePower = 0;
  private int throwUnderPressureAccuracy = 0;
  private int blockPower = 0;
  private int jukeMoves = 0;

  public static final String[] statList = { "passing", "blocking", "catching", "tackling",
      "kickPower", "strength", "speed", "stamina", "agility", "throwPower", "throwAccuracy",
      "carrying", "acceleration", "routeTracking", "toughness", "stiffArm", "kickAccuracy",
      "hitPower", "blitzing", "zoneCoverage", "manCoverage", "throwUnderPressurePower",
      "throwUnderPressureAccuracy", "blockPower", "jukeMoves" };

  private Map<String, Integer> stats = new HashMap<>();

  public Player() {
    this.name = generateName();
    this.position = generatePosition();
  }

  public String generatePosition() {
    int randomNumber = (int) (Math.random() * 8) + 1;
    position = positions[randomNumber];
    return position;
  }

  public String generateName() {
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    name = firstName + " " + lastName;
    return name;
  }

  // "Quarterback", "Running Back", "Wide Receiver","Tight End", "Offensive
  // Lineman", "Kicker", "Defensive Lineman", "Linebacker","Defensive Back"
  public Map<String, Integer> generateStats() {
    switch (this.position) {
      case "Quarterback":
        break;
      case "Running Back":
        break;
      case "Wide Receiver":
        break;
      case "Tight End":
        break;
      case "Offensive Lineman":
        break;
      case "Kicker":
        break;
      case "Defensive Lineman":
        break;
      case "Linebacker":
        break;
      case "Defensive Back":
        break;
      default:
        break;

    }
    return stats;
  }

  public int generateRating() {
    return 0;
  }

  public int trackRanking() {
    return 0;
  }

  public int trackLevel() {
    return 0;
  }

  public int trackRating() {
    return 0;
  }
}
