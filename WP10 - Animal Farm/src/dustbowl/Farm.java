// WP10 - Animal Farm - Lucas Xie - AP CSA P5 - 2/22/23
package dustbowl;


// *********  HOW IS POLYMORPHISM USED IN THIS PROJECT?  ***************************************************************************
//
// Polymorphism is seen in the two methods getType() and getSound(), as all Animals (Chick, Cow, NamedCow, Pig) implement these methods.
// Thus, when an object of any subclass of Animal (but is held in an Animal object) calls either method, polymorphism allows for the subclass' respective method to be called.
//
// *********************************************************************************************************************************


public class Farm {
    private Animal[] animals;

    public Farm() {
        animals = new Animal[]{new NamedCow(), new Cow(), new Chick(), new Pig()};
    }

    public Farm(Animal... animals) {
        this.animals = animals;
    }

    public void animalSounds() throws InterruptedException {
        for (Animal animal : animals) {
            if (animal instanceof NamedCow) {
                System.out.println(((NamedCow) animal).getName() + " the " + animal.getType() + " goes... " + animal.getSound() + ".");
            }
            else {
                System.out.println("The " + animal.getType() + " goes... " + animal.getSound() + ".");
            }
            Thread.sleep(3000);
        }
    }
}
