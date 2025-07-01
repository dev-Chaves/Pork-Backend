# 💰 Gerenciador Financeiro - MVP

## 📋 Regras de Negócio (Business Rules)

Este documento contém todas as regras de negócio do sistema de gerenciamento financeiro. Use os checkboxes para acompanhar o progresso de implementação.

---

## 📊 **Dados Financeiros**

- [ ] **RN001**: Receita mensal deve ser > 0
- [ ] **RN002**: Gastos fixos não podem exceder 100% da receita  
- [ ] **RN003**: Valores monetários sempre com 2 casas decimais
- [ ] **RN004**: Não permitir valores negativos em receitas/gastos

---

## 💰 **Cálculos Automáticos**

- [ ] **RN005**: Sobra = Receita - Soma(Gastos Fixos Ativos)
- [ ] **RN006**: Recalcular sobra sempre que gastos forem alterados
- [ ] **RN007**: Alertar se gastos > 80% da receita (zona de risco)
- [ ] **RN008**: Sugerir investimento apenas se sobra > R$ 100

---

## 🎯 **Investimentos**

- [ ] **RN009**: Meta de investimento ≤ valor disponível para investir
- [ ] **RN010**: Contribuição mensal ≤ sobra mensal
- [ ] **RN011**: Data da meta deve ser futura
- [ ] **RN012**: Usuário só pode ter 1 perfil financeiro ativo

---

## 🔒 **Validações de Integridade**

- [ ] **RN013**: Não permitir exclusão de usuário com dados financeiros
- [ ] **RN014**: Gastos inativos não entram no cálculo da sobra
- [ ] **RN015**: Histórico de alterações deve ser mantido (audit trail)

---

## 🚨 **Alertas e Limites**

- [ ] **RN016**: Alertar se gastos fixos > 70% da receita
- [ ] **RN017**: Bloquear cadastro se gastos > receita
- [ ] **RN018**: Sugerir revisão se sobra < 10% da receita
- [ ] **RN019**: Limite máximo de 20 gastos fixos por usuário

---

## 📱 **Experiência do Usuário**

- [ ] **RN020**: Mostrar impacto em tempo real ao adicionar gastos
- [ ] **RN021**: Sugestões automáticas baseadas no perfil
- [ ] **RN022**: Onboarding obrigatório para novos usuários
- [ ] **RN023**: Confirmação dupla para exclusões importantes

---

## 📈 **Relatórios e Insights**

- [ ] **RN024**: Gerar insights apenas com dados completos
- [ ] **RN025**: Comparar com médias do mercado (se disponível)
- [ ] **RN026**: Histórico mínimo de 3 meses para tendências

---

## 🎯 **Priorização para MVP**

### 🔴 **Críticas** (Implementar primeiro)
- [ ] RN001 - Receita > 0
- [ ] RN002 - Gastos ≤ Receita
- [ ] RN005 - Cálculo da sobra
- [ ] RN006 - Recálculo automático
- [ ] RN017 - Bloqueio por gastos excessivos

### 🟡 **Importantes** (Segunda iteração)
- [ ] RN007 - Alerta zona de risco (80%)
- [ ] RN008 - Sugestão de investimento
- [ ] RN016 - Alerta gastos altos (70%)
- [ ] RN018 - Sugestão de revisão

### 🟢 **Desejáveis** (Futuras versões)
- [ ] RN015 - Audit trail
- [ ] RN024 - Insights com dados completos
- [ ] RN025 - Comparação com mercado
- [ ] RN026 - Análise de tendências

---

## 📝 **Como Usar Este Documento**

1. **Durante Development**: Marque as regras implementadas
2. **Code Review**: Verifique se as regras estão sendo respeitadas
3. **Testing**: Use como checklist para casos de teste
4. **Documentation**: Mantenha atualizado conforme evolução do produto

---

## 🤝 **Contribuição**

Ao implementar uma regra de negócio:
1. Marque o checkbox correspondente
2. Adicione testes unitários
3. Documente no código com o número da RN
4. Atualize este README se necessário

---

**Status do Projeto**: 🚧 Em Desenvolvimento

**Última Atualização**: $(date)
