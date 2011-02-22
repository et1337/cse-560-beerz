Test7    .ORIG
Begin    ADD    R0, R1, #1 ; Should give an error for having improper spacing between operation and operands
        ADD     R0, R2, #2 ; Should give an error for having improper spacing between label and operation
         .END    x0