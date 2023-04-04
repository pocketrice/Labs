// WP10 - Animal Farm - Lucas Xie - AP CSA P5 - 2/22/23
package dustbowl;

public class Chick implements Animal {
    private String type, childishSound, grownSound;

    public Chick() {
        this("chicken", "cheep", "cluck");
    }

    public Chick(String t, String sound) {
        type = t;
        childishSound = grownSound = sound;
    }
    public Chick(String t, String chSound, String grSound) {
        type = t;
        childishSound = chSound;
        grownSound = grSound;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getSound() {
        return ((int)(Math.random() * 2) == 0) ? childishSound : grownSound;
    }
}
