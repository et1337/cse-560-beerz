Test9    .ORIG
Symbol   .EQU    x3
Begin    ADD     R0, R1, Symbol ; Should add the immediate value x3, not register 3 (should compile to 0x1063)
         .END    x0