Test     .EQU    x3
Test33   .ORIG             ; .ORIG is not first instruction. Should give an error
Begin    LD      R0, =x1
         .END    Begin