Test7    .ORIG
Symbol   .EQU    x3, #10     ; Should give an error for incorrect number of operands
Begin    ADD     R0, R1, Symbol
         .END    x0