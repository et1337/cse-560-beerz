Test18   .ORIG
Begin    ADD     R0, R1, #1
Test     .FILL   #-1         ; Compile to xFFFF
         .END    x0