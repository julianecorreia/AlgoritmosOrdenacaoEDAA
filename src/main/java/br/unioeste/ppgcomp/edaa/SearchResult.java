package br.unioeste.ppgcomp.edaa;

public class SearchResult {
    private int index;
    private int comparisons;

    public SearchResult(int index, int comparisons) {
        this.index = index;
        this.comparisons = comparisons;
    }

    public int getIndex() {
        return index;
    }

    public int getComparisons() {
        return comparisons;
    }
}