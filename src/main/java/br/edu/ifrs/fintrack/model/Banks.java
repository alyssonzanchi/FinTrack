package br.edu.ifrs.fintrack.model;

public enum Banks {
    AGZERO("AgZero", "/images/banks/agzero.png"),
    AGIBANK("Agibank", "/images/banks/agibank.png"),
    AILOS("Ailos", "/images/banks/ailos.png"),
    ALELO("Alelo", "/images/banks/alelo.png"),
    AME("Ame Digital", "/images/banks/ame.png"),
    BMG("BMG", "/images/banks/bmg.png"),
    BTG("BTG Pactual", "/images/banks/btg.png"),
    BARI("Banco Bari", "/images/banks/bari.png"),
    INTER("Banco Inter", "/images/banks/inter.png"),
    PAN("Banco Pan", "/images/banks/pan.png"),
    SAFRA("Banco Safra", "/images/banks/safra.png"),
    BRB("Banco de Brasília", "/images/banks/brb.png"),
    BANCO_DO_BRASIL("Banco do Brasil", "/images/banks/bb.png"),
    BANESTES("Banestes", "/images/banks/banestes.png"),
    BANPARA("Banpará", "/images/banks/banpara.png"),
    BANRISUL("Banrisul", "/images/banks/banrisul.png"),
    BINANCE("Binance", "/images/banks/binance.png"),
    BRADESCO("Bradesco", "/images/banks/bradesco.png"),
    C6("C6 Bank", "/images/banks/c6.png"),
    CAIXA_ECONOMICA("Caixa Econômica", "/images/banks/caixa.png"),
    DIGIO("Digio", "/images/banks/digio.png"),
    HSBC("HSBC", "/images/banks/hsbc.png"),
    ITAU("Itaú", "/images/banks/itau.png"),
    MERCADO_PAGO("Mercado Pago", "/images/banks/mercado_pago.png"),
    MODAL("Modal Mais", "/images/banks/modal.png"),
    MELIUZ("Méliuz", "/images/banks/meliuz.png"),
    NEON("Neon", "/images/banks/neon.png"),
    NETELLER("Neteller", "/images/banks/neteller.png"),
    NEXT("Next", "/images/banks/next.png"),
    NUBANK("Nubank", "/images/banks/nubank.png"),
    PAYPAL("PayPal", "/images/banks/paypal.png"),
    PICPAY("PicPay", "/images/banks/picpay.png"),
    RICO("Rico", "/images/banks/rico.png"),
    SANTANDER("Santander", "/images/banks/santander.png"),
    SICREDI("Sicredi", "/images/banks/sicredi.png"),
    STONE("Stone", "/images/banks/stone.png"),
    XP("XP", "/images/banks/xp.png");

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
