Test36   .ORIG
Begin    ADD     R0, R1, Symbol
         .FILL   Symbol    ; Forward referencing .FILL
Symbol   .EQU    x3
         .END