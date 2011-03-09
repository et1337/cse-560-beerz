Test45   .ORIG
         .EXT    Symbol
         .ENT    Begin
Begin    ADD     R0, R1, Symbol ; Should generate an I record
         LD      R0, Begin
Test     .FILL    Symbol    ; Should generate another I record
         .END