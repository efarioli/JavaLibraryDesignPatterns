package ooae_library.keyboard_io;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author gdm1
 */
public class KeyboardInputter
{
    private static final Scanner KYBD = new Scanner(System.in);

    public static double getDouble(String prompt)
    {
        boolean dataOK = false;
        double num = 0;

        do
        {
            System.out.print(prompt + ": > ");
            try
            {
                num = KYBD.nextDouble();
                dataOK = true;
            }
            catch (InputMismatchException ime)
            {
                System.out.println("Please enter a decimal point value");
            }
        }
        while (!dataOK);
        KYBD.nextLine(); //flush the buffer

        return num;
    }

    public static int getInteger(String prompt)
    {
        boolean dataOK = false;
        int num = 0;

        do
        {
            System.out.print(prompt + ": > ");
            try
            {
                num = KYBD.nextInt();
                dataOK = true;
            }
            catch (InputMismatchException ime)
            {
                System.out.println("Please enter an integer");
            }
        }
        while (!dataOK);
        KYBD.nextLine(); //flush the buffer

        return num;
    }

    public static String getString(String prompt)
    {
        System.out.print(prompt + ": > ");
        return KYBD.nextLine();
    }
    
}
