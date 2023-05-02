package com.example.livro;

public class Livro {

    String titulo;
    String autor;
    String editora;
    String anoedicao;
    String localizacao;

    public Livro(String id, String tituloLivro, String autorLivro, String editoraLivro, String anoedicaoLivro, String localizacaoLivro) {
    }

    public Livro(String titulo, String autor, String editora, String anoedicao, String localizacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.anoedicao = anoedicao;
        this.localizacao = localizacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getAnoedicao() {
        return anoedicao;
    }

    public void setAnoedicao(String anoedicao) {
        this.anoedicao = anoedicao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalização(String localização) {
        this.localizacao = localizacao;
    }
}
