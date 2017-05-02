/** Virtual Stack Machine
 *  Created by: Christopher Smith
 *  Skeleton created by: Dr. McGuire, Sam Houston State University
 *  Date: 4/28/2017
 */

package com.company;
import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException {
        StackMachine vm = new StackMachine();

        try {vm.readFile(args[0]); } catch (Exception e) {System.out.println("No argument provided.");}

        System.out.println("Beginning Execution...");
        vm.execute();
        System.out.println("Done");
    }
}