Test24   .ORIG
Begin    ADD     R0, R1, #1
Test     .EQU    Bear         ; Should give an error due to the undefined symbol.
         .END    x0