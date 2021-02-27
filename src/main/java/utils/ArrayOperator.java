package utils;

import java.util.Iterator;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 14:01
 */
public class ArrayOperator<T> {
    private T[] arrays;
    private int start;
    private int length;

    public ArrayOperator(T[] arrays, int start) {
        this(arrays, start, arrays.length - start);
    }

    public ArrayOperator(T[] arrays, int start, int length) {
        if (start >= arrays.length || length > arrays.length - start) {
            throw new IllegalArgumentException(String.format("start or end more than arrayLength:%d", arrays.length));
        }
        this.arrays = arrays;
        this.start = start;
        this.length = length;

    }

    public ArrayOperator<T> slice(int start) {
        return this.slice(start, length - start);
    }

    public ArrayOperator<T> slice(int start, int length) {
        return new ArrayOperator(arrays, this.start + start, length);
    }

    public T get(int index) {
        if (index >= length) {
            throw new IllegalArgumentException("index more than the array length:" + length);
        }
        return arrays[start + index];
    }

    public Iterator<ArrayOperator<T>> iterable(int step) {
        return new InnerIterator(step);
    }

    public int length() {
        return length;
    }

    private class InnerIterator implements Iterator<ArrayOperator<T>> {
        private int index = 1;

        private int step;

        public InnerIterator(int step) {
            this.step = step;
        }

        @Override
        public boolean hasNext() {
            return length >= index * step;
        }

        @Override
        public ArrayOperator<T> next() {
            return slice((index++ - 1) * step, step);
        }
    }

}
