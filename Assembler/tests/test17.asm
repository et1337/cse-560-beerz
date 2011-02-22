Test17   .ORIG
Begin    ADD     R0, R1, #1
Test     .FILL   x2
Test     .FILL   x2         ; Should give an error for symbol redefinition
         .END    x0