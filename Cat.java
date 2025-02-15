import java.util.Objects;

public class Cat {
    Clan clan;

    String name;
    String player;
    String gender;
    String appearance;
    String bonds; // todo i dislike this

    float huntingSkill;
    float fightingSkill;
    float foragingSkill;
    float healingSkill;
    float gatheringSkill;

    int energy;

    public Cat(Clan clan) {
        this.clan = clan;

        bonds = "";

        huntingSkill = -1;
        fightingSkill = -1;
        foragingSkill = -1;
        healingSkill = -1;
        gatheringSkill = -1;

        energy = 2;
    }

    public boolean can(String action) {
        switch (action) {
            case "hunt": return energy > 0 && huntingSkill != -1;
            case "forage": return energy > 0 && foragingSkill != -1;
            case "gather": return energy > 0 && gatheringSkill != -1;
            case "patrol": return energy > 0 && fightingSkill != -1;
        }
        System.out.println("THERE IS NO CAN BRANCH FOR THIS : " + action);
        return false;
    }

    public String whyCant(String action) {
        switch (action) {
            case "hunt":
                if (energy <= 0) return name + " doesn't have enough energy to hunt.";
                else if (huntingSkill == -1) return name + " doesn't have a hunting skill.";
                else throw new IllegalStateException(name + " whyCant called but " + name + " CAN " + action);
            case "forage":
                if (energy <= 0) return name + " doesn't have enough energy to forage.";
                else if (huntingSkill == -1) return name + " doesn't have a foraging skill.";
                else throw new IllegalStateException(name + " whyCant called but " + name + " CAN " + action);
            case "gather":
                if (energy <= 0) return name + " doesn't have enough energy to gather.";
                else if (huntingSkill == -1) return name + " doesn't have a gathering skill.";
                else throw new IllegalStateException(name + " whyCant called but " + name + " CAN " + action);
            case "patrol":
                if (energy <= 0) return name + " doesn't have enough energy to patrol.";
                else if (huntingSkill == -1) return name + " doesn't have a fighting skill.";
                else throw new IllegalStateException(name + " whyCant called but " + name + " CAN " + action);
            default:
                throw new IllegalStateException(name + " called whyCant but there is no whyCant for " + action);
        }
    }

    /**
     * Returns prey caught and decrements energy. Requires hunting skill
     * @return prey caught
     */
    public int hunt() {
        int skill = (int) Math.floor(huntingSkill);

        int prey = switch (skill) {
            case 0 -> Utility.random(0,1);
            case 1 -> Utility.random(0,2);
            case 2 -> Utility.random(1,2);
            case 3 -> Utility.random(1,3);
            case 4 -> Utility.random(2,3);
            case 5 -> Utility.random(2,4);
            default -> throw new IllegalStateException(name + " hunted with an illegal hunting skill. skill = " + skill + ", huntingSkill = " + huntingSkill);
        };

        if (clan.season.equals(Clan.GREENLEAF))
            prey++;
        else if (clan.season.equals(Clan.LEAFBARE))
            prey--;

        if (clan.weather.equals(Clan.WINDY)) {
            if (Utility.percent(50)) {
                prey = switch (skill) { // this works shut up
                    case 0 -> Utility.percent(10) ? ++prey : --prey;
                    case 1 -> Utility.percent(20) ? ++prey : --prey;
                    case 2 -> Utility.percent(30) ? ++prey : --prey;
                    case 3 -> Utility.percent(40) ? ++prey : --prey;
                    case 4 -> Utility.percent(50) ? ++prey : --prey;
                    case 5 -> Utility.percent(60) ? ++prey : --prey;
                    default -> throw new IllegalStateException("Unexpected value: " + skill);
                };
            }
        }

        energy--;
        return Math.max(0, prey);
    }

    /**
     * Returns herbs found and decrements energy. Requires foraging skill
     * @return herbs found
     */
    public int forage() {
        int skill = (int) Math.floor(foragingSkill);

        int herbs = switch (skill) {
            case 0 -> Utility.random(0,1);
            case 1 -> Utility.random(0,2);
            case 2 -> Utility.random(1,3);
            case 3 -> Utility.random(2,4);
            case 4 -> Utility.random(3,5);
            case 5 -> Utility.random(4,6);
            default -> throw new IllegalStateException(name + " foraged with an illegal foraging skill. skill = " + skill + ", foragingSkill = " + foragingSkill);
        };

        if (clan.season.equals(Clan.GREENLEAF))
            herbs++;
        else if (clan.season.equals(Clan.LEAFBARE))
            herbs--;

        energy--;
        return Math.max(0, herbs);
    }

    /**
     * Returns sticks found and decrements energy. Requires gathering skill
     * @return sticks found
     */
    public int gather() {
        int skill = (int) Math.floor(gatheringSkill);

        int sticks = switch (skill) {
            case 0 -> Utility.random(0,3);
            case 1 -> Utility.random(1,3);
            case 2 -> Utility.random(1,4);
            case 3 -> Utility.random(2,4);
            case 4 -> Utility.random(2,5);
            case 5 -> Utility.random(3,5);
            default -> throw new IllegalStateException(name + " gathered with an illegal gathering skill. skill = " + skill + ", gatheringSkill = " + gatheringSkill);
        };

        if (clan.season.equals(Clan.LEAFFALL))
            sticks++;
        else if (clan.season.equals(Clan.NEWLEAF))
            sticks--;

        energy--;
        return Math.max(0, sticks);
    }

    /**
     * Returns fighting skill and decrements energy. Requires fighting skill. todo maybe return how much fighting skill?
     * @return amount the border was raised by
     */
    public float patrol() {
        if (fightingSkill == -1) throw new IllegalStateException(name + " tried to patrol but doesn't have a fighting skill");

        energy--;
        return 0.25f;
    }
}
