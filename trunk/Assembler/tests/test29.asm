Test29   .ORIG
Test     .EQU    x3
Begin    LD      R0, =x0FFFFFFF ; Literal value out of bounds
         .END    Begin