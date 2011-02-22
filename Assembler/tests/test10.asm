Test7    .ORIG   R0              ; Should give an error
Symbol   .EQU    x3
Begin    ADD     R0, R1, Symbol
         .END    x0