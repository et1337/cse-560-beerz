;Test program for WI11 suite
Test0    .ORIG
         .EXT    GENER
STORA1   .BLKW   x1
STORA2   .BLKW   x1
COUNT    .FILL   x2
OUTN     .EQU    x31
HALT     .EQU    x25
BEGIN    JSR     GENER
         ST      R0,STORA1
         JSR     GENER
         ST      R0,STORA2
         LD      R1,STORA1
         LD      R2,STORA2
         ADD     R0,R1,R2
         TRAP    OUTN
         TRAP    HALT
         .END