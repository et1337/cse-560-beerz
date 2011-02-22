Test15   .ORIG
Begin    ADD     R0, R1, #1
Test     .STRZ   R2         ; Should give an error for incorrect operand type
Test2    .STRZ   x3         ; Should give an error for incorrect operand type
         .END    x0