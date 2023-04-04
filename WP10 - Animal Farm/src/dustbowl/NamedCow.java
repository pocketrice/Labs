// WP10 - Animal Farm - Lucas Xie - AP CSA P5 - 2/22/23
package dustbowl;

public class NamedCow extends Cow {
    private String name;

    public NamedCow() {
        this("cow", "moo", "Moobert");
    }

    public NamedCow(String type, String sound, String name) {
        super(type, sound);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
