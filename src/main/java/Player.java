import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

class Player {
  // possibly introduce more posistions for more complexity
  public static final String[] positions = { "Quarterback", "Running Back", "Wide Receiver",
      "Tight End", "Offensive Lineman", "Kicker", "Defensive Lineman", "Linebacker",
      "Defensive Back" };
  public static final Faker faker = new Faker();
  public static int number1 = 0;
  public static int number2 = 0;
  public static int number3 = 0;
  public static int value1 = 0;
  public static int value2 = 0;
  public static int value3 = 0;
  public static int count1 = 0;
  public static int count2 = 0;
  public static int count3 = 0;
  public static final double specialStatMulti = 2.076;
  public static final double basicStatMulti = 1.2115;
  public static final double belowStatMulti = 1.0;
  public static double average1;
  public static double average2;
  public static double average3;

  public static final int basicStatMax = 59;
  public static final int basicStatMin = 21;
  public static final int specialStatMax = 99;
  public static final int specialStatMin = 60;
  public static final int belowStatMax = 20;
  public static final int belowStatMin = 0;
  public static final int ratingMax = 99;
  public static final int ratingMin = 0;

  private String name;
  private String position;
  private int rating = 0;
  private int salary = 0;
  private int ranking = 0;

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
  private int jumping = 0;

  private static ArrayList<Player> allPlayers = new ArrayList<>();

  public static final String[] statList = { "passing", "blocking", "catching", "tackling",
      "kickPower", "strength", "speed", "stamina", "agility", "throwPower", "throwAccuracy",
      "carrying", "acceleration", "routeTracking", "toughness", "stiffArm", "kickAccuracy",
      "hitPower", "blitzing", "zoneCoverage", "manCoverage", "throwUnderPressurePower",
      "throwUnderPressureAccuracy", "blockPower", "jukeMoves", "jumping" };

  private Map<String, Integer> stats = new HashMap<>();

  public Player() {
    this.name = generateName();
    this.position = generatePosition();
    this.stats = generateStats();
    this.rating = generateRating();
    allPlayers.add(this);
    rankAll();
  }

  public void printInfo() {
    System.out.println(this.name + " - " + this.position);
    System.out.println("Rating: " + this.rating);
    for (String s : statList) {
      System.out.println(s + ": " + this.stats.get(s));
    }
  }

  public String generatePosition() {
    int randomNumber = (int) (Math.random() * positions.length);
    position = positions[randomNumber];
    return position;
  }

  public String generateName() {
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    name = firstName + " " + lastName;
    return name;
  }

  private int randomInRange(int min, int max) {
    return (int) (Math.random() * (max - min + 1)) + min;
  }

  private void assignStats(String[] special, String[] basic, String[] below) {
    count1 = 0;
    count2 = 0;
    count3 = 0;
    value1 = 0;
    value2 = 0;
    value3 = 0;
    for (String s : special) {
      number1 = randomInRange(specialStatMin, specialStatMax);
      stats.put(s, number1);
      value1 += number1;
      count1++;
    }
    for (String e : basic) {
      number2 = randomInRange(basicStatMin, basicStatMax);
      stats.put(e, number2);
      value2 += number2;

      count2++;
    }
    for (String f : below) {
      number3 = randomInRange(belowStatMin, belowStatMax);
      stats.put(f, number3);
      value3 += number3;
      count3++;
    }
  }

  // "Quarterback", "Running Back", "Wide Receiver","Tight End", "Offensive
  // Lineman", "Kicker", "Defensive Lineman", "Linebacker","Defensive Back"
  public Map<String, Integer> generateStats() {
    String[] specialStats;
    String[] basicStats;
    String[] belowStats;
    switch (this.position) {
      case "Quarterback":
        specialStats = new String[] { "passing", "throwPower", "throwAccuracy",
            "throwUnderPressurePower", "throwUnderPressureAccuracy" };
        basicStats = new String[] { "speed", "agility", "stamina", "strength", "jukeMoves",
            "routeTracking" };
        belowStats = new String[] { "catching", "tackling", "kickPower", "carrying", "acceleration",
            "toughness", "stiffArm", "kickAccuracy", "hitPower", "blitzing", "zoneCoverage",
            "manCoverage", "blockPower", "blocking", "jumping" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Running Back":
        specialStats = new String[] { "carrying", "acceleration", "jukeMoves", "stiffArm",
            "agility", "speed", "stamina" };
        basicStats = new String[] { "strength", "toughness", "jumping", "catching",
            "routeTracking" };
        belowStats = new String[] { "passing", "blocking", "tackling", "kickPower", "kickAccuracy",
            "throwPower", "throwAccuracy", "hitPower", "blitzing", "zoneCoverage", "manCoverage",
            "throwUnderPressurePower", "throwUnderPressureAccuracy", "blockPower" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Wide Receiver":
        specialStats = new String[] { "carrying", "jukeMoves", "stiffArm", "speed", "catching",
            "routeTracking", "stamina", "jumping" };
        basicStats = new String[] { "strength", "toughness", "acceleration", "agility" };
        belowStats = new String[] { "passing", "blocking", "tackling", "kickPower", "kickAccuracy",
            "throwPower", "throwAccuracy", "hitPower", "blitzing", "zoneCoverage", "manCoverage",
            "throwUnderPressurePower", "throwUnderPressureAccuracy", "blockPower" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Tight End":
        specialStats = new String[] { "carrying", "stiffArm", "catching", "stamina", "strength",
            "toughness" };
        basicStats = new String[] { "acceleration", "agility", "speed", "routeTracking", "jumping",
            "jukeMoves", "blockPower", "blocking" };
        belowStats = new String[] { "passing", "tackling", "kickPower", "kickAccuracy",
            "throwPower", "throwAccuracy", "hitPower", "blitzing", "zoneCoverage", "manCoverage",
            "throwUnderPressurePower", "throwUnderPressureAccuracy" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Offensive Lineman":
        specialStats = new String[] { "blocking", "strength", "toughness", "blockPower",
            "hitPower" };
        basicStats = new String[] { "tackling", "stamina", "stiffArm", "blitzing" };
        belowStats = new String[] { "passing", "catching", "kickPower", "speed", "agility",
            "throwPower", "throwAccuracy", "carrying", "acceleration", "routeTracking",
            "kickAccuracy", "zoneCoverage", "manCoverage", "throwUnderPressurePower",
            "throwUnderPressureAccuracy", "jukeMoves", "jumping" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Kicker":
        specialStats = new String[] { "kickPower", "kickAccuracy", "agility", "speed" };
        basicStats = new String[] { "acceleration", "stamina", "jumping" };
        belowStats = new String[] { "passing", "catching", "tackling", "strength", "throwPower",
            "throwAccuracy", "carrying", "routeTracking", "toughness", "stiffArm", "hitPower",
            "blitzing", "zoneCoverage", "manCoverage", "throwUnderPressurePower",
            "throwUnderPressureAccuracy", "blockPower", "jukeMoves", "blocking" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Defensive Lineman":
        specialStats = new String[] { "strength", "hitPower", "toughness" };
        basicStats = new String[] { "tackling", "stamina", "blitzing", "manCoverage",
            "zoneCoverage" };
        belowStats = new String[] { "speed", "jumping", "agility", "acceleration", "passing",
            "blocking", "blockPower" /* added */, "stiffArm", "kickAccuracy", "kickPower",
            "throwPower", "throwAccuracy", "carrying", "catching", "routeTracking",
            "throwUnderPressureAccuracy", "throwUnderPressurePower", "jukeMoves" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Linebacker":
        specialStats = new String[] { "blitzing", "tackling", "hitPower", "strength" };
        basicStats = new String[] { "toughness", "speed", "agility", "stamina", "acceleration",
            "manCoverage", "zoneCoverage", "routeTracking", "jumping" };
        belowStats = new String[] { "passing", "blocking", "catching", "kickPower", "throwPower",
            "throwAccuracy", "carrying", "stiffArm", "kickAccuracy", "throwUnderPressurePower",
            "throwUnderPressureAccuracy", "blockPower", "jukeMoves" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      case "Defensive Back":
        specialStats = new String[] { "tackling", "routeTracking", "manCoverage", "zoneCoverage" };
        basicStats = new String[] { "speed", "stamina", "agility", "acceleration", "jumping",
            "hitPower", "strength", "toughness", "blitzing" };
        belowStats = new String[] { "passing", "blocking", "catching", "kickPower", "throwPower",
            "throwAccuracy", "carrying", "stiffArm", "kickAccuracy", "throwUnderPressurePower",
            "throwUnderPressureAccuracy", "blockPower", "jukeMoves" };
        assignStats(specialStats, basicStats, belowStats);
        break;

      default:
        specialStats = new String[] {};
        basicStats = statList;
        belowStats = new String[] {};
        assignStats(specialStats, basicStats, belowStats);
        break;
    }
    return stats;
  }

  public int generateRating() {
    average1 = 0;
    average2 = 0;
    average3 = 0;
    rating = 0;
    average1 = (count1 == 0) ? 0 : (double) value1 / count1;
    average2 = (count2 == 0) ? 0 : (double) value2 / count2;
    average3 = (count3 == 0) ? 0 : (double) value3 / count3;

    average1 *= specialStatMulti;
    average2 *= basicStatMulti;
    average3 *= belowStatMulti;

    double raw = (average1 + average2 + average3) / 3;
    rating = (int) Math.round(70.5 + (raw - 72.272) * 3.019);

    if (rating > 99) {
      rating = 99;
    } else if (rating < 50) {
      rating = 50;
    }
    return rating;
  }

  public static void rankAll() {
    for (String pos : positions) {
      ArrayList<Player> group = new ArrayList<>();
      for (Player p : allPlayers) {
        if (p.position.equals(pos)) {
          group.add(p);
        }
      }
      group.sort((a, b) -> b.rating - a.rating);
      for (int i = 0; i < group.size(); i++) {
        group.get(i).ranking = i + 1;
      }

    }
    for (Player p : allPlayers) {
      p.salary = p.generateSalary(p.position, p.ranking, p.rating);
    }

  }

  public static void printRankings(int num) {
    if (num == 0) {
      for (String pos : positions) {
        System.out.println("=== " + pos + " ===");

        ArrayList<Player> group = new ArrayList<>();
        for (Player p : allPlayers) {
          if (p.position.equals(pos)) {
            group.add(p);
          }
        }

        group.sort((a, b) -> a.ranking - b.ranking); // rank 1 first

        for (Player p : group) {
          System.out.println(p.ranking + ". " + p.name + " (" + p.rating + ")");
        }
        System.out.println();
      }
    } else if (num == 1) {
      ArrayList<Player> bySalary = new ArrayList<>(allPlayers);
      bySalary.sort((a, b) -> Integer.compare(b.salary, a.salary)); // highest pay first

      System.out.println("=== Highest Paid ===");
      for (int i = 0; i < bySalary.size(); i++) {
        Player p = bySalary.get(i);
        System.out.println(
            (i + 1) + ". " + p.name + " (" + p.position + "): $" + String.format("%,d", p.salary));
      }
    }

  }

  public Integer generateSalary(String pos, int ranking, int rating) {
    int minSalary = 800000;
    int maxSalary = 75000000;

    // 0.0 at rating 50, 1.0 at rating 99
    double t = (rating - 50) / 49.0;

    // cubing it makes stars paid far more than average players
    double curve = Math.pow(t, 3);

    // how much each position can earn compared to a Quarterback
    double posMulti;
    switch (position) {
      case "Quarterback":
        posMulti = 1.00;
        break;
      case "Defensive Lineman":
        posMulti = 0.60;
        break;
      case "Wide Receiver":
        posMulti = 0.55;
        break;
      case "Offensive Lineman":
        posMulti = 0.50;
        break;
      case "Defensive Back":
        posMulti = 0.45;
        break;
      case "Linebacker":
        posMulti = 0.45;
        break;
      case "Tight End":
        posMulti = 0.30;
        break;
      case "Running Back":
        posMulti = 0.30;
        break;
      default:
        posMulti = 0.15;
        break; // Kicker
    }

    salary = (int) (minSalary + (maxSalary - minSalary) * curve * posMulti);
    salary = Math.max(minSalary, Math.min(maxSalary, salary));
    return salary;
  }

  @Override
  public String toString() {
    return "Position: %s Name: %s ".formatted(this.position, this.name);
  }

}
