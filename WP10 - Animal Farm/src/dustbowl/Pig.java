// WP10 - Animal Farm - Lucas Xie - AP CSA P5 - 2/22/23
package dustbowl;

public class Pig implements Animal {
    private String type, sound;

    public Pig() {
        this("pig", "wee");
    }

    public Pig(String type, String sound) {
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
