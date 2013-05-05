/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author David
 */
public class SortedArrayList<T> extends ArrayList<T> {

    @SuppressWarnings("unchecked")
    public void insertSorted(T value) {
        add(value);
        Comparable<T> cmp = (Comparable<T>) value;
        for (int i = size() - 1; i > 0 && cmp.compareTo(get(i - 1)) < 0; i--) {
            Collections.swap(this, i, i - 1);
        }
    }
}
