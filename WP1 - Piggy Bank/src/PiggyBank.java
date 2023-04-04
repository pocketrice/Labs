import java.util.*;
import java.util.regex.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.objecthunter.exp4j.*; // exp4j lib dependency
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Boolean.parseBoolean;

public class PiggyBank {
static final Map<String, Double> coinValues = new HashMap<>();
static Map<String,PiggyBank> piggyBanks = new HashMap<>();
static Map<String, String> helpMessages = new HashMap<>();
static Map<String, Double> stockValues = new HashMap<>();
Map<String, Integer> coinTypes = new LinkedHashMap<>();


//static Map<String, Integer[]> bankDatabase = new HashMap<>(); // Integer array = {pennies, nickels, dimes, quarters, dollars}.

private int pennies, nickels, dimes, quarters, dollars;
private double balance;
static double pocket = BigDecimal.valueOf(Math.random() * 19 + 1).setScale(2, RoundingMode.HALF_DOWN).doubleValue(); // Random pocket change between $1.00 to $20.00
static String selectedBank;
static boolean continueFlag = true;
static Scanner input = new Scanner(System.in);




    // PUSHED TO LATER:
    // - Break the piggy bank (secret; if you break the bank you gain a piece of bacon but lose all your money) [CUT]
    // - Multicommand support
    // - Interest/tax/stock simulator (draw in the stocks system from SF again; have a few default stocks that change every minute?)
    // - Cryptographically secure banks (a login system; look into java's SHA encryptions and stuff?) [CUT]
    // - A visual console? (e.g. NPB background, maybe buttons for options or additional UI stuff instead of just "type your crap out!" -- KEEP IT SIMPLE)
        // * To add to this, maybe some sort of screen where you are on the streets outside the bank, and there are realistic env (e.g. vehicles moving around, doppler whirrr, moving sky). Click on door to enter bank.)
        // * When you enter the bank you hear generic bustling sounds and a bell. Click the bell and the environment blurs and the welcome screen comes in (logo, login, and info button. Looks like an OS login panel). An "intercom" system inspired by AA using dialogs.
        // * After logging in it goes to the NPB bg and the banking begins. Use a console-like input probably -- too lazy to make actual buttons (yet).


// ANSI colors
public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_YELLOW = "\u001B[33m";
public static final String ANSI_BLUE = "\u001B[34m";
public static final String ANSI_PURPLE = "\u001B[35m";
public static final String ANSI_CYAN = "\u001B[36m";


    public PiggyBank() // Constructor (empty)
    {
        pennies = 0;
        nickels = 0;
        dimes = 0;
        quarters = 0;
        dollars = 0;
        balance = 0;
    }

    public PiggyBank(int p, int n, int d1, int q, int d2) // Constructor (isolated params)
    {
        pennies = p;
        nickels = n;
        dimes = d1;
        quarters = q;
        dollars = d2;
        balance = pennies * 0.01 + nickels * 0.05 + dimes * 0.1 + quarters * 0.25 + dollars;
    }

    public PiggyBank(int[] coinage) { // Constructor (array params)
        pennies = coinage[0];
        nickels = coinage[1];
        dimes = coinage[2];
        quarters = coinage[3];
        dollars = coinage[4];
        balance = pennies * 0.01 + nickels * 0.05 + dimes * 0.1 + quarters * 0.25 + dollars;
    }

    public void query(boolean isAllCoins)
    {
        while (continueFlag) {
            int[] coinage;
            if (isAllCoins) {
                coinage = new int[]{pennies, nickels, dimes, quarters, dollars}; // NOTE: balance is not used right now. This will come to use when it needs to be passed along rather than rn just having values outputted.
                System.out.printf(ANSI_CYAN + "You have " + pennies + " pennies, " + nickels + " nickels, " + dimes + " dimes, " + quarters + " quarters, and " + dollars + " dollars deposited, or a total of $%.2f.\n\n" + ANSI_RESET, coinsToBal(coinage)); // Replace when GUI is implemented (e.g. pass values along instead)
            } else {
                String coinageType = prompt("Which coinage?", "Error: invalid input.", new String[]{"pennies", "nickels", "dimes", "quarters", "dollars"}, "QUERY_SPECIFIC");
                coinage = new int[]{coinTypes.get(coinageType)};
                System.out.println(ANSI_CYAN + "You have" + Arrays.toString(coinage).replace("[", " ").replace("]", " ") + coinageType + " deposited." + ANSI_RESET); // Replace when GUI is implemented
            }

            break;
        }
        // Cancel-safe: there are no parameters that need to be "made safe".
    }

    public static void helpMode(String context) // TODO: find a way to pass in rf variables (really just rfAmount and rfType).
    {
        int rfAmount = 0; // REPLACE SOON
        String rfType = "pennies"; // REPLACE SOON
        String parsedMessage = helpMessages.get(context);

        if (parsedMessage.contains("{")) // The help message contains a "live variable" (one that needs to be updated continuously)
        {
            List<String> rsrfCointypes = new ArrayList<>(), rsrfRtCointypes = new ArrayList<>();
            for (String cointype : piggyBanks.get(selectedBank).coinTypes.keySet())
            {
                if (piggyBanks.get(selectedBank).coinTypes.get(cointype) != 0)
                {
                    rsrfCointypes.add(cointype);
                }

                if (rfAmount * coinValues.get(rfType) >= coinValues.get(cointype)) // If the $ value can be converted to at least one of 'cointype'...
                {
                    rsrfRtCointypes.add(cointype);
                }
            }



            parsedMessage = parsedMessage.replace("{BANK_KEYSET}", Arrays.toString(piggyBanks.keySet().toArray()))
                    .replace("{POCKET}", String.format("%.2f", pocket))
                    .replace("{BALANCE}", String.format("%.2f", piggyBanks.get(selectedBank).balance))
                    .replace("{RSRF_COINTYPES}", rsrfCointypes.toString())
                    .replace("{RSRF_RT_COINTYPES}", rsrfRtCointypes.toString())
                    .replace("{RFTYPE}", /*Double.toString(*/rfType/*)*/) // Pass in rfType (FIX)
                    .replace("{RFAMOUNT}", Integer.toString(rfAmount)); // Pass in rfTypeAmount (FIX)
        }

        System.out.println(ANSI_BLUE + "\n*************************************************************************************************\n");
        System.out.println(parsedMessage);
        System.out.println("\n*************************************************************************************************\n" + ANSI_RESET);
    }

    public static void populateHelpMessages()
    {
        helpMessages.put("INTRO", "Choose an existing bank or if it does not exist, start the bank creation process.\nExisting banks: {BANK_KEYSET} \nPermitted input: any string \n\nSome general tips: \n\t- All selections are done by typing the choice and hitting 'return'.\n\t- You can always see more information about an action using the 'help' keyword, no matter what kind of prompt.");
        helpMessages.put("INITBANK_PREFILL", "Type in the amount of money to prefill the new bank with.\nThis amount is automatically upchanged to the highest possible coin types.\n\nPermitted input: any monetary amount (0 ≤ input < ∞) \n\nTip: $0.00 is also valid.");
        helpMessages.put("ACTION", "Type in a keyword to execute that action on this piggy bank. \nKeywords: deposit, withdraw, query, reformat, pocket, transfer, switch, stocks, logout \n\nDeposit: Put money from your pocket into your piggy bank.\nWithdraw: Take money out of your piggy bank and into your pocket.\nQuery: Check how much of one coin or all coins you have in your bank.\nReformat: Automatically convert coinage in your bank to highest possible coin types or convert a specific coin type to another type.\nPocket: Check how much money you have in your pocket.\nTransfer: Send an amount from one piggy bank to another.\nSwitch: Log-in to a different piggy bank or create a new bank to do operations on.\nStocks: Directly invest in and track the stock market.\nLogout: Exit the session/program.\n\nTip: you may use multicommands (e.g. chaining several actions together in one return line).");
        helpMessages.put("DEPOSIT", "Type in the amount of money to deposit from your pocket into your piggy bank. \nYou currently have ${POCKET} in your pocket.\n\nPermitted input: any amount within your pocket (0 ≤ input < pocket's balance) \n\nTip: you can check your pocket and bank balances using the 'pocket' and 'query' keywords respectively.");
        helpMessages.put("WITHDRAW", "Type in the amount of money to withdraw from your piggy bank into your pocket. \nYou currently have ${BALANCE} in your bank. \n\nPermitted input: any amount within your bank (0 ≤ input ≤ bank's balance) \n\nTip: you can check your pocket and bank balances using the 'pocket' and 'query' keywords respectively.");
        helpMessages.put("QUERY", "Choose either to check how much money you have in general or of how much of a specific coin type. \nKeywords: all, specific");
        helpMessages.put("QUERY_SPECIFIC", "Choose which coin type to query. \nKeywords: pennies, nickels, dimes, quarters, dollars \n\nTip: want to check how much money you have in total? Use the keyword 'query all'.");
        helpMessages.put("REFORMAT", "Choose either to do upchange all coinage (generic) or to convert some amount of a specific coin type to another type (specific). \nKeywords: generic, specific");
        helpMessages.put("REFORMAT_SPECIFIC_RFTYPE", "Choose which coin type to convert from. \nKeywords: pennies, nickels, dimes, quarters, dollars.\nAvailable cointypes: {RSRF_COINTYPES} \n\nTip: this operation will not convert all of that coin type -- you will be able to specify an amount in the next step.");
        helpMessages.put("REFORMAT_SPECIFIC_RFAMOUNT", "Choose how many of that coin type to convert to another type. \nPermitted input: any valid number of coins (0 ≤ input ≤ # of coins of cointype; INT ONLY!)\n# of {RFTYPE}: {RFAMOUNT} \n\nTip: This is the # of coins, not the $ value of how much you want to convert!"); // WIP: need to implement the "active info" lines (e.g. "Available choices: pennies, nickels") based on previous inputs.
        helpMessages.put("REFORMAT_SPECIFIC_RFRESULTTYPE", "Choose which coin type to convert that amount of the initial cointype to. \nKeywords: pennies, nickels, dimes, quarters, dollars.\nAvailable cointypes: {RSRF_RT_COINTYPES} \n\nTip: any unconvertable amount is automatically redeposited back into your bank (e.g. 3 dimes -> 1 quarter leaves $0.05, which is redeposited as 1 nickel).\nYou will not lose any remainder amount!");
        helpMessages.put("TRANSFER", "Choose which bank to transfer money from your piggy bank to. \nExisting banks: {BANK_KEYSET}\nPermitted input: any existing bank\n\nTip: You cannot create a new bank in 'transfer'. Instead, use 'switch' and type a new bank's name to create a new piggy bank.");
        helpMessages.put("TRANSFER_AMOUNT", "Type in how much you want to transfer from your piggy bank to the specified bank. \nYou currently have ${BALANCE} in your bank. \n\nPermitted input: any amount within your bank (0 ≤ input ≤ bank's balance).");
        helpMessages.put("SWITCH", "Choose which existing bank to switch to, or if it does not exist, start the bank creation process. \nExisting banks: {BANK_KEYSET}\nPermitted input: any string. \n\nTip: if you are trying to transfer money between banks, use 'transfer' instead.");
        helpMessages.put("STOCKS", "Help message coming soon!");
    }


    // ============================================================================================================================================
    //                      Hey, you! THIS FUNCTION IS REALLY USEFUL AND SHOULD BE YOINKED FOR FUTURE PROJECTS!!
    // ============================================================================================================================================

    // TODO: implement string formatting from bufferedPrompt()
    public static String determineError(String errorBehavior, double[] variables, String variablesRegex) // DESCRIPTION: Proof of concept, maybe this can come in handy later for more complex prompts that need lots of conditions/error messages. This should follow a prompt-set variable (e.g. double rfRemainder = prompt(...); determineError(...);)
    {
        // Decompress the error pairs (error key, error condition) from the single-string errorBehavior.
        String[] errorPairs = errorBehavior.split("\\|");


        for (String errorPair : errorPairs) {
            // EXAMPLE FORMAT: "Error: exceeds limit of {limit-1} or {limit2}@limit1,limit2!$input * anotherInput [>] coinTypes.get(rfType)@input,anotherInput|Error: odd number$input % 2 [!=] 1@input"

            String errorKey = errorPair.split("\\$")[0]; // The errorPair is split into an array: [errorKey, errorCondition] and we assign those two parts.
            String parsedErrorKey = errorKey; // Set PEK to EK because we'll iterate and modify it to eventually parse it.
            int numOfErrorKeyExpr = errorKey.length() - errorKey.replace("{", "").length(); // Yoinked from https://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string!
            Expression[] errorKeyExpr = new Expression[numOfErrorKeyExpr];

            if (parsedErrorKey.contains("@"))
            {
                parsedErrorKey = parsedErrorKey.substring(0,errorKey.indexOf("@"));
            }


            String[] errorKeyExprVars = errorKey.substring(errorKey.indexOf("@")+1).split(","); // Get variable names from the tail-end of errorKey.
            Map <String, Double> keyVarPairs = new TreeMap<>(); // Pair up variable names and actual variable values.
            for (int j = 0; j < errorKeyExprVars.length; j++)
            {
                if (errorKeyExprVars[j].matches(variablesRegex)) {
                    keyVarPairs.put(errorKeyExprVars[j], variables[j]);
                }
            }

            for (int i = 0; i < numOfErrorKeyExpr; i++) // Iterate and parse each expression ({}).
            {
                Pattern curlyPattern = Pattern.compile("\\{.*?}"); // Note: not using errorKey.match(PATTERN) b/c Java forces it to check for only when the entire String MATCHES, not if it merely CONTAINS it.
                Matcher curlyMatcher = curlyPattern.matcher(errorKey);

                if (curlyMatcher.find()) // In case the errorKey does not contain any expressions ({}).
                {
                    errorKeyExpr[i] = new ExpressionBuilder(parsedErrorKey.substring(parsedErrorKey.indexOf("{")+1,parsedErrorKey.indexOf("}"))).variables(errorKeyExprVars).build().setVariables(keyVarPairs);
                    parsedErrorKey = parsedErrorKey.replaceFirst("\\{.*?}", ""+errorKeyExpr[i].evaluate());
                }
            }



            String errorCondition = errorPair.substring(errorPair.indexOf("$")+1,errorPair.lastIndexOf("@")); // Note: the variables snippet of the errorPair is trimmed off.
            String[] variableNames = errorPair.substring(errorPair.lastIndexOf("@")+1).split(","); // Variable names are extracted from the errorBehavior string (necessary for exp4j).

            Map<String, Double> variablePairs = new TreeMap<>(); // Pairs the extracted variable names with the actual variables themselves.


            for (int j = 0; j < variableNames.length; j++) { // Populate variablePairs with specifically only the vars that exist in both variableNames and actual variables array.
                if (variableNames[j].matches(variablesRegex))
                {
                    variablePairs.put(variableNames[j], variables[j]);
                }
            }


            // A condition is defined as leftExpr [evalOperator] rightExpr, where the two sides of the "equation" are explicitly denoted with the "[]" in-between.
            Expression leftExpr = new ExpressionBuilder(errorCondition.split("\\[.*]")[0]).variables(variableNames).build().setVariables(variablePairs); // So, we can split the equation and build the expressions using exp4j.
            Expression rightExpr = new ExpressionBuilder(errorCondition.split("\\[.*]")[1]).variables(variableNames).build().setVariables(variablePairs); // Note: exp4j is necessary to actually evaluate those expressions. Surprisingly, Java can't really convert String to actual stuff to evaluate.

            String evalOperator = errorCondition.substring(errorCondition.indexOf("[")+1, errorCondition.indexOf("]")); // Get the operator between the two "sides of the equation".


            if (evalOperator.matches("==")) {
                if (leftExpr.evaluate() == rightExpr.evaluate()) {
                    return parsedErrorKey;
                }
            } else if (evalOperator.matches("!=")) {
                if (leftExpr.evaluate() != rightExpr.evaluate()) {
                    return parsedErrorKey;
                }
            } else if (evalOperator.matches(">")) // < and <= are redundant b/c you can just switch expr order.
            {
                if (leftExpr.evaluate() > rightExpr.evaluate()) {
                    return parsedErrorKey;
                }
            } else if (evalOperator.matches(">=")) {
                if (leftExpr.evaluate() >= rightExpr.evaluate()) {
                    return parsedErrorKey;
                }
            }
        }
        return "NO_ERROR"; // If we loop through all errorPairs and no errors occur, then hooray! No error!
    }

    public String prompt(String message, String errorMessage, String[] keywords, String context) // All-purpose, does-it-all prompter.
    {
        boolean helpFlag;
        continueFlag = true;

        while (true)
        {
            helpFlag = false;
            if (!(message.matches("NO_MESSAGE")))
            {
                System.out.println(message);
            }

            /* TODO: return to multicommands later.
            String nextInput; // Optimize this code later

            int multicommandCount = 0;
            String fullInput = input.nextLine();
            input.nextLine();
            if (fullInput.matches("\\s"))
            {
                String[] splitInput = fullInput.split("\\s+");
                nextInput = splitInput[0];
                multicommandCount = splitInput.length - 1;
                System.out.println(splitInput[0]);
            }
            else
            {
                nextInput = fullInput;
            }*/

            String nextInput = input.next();

            if (nextInput.matches("help") && !context.matches("NO_CONTEXT"))
            {
                helpMode(context);
                helpFlag = true;
            }

            //List<String> keywordsList = Arrays.asList(keywords); // NOTE: Convert to list to allow for checking String[] for a keyword

            if (!helpFlag) {
                if (nextInput.matches("cancel"))// NOTE: Failsafe in case 'cancel' is a part of keywords (which should not happen b/c it shouldn't even be used as a keyword.)
                {
                    System.out.println(ANSI_RED + "Action cancelled.\n" + ANSI_RESET);
                    continueFlag = false;
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

    public void initializeBank(String bankName)
    {
        while (continueFlag) {
            if (prompt("Do you want to prefill this piggy bank with coinage?", "Error: invalid choice.", new String[]{"yes", "no"}, "NO_CONTEXT").matches("yes")) { // NOTE: Below is some mantissa manipulation (basically just get it to two decimal places).
                double prefillBalance = BigDecimal.valueOf(prompt("How much money?", "Error: invalid amount.", 0, Double.POSITIVE_INFINITY, false, "INITBANK_PREFILL")).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                piggyBanks.put(bankName, new PiggyBank(balToCoins(prefillBalance)));
            } else {
                piggyBanks.put(bankName, new PiggyBank(0, 0, 0, 0, 0));
            }
            System.out.println(ANSI_GREEN + "Piggy bank of name " + selectedBank + " has successfully been created!\n" + ANSI_RESET);
            break;
        }
    }

    public double prompt(String message, String errorMessage, double min, double max, boolean isIntegerMode, String context)
    {
        String nextInput;
        double parsedInput = 0;
        boolean isValid;

        while (true) {
            if (!(message.matches("NO_MESSAGE"))) {
                System.out.println(message);
            }

            nextInput = input.next();

            if (nextInput.matches("help") && !context.matches("NO_CONTEXT"))
            {
                helpMode(context);
            }
            else {
                try {

                    if (!isIntegerMode) {
                        parsedInput = parseDouble(nextInput);
                    }
                    else {
                        parsedInput = parseInt(nextInput);
                    }

                    input.nextLine();
                    isValid = true;
                }

                catch (Exception e) {
                    isValid = false;
                }

                if (parsedInput > min && parsedInput < max && isValid) {
                    return parsedInput;
                }
                else {
                    System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
                }
            }
        }
    }
    public String prompt(String message, String context) // General, very "loose" prompter; use for manually setting how to process values.
    {
        String nextInput;
        while (true)
        {
            System.out.println(message);
            nextInput = input.next();

            if (nextInput.matches("help") && !context.matches("NO_CONTEXT"))
            {
                helpMode(context);
            }

            else if (nextInput.matches("cancel"))
            {
                return "NOT_IMPLEMENTED"; // FIX THIS
            }

            else {
                break;
            }
        }

        return nextInput;
        /*
        String output = input.next(); // FOR USE WITH MULTICOMMANDS
        input.nextLine();
        return output;*/
    }


    public String bufferedPrompt(String message, String errorMessage, String conditionKey, String datatype, String context) // Use bufferedPrompt when an error message requires the next input. This isn't replacing prompt b/c prompt is a little more readable in terms of function params. // TODO: help and cancel
    {
        boolean helpFlag;
        continueFlag = true;


        while (true) {
            helpFlag = false;
            if (!(message.matches("NO_MESSAGE"))) {
                System.out.println(message);
            }


            String nextInput = input.next();


            if (nextInput.matches("help") && !context.matches("NO_CONTEXT")) {
                helpMode(context);
                helpFlag = true;
            }

            if (!helpFlag) {
                if (datatype.matches("double")) // Parameters: double min, double max, boolean isIntegerMode
                {
                    double min = parseDouble(conditionKey.split(",")[0]);
                    double max = parseDouble(conditionKey.split(",")[1]);
                    boolean isIntegerMode = parseBoolean(conditionKey.split(",")[2]);

                    String parsedErrorMessage = errorMessage;
                    int numOfExpr = errorMessage.length() - errorMessage.replace("{", "").length(); // Yoinked from https://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string!
                    Expression[] expressions = new Expression[numOfExpr];

                    if ((parseDouble(nextInput) > min && parseDouble(nextInput) < max && isIntegerMode) || ((int)parseDouble(nextInput) > min && (int)parseDouble(nextInput) < max && !isIntegerMode)) {
                        return nextInput;
                    } else {
                        for (int i = 0; i < numOfExpr; i++) // Iterate and parse each expression ({}).
                        {
                            String exprFormat = "%s"; // %s is no formatting.

                            if (errorMessage.contains("::")) // Special char sequence that should ONLY be used to denote exprFormat. It would be best if some regex could be used to check for :: only in {}.
                            {
                                parsedErrorMessage = parsedErrorMessage.replaceAll("::(.*?)(?=})", "");
                                exprFormat = errorMessage.substring(errorMessage.indexOf("::") + 2, errorMessage.indexOf("}")); // How the expression's output should be formatted.
                            }

                            Pattern curlyPattern = Pattern.compile("\\{.*?}");
                            Matcher curlyMatcher = curlyPattern.matcher(errorMessage);

                            if (curlyMatcher.find()) // In case the errorKey does not contain any expressions ({}).
                            {
                                expressions[i] = new ExpressionBuilder(parsedErrorMessage.substring(parsedErrorMessage.indexOf("{") + 1, parsedErrorMessage.indexOf("}"))).variable("BUFFERED_INPUT").build().setVariable("BUFFERED_INPUT", parseDouble(nextInput));
                                parsedErrorMessage = parsedErrorMessage.replaceFirst("\\{.*?}", String.format(exprFormat, expressions[i].evaluate()));
                            }
                        }

                        System.out.println(ANSI_RED + parsedErrorMessage + ANSI_RESET);
                    }
                }

                if (datatype.matches("String|string")) // Parameters: String[] keywords
                {
                    String[] keywords = conditionKey.split(",");

                    if (nextInput.matches(String.join("|", keywords)) || keywords[0].matches("")) {
                        return nextInput;
                    } else {
                        System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
                    }
                }
            }
        }
    }

    public int[] balToCoins(double amount)
    {
        int[] coinage = {pennies, nickels, dimes, quarters, dollars};

        coinage[4] = (int)amount;
        amount -= coinage[4];

        coinage[3] = (int)(amount / 0.25);
        amount -= coinage[3] * 0.25;

        coinage[2] = (int)(amount / 0.1);
        amount -= coinage[2] * 0.1;

        coinage[1] = (int)(amount / 0.05);
        amount -= coinage[1] * 0.05;

        coinage[0] = (int)(amount / 0.01);

        return coinage;
    }

    public double coinsToBal(int[] coinage)
    {
        return (coinage[4] + coinage[3] * 0.25 + coinage[2] * 0.1 + coinage[1] * 0.05 + coinage[0] * 0.01);
    }

    public String parseArrayToString(int[] array, String[] names)
    {
        StringBuilder parsedString = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++)
        {
            parsedString.append(array[i]).append(" ").append(names[i]).append(", ");
        }

        parsedString.append("and ").append(array[array.length - 1]).append(" ").append(names[array.length - 1]); // Final element of array is a special case

        return parsedString.toString();
    }

    public void deposit(double amount)
    {
        pocket -= amount;
        balance += amount;

        int netDollars = (int) (amount / coinValues.get("dollars"));
        amount -= netDollars * coinValues.get("dollars");
        dollars += netDollars;

        int netQuarters = (int) (amount / coinValues.get("quarters"));
        amount -= netQuarters * coinValues.get("quarters");
        quarters += netQuarters;

        int netDimes = (int) (amount / coinValues.get("dimes"));
        amount -= netDimes * coinValues.get("dimes");
        dimes += netDimes;

        int netNickels = (int) (amount / coinValues.get("nickels"));
        amount -= netNickels * coinValues.get("nickels");
        nickels += netNickels;

        int netPennies = (int) (amount / coinValues.get("pennies"));
        pennies += netPennies;
    }

    public void withdraw(double amount)
    {
        pocket += amount;
        balance -= amount;

        int netDollars = (int)(amount / coinValues.get("dollars"));
        amount -= netDollars * coinValues.get("dollars");
        dollars -= netDollars;

        int netQuarters = (int)(amount / coinValues.get("quarters"));
        amount -= netQuarters * coinValues.get("quarters");
        quarters -= netQuarters;

        int netDimes = (int)(amount / coinValues.get("dimes"));
        amount -= netDimes * coinValues.get("dimes");
        dimes -= netDimes;

        int netNickels = (int)(amount / coinValues.get("nickels"));
        amount -= netNickels * coinValues.get("nickels");
        nickels -= netNickels;

        pennies = (int)(amount / coinValues.get("pennies")); // NOTE: amount variable is no longer necessary because this is the last step.
    }

    public void action(String selection)
    {
        if (selection.matches("deposit|withdraw|query|reformat|pocket|switch|transfer|stocks|logout"))
        {
            if (selection.matches("deposit"))
            { // CHANGE NAME OF BUFFEREDPROMPT
                double depositVal = BigDecimal.valueOf(parseDouble(bufferedPrompt("How much?", "Error: you do not have enough pocket change to deposit ${BUFFERED_INPUT::%.2f}. You need ${BUFFERED_INPUT-" +pocket+ "::%.2f} more. Deposit cancelled.", "0,"+pocket+",false", "double", "DEPOSIT"))).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                deposit(depositVal);
                updateCoinTypes(false);
                System.out.printf(ANSI_CYAN + "Successfully deposited $%.2f into your piggy bank.\n\n" + ANSI_RESET, depositVal);
            }

            if (selection.matches("query"))
            {
                while (continueFlag)
                {
                if (prompt("All coinage or a specific coin type?", "Error: invalid input.", new String[]{"all","specific"}, "QUERY").matches("all"))
                {
                    query(true);
                }
                else
                {
                    query(false);
                }

                break;
                }
            }

            if (selection.matches("withdraw"))
            {
                double withdrawVal = BigDecimal.valueOf(parseDouble(bufferedPrompt("How much?", "Error: you do not have enough money to withdraw ${BUFFERED_INPUT::%.2f}. You need ${BUFFERED_INPUT-" +balance+ "::%.2f} more. Withdrawal cancelled.", "0,"+balance+",false", "double", "WITHDRAW"))).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
                withdraw(withdrawVal);
                updateCoinTypes(false);
                System.out.printf(ANSI_CYAN + "Successfully withdrawn $%.2f from your piggy bank into your pocket.\n\n" + ANSI_RESET, withdrawVal);
            }

            if (selection.matches("reformat"))
            {
                String conversionType = prompt("Would you like to do a generic reformatting or make a specific reformat?", "Error: invalid option.", new String[]{"generic", "specific"}, "REFORMAT");

                    if (conversionType.matches("generic"))
                    {
                        dollars = balToCoins(balance)[4];
                        quarters = balToCoins(balance)[3];
                        dimes = balToCoins(balance)[2];
                        nickels = balToCoins(balance)[1];
                        pennies = balToCoins(balance)[0];
                        updateCoinTypes(false);

                        System.out.println(ANSI_CYAN + "Successfully reformatted your piggy bank.\n" + ANSI_RESET);
                    }

                    if (conversionType.matches("specific")) {

                        String rfType = prompt("Which coin type do you want to reformat?", "Error: invalid coin type.", new String[]{"pennies", "nickels", "dimes", "quarters", "dollars"}, "REFORMAT_SPECIFIC_RFTYPE");
                        if (coinTypes.get(rfType) == 0)
                        {
                            System.out.println(ANSI_RED + "Error: you don't have any coins of that type.\n" + ANSI_RESET);
                            return;
                        }

                        int rfAmount = (int)prompt("And how many " + rfType + " would you like to convert?", "Error: invalid amount.", 0, Double.POSITIVE_INFINITY, true, "REFORMAT_SPECIFIC_RFAMOUNT");
                        String rfResultType = prompt(rfAmount + " " + rfType + " to which coin type?", "Error: invalid coin type.", new String[]{"pennies", "nickels", "dimes", "quarters", "dollars"}, "REFORMAT_SPECIFIC_RFRESULTTYPE");


                        if (!(rfAmount * coinValues.get(rfType) >= coinValues.get(rfResultType)))
                        {
                            System.out.println(ANSI_RED + "Error: you do not have enough " + rfType + " to convert to " + rfResultType + ".\n" + ANSI_RESET);
                            return;
                        }


                        int rfConvertedCount = (int)((rfAmount * coinValues.get(rfType)) / coinValues.get(rfResultType));
                        double rfRemainder = BigDecimal.valueOf(((rfAmount * coinValues.get(rfType) * 100) % (coinValues.get(rfResultType) * 100))/100).setScale(2, RoundingMode.HALF_DOWN).doubleValue();

                        // ============ NEW TESTING CODE ============
                        System.out.println(ANSI_PURPLE + "=======================================================================================\n");
                        System.out.println(ANSI_PURPLE + "DO NOT PANIC, MYSELF! This is testing code for determineError(), an advanced error-determination method I made (line 498)!");
                        System.out.println(ANSI_PURPLE + determineError("Error: remainder is 0!$rfRemainder[==]0@rfRemainder|Error: rfAmount > remainder! Also, rfAmount++ = {rfAmount + 1} and rfRemainder++ = {rfRemainder + 1}.@rfAmount,rfRemainder$rfAmount-3[>]rfRemainder-3@rfRemainder,rfAmount", new double[]{rfRemainder,rfAmount}, "rfRemainder|rfAmount") + ANSI_RESET);
                        System.out.println(ANSI_PURPLE + "END OF TEST CODE.\n");
                        System.out.println(ANSI_PURPLE + "=======================================================================================\n");
                        // ============= END TESTING CODE ===========


                        int rfConvertingCount = (rfAmount - (int)(rfRemainder / coinValues.get(rfType)));


                        coinTypes.replace(rfResultType, coinTypes.get(rfResultType) + rfConvertedCount);
                        coinTypes.replace(rfType, coinTypes.get(rfType) - rfAmount);
                        updateCoinTypes(true);

                        for (int i = 0; i < coinTypes.size(); i++)
                        {
                            String[] coinNames = {"pennies", "nickels", "dimes", "quarters", "dollars"};
                            coinTypes.replace(coinNames[i], coinTypes.get(coinNames[i]) + balToCoins(rfRemainder)[coinTypes.size() - 1]);
                        }

                        if ((double)Math.round(rfRemainder * 100) / 100 == 0)
                        {
                            System.out.println(ANSI_GREEN + "Successfully converted " + rfConvertingCount + " " + rfType + " to " + rfConvertedCount + " " + rfResultType + ".\n" + ANSI_RESET);
                        }
                        else
                        {
                            System.out.println(ANSI_GREEN + "Successfully converted " + rfConvertingCount + " " + rfType + " to " + rfConvertedCount + " " + rfResultType + ". Remaining $" + rfRemainder + " has been automatically redeposited.\n" + ANSI_RESET);
                        }
                    }

            }

            if (selection.matches("pocket"))
            {
                System.out.printf(ANSI_GREEN + "You currently have $%.2f in your pocket, or " + parseArrayToString(balToCoins(pocket), new String[]{"pennies", "nickels", "dimes", "quarters", "dollars"}) + ".\n\n" + ANSI_RESET, pocket);
            }

            if (selection.matches("switch"))
            {
                String potentialBank = prompt("Which piggy bank do you want to switch operations to?", "Error: Invalid piggy bank.", new String[]{""}, "SWITCH");

                if (!potentialBank.matches(piggyBanks.keySet().toString().replace("[", "").replace("]","").replace(",", "|").replace(" ", ""))) // Omega unoptimized!! But it works, so ¯\_(ツ)_/¯
                {
                    if (prompt("That piggy bank does not exist yet. Do you want to create it?", "Error: invalid choice.", new String[]{"yes", "no"}, "NO_CONTEXT").matches("yes"))
                    {
                        initializeBank(potentialBank);
                        selectedBank = potentialBank;
                        //updateCoinTypes(true); // FIX
                    }
                    else
                    {
                        System.out.println(ANSI_RED + "Bank creation cancelled." + ANSI_RESET);
                    }
                }
                else
                {
                    selectedBank = potentialBank;
                    //updateCoinTypes(true); // Fix all the updatecointypes because of stupid code
                    System.out.println(ANSI_BLUE + "Successfully switched to piggy bank of name " + selectedBank + ".\n" + ANSI_RESET);
                }
            }

            if (selection.matches("transfer")) // VERIFIED WORKING: switch is being wonky so it's not exactly showing up, but via debug this DOES work.
            {
                String targetBank = prompt("Which bank do you want to transfer to?", "Error: Invalid piggy bank.", piggyBanks.keySet().toArray(new String[0]), "TRANSFER"); // TODO: make sure you cannot transfer to your own bank.
                double transferAmount = BigDecimal.valueOf(prompt("How much do you want to transfer?", "Error: invalid amount.", 0.0, balance, false, "TRANSFER_AMOUNT")).setScale(2, RoundingMode.HALF_DOWN).doubleValue();

                this.withdraw(transferAmount);
                piggyBanks.get(targetBank).deposit(transferAmount);
                piggyBanks.get(targetBank).updateCoinTypes(false);

                System.out.printf(ANSI_GREEN + "Successfully transferred $%.2f from your piggy bank to the " + targetBank + " bank.\n\n" + ANSI_RESET, transferAmount);
            }

            if (selection.matches("stocks"))
            {
                System.out.println(ANSI_PURPLE + "Stocks coming soon!" + ANSI_RESET);
            }

            if (selection.matches("logout"))
            {
                System.out.println("\n=========================================================================================");
                System.out.println(ANSI_BLUE + "Successfully logged out. Session ended. Thanks for doing business with the National Pork Bank!" + ANSI_RESET);
                System.exit(0);
            }
        }
        else
        {
            System.out.println(ANSI_RED + "Error: invalid input. Valid keywords are deposit, withdraw, query, reformat, pocket, transfer, switch, and logout.\n" + ANSI_RESET);
        }
    }


    public void updateCoinTypes(boolean isMapToVars)
    {

        if (isMapToVars) {
            pennies = coinTypes.get("pennies");
            nickels = coinTypes.get("nickels");
            dimes = coinTypes.get("dimes");
            quarters = coinTypes.get("quarters");
            dollars = coinTypes.get("dollars");
        }
        else
        {
            coinTypes.replace("pennies", pennies);
            coinTypes.replace("nickels", nickels);
            coinTypes.replace("dimes", dimes);
            coinTypes.replace("quarters", quarters);
            coinTypes.replace("dollars", dollars);
        }
    }



    public static void main(String[] args) // To-do: make sure it's possible to prompt from other objs too (e.g. use a variable to set the obj for things like porky.prompt).
    {
        boolean isFirstTime = true;
        coinValues.put("pennies", 0.01);
        coinValues.put("nickels", 0.05);
        coinValues.put("dimes", 0.1);
        coinValues.put("quarters", 0.25);
        coinValues.put("dollars", 1d);

        piggyBanks.put("porky", new PiggyBank(1,13,13,24,5));
        piggyBanks.put("pokey", new PiggyBank(1,20,32,1,51));

        populateHelpMessages();

        System.out.println(ANSI_YELLOW + "Welcome to National Pork Bank, where money hogging is never a crime! Type 'help' to learn more about what you can do. When you're all done, use the 'logout' keyword." + ANSI_RESET);
        selectedBank = piggyBanks.get("porky").prompt(ANSI_YELLOW + "To begin your session, please type the name of the piggy bank you'd like to access. Alternatively, you may create a new piggy bank." + ANSI_RESET, "INTRO");

        if (!selectedBank.matches(piggyBanks.keySet().toString().replace("[", "").replace("]","").replace(",", "|").replace(" ", "")))
        {
            if (piggyBanks.get("porky").prompt(ANSI_CYAN + "The piggy bank of name " + selectedBank + " does not exist yet. Do you want to create it?" + ANSI_RESET, "Error: invalid choice.", new String[]{"yes", "no"}, "NO_CONTEXT").matches("yes"))
            {
                piggyBanks.get("porky").initializeBank(selectedBank);
            }
            else
            {
                System.out.println(ANSI_RED + "Piggy bank creation cancelled." + ANSI_RESET); // TODO: redirect to "Begin your session" prompt again. Since I have no ideas for how to do so it'll just exit(0). Note: this case also handles "no" AND any invalid input. Fix prompt() so it'll spit an error and reask the prompt again if invalid input.
                System.exit(0);
            }
        }

        System.out.println(ANSI_BLUE + "Successfully accessed the piggy bank of name " + selectedBank + "." + ANSI_RESET);

        piggyBanks.get(selectedBank).coinTypes.put("pennies", piggyBanks.get(selectedBank).pennies);
        piggyBanks.get(selectedBank).coinTypes.put("nickels", piggyBanks.get(selectedBank).nickels);
        piggyBanks.get(selectedBank).coinTypes.put("dimes", piggyBanks.get(selectedBank).dimes);
        piggyBanks.get(selectedBank).coinTypes.put("quarters", piggyBanks.get(selectedBank).quarters);
        piggyBanks.get(selectedBank).coinTypes.put("dollars", piggyBanks.get(selectedBank).dollars);



        while (true)
        {
            //updateCoinTypes(false); // Look into this more; is this redundant? Better implementation?

            String promptMessage = "What would you like to do next?";

            if (isFirstTime) {
                promptMessage = "What would you like to do with " + selectedBank + "?";
                isFirstTime = false;
            }

            String nextAction = piggyBanks.get("porky").prompt(promptMessage, "ACTION");
          piggyBanks.get(selectedBank).action(nextAction);
        }
    }
}
