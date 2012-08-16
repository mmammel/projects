package org.mjm.util;

/**
 * User: mammelma
 * Date: Sep 8, 2010
 * Time: 10:00:56 PM
 * To change this template use File | Settings | File Templates.
 *
 * Example:  You have an array of 10 elements, and want to iterate over all N tuples made of
 *           N elements between index 4 (inclusive) and index 9 (exclusive).  Instantiate
 *           a NTupleIndexIterator with ( 4, 9, N ), where N <= 9 - 4 = 5.
 *
 *                           4         9
 *           array: [a,b,c,d,e,f,g,h,i,j]
 *
 *           N = 3
 *
 *           iterator will return, in order:
 *           [ 4 5 6 ]
 *           [ 4 5 7 ]
 *           [ 4 5 8 ]
 *           [ 4 6 7 ]
 *           [ 4 6 8 ]
 *           [ 4 7 8 ]
 *           [ 5 6 7 ]
 *           [ 5 6 8 ]
 *           [ 5 7 8 ]
 *           [ 6 7 8 ]
 *
 */
public class NTupleIndexIterator {
    private int startIdx = 0;
    private int endIdx = -1;
    private int numElements = -1;
    private int currPtr = -1;
    private int [] indices = null;

    public NTupleIndexIterator( int startIdx, int endIdx, int numElements )
    {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.numElements = numElements;

        if( this.numElements < 1 || this.startIdx < 0 ||
            this.endIdx <= this.startIdx || (this.endIdx - this.startIdx) < this.numElements )
        {
            throw new IllegalArgumentException( "Invalid arguments given: [" + this.startIdx + "," + this.endIdx + "," + this.numElements + "]");
        }
        else
        {
            this.indices = new int [ this.numElements + 1 ];
            int tempIndex = this.startIdx;
            for( int i = 0; i < this.indices.length; i++ )
            {
                this.indices[i] = tempIndex++;
            }

            this.indices[this.numElements] = this.endIdx;

            this.currPtr = this.indices.length - 2;
        }
    }


    public boolean hasNext()
    {
        return this.indices != null;
    }

    public int [] next()
    {
        if( this.indices == null ) throw new IllegalStateException("Nothing to return!");

        int [] retVal = new int [ this.numElements ];

        System.arraycopy(this.indices,0,retVal,0,this.indices.length - 1);

        this.advance();

        return retVal;
    }

    private void advance()
    {
        int tempIdx = this.numElements - 1;
        int tempVal = 0;

        while( this.indices[tempIdx] == (this.indices[tempIdx + 1] - 1))
        {
            if( tempIdx == 0 )
            {
                this.indices = null;
                break;
            }

            tempIdx--;
        }

        if( this.indices != null )
        {
            this.indices[tempIdx]++;
            tempVal = this.indices[tempIdx];
            for( int i = tempIdx + 1; i < this.numElements; i++ )
            {
                this.indices[i] = ++tempVal;
            }
        }
    }

    public static void main( String [] args )
    {
        NTupleIndexIterator iter = new NTupleIndexIterator(4,27,1);
        int [] temp = null;
        while( iter.hasNext() )
        {
            temp = iter.next();
            System.out.print("[");
            for( int i = 0; i < temp.length; i++ )
            {
                System.out.print( " " + temp[i] );
            }
            System.out.println( " ]" );
        }
    }
}
