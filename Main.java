import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static final int CURRENT_MOON = 3; // moon we read from
    public static final int PENDING_MOON = 4; // moon we write to
    public static final String PATH = "WC CG Moons/moon%s.txt";

    public static void main(String[] args) throws Exception {
        // read
        Clan clan = readClan();
        System.out.println(clan);

        // edit
        inputCommands(clan);
        clan.passMoon();

        // write
        PrintWriter writer = new PrintWriter(String.format(PATH, PENDING_MOON));
        clan.moon = PENDING_MOON;
        writer.println(clan);
        writer.close();
    }

    public static Clan readClan() throws Exception {
        File file = new File(String.format(PATH, CURRENT_MOON));
        Scanner scan = new Scanner(file);

        Clan clan = new Clan();

        scan.next(); // #
        scan.next(); // moon
        clan.moon = scan.nextInt();
        scan.nextLine();
        scan.nextLine(); // space

        scan.next(); // season:
        clan.season = scan.next();
        clan.seasonCount = scan.next().charAt(1) - '0';

        scan.next(); // weather:
        clan.weather = scan.next();

        scan.next(); // event:
        clan.event = scan.next();
        scan.nextLine();

        scan.nextLine();
        scan.nextLine();
        scan.nextLine();

        scan.next(); // resources
        clan.prey = scan.nextInt();
        scan.next();
        clan.herbs = scan.nextInt();
        scan.next();
        clan.sticks = scan.nextInt();
        scan.next(); scan.next();
        String borderFraction = scan.next();
        clan.border = Float.parseFloat(borderFraction.substring(0,borderFraction.indexOf('/')));
        scan.next();
        clan.numDens = scan.nextInt();
        scan.nextLine();

        scan.nextLine();
        scan.nextLine();
        scan.nextLine();

        // cats
        scan.next();
        clan.numCats = scan.nextInt();
        clan.playerNames = new String[6]; // todo magic number
        clan.playerNumCats = new int[clan.numCats];
        for (int i = 0; i < clan.playerNames.length; i++) {
            String nameColon = scan.next();
            clan.playerNames[i] = nameColon.substring(0, nameColon.length()-1);
            clan.playerNumCats[i] = scan.nextInt();
        }
        scan.nextLine();

        scan.nextLine();
        scan.nextLine();
        scan.nextLine();

        String str = scan.nextLine();
        while (!str.equals("# WhaleClan Cats")) {
            str = scan.nextLine();
        }
        scan.nextLine();

        // input per cat info
        for (int i = 0; i < clan.numCats; i++) {
            Cat cat = new Cat(clan);
            cat.name = scan.next();
            cat.name = cat.name.substring(2, cat.name.length()-2); // cut out the **
            scan.next();
            cat.player = scan.next();
            scan.nextLine();
            cat.gender = scan.nextLine();
            cat.appearance = scan.nextLine();
            // skills
            for (int j = 0; j < 3; j++) {
                String skillType = scan.next();
                scan.next();
                float skillLevel = scan.nextFloat();

                switch(skillType) {
                    case "Hunting": cat.huntingSkill = skillLevel; break;
                    case "Fighting": cat.fightingSkill = skillLevel; break;
                    case "Foraging": cat.foragingSkill = skillLevel; break;
                    case "Healing": cat.healingSkill = skillLevel; break;
                    case "Gathering": cat.gatheringSkill = skillLevel; break;
                    default: throw new Exception("Bad skill type when inputting info for cat");
                }
            }
            scan.nextLine();
            String nextLine = scan.nextLine();
            while (scan.hasNextLine() && !nextLine.isEmpty()) { // todo ??
                cat.bonds += nextLine + "\n";
                nextLine = scan.nextLine();
            }
            clan.cats.add(cat);
        }

        scan.nextLine(); // # whaleclan dens
        scan.nextLine();

        // input per den // todo this doesn't work if no dens exist
        for (int i = 0; i < clan.numDens; i++) {
            Den den = new Den();
            String line = scan.nextLine();
            den.name = line.substring(0, line.indexOf('(')-1);
            den.numOccupants = Integer.parseInt(line.substring(line.indexOf('(')+1, line.indexOf('/')));
            den.maxOccupants = Integer.parseInt(line.substring(line.indexOf('/')+1, line.indexOf(')')));
            scan.next(); // housing:
            String occupants = scan.nextLine().trim();
            if (!occupants.equals("N/A")) {
                String[] names = occupants.split(" ");
                for (String name : names) {
                    den.occupants.add(clan.findCat(name));
                }
            }
            scan.next(); // status:
            den.status = scan.next();
            den.clan = clan;
            clan.dens.add(den);
            scan.nextLine();
        }

        scan.nextLine();
        scan.nextLine();
        scan.next(); scan.next(); // natural danger:
        clan.naturalDanger = scan.nextInt();
        scan.next(); scan.next(); // attack danger:
        clan.attackDanger = scan.nextInt();
        scan.next(); scan.next(); // illness danger:
        clan.illnessDanger = scan.nextInt();
        scan.next(); scan.next(); // encounter bonus:
        clan.encounterBonus = scan.nextInt();

        return clan;
    }

    public static void inputCommands(Clan clan) {
        System.out.println("INPUT COMMANDS.\n");
        Scanner scan = new Scanner(System.in);

        String input;
        inputLoop: do {
            input = scan.nextLine().trim();
            String[] command = input.substring(0, input.indexOf('(')).split("[ +]");
            String[] names = input.substring(input.indexOf('(') + 1).split("[ ,()]+");
            Cat[] cats = new Cat[names.length];
            for (int i = 0; i < names.length; i++) {
                cats[i] = clan.findCat(names[i]);
                if (cats[i] == null) {
                    System.out.println(names[i] + " is an invalid name, try again");
                    continue inputLoop;
                }
            }

            // main command switch
            switch (command[0]) {
                case "/hunt":
                    int prey = 0;
                    ArrayList<Cat> hunters = new ArrayList<>();
                    for (Cat cat : cats) {
                        if (cat.can("hunt")) {
                            prey += cat.hunt();
                            hunters.add(cat);
                        } else {
                            System.out.println(cat.whyCant("hunt"));
                        }
                    }
                    prey += hunters.size()-1;
                    clan.prey += prey;
                    if (!hunters.isEmpty())
                        System.out.printf("%s hunted and caught %d prey!\n", Utility.formatCatList(hunters), prey);
                    break;
                case "/forage":
                    int herbs = 0;
                    ArrayList<Cat> foragers = new ArrayList<>();
                    for (Cat cat : cats) {
                        if (cat.can("forage")) {
                            herbs += cat.forage();
                            foragers.add(cat);
                        } else {
                            System.out.println(cat.whyCant("forage"));
                        }
                    }
                    herbs += foragers.size()-1;
                    clan.herbs += herbs;
                    if (!foragers.isEmpty()) {
                        if (herbs == 1)
                            System.out.printf("%s foraged and found 1 herb!\n", Utility.formatCatList(foragers));
                        else
                            System.out.printf("%s foraged and found %d herbs!\n", Utility.formatCatList(foragers), herbs);
                    }
                    break;
                case "/gather":
                    int sticks = 0;
                    ArrayList<Cat> gatherers = new ArrayList<>();
                    for (Cat cat : cats) {
                        if (cat.can("gather")) {
                            sticks += cat.gather();
                            gatherers.add(cat);
                        } else {
                            System.out.println(cat.whyCant("gather"));
                        }
                    }
                    sticks += gatherers.size()-1;
                    clan.herbs += sticks;
                    if (!gatherers.isEmpty()) {
                        if (sticks == 1)
                            System.out.printf("%s gathered and found 1 stick!\n", Utility.formatCatList(gatherers));
                        else
                            System.out.printf("%s gathered and found %d sticks!\n", Utility.formatCatList(gatherers), sticks);
                    }
                    break;

                case "/patrol":
                    float border = 0;
                    float strength = 0;
                    ArrayList<Cat> patrollers = new ArrayList<>();
                    for (Cat cat : cats) {
                        if (cat.can("patrol")) {
                            border += 0.25f;
                            strength += cat.patrol();
                            patrollers.add(cat);
                        } else {
                            System.out.println(cat.whyCant("patrol"));
                        }
                    }
                    clan.border += border;
                    strength += (patrollers.size()-1) * 2;
                    if (!patrollers.isEmpty()) {
                        if (border == 1)
                            System.out.printf("%s patrolled and raised the border by %s point!\n", Utility.formatCatList(patrollers), Utility.formatDecimal(border));
                        else
                            System.out.printf("%s patrolled and raised the border by %s points!\n", Utility.formatCatList(patrollers), Utility.formatDecimal(border));
                    }

                    int chance = (int) (100 / (1 + Math.pow(Math.E, (-0.2 * (clan.encounterBonus-30)))));

                    if (Utility.percent(chance)) {
                        // todo they encountered something!


                        switch (Utility.random(0, 2)) {
                            case 0: // happy cats // todo
                                System.out.println("They encountered cats who wanna join");
                                break;
                            case 1: // happy injured or sick cats? they'll want to stay with the clan if healed quick. // todo
                                break;
                            case 2: // angry cats
                                System.out.print("While patrolling, the group encountered hostile cats, ");
                                if (strength > Utility.random(0, 25)) { // todo i made up this number
                                    System.out.println("but they were scared off. The border was raised by an extra point!");
                                    clan.attackDanger -= 5;
                                    clan.border++;
                                }
                                else {
                                    System.out.println("and the group didn't manage to scare them off...Uh oh.");
                                    clan.attackDanger += 5;
                                }
                                break;
                            case 3: // fighty cats // todo
                                System.out.println("They encounted cats via fight");
                                break;
                        }
                        clan.encounterBonus /= 2;
                    } else {
                        clan.encounterBonus += 5;
                    }

                    // todo patrol events
                    /**
                     * finding new cats - best if both groups have an equal size?
                     * getting into a small scuffle (injuries, plus/minus attack danger depending on strength)
                     * finding angry cats who hate you but no fight (plus or minus attack danger depending on strength)
                     */

                    break;

                default:
                    System.out.println("Not a command with stuff");
            }

        } while (!input.equals("/end moon"));
    }
}