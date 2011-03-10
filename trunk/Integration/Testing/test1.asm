;Test program for WI11 suite (relocatable)
Test1    .ORIG
HALT     .EQU    x25
EG0      .EQU    x0
SPACE    .BLKW   x1
COUNT    .FILL   x5
BEGIN    TRAP    x43
         ST      EG0,SPACE
         AND     R1,R1,x10
         NOT     R2,R1
         LD      R3,SPACE
         ADD     R4,R2,R3
         LD      R5,COUNT
         ADD     R5,R5,#-1
         ST      R5,COUNT
         BRNP    BEGIN       ;If CCRs are not sent to Zero, will branch back to BEGIN
         ADD     EG0,R4,x0
         TRAP    x31
         TRAP    HALT
         .END