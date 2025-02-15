import java.util.ArrayList;

public class Utility {

    /**
     * Randomly selects an option out of a list of options
     * @param options possible items
     * @return random option
     */
    public static String choose(String... options) {
        int idx = (int) (Math.random() * options.length);
        return options[idx];
    }

    /**
     * Returns a random number in [min, max] inclusive
     * @param min smallest possible number
     * @param max largest possible number
     * @return random number in [min, max] inclusive
     */
    public static int random(int min, int max) {
        return (int) (Math.random() * (max-min+1)) + min;
    }

    /**
     * Returns true percent% of the time
     * @param percent how often to return true, x/100
     * @return true percent% of the time
     */
    public static boolean percent(int percent) {
        return percent >= random(1,100);
    }

    public static String formatDecimal(float num) {
        if (num % 1 == 0) return String.format("%.0f", num);
        if (num % 0.5 == 0) return String.format("%.1f", num);
        return String.format("%.2f", num);
    }

    public static String formatCatList(ArrayList<Cat> arr) {
        if (arr.size() == 1) return arr.getFirst().name;
        if (arr.size() == 2) return arr.get(0).name + " and " + arr.get(1).name;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.size()-1; i++)
            sb.append(arr.get(i).name).append(", ");
        sb.append("and ").append(arr.getLast().name);
        return sb.toString();
    }

    public static String formatStringList(ArrayList<String> arr) {
        if (arr.size() == 1) return arr.getFirst();
        if (arr.size() == 2) return arr.get(0) + " and " + arr.get(1);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.size()-1; i++)
            sb.append(arr.get(i)).append(", ");
        sb.append("and ").append(arr.getLast());
        return sb.toString();
    }

    public static String formatStringList(String[] arr) {
        if (arr.length == 1) return arr[0];
        if (arr.length == 2) return arr[0] + " and " + arr[1];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length-1; i++)
            sb.append(arr[i]).append(", ");
        sb.append("and ").append(arr[arr.length-1]);
        return sb.toString();
    }

}
