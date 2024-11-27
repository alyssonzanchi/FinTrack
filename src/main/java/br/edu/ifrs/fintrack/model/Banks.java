package br.edu.ifrs.fintrack.model;

public enum Banks {
    BANCO_DO_BRASIL("Banco do Brasil", "/images/bb.png"),
    CAIXA_ECONOMICA("Caixa Econômica", "/images/caixa.png"),
    ITAU("Itaú", "/images/itau.png"),
    SANTANDER("Santander", "/images/santander.png"),
    SICREDI("Sicredi", "/images/sicredi.png"),
    NUBANK("Nubank", "/images/nubank.png"),
    BANRISUL("Banrisul", "/images/banrisul.png");

    private final String name;
    private final String path;

    Banks(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public static Banks fromName(String name) {
        for (Banks bank : Banks.values()) {
            if (bank.getName().equalsIgnoreCase(name)) {
                return bank;
            }
        }
        throw new IllegalArgumentException("Nenhuma constante com esse nome: " + name);
    }
}
