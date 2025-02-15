import java.util.ArrayList;

public class Den {
    public static final String HUGE = "Huge";
    public static final String LARGE = "Large";
    public static final String MEDIUM = "Medium";
    public static final String SMALL = "Small";
    public static final String TINY = "Tiny";

    String name;
    int numOccupants;
    int maxOccupants;
    ArrayList<Cat> occupants;
    String status;
    Clan clan;

    public Den() {
        occupants = new ArrayList<>();
    }

    // intended for commands that make dens TODO THIS!
    public Den(String type) {
        occupants = new ArrayList<>();
        status = "Sturdy";
    }

    public String toString() {
        return String.format("%s (%d/%d)\n", name, numOccupants, maxOccupants) +
                String.format("Housing: %s\n", occupants.isEmpty() ? "N/A" : Utility.formatCatList(occupants)) +
                String.format("Status: %s\n", status);
    }
}
