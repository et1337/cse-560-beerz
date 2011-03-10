; Sample program
Test3    .ORIG   x30B0
INN      .EQU    x33
OUTN     .EQU    x31
PUTS     .EQU    x22
PROMPT   .STRZ   "Enter 'a' for add, 's' for subtract, or 'm' for mulitply"
PRMPT1   .STRZ   "Enter the first number to be added:"
PRMPT2   .STRZ   "Enter the number to be subtracted from"
PRMPT3   .STRZ   "Enter the first number to multiply:"
PRMPT4   .STRZ   "Enter the second number to be added:"
PRMPT5   .STRZ   "Enter the number to subtact:"
PRMPT6   .STRZ   "Enter second number to multiply"
Begin    LEA     R0,PROMPT
         TRAP    PUTS
         TRAP    x23
         LD      R1,CHARA
         ADD     R2,R1,R0
         NOT     R2,R2
         BRZ     OPADD
         LD      R1,CHARS
         ADD     R2,R1,R0
         NOT     R2,R2
         BRZ     OPSUB
         LD      R1,CHARM
         ADD     R2,R1,R0
         NOT     R2,R2
         BRZ     OPMUL
EXIT     TRAP    HALT
OPADD    LEA     R0,PRMPT1
         TRAP    PUTS
         TRAP    INN
         ST      R0,TEMP
         LEA     R0,PRMPT4
         TRAP    PUTS
         TRAP    INN
         LD      R1,TEMP
         ADD     R0,R0,R1
         TRAP    OUTN
         BRNZP   EXIT
OPSUB    LEA     R0,PRMPT2
         TRAP    PUTS
         TRAP    INN
         ST      R0,TEMP
         LEA     R0,PRMPT5
         TRAP    PUTS
         TRAP    INN
         LD      R1,TEMP
         NOT     R0,R0
         ADD     R0,R0,R1
         ADD     R0,R0,#1
         TRAP    OUTN
         BRNZP   EXIT
OPMUL    LEA     R0,PRMPT3
         TRAP    PUTS
         TRAP    INN
         ST      R0,TEMP
         LEA     R0,PRMPT6
         TRAP    PUTS
         TRAP    INN
         ST      R0,TEMP2
         LD      R2,TEMP2
         LD      R1,TEMP
         ADD     R1,R1,#-1
MUL      ADD     R0,R0,R2
         ADD     R1,R1,#-1
         BRNP    MUL 
         TRAP    OUTN        
         BRNZP   EXIT
; ----- Scratch Space -----
TEMP     .BLKW   #1
TEMP2    .BLKW   #1
CHARA    .FILL   xFF9E
CHARS    .FILL   xFF8C
CHARM    .FILL   xFF92
         .END    Begin
HALT     .EQU    x25