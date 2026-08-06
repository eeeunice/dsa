package utility;

public class Header {
    // 1. Moved inside the class block
<<<<<<< HEAD
=======
    // 2. Changed from 'private' to 'public' so other packages can use them
>>>>>>> c8370ef42b7c08cce802b72853b49e442959cfee
    public static final String RESET      = "\u001B[0m";
    public static final String DARK_BLUE  = "\u001B[34m";   
    public static final String PURPLE     = "\u001B[35m"; 
    public static final String YELLOW     = "\u001B[33m";
    public static final String RED        = "\u001B[31m";
    public static final String GREEN      = "\u001B[32m";

<<<<<<< HEAD
=======
    // 3. Changed from 'private' to 'public'
>>>>>>> c8370ef42b7c08cce802b72853b49e442959cfee
    public static void printHeader() {
        System.out.println();
        System.out.println(DARK_BLUE + "  +-------------------------------------+" + RESET);
        System.out.println(DARK_BLUE + "  |        HOTEL MANAGEMENT SYSTEM      |" + RESET);
        System.out.println(DARK_BLUE + "  +-------------------------------------+" + RESET);
    }
}