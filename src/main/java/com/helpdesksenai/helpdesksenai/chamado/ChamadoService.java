package com.helpdesksenai.helpdesksenai.chamado;

import com.helpdesksenai.helpdesksenai.cliente.Cliente;
import com.helpdesksenai.helpdesksenai.cliente.ClienteDTO;
import com.helpdesksenai.helpdesksenai.cliente.ClienteService;
import com.helpdesksenai.helpdesksenai.enums.PrioridadeEnum;
import com.helpdesksenai.helpdesksenai.enums.StatusEnum;
import com.helpdesksenai.helpdesksenai.tecnico.Tecnico;
import com.helpdesksenai.helpdesksenai.tecnico.TecnicoService;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {
    @Autowired
    ChamadoRepository repository;
    @Autowired
    TecnicoService tecnicoService;
    @Autowired
    ClienteService clienteService;

    public Chamado findById(Integer id) {
        Optional<Chamado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id, obj));
    };
    public List<Chamado> findAll() {
        return repository.findAll();
    }
    public Chamado create(ChamadoDTO objDTO) {
        objDTO.setId(null);
        Chamado newObj = new Chamado(objDTO);
        return repository.save(newObj);
    }
    public Chamado update(Integer id, @Valid ChamadoDTO chamadoDTO) {
        chamadoDTO.setId(id);
        Chamado chamado = findById(id);
        chamado = novoChamado(chamadoDTO);
        return repository.save(chamado);
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
        chamado.setPrioridadeEnum(PrioridadeEnum.toEnum(chamadoDTO.getPrioridade()));
        chamado.setStatusEnum(StatusEnum.toEnum(chamadoDTO.getStatus()));
        chamado.setTitulo(chamadoDTO.getTitulo());
        chamado.setObservacoes(chamadoDTO.getObservacoes());
        return chamado;
    }

}
