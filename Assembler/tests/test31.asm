Test31   .ORIG
         .ORIG   x30b0   ; Multiple .ORIG instructions; should not compile.
Test     .EQU    x3
Begin    LD      R0, =x1
         .END    Begin