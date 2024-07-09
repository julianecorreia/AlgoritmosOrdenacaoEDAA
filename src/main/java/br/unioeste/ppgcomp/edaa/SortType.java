package br.unioeste.ppgcomp.edaa;

public enum SortType {

    QUICK("QuickSort"),
    MERGE("MergeSort"),
    HEAP("HeapSort");

    private final String name;

    SortType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
