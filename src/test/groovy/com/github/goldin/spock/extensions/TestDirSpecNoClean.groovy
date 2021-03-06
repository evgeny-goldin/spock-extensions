package com.github.goldin.spock.extensions

import com.github.goldin.spock.extensions.testdir.TestDir
import spock.lang.Specification


/**
 * {@code @TestDir} extension test spec.
 */
class TestDirSpecNoClean extends Specification
{
    @SuppressWarnings( 'StatelessClass' )
    @TestDir( clean = false ) File testDir


    def 'test method()'() {

        expect:
        true
        testDir.directory
        ! testDir.listFiles()
    }


    def 'data-driven test method()'( int x, int y, int z ) {

        expect:
        x + y == z
        testDir.directory
        ! testDir.listFiles()
        ( x == 0 ) || ( testDir.path.matches( /^.+_\d+$/ ))

        where:
        x | y | z
        0 | 1 | 1
        1 | 1 | 2
        2 | 2 | 4
        3 | 6 | 9
        4 | 7 | 11
    }


    def 'data-driven test method using testDir'( int x, int y, int z ) {

        expect:
        x + y == z
        testDir.directory
        ! testDir.listFiles()
        ( x == 0 ) || ( testDir.path.matches( /^.+_\d+$/ ))

        new File( testDir, '1' ).mkdirs()
        new File( testDir, '1/2' ).mkdirs()
        new File( testDir, '1/2/3' ).mkdirs()
        new File( testDir, '1/2/3/4' ).mkdirs()
        new File( testDir, "1/2/3/$x-$y.$z" ).write( "$x-$y-$z" )

        where:
        x | y | z
        0 | 1 | 1
        1 | 1 | 2
        2 | 2 | 4
        3 | 6 | 9
        4 | 7 | 11
    }
}
