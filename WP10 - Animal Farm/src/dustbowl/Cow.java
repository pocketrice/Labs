// WP10 - Animal Farm - Lucas Xie - AP CSA P5 - 2/22/23
package dustbowl;

public class Cow implements Animal {
    private String type, sound;

    public Cow() {
        this("cow", "moo");
    }

    public Cow(String type, String sound) {
        this.type = type;
        this.sound = sound;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getSound() {
        return sound;
    }
}
