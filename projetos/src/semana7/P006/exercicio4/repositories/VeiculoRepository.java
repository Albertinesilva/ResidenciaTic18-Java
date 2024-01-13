package semana7.P006.exercicio4.repositories;

import java.util.List;

import semana6.atvEmSala.P005.entities.Veiculo;

public interface VeiculoRepository {
  
  public void adicionar(Veiculo veiculo);

  public List<Veiculo> getVeiculos();

  public void cadastrarVeiculo();

  public void listarVeiculos();
}