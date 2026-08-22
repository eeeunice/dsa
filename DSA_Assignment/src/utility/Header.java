package utility;
//Author : LIM CHUN CHUAN

public class Header {
    // 1. Moved inside the class block

    // 2. Changed from 'private' to 'public' so other packages can use them

    public static final String RESET      = "\u001B[0m";
    public static final String DARK_BLUE  = "\u001B[34m";   
    public static final String PURPLE     = "\u001B[35m"; 
    public static final String YELLOW     = "\u001B[33m";
    public static final String RED        = "\u001B[31m";
    public static final String GREEN      = "\u001B[32m";


    // 3. Changed from 'private' to 'public'

    public static void printHeader() {
        System.out.println();
        System.out.println(DARK_BLUE + "  +-------------------------------------+" + RESET);
        System.out.println(DARK_BLUE + "  |        HOTEL MANAGEMENT SYSTEM      |" + RESET);
        System.out.println(DARK_BLUE + "  +-------------------------------------+" + RESET);
    }
}