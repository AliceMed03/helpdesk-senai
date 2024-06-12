package com.helpdesksenai.helpdesksenai.enums;

public enum PrioridadeEnum {
    BAIXA(0, "Baixa"),
    MEDIA(1, "Média"),
    ALTA(2, "Alta");
    private Integer codigo;
    private String descricao;

    PrioridadeEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    public static PrioridadeEnum toEnum(Integer cod){
        if(cod == null){
            return null;
        } for (PrioridadeEnum prioridade: PrioridadeEnum.values()) {
            if (cod.equals(prioridade.getCodigo())){
                return prioridade;
            }
        } throw new IllegalArgumentException("Prioridade inválida"); }
}
