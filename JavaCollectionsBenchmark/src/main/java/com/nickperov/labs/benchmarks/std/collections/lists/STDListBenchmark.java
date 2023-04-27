package com.nickperov.labs.benchmarks.std.collections.lists;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public abstract class STDListBenchmark {

    public enum ListType {
        ArrayList, LinkedList, Vector, CopyOnWriteArrayList
    }



    List<Integer> list;

    void initializeList(final ListType listType, int preAllocatedSize) {
        list = getListSupplier(listType).apply(preAllocatedSize);
    }

    private Function<Integer, List<Integer>> getListSupplier(final ListType listType) {

        switch (listType) {
            case ArrayList:
                return ArrayList::new;
            case LinkedList:
                return (Integer size) -> new LinkedList<>();
            case Vector:
                return Vector::new;
            case CopyOnWriteArrayList:
                return (Integer size) -> new CopyOnWriteArrayList<>();
        }

        throw new RuntimeException("List type not found: " + listType);
    }

}
