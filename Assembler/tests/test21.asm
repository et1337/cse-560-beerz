Test21   .ORIG
         .FILL   x0
         .FILL   x0
Test     .BLKW   #-2
         ADD     R3, R1, #1      ; Should not overwrite the first two .FILL instructions
         .END    x0