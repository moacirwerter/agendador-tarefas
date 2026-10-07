package com.praticando.agendadortarefas.infrastructure.repository;

import com.praticando.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefasRepository  extends MongoRepository<TarefasEntity, String> {
}
