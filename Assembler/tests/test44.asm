Test44   .ORIG
         .EXT    Symbol
Begin    ADD     R0, R1, Symbol ; Should generate an I record
Test     .FILL    Symbol    ; Should generate another I record
         .END