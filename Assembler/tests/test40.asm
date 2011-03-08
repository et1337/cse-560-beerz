Test40   .ORIG
         .ENT    Symbol, Begin    ; Should generate an EA record and an ER record
Begin    ADD     R0, R1, Symbol
         .FILL   Symbol    ; Forward referencing .FILL
Symbol   .EQU    x3
         .END