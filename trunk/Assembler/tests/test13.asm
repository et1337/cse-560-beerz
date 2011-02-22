Test13   .ORIG
Begin    ADD     R0, R1, #1
Test     .EQU    Test         ; Should give an error due to aliasing.
         .END    x0