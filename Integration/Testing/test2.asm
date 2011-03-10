; Sample program
Test2    .ORIG   x30B0
count    .FILL   #4
PUTS     .EQU    x22
ANS      .BLKW   x1
QUEST    .STRZ   "Which company introduced the first laptop computer in 1981?"
ANS1     .STRZ   "a) Hewlett Packard"
ANS2     .STRZ   "b) Dell"
ANS3     .STRZ   "c) Epson"
ANS4     .STRZ   "d) Compaq"
INCO     .STRZ   "Incorrect, the correct answer is:"
CORRA    .STRZ   "That's Correct!"
Begin    LEA     R0,QUEST
         TRAP    PUTS
         LEA     R0,ANS1
         TRAP    PUTS
         LEA     R0,ANS2
         TRAP    PUTS
         LEA     R0,ANS3
         TRAP    PUTS
         LEA     R0,ANS4
         TRAP    PUTS
         TRAP    x23
         LD      R1,CORR
         ADD     R2,R1,R0
         NOT     R2,R2
         BRZ     ESUL
         LEA     R0,INCO
         TRAP    PUTS
         LEA     R0,ANS3
         TRAP    PUTS
EXIT     TRAP    HALT
ESUL     LEA     R0,CORRA
         TRAP    PUTS
         BRNZP   EXIT
; ----- Scratch Space -----
Array    .BLKW   #3
CORR     .FILL   xFF9C
MASK     .FILL   xFFFF
         .END    Begin
HALT     .EQU    x25