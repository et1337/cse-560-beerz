Test14   .ORIG
Begin    ADD     R0, R1, #1
Test     .FILL   R2         ; Should give an error for incorrect operand type
         .END    x0