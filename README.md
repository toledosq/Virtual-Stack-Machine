# Virtual-Stack-Machine
A Stack Machine emulator written in Java


 Created by: Christopher Smith
 Skeleton created by: Dr. McGuire, Sam Houston State University
 Date: 4/28/2017

This program is a Stack Machine Emulator that accepts a .bin file as a Main argument. 

This machine contains a .code and .data segment. As of initial release, the data segment must be hardcoded or initialized.
All instructions are 32-bit integers.

Each instruction is read as follows:

Bits 32-22 are ignored.
Bits 21-16 hold the opcode.
Bits 15-0 hold the operand. (If there are no operands, these bits are set to 0)

Opcodes include:

HALT = 0
PUSH = 1
RVALUE = 2
LVALUE = 3
POP = 4
STO = 5
COPY = 6
ADD = 7
SUB = 8
MPY = 9
DIV = 10
MOD = 11
NEG = 12
NOT = 13
OR = 14
AND = 15
EQ = 16
NE = 17
GT = 18
GE = 19
LT = 20
LE = 21
LABEL = 22
GOTO = 23
GOFALSE = 24
GOTRUE = 25
PRINT = 26
READ = 27
GOSUB = 28
RET = 29
ORB = 30
ANDB = 31
XORB = 32
SHL = 33
SHR = 34
SAR = 35
