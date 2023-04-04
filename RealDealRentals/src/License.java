import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;

import com.google.common.base.Throwables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.math.NumberUtils; // Apache Lang3
import org.lwjgl.*; // LWJGL
import org.javatuples.*; // JavaTuples
import net.objecthunter.exp4j.*; // Exp4j
import java.sql.*; // SQL


public class License<E> {
    private String vehicleType, brand, model, licensePlate, licenseKey, signature;
    private String[] fields = new String[5];
    Scanner input = new Scanner(System.in);
    static Map<String, String> helpMessages = new HashMap<>();
    static boolean ifRegistering = true;

    // ANSI colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    static final Map<String, String[]> brands = new LinkedHashMap<>();
    static final Map<String, Triplet<String, Integer, String>[]> models = new LinkedHashMap<>();

    public static void populateFieldMaps()
    { // todo: more encompassing / consistent model listing. Right now it's just some "highlights" of respective brand histories.

        brands.put("car", new String[]{"Toyota", "Annableu", /*"BMW",*/ "Bentley", "Buick", "DMC"/*"Volvo", "Mercedes-Benz", "Chevrolet", "Subaru", "Ford", "Volkswagen"*/});
        brands.put("truck", new String[]{"Toyota", "Ford", /*"Chevrolet", "GMC",*/ "Hoglovíc", "Canary"});
        brands.put("golfcart", new String[]{"Yamaha", "Club Cart", "Hoglovíc", "Annableu", /*"Congsia",*/ "Land'ola"});
        brands.put("tire", new String[]{/*"Continental", "Goodyear",*/ "Bridgestone", "Hoglovíc", "Canary"});


        // REAL BRANDS
        models.put("Toyota", new Triplet[]{Triplet.with("Camry", 2001, "car"), Triplet.with("Corolla", 2012, "car"), Triplet.with("Highlander", 2011, "car"), Triplet.with("Sequoia", 2011, "truck")});
        //models.put("BMW", new Triplet[]{Triplet.with("Coupé", 2020, "car"), Triplet.with("F40", 2004, "car"), Triplet.with("F52", 2017, "car"), Triplet.with("Tourer", 2014, "car"), Triplet.with("Isetta", 1953, "car")});
        models.put("Bentley", new Triplet[]{Triplet.with("Four Litre", 1931, "car"), Triplet.with("Mark VI", 1946, "car"), Triplet.with("Azure", 1995, "car"), Triplet.with("Hunaudieres", 1999, "car"), Triplet.with("Bentayga", 2022, "car")});
        models.put("Buick", new Triplet[]{Triplet.with("Excelle", 2003, "car"), Triplet.with("Verano", 2012, "car"), Triplet.with("Enclave", 2007, "car"), Triplet.with("Encore", 2019, "car"), Triplet.with("Invicta", 1959, "car"), Triplet.with("Rainier", 2003, "car")});
        //models.put("Volvo", new Triplet[]{Triplet.with("Carioca", 1935, "car"), Triplet.with("S60", 2018, "car"), Triplet.with("Duett", 1953, "car"), Triplet.with("City Taxi", 1977, "car")});
        models.put("Ford", new Triplet[]{Triplet.with("Club Cab", 1973, "truck"), Triplet.with("Lightning", 1993, "truck"), Triplet.with("Platinum", 2009, "truck"), Triplet.with("Maverick", 2023, "truck")});
        models.put("Yamaha", new Triplet[]{Triplet.with("Drive", 2007, "golfcart"), Triplet.with("G2", 1985, "golfcart"), Triplet.with("G11", 1994, "golfcart")});
        models.put("Club Cart", new Triplet[]{Triplet.with("DS", 1982, "golfcart"), Triplet.with("Precedents", 2004, "golfcart")});
        models.put("Bridgestone", new Triplet[]{Triplet.with("Potenza", 1979, "tire"), Triplet.with("Alenza", 2021, "tire"), Triplet.with("Turanza", 2018, "tire"), Triplet.with("Ecopia", 2010, "tire"), Triplet.with("Blizzak", 1988, "tire")});
        models.put("DMC", new Triplet[]{Triplet.with("DeLorean", 1981, "car")});

        // CUSTOM BRANDS
        models.put("Annableu", new Triplet[]{Triplet.with("Bleue", 1998, "car"), Triplet.with("Aquarèlle", 2000, "car"), Triplet.with("Lavande", 2005, "car"), Triplet.with("Cerise", 2006, "golfcart"), Triplet.with("Ville", 2008, "golfcart"), Triplet.with("Rainure", 2019, "car")});
        models.put("Hoglovíc", new Triplet[]{Triplet.with("Hoggo", 2003, "truck"), Triplet.with("Boar", 2005, "truck"), Triplet.with("Oinker", 2009, "golfcart"), Triplet.with("Baconéur", 2010, "tire")});
        models.put("Land'ola", new Triplet[]{Triplet.with("'Alawai", 1966, "golfcart"), Triplet.with("Pāʻani", 2010, "golfcart"), Triplet.with("Hibiscus", 2018, "golfcart")});
        models.put("Canary", new Triplet[]{Triplet.with("Thrush", 1999, "tire"), Triplet.with("Plover", 2000, "tire"), Triplet.with("Kiwi", 2002, "truck"), Triplet.with("Sparrow", 2005, "tire"), Triplet.with("Hummingbird", 2012, "tire")});
    }
    public License()
    {
        vehicleType = "N/A";
        brand = "N/A";
        model = "N/A";
        licensePlate = "N/A";
        licenseKey = "N/A";
        signature = "N/A";

        fields = new String[]{vehicleType, brand, model, licensePlate, licenseKey, signature};
    }

    public License(String vhcType, String br, String mdl, String lcsPlt, String lcsKey, String sgntr) // Allows for "variable filling" -- instead of a million constructors, you can put "N/A" to leave an instance var default!
    {
        String[] inputs = {vhcType, br, mdl, lcsPlt, lcsKey, sgntr};

        for (int i = 0; i < 5; i++)
        {
            if (!inputs[i].equals("N/A"))
            {
                fields[i] = inputs[i];
            }
        }

        vehicleType = fields[0];
        brand = fields[1];
        model = fields[2];
        licensePlate = fields[3];
        licenseKey = fields[4];
        signature = fields[5];
    }

    public void intelliCents(String input, String inputType)
    {
        List<String> unfilledFields = new ArrayList<>();
        List<String> filledFields = new ArrayList<>();

        for (String field : fields) // Categorize fields based on filled or not
        {
            if (field.equals("N/A"))
            {
                unfilledFields.add(field); // does this only add N/A? need to make it get name of var?
            }
            else
            {
                filledFields.add(field); // see above question
            }
        }

        // Possibilities...
        // FIELDS: vehicleType, brand, model, license, signature
        // - In any case that there is only one type of option for a particular set of choices (e.g. golfcart >> 1998 only has one brand or model), auto-fill that.
        // - If a path does not need all choices (e.g. a brand will only have that brand's models) then limit the choice pool for other selections. (no autofill yet).
        // - If a model is selected then vehicleType and brand should be auto-filled.
        // - A particular license variant should automatically match it to the valid state, or if several exist limit the pool. TODO states
        // - Derive potential license plates based on key (for renewals perhaps). Not sure if there can be identical ones, but if so list pool to user.
        // - If a signature is filled, the system should maybe compile probable fills to the user (e.g. [filling] (80% certainty), [filling] (53% certainty))
    }

    public String[] icFields(String fieldType) { // Restrict field options based on IC data.
        List<String> unfilledFields = List.of(determineArrayContentState(fields,"N/A"));
        List<String> filledFields = List.of(determineArrayContentState(fields, "{!}N/A"));

        List<String> fieldOptions = new ArrayList<>();

        switch (fieldType) {
            case "brand" -> {
                //if (filledFields.contains("vehicleType")) {
                    for (Map.Entry<String, List<String>> entry : icCategorize().entrySet()) {
                        if (entry.getValue().contains(/*vehicleType*/"car"))
                            fieldOptions.add(entry.getKey());
                    }
                //}
            }

            default -> {
                // Do crap
            }

        }

        return fieldOptions.toArray(new String[0]);
    }

    public ListMultimap<String, String> icCategorize() { // <brand, vehicleTypes> // todo: fix listmultimap
        //Map<String, List<String>> brandCategorization = new HashMap<>();
        ListMultimap<String, String> brandCategorization = new MultimapBuilder.ListMultimapBuilder<>

        // Get all possible brand names by condensing all entries in 'brands' map. Then, determine which types each brand sells for.
        for (String key : brands.keySet()) {
            for (String brand : brands.get(key)) {
                if (!brandCategorization.containsKey(brand)) {
                    brandCategorization.put(brand, List.of(key));
                }
                else {
                    List<String> updatedCategorization = new ArrayList<>(brandCategorization.get(brand)); // Temporarily categorization list to apply changes to, then "commit" them to main map.
                    updatedCategorization.add(key);

                    brandCategorization.replace(brand, updatedCategorization);
                }
            }
        }
        return brandCategorization;
    }

    public <T> T[] determineArrayContentState(T[] array, String params) {
    parseCondition("{wario} - 1", )
    }



    @SafeVarargs
    public final <T> boolean parseCondition(String condition, T... variables) { // Varargs!
        //!({wario} - {bucket} == 0) || {war} > 3
        // v Variables support
        //      o Variable validation (can those vars work?)
        //      v Fix vararg crap (something something MEMORY? something something)
        //
        // - OR (||), AND (&&)
        //      o Requires multicondition support
        //
        // - Equals (==)
        //
        // - Inverter (!), Inv-eq (!=)
        //
        // - Parentheses ()
        //      (?) Ability to set precedence/define bounds.

        List<String> allVars = new ArrayList<>(List.of(getSubstrings(condition, Pattern.compile("\\{.*?}"))));
        assert(allVars.size() == variables.length);

        // Multiconditions (OR/ADD)
        List<String> exprs = new ArrayList<>(List.of(condition.split("\\|\\||&&"))); // Split into expressions
        List<Integer> exprVarCount = new ArrayList<>();

        for (String expr : exprs) { // Sample: !{wario} - {bucket} == 0
            exprVarCount.add(numOfStringContent(expr, Pattern.compile("\\{.*?}")));
            for (int i = 0; i < exprVarCount.get(exprs.indexOf(expr)); i++) { // Evaluate vars in expr
                expr = expr.replaceFirst("\\{.*?}", setGet(variables, expr.substring(expr.indexOf("{")+1, expr.indexOf("}"))).toString());
            } // Set<T> = variables
        }
    }

    public <T> T setGet(T[] collection, String item) throws {// It's called setGet, but it can apply to all Collections. It's called that just b/c I decided to make it after not seeing any .get for Sets.
        for (T colItem : collection) {
            if (colItem.toString().matches(item)) {
                return colItem;
            }
        }
        throw new
    }


    /*
    public <T> List<T> mapGetAll(Map<?, T> map) { // is this useless? map.values()?
        List<T> getsList = new ArrayList<>();

        for (Object key : map.keySet()) {
            getsList.add(map.get(key));
        }

        return getsList;
    }*/

    public <T /*extends String, Pattern*/> int numOfStringContent(String string, T content) {
        if (content instanceof Pattern) { // Use regex instead of literally checking for that string
            return string.length() - string.replaceAll(String.valueOf(content), "").length();
        }

        return string.length() - string.replace((CharSequence) content, "").length();
    }

    public String[] getSubstrings(String string, Pattern pattern) {
        List<String> substrings = new ArrayList<>();
        String editedString = string;

        for (int i = 0; i < numOfStringContent(string, pattern); i++) {
            substrings.add(pattern.matcher(editedString).group(1));
            editedString = editedString.replaceFirst(String.valueOf(pattern), "");
        }

        return substrings.toArray(new String[0]);
    }


    public static ArrayList<String> generateRandomData(String fieldType, int amount) // todo: allow for random garbage data when some fields filled (there has to be restrictions)
    {
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            switch (fieldType) {
                case "vehicleType" -> {
                     data.add(weightedRandom(new String[]{"car", "truck", "golfcart", "tire"}, new double[]{0.5, 0.2, 0.2, 0.1}));
                }

                case "brand" -> {
                    data.add(weightedRandom(new String[]{"Toyota", "Bentley", "Buick", "Ford", "Yamaha", "Club Cart", "Bridgestone", "DMC", "Annableu", "Hoglovíc", "Land'ola", "Canary"}, new double[]{0.083, 0.083, 0.083, 0.083, 0.083, 0.083, 0.083, 0.083, 0.083, 0.083, 0.083, 0.087}));
                }

                case "model" -> { // todo: actually put models here
                    data.add(weightedRandom(new String[]{"NO_MODEL_FOUND"}, new double[]{1}));
                }

                case "license" -> { // You should generate only one or the other -- b/c they aren't connected right now.
                    data.add("" + (char) ((int) (Math.random() * 26)) + (char) ((int) (Math.random() * 26)) + (char) ((int) (Math.random() * 26)) + Math.ceil((Math.random() * 10 - 1)) + Math.ceil((Math.random() * 10 - 1)) + Math.ceil((Math.random() * 10 - 1)));
                }

                case "licenseKey" -> {
                    data.add("" + (char) ((int) (Math.random() * 26)) + +Math.ceil((Math.random() * 10 - 1)) + Math.ceil((Math.random() * 10 - 1)) + Math.ceil((Math.random() * 10 - 1)));
                }
            }
        }

        return data;
    }

    public String weightedRandom(String[] choices, double[] weights) throws BadWeightsException, NoReturnValueException // Weights must add up to 1.
    {
        if (DoubleStream.of(weights).sum() != 1) { // <- Compact "add up all values of array" 1-liner
            System.out.println(ANSI_RED + "Warning to lazy dev! weightedRandom() weights were not configured properly (must add up to 1).");
            throw new BadWeightsException("BAD_WEIGHTS_EXCEPTION: " + Arrays.toString(weights));
        }

        double rng = Math.random();

        for (int i = 0; i < weights.length; i++)
        {
            if (rng < weights[i])
                return choices[i];
            else
                rng -= weights[i];
        }
        throw new NoReturnValueException("NO_RETURN_VALUE_EXCEPTION");
    }

    public class BadWeightsException extends Exception {
        public BadWeightsException(String errorMessage) {
             super (errorMessage);
        }
    }

    public class NoReturnValueException extends Exception {
        public NoReturnValueException(String errorMessage) {
            super (errorMessage);
        }
    }


    public static void main(String[] args)
    {
        // ** MYSQL DATABASE TESTING BELOW. **
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "ParadigousParaPlat!*004");
            Statement sment = con.createStatement();
            ResultSet rs = sment.executeQuery("select * from vehicles");
            while (rs.next())
            {
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
            }
        }
        catch(Exception e) {
            System.out.println(ANSI_RED + "MySQL error! " + e + ANSI_RESET);
        }

        // ** END OF MDTB. **

        populateHelpMessages();
        populateFieldMaps();

        System.out.println(generateRandomData("brand", 10));

        License licenseman = new License();
        System.out.println(licenseman.icCategorize());

        //System.out.println(ANSI_CYAN + "Welcome to Real Deal Rentals, the fastest deals and wheels in the west! \nWith accolades such as publisher of a NYT bestseller for 52 weeks straight and honorary Alaska fisher-of-the-day, we are literally unbeatable in exemplary accomplishment. Seriously.");


        System.out.println(ANSI_CYAN + "\nThe name's Jeroo J. Joe, and I'll be helping you with your rental today. C'mon, hop to it! More time means more motor for you, after all." + ANSI_RESET);

        //System.out.println(ANSI_CYAN + "========================================================================================================================================");
            switch (licenseman.prompt(ANSI_CYAN + "What's on your mind today? Register or renew a rental? Look around for a rental car? Or perhaps not sure yet?" + ANSI_RESET, "Error: this message should not appear!", new String[]{"register", "renew", "lookaround", "notsure"}, "INTRO", false, false)) {
                case "register" -> {
                    break;
                }

                case "renew" -> {
                    System.out.println(ANSI_GREEN + "Renewed your vehicle rental!" + ANSI_RESET);
                    System.exit(0);
                }

                case "lookaround" -> {
                    System.out.println(ANSI_GREEN + "Looked around the premises!" + ANSI_RESET);
                    System.exit(0);
                }

                case "notsure" -> {
                    System.out.println(ANSI_GREEN + "Did whatever you wanted to!" + ANSI_RESET);
                    System.exit(0);
                }
            }

        System.out.println(ANSI_PURPLE + "\nYou'll need to fill out a form before finalizing this rental. Enclosed is a form with fields -- check 'HELP' for more info!");
        System.out.println("Go to the 'SIGNATURE' field to wrap up your form-filling spree. And make sure to keep an eye on your progress using 'PROGRESS'!\n\n\n" + ANSI_RESET);

            while (ifRegistering) {
                switch (licenseman.prompt("What field would you like to fill out?", "Error: that's not a valid field. The form's fields are 'vehicle type', 'brand', 'model', 'license', and 'signature'.\nYou may also use 'progress' to see how much you have left to do.\n", new String[]{"vehicle_type", "brand", "model", "license", "signature", "progress"}, "INTRO", false, false)) {
                    case "vehicle_type" -> {
                        if (licenseman.fillAssertion(0)) {
                            licenseman.vehicleType = licenseman.prompt("What kind of wheeler are you driving?", "Error: invalid type.", new String[]{"car", "truck", "motorcycle", "golfcart", "tire"}, "VEHICLE_TYPE", false, false);
                            System.out.println(ANSI_GREEN + "Gotcha! I've got your vehicle marked down as a " + licenseman.vehicleType + ".\n" + ANSI_RESET);
                            System.out.println(Arrays.toString(licenseman.icFields("brand")));
                        }
                    }

                    case "brand" -> {
                        if (licenseman.fillAssertion(1)) {
                            licenseman.brand = licenseman.prompt("Hmm, what brand is that vehicle you're driving?", "Error: invalid brand. You may submit a new brand to our lists if you'd like.", new String[]{"Toyota", "Hyundai", "DeLorean", "Hoglovíc", "Canary", "Congsia", "Land'ola", "Annableu"}, "BRAND", false, true);
                            System.out.println(ANSI_GREEN + "Sweet! That's a mighty fine " + licenseman.brand + " you're rockin'!\n" + ANSI_RESET);
                        }
                    }

                    case "model" -> {
                        if (licenseman.fillAssertion(2)) {
                            licenseman.model = licenseman.prompt("Ah, what model have ya got?", "Error: invalid model. You may submit a new model to our lists if you'd like or do a quick look-up.", new String[]{"Camry", "Corolla", "Highlander", "Sienna", "Tacoma", "Tundra"}, "MODEL", false, true);
                            System.out.println(ANSI_GREEN + "Radical! A great driver deserves a great " + licenseman.model + ", after all.\n" + ANSI_RESET);
                        }
                    }

                    case "license" -> {
                        if (licenseman.fillAssertion(3)) {
                            String rawLicensePlate = licenseman.prompt("Interesting, well hit me with it. What's your license plate ID?", "Error: invalid license plate. Type 'help' and let's see if maybe you made a typo.", new String[]{""}, "LICENSE", true, false).toUpperCase();
                            rawLicensePlate = rawLicensePlate.replace("-", "").replace("_", "").replace("/", "").replace(" ", ""); // Getting closer to standard form (ABC123)

                            if (rawLicensePlate.length() == 6 && rawLicensePlate.substring(0, 3).matches("[a-zA-Z]*") && rawLicensePlate.substring(rawLicensePlate.length() - 3).matches("[0-9]*")) {
                                licenseman.licensePlate = new StringBuilder(rawLicensePlate).insert(3, " ").toString(); // Set to standard form (ABC 123).
                                licenseman.licenseKey = licenseKey(licenseman.licensePlate);
                                System.out.println(ANSI_GREEN + "OK, so your license plate is \"" + licenseman.licensePlate + "\" and your rental key shall hereby be " + licenseman.licenseKey + ". Duly noted!\n" + ANSI_RESET);
                            } else {
                                System.out.println(ANSI_RED + "Error: invalid license plate. Remember that a license plate's format is 'ABC123', but you can format in any way!\n" + ANSI_RESET);
                            }
                        }
                    }

                    case "signature" -> {
                        if (licenseman.fillAssertion(4)) {
                            String confirmation = licenseman.prompt("Gotcha, you all ready to confirm your details and wrap up?", "Error: invalid option.", new String[]{"yes", "no"}, "NO_CONTEXT", false, false);
                            if (confirmation.equals("yes")) {
                                licenseman.signature = licenseman.prompt("Sweet. Let's get started then. Just digitally sign with this oversized fountain pen right here... and all done!", "Error: invalid signature.", new String[]{""}, "SIGNATURE", true, true);
                                ifRegistering = false;
                            } else {
                                System.out.println("No worries. Let's get back to fixin' up anything you've got left...");
                            }
                        }
                    }

                    case "progress" -> { // Overcomplicated because proper grammar is a must!
                        boolean isAllFieldsFilled = true;
                        String[] fields = {licenseman.vehicleType, licenseman.brand, licenseman.model, licenseman.licensePlate, licenseman.signature}; // Since LP and LK are linked, it's safe to assume if one is filled then the other is too.
                        String[] remainingFields = {"your vehicle type", "brand", "model", "license info", "a confirmation signature"};
                        StringBuilder progressMessage = new StringBuilder("You still need to fill out ");
                        int unfilledFields = 0;

                        for (int i = 0; i < 5; i++) {
                            if (fields[i].equals("N/A")) {
                                progressMessage.append(remainingFields[i]).append(", ");
                                isAllFieldsFilled = false; // At least one field is not filled.
                                unfilledFields++;
                            }
                        }


                        // Note: YSNTFO == you still need to fill out
                        progressMessage.delete(progressMessage.length() - 2, progressMessage.length()); // "YSNTFO brand, " -> "YSNTFO brand"

                        if (progressMessage.toString().contains(",")) {
                            progressMessage.insert(progressMessage.lastIndexOf(",") + 1, " and"); // Insert 'and' at the last comma ("YSNTFO brand, model, license info" -> "YSNTFO brand, model, and license info")
                        }

                        if (progressMessage.length() - progressMessage.toString().replace(",", "").length() == 1) // A comma is not necessary if 2 items.
                        {
                            progressMessage.deleteCharAt(progressMessage.indexOf(",")); // "YSNTFO brand, and model" -> "YSNTFO brand and model"
                        }

                        if (unfilledFields != 1) {
                            progressMessage.append(". ").append(unfilledFields).append(" more to go!"); // Conclude the sentence.
                        } else
                            progressMessage.append(". ").append("Just one final step left!");


                        if (isAllFieldsFilled) {
                            progressMessage.replace(0, progressMessage.length(), "All clear! You have all fields filled, and you're ready to roll!");
                        }
                        System.out.println(ANSI_CYAN + "\n" + progressMessage + "\n" + ANSI_RESET);
                    }
                }
            }

            System.out.println(ANSI_YELLOW + "\n\nThanks for doing business with us today -- you're the real deal!! Attached below is your receipt." + ANSI_RESET);
            licenseman.printReceipt();
    }


    public boolean fillAssertion(int index) // TODO: unique dialogue for repeated fills (e.g. after the first fill). Do this through deepPrompt().
    {
        String[] fields = {vehicleType, brand, model, licensePlate, signature}; // Again, LP and LK are linked so the assumption is if one is filled the other is too
        if (!fields[index].equals("N/A"))
        {
            String revisionConfirmation = prompt(ANSI_YELLOW + "This field has been filled out already. Do you want to revise it?" + ANSI_RESET, "Error: invalid option.", new String[]{"yes", "no"}, "NO_CONTEXT", false, false);

            if (revisionConfirmation.equals("yes"))
            {
                return true;
            }
            else {
                System.out.println(ANSI_YELLOW + "Revision cancelled.\n" + ANSI_RESET);
                return false;
            }
        }
        else
            return true;
    }

    public static String licenseKey(String license)
    {
        // CPR 607
        // Add ASCII values of three letters
        // Sum of three # + ans
        // Letter ID = ans % 26 + 1
        // Make, model, license #

        char[] licenseLetters = license.split(" ")[0].toCharArray();
        int licenseNumbers = Integer.parseInt(license.split(" ")[1]);

        /*int[] licenseNumbers = new int[3]; // The license #s don't need to be separated into individual numbers -- only need the 3-digit number instead. Makes things a whole lot easier!

       for (int i = 0; i < 3; i++)
       {
            licenseNumbers[i] = Character.getNumericValue(license.split(" ")[1].charAt(i));
       }*/


       int numKeyHalf = /*Arrays.stream(licenseNumbers).sum()*/licenseNumbers + licenseLetters[0] + licenseLetters[1] + licenseLetters[2];
       char charKeyHalf = (char)('A' + numKeyHalf % 26);

       return "" + charKeyHalf + numKeyHalf;
    }

    public String prompt(String message, String errorMessage, String[] keywords, String context, boolean lineMode, boolean isCaseSensitive) // TODO: a regex option, case-sensitivity toggle
    {
        boolean helpFlag;
        String nextInput;

        while (true)
        {
            helpFlag = false;
            if (!(message.matches("NO_MESSAGE")))
            {
                System.out.println(message);
            }


            if (lineMode) {
                input.nextLine();
                nextInput = input.nextLine();
            }
            else {
                nextInput = input.next();
            }

            if (!isCaseSensitive)
            {
                nextInput = nextInput.toLowerCase(); // Set to lowercase (TODO: for case-sensitivity, make sure to apply to keywords too!)
            }



            if (nextInput.matches("help") && !context.matches("NO_CONTEXT"))
            {
                helpMode(context);
                helpFlag = true;
            }


            if (!helpFlag) {
                if (nextInput.matches("cancel"))// NOTE: Failsafe in case 'cancel' is a part of keywords (which should not happen b/c it shouldn't even be used as a keyword.)
                {
                    System.out.println(ANSI_RED + "Action cancelled.\n" + ANSI_RESET);
                    return null;
                }
                if (nextInput.matches("help")) {
                    helpMode("CONTEXT");
                }

                if (nextInput.matches(String.join("|", keywords)) || keywords[0].matches("")) {
                    return nextInput;
                } else {
                    System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
                }

            }
        }
    }
/*
    public String deepPrompt(String message, String errorMessage, String[] keywords, String context, boolean lineMode) // WIP: deepPrompt will allow for message 'depth' -- so if the prompt is called again, it can output a different message and vice versa. It'll "remember" your past decisions/actions.
    {
        boolean helpFlag;
        String nextInput = "";

        while (true)
        {
            helpFlag = false;
            if (!(message.matches("NO_MESSAGE")))
            {
                System.out.println(message);
            }


            if (lineMode) {
                input.nextLine();
                nextInput = input.nextLine();
            }
            else {
                nextInput = input.next();
            }



            if (nextInput.matches("help") && !context.matches("NO_CONTEXT"))
            {
                helpMode(context);
                helpFlag = true;
            }


            if (!helpFlag) {
                if (nextInput.matches("cancel"))// NOTE: Failsafe in case 'cancel' is a part of keywords (which should not happen b/c it shouldn't even be used as a keyword.)
                {
                    System.out.println(ANSI_RED + "Action cancelled.\n" + ANSI_RESET);
                    return null;
                }
                if (nextInput.matches("help")) {
                    helpMode("CONTEXT");
                }

                if (nextInput.matches(String.join("|", keywords)) || keywords[0].matches("")) {
                    return nextInput;
                } else {
                    System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
                }

            }
        }
    }*/

    public String fancify(String string, String font)
    {
        StringBuilder mutableString = new StringBuilder(string);
        String[] fontset = new String[10]; // TODO: refix to char? -- might need ascii codes for the fancy-chars rather than actual char themselves

        if (font.equals("Moderna"))
        { // TODO: letters in fontset -- might need to do map since the workaround'll break w/ letters.
            fontset = new String[]{"\uD835\uDFE2","\uD835\uDFE3","\uD835\uDFE4","\uD835\uDFE5", "\uD835\uDFE6","\uD835\uDFE7","\uD835\uDFE8","\uD835\uDFE9","\uD835\uDFEA","\uD835\uDFEB"}; // 𝟢𝟣𝟤𝟥𝟦𝟧𝟨𝟩𝟪𝟫
        }

        if (font.equals("New Roman"))
        {
            fontset = new String[]{"\uD835\uDFCE","\uD835\uDFCF","\uD835\uDFD0","\uD835\uDFD1","\uD835\uDFD2","\uD835\uDFD3","\uD835\uDFD4","\uD835\uDFD5","\uD835\uDFD6","\uD835\uDFD7"}; // 𝟎𝟏𝟐𝟑𝟒𝟓𝟔𝟕𝟖𝟗
        }

        for (int i = 0; i < string.length(); i++)
        {
            if (NumberUtils.isParsable(String.valueOf(string.charAt(i)))) // Is the char at this index an integer?
                mutableString.insert(i, fontset[Integer.parseInt(String.valueOf(string.charAt(i)))]).deleteCharAt(i); // TEMPORARY WORKAROUND!!! works only with numbers
        }

        return mutableString.toString();
    }

    public static void helpMode(String context)
    {
        System.out.println(ANSI_BLUE + "\n*************************************************************************************************\n");
        System.out.println(helpMessages.get(context));
        System.out.println("\n*************************************************************************************************\n" + ANSI_RESET);
    }

    public static void populateHelpMessages()
    {
        helpMessages.put("INTRO", "Choose which field to fill out. When you're all done, go ahead and confirm with your signature. \nCheck your progress using the 'PROGRESS' keyword. \nOptions: VEHICLE_TYPE, BRAND, MODEL, LICENSE, SIGNATURE, PROGRESS \n\nTip: based on the field you fill out, another field may be automatically prefilled with our IntelliCents system. If you would like to disable this feature, type \"DISABLE_IC\".");
        helpMessages.put("VEHICLE_TYPE", "Type in the type of vehicle as per registered. \nTypes: car, truck, motorcycle, golfcart, tire"); // Put some serious options and some real funny ones (I love driving around with a singular registered, legally licensed tire!)
        helpMessages.put("BRAND", "Type in the brand of your vehicle. \nBrands: Toyota, Hyundai, DeLorean, Hoglovíc, Canary, Congsia, Land'ola, Annableu"); // TODO: options based on what type of vehicle. This will require IntelliCents first. Also, add a way to submit a new brand name if needed.
        helpMessages.put("MODEL", "Type in the model of your vehicle. \nModels: Camry (2010), Corolla (2012), Highlander (2011), Tundra (2011), Tacoma (2016), Sienna (2011)"); // TODO: IntelliCents. Also perhaps a search system (e.g. by year, by alpha, etc). Add refs to past endeavors (e.g. Lando, Congsian) through ads perhaps!
        helpMessages.put("LICENSE", "Type in your license plate id -- formatted as 'ABC123'.\n\nTip: several formats are acceptable (e.g. 'ABC 123', 'ABC-123', 'ABC_123') but you may not fragment either half (e.g. 'AB C123')."); // For now only accept 'ABC123' format.
        helpMessages.put("SIGNATURE", "Digitally sign to confirm your details. You will be prompted to review what you filled out. \nYou may sign in any way you'd like.");
    }

    public void printReceipt()
    {
        System.out.println(ANSI_YELLOW + "\n==================================================\n");

        System.out.println("  \uD835\uDE4F\uD835\uDE44\uD835\uDE48\uD835\uDE40\uD835\uDE4E\uD835\uDE4F\uD835\uDE3C\uD835\uDE48\uD835\uDE4B: " + /*fancify(String.valueOf(*/LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", ", ") + ", " + TimeZone.getDefault().getID().replace("_", " ") + " time"/*), "New Roman")*/); // TODO: print UTC offset (e.g. UTC-4?)
        System.out.println("\n\t\tVehicle type - " + vehicleType);
        System.out.println("\t\tBrand - " + brand);
        System.out.println("\t\tModel - " + model);
        System.out.println("\n\t\tLicense plate - " + licensePlate);
        System.out.println("\t\tLicense key - " + licenseKey);
        System.out.println("\n\t\tSigned, " + signature);

        System.out.println("\n==================================================\n" + ANSI_RESET);
    }
}

// ======================================================================================================
//
//  Realistic ideas:
//
// - MySQL database setup
//     o IntelliCents
//     o Database interactions
//
// - deepPrompt()
// - piggyBank integration (for future, have it link to actual PB proj but for now do a simplified in-house version.)
// - Vehicle picking minigame
//     o Randomized rounds for inspection
//     o Post-game feedback (e.g. how much value you gained)
//
// - License plate registration service
//     o Check if license already exists
//     o Console graphics?
//
// x Generate random garbage data (for debugging)
//
// ======================================================================================================







// Ideas:
// - Maybe license plate generator? (state, special stamps, etc.)
// - A physical key ring? (PS1 / N64-like graphics and spinning around? For some fun graphics shenanigans)
// - GUI controls? Maybe controlled either a modern mouse-click program or a GBA-like "controller-input" program. If latter, maybe inspired from M3?
// - Theme it around the 1990s? 'Real Deal Rentals' does exudes that "radical, cool" flavor, y'know. And number one 1997 salesman.
// - Secret racing game? It is a car rental, after all. Either a lil' console-based game or it's 'hidden' behind the car rental center if using graphics. Make it really surprising and comedically childish (e.g. make the rental center real drab and racetrack like it was designed by a 5yo).
// - Vehicle showcase -- show off all the real (and made-up) vehicle models and cars. Maybe make it a game where you have to determine whether or not to buy a car (do I smell piggy bank-integration??) by inspecting parts of the car. You'd have to manually and actively keep track of things or else you might miss that crater in the backseat!
// - Extra options? Perhaps rental times, pricing based on car quality?
