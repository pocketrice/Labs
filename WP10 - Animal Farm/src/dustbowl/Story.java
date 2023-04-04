// WP10 - Animal Farm - Lucas Xie - AP CSA P5 - 2/22/23
package dustbowl;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static dustbowl.AnsiCode.*;

public class Story {
    public static void main(String[] args) throws InterruptedException {
        switch (prompt(ANSI_PURPLE + "You can pick between just getting the results or to hear a story based on Old McDonald with these results. I think the latter's a bit more entertaining, anyway...\n" + ANSI_RESET + "(Select 'RESULTS' for just results, or 'STORY' to hear a little tale.)", "Error: invalid option.", new String[]{"results", "story"}, false, false)) {
            case "results" -> {
                Farm farm = new Farm();
                farm.animalSounds();
            }

            case "story" -> {
                System.out.println(ANSI_CYAN + "\n\n\nGood morning. Or night, afternoon, et cetera. Whatever time you prefer. I'm the narrator of this fine story to be told today.");
                Thread.sleep(8000);
                System.out.println("My name? No, that's not necessary. The story's far more important, after all.");
                Thread.sleep(6000);
                System.out.println("Anyway, it's an honor to be here, telling you this epic tale of great proportions, blah blah blah...");
                Thread.sleep(6000);
                System.out.println("What's that? You're wondering what the story even is? Well, blimey, just wait a moment...");
                Thread.sleep(6000);
                fancyDelay(500, ANSI_RESET + "Finding a story...", "Ah, found it!", 5);
                System.out.println(ANSI_CYAN + "Here we are. Oh, this is quite a fascinating one. Now, shall we? Let me tell you a story...");
                Thread.sleep(10000);

                System.out.println(ANSI_BLUE + "\n\n\n\nOnce there was a wee lad who decided to be a farmer. His name was Ol' Mac.");
                Thread.sleep(7000);
                System.out.println("Now, Mac was a rather bright lad, and deciding to embrace a peaceful life of quiet, he decided to move to the middle of good ol' Britain.");
                Thread.sleep(9000);
                System.out.println("See, right here...? Ah, where'd he go?");
                Thread.sleep(6000);
                fancyDelay(400, ANSI_RESET + "Finding Ol' Mac...", "Found 'em!", 5);
                Story OldMac = new Story();

                System.out.println(ANSI_BLUE + "Ah, here we are. This is Ol' Mac. Say hello now, don't be shy.");
                Thread.sleep(5000);
                System.out.println(ANSI_RESET + "(You squint your eyes and see a faint wave in the far distance beyond a thick field of crops and dust.)");
                Thread.sleep(6000);

                System.out.println(ANSI_BLUE + "\n\nNow, Ol' Mac was feeling awfully hungry one sleepy morning, and so he decided to make a great farm.");
                Thread.sleep(7000);
                System.out.println("Not that he plans to fry 'em up in a little steel pan or anything. But we'll see what MacDonald does later in the story.");
                Thread.sleep(7000);

                System.out.println("\nAnyway, let's build Ol' Mac's farm now, shall we?");
                Thread.sleep(6000);
                Farm farm = OldMac.buildFarm();

                System.out.println(ANSI_BLUE + "\n\n\nRighty-o. Seems like Ol' Mac is happy with this bunch of farm creatures.");
                Thread.sleep(6000);
                System.out.println("Content with his haul of farm animals, Ol' Mac heads into the curiously blue cabin nearby...");
                Thread.sleep(6000);
                System.out.println("...soon.");
                Thread.sleep(6000);
                System.out.println("Almost there...");
                Thread.sleep(6000);
                System.out.println("Getting closer...");
                Thread.sleep(6000);
                System.out.println("Just a smidge more, Mac...");
                Thread.sleep(6000);
                System.out.println("C'mon, just... one... more... step...");
                Thread.sleep(6000);
                System.out.println("\n\nAh, he's finally made it. I do wonder what he may be up to in there. Don't you?");
                Thread.sleep(6000);
                System.out.println("Could he be calling his dear mother with this great news? Or perhaps he may be taking a nap? Ah, I can't decide...");
                Thread.sleep(9000);
                System.out.println("\n\nNow, now, let's not worry about Ol' Mac's whereabouts, since we're just in time for the animals to be filing in.");
                Thread.sleep(7000);
                System.out.println("By jove, look at them go! And look, look, they're making sounds too!");
                Thread.sleep(6000);
                System.out.println("\n" + ANSI_RESET);
                farm.animalSounds();
                System.out.println(ANSI_BLUE + "\n\nWell isn't that a delight?");
                Thread.sleep(6000);
                System.out.println("Now, I do wonder what Mac is up to. I haven't the faintest clue.");
                Thread.sleep(6000);
                System.out.println("...what's that? You say it's been foreshadowed? That he's going to bring out his pan and make a mockery of this story by nonchalantly and selfishly making some feast out of his precious farm animals?");
                Thread.sleep(12000);
                System.out.println("\nMm...");
                Thread.sleep(10000);
                System.out.println("\nI wholeheartedly believe in the righteous morals of Mac. You haven't got the faintest sense in you, what with your wild accusations and all. Blimey.");
                Thread.sleep(8000);
                System.out.println("\n\nSee, here comes Ol' Mac right now! Coming right along to prove me right and you wrong. Hmph.");
                Thread.sleep(8000);
                System.out.println("\nDon't you see? He's coming over to feed his farm animals with that great big glistening pan in his hand... ah.");
                Thread.sleep(10000);
                System.out.println("...and now he's, um, staking a For Sale sign on the property. Ah. I'm sure he's got plans to... expand his farmland or something. Yes, that's right.");
                Thread.sleep(10000);
                System.out.println("See? He's clearly... ah, heading into the city to start a multitrillion fast food corporation to live a life of gold and glory, ruining my story outright with an anticlimactic, disappointing turn of events. I see.");
                Thread.sleep(12000);
                System.out.println("Well, this is... wonderful. Brilliant. Absolutely brilliant.");
                Thread.sleep(7000);
                System.out.println("Not that I had any sort of investment in this story or anything, not like I wrote the script and paid Mac to act the part or put all my savings into this tomfoolery. Ah, absolutely not.");
                Thread.sleep(8000);
                System.out.println("Hmmmmm.");
                Thread.sleep(6000);
                System.out.println("\n\n\nWell, any thoughts? Seems like this story may have reached its end.");
                Thread.sleep(6000);
                System.out.println("Perhaps I should write a book on this little enigma. Yes, and I'll call it... 'Animal Farm'.");
                Thread.sleep(7000);
                System.out.println("I can see it now... the great story of a band of pigs set in sleepy England. Brilliant, isn't it?");
                Thread.sleep(8000);
                System.out.println("Or perhaps I'll make a lesson out of this story. Oh, I dunno, some spiel about polymorphism or whatever. Hmm.");
                Thread.sleep(8000);
                System.out.println("Well, no matter, as it looks like it's time for farewell. Y'see, we've reached...");
                Thread.sleep(6000);
                System.out.println(ANSI_RESET + "\n\n\t\t\tTHE END");
            }
        }
    }

    public Farm buildFarm() {
        Farm farm = new Farm();
        System.out.println(ANSI_PURPLE + "\n\n\n\nWe could make up some animals for story's sake, or we could, ahem, \"borrow\" some animals. The choice's yours!");
        switch (prompt(ANSI_RESET + "(Select NEW to create new animals, or PRESET to 'borrow' some preset animals.)\n", "Whoops, that's not an option. Let's try again...", new String[]{"new", "preset"}, false, false)) {
            case "new" -> {
                System.out.println(ANSI_PURPLE + "\nRight then, let's get to making some animals.");
                Pig pig = new Pig("pig", prompt("First off, what sound does this piggy make? I'd recommend 'wee', if you wouldn't mind. For the story's sake, of course.", false, true));
                Chick chick = new Chick("chick", prompt("Brilliant! Now, how about this tiny little chick here? Perhaps you might pick something such as 'peep'?", false, true));
                Cow cow = new Cow("cow", prompt("Interesting choice. What sound does this 'mooing creature' make? Might you pick... moo? Perhaps?",  false, true));
                NamedCow ncow = new NamedCow("cow", prompt("Remarkable. I'd have picked moo, though. Anyways, how about this other cow over here? I do wonder what sound it might make... maybe something along the lines of moo... hm.", false, true), prompt("Seems like this lil' beefer needs a name to. Might you name it something... such as 'Moobert'? I-- ahem, the story, needs a Moobert, of course. Do it for the story's sake, won't you?", false, true));
                farm = new Farm(pig, chick, cow, ncow);
            }

            case "preset" -> {
                farm = new Farm();
            }
        }
        return farm;
    }

    public static void fancyDelay(long delay, String loadMessage, String completionMessage, int iterations) throws InterruptedException { // Yoinked from SchudawgCannoneer
        int recursionCount = 0;
        System.out.print(loadMessage + " /");

        while (recursionCount < iterations) {
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b—");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b\\");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b|");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b/");
            recursionCount++;
        }
        if (!completionMessage.isBlank()) System.out.print("\b" + completionMessage + "\n" + ANSI_RESET);
        else System.out.println();
    }
    public static String prompt(String message, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        System.out.print(message);
        if (!message.equals(""))
            System.out.println();

        if (lineMode) {
            input.nextLine();
            nextInput = input.nextLine();
        } else {
            nextInput = input.next();
        }

        if (!isCaseSensitive) {
            nextInput = nextInput.toLowerCase();
        }

        return nextInput;
    }

    public static String prompt(String message, String errorMessage, String[] bounds, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        while (true)
        {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            if (lineMode) {
                input.nextLine();
                nextInput = input.nextLine();
            }
            else {
                nextInput = input.next();
            }

            if (!isCaseSensitive)
            {
                nextInput = nextInput.toLowerCase();

                for (int i = 0; i < bounds.length; i++)
                    bounds[i] = bounds[i].toLowerCase();
            }

            if (nextInput.matches(String.join("|", bounds)) || bounds[0].equals("")) {
                return nextInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
            }

        }
    }

    public static double prompt(String message, String errorMessage, double min, double max, boolean isIntegerMode)
    {
        Scanner input = new Scanner(System.in);
        String nextInput;
        double parsedInput = 0;
        boolean isValid;

        while (true) {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            nextInput = input.next();
            try {

                if (!isIntegerMode) {
                    parsedInput = Double.parseDouble(nextInput);
                } else {
                    parsedInput = Integer.parseInt(nextInput);
                }

                input.nextLine();
                isValid = true;
            } catch (Exception e) {
                isValid = false;
            }

            if (parsedInput >= min && parsedInput <= max && isValid) {
                return parsedInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
            }
        }
    }
}
