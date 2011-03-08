Test43   .ORIG
         .EXT    Symbol    ; Should generate an I record
Begin    ADD     R0, R1, Symbol
Test     .EQU    Symbol    ; Should generate an error
         .END