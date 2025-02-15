import java.util.ArrayList;
import java.util.Arrays;

public class Clan {

    //-----SEASONS & WEATHER-----//

    public static String NEWLEAF = "New-leaf";
    public static String GREENLEAF = "Green-leaf";
    public static String LEAFFALL = "Leaf-fall";
    public static String LEAFBARE = "Leaf-bare";
    String[] seasonArr = { NEWLEAF, GREENLEAF, LEAFFALL, LEAFBARE };

    public static String SUNNY = "Sunny";
    public static String CLOUDY = "Cloudy";
    public static String WINDY = "Windy";
    public static String RAINING = "Raining";
    public static String SNOWING = "Snowing";

    //-----OVERVIEW-----//

    int moon;
    String name; // todo currently not used lmao
    String season;
    int seasonCount;
    String weather;
    String event;

    ArrayList<String> news = new ArrayList<>();

    //-----RESOURCES-----//

    int prey;
    int herbs;
    int sticks;
    float border;

    //-----CATS-----//

    int numCats;
    String[] playerNames;
    int[] playerNumCats;
    ArrayList<Cat> cats = new ArrayList<>();

    //-----DENS-----//

    int numDens;
    ArrayList<Den> dens = new ArrayList<>();

    //-----ADMIN CONFIG: DANGER-----//

    int naturalDanger;
    int attackDanger;
    int illnessDanger;
    int encounterBonus;

    //-----METHODS-----//

    public Clan() {
    }

    public void passMoon() {
        moon++;

        // increment season
        seasonCount++;
        if (seasonCount == 4) {
            seasonCount = 1;
            int seasonIdx = Arrays.binarySearch(seasonArr, season) + 1;
            if (seasonIdx == 4) seasonIdx = 0;
            season = seasonArr[seasonIdx];
        }

        // randomize weather
        if (season.equals(LEAFBARE))
            weather = Utility.choose(SUNNY, CLOUDY, WINDY, SNOWING);
        else
            weather = Utility.choose(SUNNY, CLOUDY, WINDY, RAINING);



        // TODO GENERATE EVENTS
        // TODO GENERATE ILLNESS

        // admin configs. todo should probably check if no event happened first lmao
        naturalDanger += 5;
        attackDanger += 5;
        illnessDanger += 5;
    }

    /**
     * Finds 1 cat whose name starts with a given start or null if none or multiple match it
     * @param start prefix of name
     * @return cat that matches or null
     */
    public Cat findCat(String start) {
        ArrayList<Cat> matches = new ArrayList<>();
        for (Cat cat : cats) {
            if (cat.name.toLowerCase().startsWith(start.toLowerCase())) {
                matches.add(cat);
            }
        }
        return matches.size() == 1 ? matches.getFirst() : null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("# MOON %d\n", moon));
        sb.append(("** **\n"));
        sb.append(String.format("Season: %s [%d/3]\n", season, seasonCount));
        sb.append(String.format("Event: %s\n", event));
        sb.append("\n");

        sb.append("# WhaleClan Stats\n");
        sb.append(("** **\n"));
        sb.append(String.format("Prey: %d\n", prey));
        sb.append(String.format("Herbs: %d\n", herbs));
        sb.append(String.format("Sticks: %d\n", sticks));
        sb.append(String.format("Border Strength: %s/20\n", Utility.formatDecimal(border)));

        sb.append("\n");
        sb.append("\uD83D\uDC33\n");
        sb.append("\n");

        sb.append(String.format("Cats: %d\n", numCats));
        for (int i = 0; i < playerNames.length; i++) {
            sb.append(String.format("%s: %d\n", playerNames[i], playerNumCats[i]));
        }

        sb.append("\n");
        sb.append("\uD83D\uDC33\n");
        sb.append("\n");

        sb.append("Dens:\n");
        sb.append("N/A\n"); // todo hardcoded no dens
        sb.append("\n");

        sb.append("# WhaleClan News\n");
        sb.append("** **\n");
        sb.append("// INSERT NEWS HERE\n"); // todo hardcoded no news
        sb.append("\n");

        sb.append("# WhaleClan Cats\n");
        sb.append("** **\n");

        for (Cat cat : cats) {
            sb.append(String.format("**%s** | %s\n", cat.name, cat.player));
            sb.append(String.format("%s\n", cat.gender));
            sb.append(String.format("%s\n", cat.appearance));
            if (cat.huntingSkill != -1) sb.append(String.format("Hunting Skill: %s\n", Utility.formatDecimal(cat.huntingSkill)));
            if (cat.fightingSkill != -1) sb.append(String.format("Fighting Skill: %s\n", Utility.formatDecimal(cat.fightingSkill)));
            if (cat.foragingSkill != -1) sb.append(String.format("Foraging Skill: %s\n", Utility.formatDecimal(cat.foragingSkill)));
            if (cat.healingSkill != -1) sb.append(String.format("Healing Skill: %s\n", Utility.formatDecimal(cat.healingSkill)));
            if (cat.gatheringSkill != -1) sb.append(String.format("Gathering Skill: %s\n", Utility.formatDecimal(cat.gatheringSkill)));
            if (!cat.bonds.isEmpty()) sb.append(cat.bonds);
            sb.append("\n");
        }

        sb.append("# WhaleClan Dens\n");
        sb.append("** **\n");

        for (Den den : dens) {
            sb.append(den.toString());
            sb.append("\n");
        }

        sb.append("# ADMIN CONFIG PURPOSES\n");
        sb.append(String.format("Natural Danger: %d\n", naturalDanger));
        sb.append(String.format("Attack Danger: %d\n", attackDanger));
        sb.append(String.format("Illness Danger: %d\n", illnessDanger));

        return sb.toString();
    }
}
