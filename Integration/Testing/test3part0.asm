;Test program for WI11 suite
Test0    .ORIG
         .EXT    GENER
STORA1   .BLKW   x1
STORA2   .BLKW   x1
COUNT    .FILL   x2
OUTN     .EQU    x31
HALT     .EQU    x25
BEGIN    JSR     GENER
         ADD     R1,R0,x0
         JSR     GENER
         ADD     R0,R1,R0
         TRAP    OUTN
         TRAP    HALT
         .END