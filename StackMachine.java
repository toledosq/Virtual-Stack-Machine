/** Virtual Stack Machine
 *  Created by: Christopher Smith
 *  Skeleton created by: Dr. McGuire, Sam Houston State University
 *  Date: 4/28/2017
 */


package com.company;

// Imports
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.Scanner;


// Stack machine object
public class StackMachine {

    final int MAX_CODE = 65536;         // size of code memory
    final int MAX_DATA = 65536;         // size of data memory



    final int HALT = 0;                 // Define opcodes
    final int PUSH = 1;
    final int RVALUE = 2;
    final int LVALUE = 3;
    final int POP = 4;
    final int STO = 5;
    final int COPY = 6;
    final int ADD = 7;
    final int SUB = 8;
    final int MPY = 9;
    final int DIV = 10;
    final int MOD = 11;
    final int NEG = 12;
    final int NOT = 13;
    final int OR = 14;
    final int AND = 15;
    final int EQ = 16;
    final int NE = 17;
    final int GT = 18;
    final int GE = 19;
    final int LT = 20;
    final int LE = 21;
    final int LABEL = 22;
    final int GOTO = 23;
    final int GOFALSE = 24;
    final int GOTRUE = 25;
    final int PRINT = 26;
    final int READ = 27;
    final int GOSUB = 28;
    final int RET = 29;
    final int ORB = 30;
    final int ANDB = 31;
    final int XORB = 32;
    final int SHL = 33;
    final int SHR = 34;
    final int SAR = 35;



    int[] code;                                             // reference for the memory modules
    int[] data;
    int PC;                                                 // Program Counter (holds current instruction address)
    int IR;                                                 // Instruction Reader (holds instruction at current PC)
    boolean run;                                            // Boolean for starting/stopping VM
    Stack<Integer> stack;                                   // data stack
    Stack<Integer> callStack;                               // call stack



    public StackMachine() {
        code = new int[MAX_CODE];                           // constructor initializes the stack machine
        data = new int[MAX_DATA];                           // Set mem sizes to max value (32 bits)
        PC = 0;
        run = true;
        stack = new Stack<Integer>();
        callStack = new Stack<Integer>();
        data[0] = 6;                                        // Initialize a few values in segment .data to manipulate
        data[1] = 2;
        data[2] = 14;
        data[3] = 12;
        data[4] = 8;
        data[5] = 4;
    }

    public void readFile(String file) throws IOException {
        boolean endOfFile = false;     // EOF flag
        int counter = 0;
        int number; // A number read from the file

        // Create the binary file input objects.
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream inputFile = new DataInputStream(fstream);

        while (!endOfFile)
        {
            try {
                // Retrieve binary number from file
                number = inputFile.readInt();
                code[counter] = number;
                counter++;
            }
            catch (EOFException e)
            {
                endOfFile = true;
            }
        }

        // Close the file.
        inputFile.close();
        return;
    }


    public void execute() {
        while (run)                             // the fetch-execute cycle
        {
            getNextInstruction();               // Fetch next instruction
            decodeAndRunInstruction();          // Execute instruction
        }
    }


    public void getNextInstruction() {
        IR = code[PC++];
    }   // Send contents at current PC to IR, increment PC


    public void decodeAndRunInstruction() {
        int opcode = IR >> 16;                  // pull out the opcode and the operands
        int operand = IR & 0xFFFF;
        int op1;                                // Placeholder registers
        int op2;



        switch (opcode) {
            case HALT:
                run = false;
                break;

            case PUSH:                          // push a literal value
                stack.push(operand);
                break;

            case RVALUE:                        // push the contents of a memory address
                stack.push(data[operand]);
                break;

            case LVALUE:                        // push the address (which, come to think of it, is exactly the same as pushing a literal)
                stack.push(operand);
                break;

            case POP:
                stack.pop();                    // Throw away the value on top of the stack
                break;

            case STO:                           // Place rvalue on top of stack in lvalue below it, pop both
                int rvalue = stack.pop();
                int lvalue = stack.pop();
                data[lvalue] = rvalue;
                break;

            case COPY:                          // Push a copy of the top value on the stack ***MAY NEED TO BE CHANGED***
                op1 = stack.peek();
                stack.push(op1);
                break;

            case ADD:                           // pop top 2 values off stack, add, push result
                op1 = stack.pop();
                op2 = stack.pop();
                op1 += op2;
                stack.push(op1);
                break;

            case SUB:                           // pop top 2 values off stack, sub, push result
                op1 = stack.pop();
                op2 = stack.pop();
                op1 -= op2;
                stack.push(op1);
                break;

            case MPY:                           // pop top 2 values off stack, multiply, push result
                op1 = stack.pop();
                op2 = stack.pop();
                op1 *= op2;
                stack.push(op1);
                break;

            case DIV:                           // pop top 2 values off stack, divide, push result
                op1 = stack.pop();
                op2 = stack.pop();
                op1 /= op2;
                stack.push(op1);
                break;

            case MOD:                           // pop top 2 values off of stack, calculate the modulus, push the result
                op1 = stack.pop();
                op2 = stack.pop();
                op1 %= op2;
                stack.push(op1);
                break;

            case NEG:                           // pop top value off of stack, negate, push result
                op1 = stack.pop();
                op1 = 0 - op1;
                stack.push(op1);
                break;

            case NOT:                           // pop the top value off of the stack, invert all the bits, push result
                op1 = stack.pop();
                op1 = ~op1;
                stack.push(op1);
                break;

            case OR:                            // pop the top 2 values off of the stack, compute logical OR, push result
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 != 0 || op2 != 0)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case AND:                           // pop the top 2 values off of the stack, compute logical AND, push result
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 != 0 && op2 != 0)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case EQ:                            // pop top 2 values off stack, compare, return 1 if equal, 0 if not equal
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 == op2)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case NE:                            // pop top 2 values off stack, compare, return 1 if not equal, 0 if equal
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 != op2)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case GT:                            // pop top 2 values off stack, compare, if first value is greater than second, return 1. Else return 0
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 > op2)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case GE:                            // pop top 2 values off stack, compare, return 1 if op1 >= op2
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 >= op2)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case LT:                            // pop top 2 values off stack, compare, return 1 if op1 < op2
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 < op2)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case LE:                            // pop top 2 values off stack, compare, return 1 if op1 <= op2
                op1 = stack.pop();
                op2 = stack.pop();
                if (op1 <= op2)
                    stack.push(1);
                else
                    stack.push(0);
                break;

            case LABEL:                         // Turns out due to nature of machine, this appears unnecessary
                break;                          // I'll leave it in anyway

            case GOTO:                          // Update PC to jump location (label (pop from stack?))
                PC = operand;
                break;

            case GOFALSE:
                op1 = stack.pop();
                if (op1 == 0) {PC = operand;}   // Update PC to jump location
                break;

            case GOTRUE:
                op1 = stack.pop();
                if (op1 != 0) {PC = operand;}   // Update PC to jump location
                break;

            case PRINT:                         // pop value off top of stack and print to terminal
                op1 = stack.pop();
                System.out.println(op1);
                stack.push(op1);
                break;

            case READ:                          // Read an integer from the keyboard and push onto stack
                Scanner keyboard = new Scanner(System.in);
                int input = keyboard.nextInt();
                stack.push(input);
                break;

            case GOSUB:                         // push PC+1 onto call stack, jump to subroutine address in operand
                callStack.push(PC + 1);
                PC = operand;
                break;

            case RET:                           // pop top address off of call stack, assign to PC
                PC = callStack.pop();
                break;

            case ORB:                           // Compute bitwise OR and push result
                op1 = stack.pop();
                op2 = stack.pop();
                stack.push(op1 | op2);
                break;

            case ANDB:                          // Compute bitwise AND and push result
                op1 = stack.pop();
                op2 = stack.pop();
                stack.push(op1 & op2);
                break;

            case XORB:                          // Compute bitwise XOR and push result
                op1 = stack.pop();
                op2 = stack.pop();
                stack.push(op1 ^ op2);
                break;

            case SHL:                           // shift arithmetic/logical left and push result
                op1 = stack.pop();
                op1 = op1 << 1;
                stack.push(op1);
                break;

            case SHR:                           // shift logical right and push result
                op1 = stack.pop();
                op1 = op1 >>> 1;
                stack.push(op1);
                break;

            case SAR:                           // shift arithmetic right and push result
                op1 = stack.pop();
                op1 = op1 >> 1;
                stack.push(op1);
                break;

            default:                            // oops!
                System.err.println("Unimplemented opcode");
                System.exit(opcode);


        }
        if (IR == 0) run = false;               // fail safe for HALT. Terminates program
    }
}