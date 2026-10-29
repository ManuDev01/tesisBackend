package com.tesis.urbe.quizz.entity;

import com.tesis.urbe.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "fallo_usuario_quizz")
public class FalloUsuarioQuizzEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_falla_de_usuario_en_quizz")
    private Integer idFallaDeUsuarioEnQuizz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", nullable = false)
    private UserEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idquizz", nullable = false)
    private QuizzEntity quizz;

    public FalloUsuarioQuizzEntity() {
    }

    public FalloUsuarioQuizzEntity(Integer idFallaDeUsuarioEnQuizz, UserEntity usuario, QuizzEntity quizz) {
        this.idFallaDeUsuarioEnQuizz = idFallaDeUsuarioEnQuizz;
        this.usuario = usuario;
        this.quizz = quizz;
    }

    public Integer getIdFallaDeUsuarioEnQuizz() {
        return idFallaDeUsuarioEnQuizz;
    }

    public void setIdFallaDeUsuarioEnQuizz(Integer idFallaDeUsuarioEnQuizz) {
        this.idFallaDeUsuarioEnQuizz = idFallaDeUsuarioEnQuizz;
    }

    public UserEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UserEntity usuario) {
        this.usuario = usuario;
    }

    public QuizzEntity getQuizz() {
        return quizz;
    }

    public void setQuizz(QuizzEntity quizz) {
        this.quizz = quizz;
    }
}
