package com.projetoJAVA.Application.ChefeDTO;

public class Chefe_SubordinadoDTO {

        private Integer IdChefe;
        private Integer IdSubordinado;

    private Chefe_SubordinadoDTO() {
    }

    public Integer getIdChefe() {
                return IdChefe;
        }
        public void setIdChefe(Integer idChefe) {
                this.IdChefe = idChefe;
        }
        public Integer getIdSubordinado() {
        return IdSubordinado;
        }
        public void setIdSubordinado(Integer idSubordinado) {
        this.IdSubordinado = idSubordinado;
        }
}
