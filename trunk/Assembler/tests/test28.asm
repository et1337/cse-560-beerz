Test28   .ORIG
Test     .EQU    x3
Begin    LD      R0, =xFFFFFFFF ; Should create a literal with value -1
         .END    Begin