package com.pocketrice;

import java.util.*;

import static com.pocketrice.AnsiCode.*;

public class Assortment {
    private Comparable[] arr;
    private List<Comparable[]> passHistory;
    private int passCount;
    private int subpassCount;
    private SortType currentSort;

    public enum SortType {
        NONE(-1),
        INSERTION(0),
        SELECTION(1),
        MERGE(2),
        BUBBLE(3);

        SortType(int i) {
            index = i;
        }

        public int index() {
            return index;
        }

        @Override
        public String toString() {
            return this.name().charAt(0) + this.name().toLowerCase().substring(1);
        }

        private int index;
    }

    public Assortment(Comparable[] arr) {
        this.arr = arr;

        passHistory = new ArrayList<>();
        passCount = 0;
        currentSort = SortType.NONE;
    }

    public Assortment() {
        Random rng = new Random();
        int dataLength = (int) (proximizedRandom(7, 3, 5));
        do {
            arr = Arrays.stream(new Integer[dataLength]).map(i -> rng.nextInt(100) - 50).toList().toArray(new Integer[0]);
        } while (!validateArr(arr));

        passHistory = new ArrayList<>();
        passCount = 0;
        subpassCount = 0;
        currentSort = SortType.NONE;
    }



    // Determine if the array is suitable for a sort (size >= 2, not fully sorted, not all same item, etc.) Use this to warn the user
    public static boolean validateArr(Comparable[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) { // Implicitly ensures size ≥ 2.
                return Arrays.stream(arr).distinct().count() != 1; // If not all sorted, do final "same item" check.
            }
        }
        return false; // If all sorted, no need for sort.
    }

    public void setSort(SortType sort) {
        currentSort = sort;
    }

    public void setArr(Comparable[] arr) {
        this.arr = arr;
    }

    public SortType getSort() {
        return currentSort;
    }

    public Comparable[] getArr() { return arr; }

    public void sort() {
        System.out.print(ANSI_YELLOW + "***  Current sort: " + currentSort.toString().toUpperCase() + "  ***");
        switch (currentSort) {
            case INSERTION -> insertionSort();
            case SELECTION -> selectionSort();
            case MERGE -> mergeSort();
            case BUBBLE -> bubbleSort();
        }

        System.out.println(ANSI_BLUE + "✔ Done." + ANSI_RESET);
    }


    // each pass consists of a particular action.
    // 1) SWAP: swaps two items - two items changed; 123 -> 321 (original order fragmented)
    // 2) INSERT: puts an item b/w two items - length-1 changed; 123 -> 312 (original w/out affected intact)

    // * This can normally be checked by just checking # of changed items (2 = swap, >2 = insert) except for length == 2... which can just be considered a swap. Ah.
    // * There is also the case where an adjacent insert occurs... which is effectively a swap. Well, this simplifies things!
    // ** Basically, these fringe cases can be ignored and just lumped into the general categories of swap and insert.

    // Merge sort could consist of a combine action (assume pass #0 starts at the "single-item array" state).
    // The merge action is also unique (e.g. something like -> <- to represent a merge, as it's assumed a merge = sorts in ascending order).




    // ***** Ignore everything mentioned above. Turns out, selection/bubble is only swap, insertion is only insert. Lovely. *****



    public void startPass() {
        passCount++;
        subpassCount = 0;
        System.out.println(ANSI_CYAN + "\n\t\t\t\t\t「  Pass " + passCount + "  」");
        System.out.println("========================================================================\n" + ANSI_RESET);
    }

    // i = affected indices (SEL, BUBBLE = swapped indices, INS = original/new indices).
    public void printPass(int n1, int n2) {
        if (Arrays.equals(arr, passHistory.get(passHistory.size()-1))) {
            System.out.println(((subpassCount != 0) ? "(" + subpassCount + "): " : " ".repeat(5)) + "[" + Arrays.stream(arr).map(s -> ((s == arr[n1] || s == arr[n2]) ? String.valueOf(ANSI_BLUE) : "") + s + ((arr[arr.length-1].equals(s)) ? "" : ", ") + ANSI_RESET).reduce("", (str, item) -> str + item) + "]\n\n");
            return;
        }

        String arrState = "";
        String[] swapState = new String[]{"", ""};

        //             "┌-----------┐"
        //             "↓           ↓"
        //           1, 6, 3, 4, 5, 2

        //        vs

        //             "┌-------┐"
        //             "↓       |"
        //           1, 4, 2, 3, 5, 6
        //                    *-^

        //        or
        //            "┌---┐"
        //            "|   ↓"
        //           1, 3, 2, 4, 5, 6
        //             ^*

        // Swap = involved indices.
        // Insert = FROM (behind END) ? oIndex + 2 : oIndex - 1. END = new index.

        switch (currentSort) {
            case SELECTION, BUBBLE -> {
                for (int i = 0; i < arr.length; i++) {
                    if (i == n1 || i == n2) {
                        arrState += ANSI_PURPLE;
                    }
                    arrState += arr[i] + ((i != arr.length - 1) ? ", " : "") + ANSI_RESET;

                    int addlSpaceSize = arr[i].toString().length() - 1; // Additional spaces for n > 1 digit #s (none "repeat" bits assume n=1 digits).
                    swapState[0] += ((i == n1) ? "┌——" + "—".repeat(addlSpaceSize) : ((i == n2) ? "┐" : (i > n1 && i < n2) ? "———" + "—".repeat(addlSpaceSize): "   " + " ".repeat(addlSpaceSize)));
                    swapState[1] += ((i == n1 || i == n2) ? "↓  " + " ".repeat(addlSpaceSize): "   " + " ".repeat(addlSpaceSize));
                }
            }

            case INSERTION -> {
                // Insertion can result in:
                // 12345 -> 51234 [_, (i+1)*4)] = extremity swap
                // 12345 -> 12534 [(i)*2, _, (i+1)*2] = n>1 group split
                // 12345 -> 15234 [i, _, (i+1)*3] = edge split (n=1, n>1).

                // 12345 -> 14235 [i, _, i, (i+1)*2]...

                // Not as easy as it looks to find what # was inserted. Hence, bypass this and instead just receiving the sort's vals themselves.
                for (int i = 0; i < arr.length; i++) {
                    if (i == n2) arrState += ANSI_PURPLE;
                    arrState += arr[i] + ((i != arr.length - 1) ? ", " : "") + ANSI_RESET;

                    /*boolean isForward = (n2 > n1); // is END in front of FROM? (END > FROM)

                    // isForward = [n1, n2]; n1 char shifted back 1 space.
                    // !isForward = [n2, n1]; n1 char shifted forward 2 spaces.
                    if (isForward)
                        swapState[0] += (i > n2 || i < n1) ? "   " : (i == n2) ? "——┐" : ((i == n1) ? "\b┌——" : "———");
                    else
                        swapState[0] += (i < n2 || i > n1) ? "   " : (i == n2) ? "┌——" : ((i == n1) ? "——┐" : "———");

                    swapState[1] += ((i == n2) ? "↓" : (i == n1) ? ((isForward) ? "\b|  " : "  |") : " ") + "  ";*/




                    // ** Here's a fun fact!! Insertion sort will NEVER insert forwards, which means 70% of the code/thinking/time spent above was useless. Yay!!
                    int addlSpaceSize = arr[i].toString().length() - 1; // Additional spaces for n > 1 digit #s (none "repeat" bits assume n=1 digits).
                    swapState[0] += (i < n2 || i > n1) ? "   " + " ".repeat(addlSpaceSize) : (i == n2) ? "┌——" + "—".repeat(addlSpaceSize): ((i == n1) ? "—".repeat(addlSpaceSize) + "——┐" : "———" + "—".repeat(addlSpaceSize));
                    swapState[1] += ((i == n2) ? "↓" + " ".repeat(addlSpaceSize) : (i == n1) ?  " ".repeat(addlSpaceSize) + " |" + " ".repeat(addlSpaceSize) : " ".repeat(addlSpaceSize)) + "   ";
                }
            }

            default -> throw new IllegalCallerException();
        }
        System.out.println(" ".repeat(6) + swapState[0] + "\n" + " ".repeat(6) + swapState[1]); // "Pass #" (6) + passCount (1) + ": [" (3) = 10
        System.out.println(((subpassCount != 0) ? "(" + subpassCount + "): " : " ".repeat(5)) + "[" + arrState + "]\n\n");
    }

    // Only preliminary passes should call this.
    public void printPass() {
        switch (currentSort) {
            case SELECTION, BUBBLE, INSERTION, MERGE ->
                System.out.println(((subpassCount != 0) ? "(" + subpassCount + "): " : " ".repeat(5)) + "[" + Arrays.stream(arr).map(s -> s + ((arr[arr.length-1].equals(s)) ? "" : ", ")).reduce("", (str, item) -> str + item) + "]" + ANSI_RESET);

            default -> throw new IllegalCallerException();
        }

        System.out.println("\n");
    }

    // Only MERGE should call this -- special case as the original arr is only assigned at end of recursion, so printPass must be tailored to fit that recursion.
    public void printPass(Comparable[]... arrs) {
        switch (currentSort) {
            case MERGE -> {
                System.out.print(((subpassCount != 0) ? "(" + subpassCount + "): " : " ".repeat(5)));
                for (Comparable[] arr : arrs) {
                     System.out.print("[" + Arrays.stream(arr).map(s -> s + ((arr[arr.length - 1].equals(s)) ? "" : ", ")).reduce("", (str, item) -> str + item) + "]");
                     if (!Arrays.equals(arrs[arrs.length - 1], arr)) System.out.print(", ");
                }
            }

            default -> throw new IllegalCallerException();
        }
    }


    public void reset() {
        arr = passHistory.get(0); // Assume first item = original arr.
        passHistory.clear();
        passCount = 0;
        subpassCount = 0;
    }

    public void insertionSort() {
        passHistory.add(arr.clone());
        printPass();

        // Compare item to (i-1) item until either i = 0 or not less than item
        for (int i = 1; i < arr.length; i++) {
            startPass();
            Comparable temp = arr[i]; // Store value (arr's item at index i will be changed).
            int candIndex = i; // potential index to move item

            while (candIndex > 0 && temp.compareTo(arr[i-1]) < 0) { // Index is valid AND item is smaller than prev item
                arr[candIndex] = arr[candIndex - 1]; // Move prev item up one space (our selected item has been "swapped")
                candIndex--;
            }

            arr[candIndex] = temp; // Item is in right spot, so set it in arr. Note that the item at this decremented spot is "duplicated" to the next spot, so we can safely replace this spot.

            printPass(i, candIndex);
            passHistory.add(arr.clone());
        }

        reset();
    }
    public void selectionSort() {
        passHistory.add(arr.clone());
        printPass();

        for (int i = 0; i < arr.length-1; i++) {
            startPass();
            int minIndex = i;

            // Iterate through all "unlocked" items (the item itself, and any "locked-in" items -- which are behind index i).
            for (int j = i + 1; j < arr.length; j++)
                if (arr[j].compareTo(arr[minIndex]) < 0) minIndex = j; // Find unlocked item with smallest value.

            // Swap 'em!
            Comparable temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;

            printPass(i, minIndex);
            passHistory.add(arr.clone());
        }

        reset();
    }
    public void mergeSort() {
        passHistory.add(arr.clone());
        printPass();
        mergeSortHelper(arr, arr.length); // For consistency, mergeSort() accepts just the array; low and high setting is handled by the actual algorithm method (for recursion purposes).
        System.out.println(Arrays.toString(arr));
        reset();
    }

    public void mergeSortHelper(Comparable[] a, int n) {
        if (n < 2) { // arr.length == 1; means already sorted. Also a recursion end condition
            return;
        }

        int mid = n / 2;

        // Temp arrays (subarrays)
        Comparable[] l = new Comparable[mid];
        Comparable[] r = new Comparable[n - mid];

        // Copy data to temp arrays
        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        // Merge sort on split arr (low-mid, mid-high). The end condition means merge sort is called for every split until arr.length 1
        mergeSortHelper(l, mid);
        mergeSortHelper(r, n - mid);

        startPass();
        printPass(a);

        // Merge two arrays in ascending order.
        merge(a, l, r, mid, n - mid);
    }


    public void merge(Comparable[] a, Comparable[] l, Comparable[] r, int left, int right) {
        // Initial indices of 1st subarray, 2nd subarray, merged array
        int i = 0, j = 0, k = 0;

        // Begin comparing pairs and inserting smallest val; e.g. L[0] v. R[0], L[1] v. R[0]...
        while (i < left && j < right) {
            if (l[i].compareTo(r[j]) <= 0) {
                a[k++] = l[i++];
            }
            else {
                a[k++] = r[j++];
            }
        }

        // Copy remaining elements of L[] if any
        while (i < left) {
            a[k++] = l[i++];
        }

        // Copy remaining elements of R[] if any
        while (j < right) {
            a[k++] = r[j++];
        }
    }

    public void bubbleSort() {
        passHistory.add(arr.clone());
        printPass();

        for (int i = 0; i < arr.length - 1; i++) { // For all items (except end)...
            startPass();
            for (int j = 0; j < arr.length - 1; j++) { // Compare pairs and swap big front of small accordingly.
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    Comparable temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
                subpassCount++;
                printPass(j, j+1);
                passHistory.add(arr.clone());
            }
        }

        reset();
    }

    public static double proximizedRandom(double base, double lowOffset, double highOffset) { // <+> APM
        return Math.random()*(lowOffset + highOffset) + base - lowOffset;
    }

    public static String[] toStrCharArray(String word) {
        // chars kinda suck (why can't I stream char[] and turn it into a string[]?? why??) so being lazy again >:)
        String[] arr = new String[word.length()];

        for (int i = 0; i < word.length(); i++) {
            arr[i] = word.substring(i, i+1);
        }

        return arr;
    }
}