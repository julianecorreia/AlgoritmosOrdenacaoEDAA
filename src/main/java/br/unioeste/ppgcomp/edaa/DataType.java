package br.unioeste.ppgcomp.edaa;

public enum DataType {

    RAND("Aleatório", "aleatorios/a"),
    DESC("Decrescente", "decrescentes/d"),
    SORT("Ordenado", "ordenados/o"),
    HALF_SORT("Parcialmente Ordenado", "parcialmenteOrdenados/po");

    private final String name;
    private final String folder;

    DataType(String name, String folder) {
        this.name = name;
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public String getFolder() {
        return folder;
    }
}
