package semana8.p007.exercicio3.repositories;

import semana8.p007.exercicio3.entities.PontosDeParada;

public interface PontoDeParadaRepository {
  
  public void adicionar(PontosDeParada pontoDeParada);

  public void cadastrarPontoDeParada();

  public void listarPontosDeParada();

  public String trajetoEmbarque();

  public String trajetoDesembarque();

  public void carregarArquivo(String nomeArquivo);

  public void salvarArquivo(String nomeArquivo);
}
