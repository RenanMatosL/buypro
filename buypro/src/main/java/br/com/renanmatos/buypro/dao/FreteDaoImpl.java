package br.com.renanmatos.buypro.dao;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

//Implementação do repositório  
@Repository  
public class FreteDaoImpl implements FreteDao {  
 
 @PersistenceContext  
 private EntityManager entityManager;  
 
 @Override  
 public BigDecimal calcularFrete(BigDecimal peso) {  
     // Lógica para calcular o frete  
     Query query = entityManager.createQuery("SELECT f.valor FROM Frete f WHERE f.peso = :peso");  
     query.setParameter("peso", peso);  
     return (BigDecimal) query.getSingleResult();  
 }  
}  