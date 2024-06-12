package com.helpdesksenai.helpdesksenai.chamado;

import com.helpdesksenai.helpdesksenai.cliente.Cliente;
import com.helpdesksenai.helpdesksenai.cliente.ClienteService;
import com.helpdesksenai.helpdesksenai.enums.PrioridadeEnum;
import com.helpdesksenai.helpdesksenai.enums.StatusEnum;
import com.helpdesksenai.helpdesksenai.exceptions.ObjectNotFoundException;
import com.helpdesksenai.helpdesksenai.tecnico.Tecnico;
import com.helpdesksenai.helpdesksenai.tecnico.TecnicoDTO;
import com.helpdesksenai.helpdesksenai.tecnico.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {
    private final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado ";
    private final String POSSUI_CHAMADO_EM_ABERTO = "A entidade possui chamado em aberto e não pode ser excluído ";
    @Autowired
    private ChamadoRepository repository;
    @Autowired
    private TecnicoService tecnicoService;
    @Autowired
    private ClienteService clienteService;


    public Chamado create(ChamadoDTO objDTO) {
        objDTO.setId(null);
        return repository.save(novoChamado(objDTO));
    }

    public List<Chamado> findAll() {
        return repository.findAll();
    }

    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO + id));
    };

    public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
        objDTO.setId(id);
        Chamado oldObj = findById(id);
        oldObj = novoChamado(objDTO);
        return repository.save(oldObj);
    }

    private Chamado novoChamado(ChamadoDTO chamadoDTO){
        //Verifica se existe técnico e cliente na base
        Tecnico tecnico = tecnicoService.findById(chamadoDTO.getTecnico());
        Cliente cliente = clienteService.findById(chamadoDTO.getCliente());
        //Cria um novo chamado para popular
        Chamado chamado = new Chamado();
        //Verifica se o id é diferente de nulo, aqui define se vai criar ou atualizar
        if(chamadoDTO.getId() != null) {
            //se não for nulo se trata de uma atualização e seta o id
            chamado.setId(chamadoDTO.getId());
        }
        //Aqui (STATUS2) verifica se a atualização é para fechar o serviço
        if(chamadoDTO.getStatus().equals(2)) {
            chamado.setDataFechamento(LocalDate.now());
        }
        //Abaixo popula os demais atributos
        chamado.setTecnico(tecnico);
        chamado.setCliente(cliente);
        chamado.setPrioridadeEnum(PrioridadeEnum.toEnum(chamadoDTO.getPrioridade().getCodigo()));
        chamado.setStatusEnum(StatusEnum.toEnum(chamadoDTO.getStatus().getCodigo()));
        chamado.setTitulo(chamadoDTO.getTitulo());
        chamado.setObservacoes(chamadoDTO.getObservacoes());
        return chamado;
    }

}
