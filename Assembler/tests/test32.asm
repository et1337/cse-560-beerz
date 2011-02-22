Test32   .ORIG
Test     .EQU    x3
Begin    LD      R0, =x1
         .END    Begin
         .END    Test    ; Multiple .END instructions; should not compile.