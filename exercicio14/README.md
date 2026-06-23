# Exercício 14 - Deadlocks


## Objetivo

1. Executar o exemplo com deadlock.
2. Observar que as threads ficam bloqueadas.
3. Executar a versão corrigida.
4. Confirmar que a versão corrigida termina normalmente.

## Organização

```text
exercicio14-deadlocks/
├── README.md
├── src/
│   └── br/
│       └── ufg/
│           └── scd/
│               └── deadlock/
│                   ├── DeadlockExample.java
│                   ├── DeadlockFixed.java
│                   └── DeadlockCheck.java
└── out/                 # criada depois da compilação
```

## Arquivos

- `DeadlockExample.java`: versão com deadlock, equivalente ao exemplo da atividade.
- `DeadlockFixed.java`: versão corrigida. As duas threads adquirem os locks sempre na mesma ordem.
- `DeadlockCheck.java`: programa auxiliar de teste que executa um cenário com deadlock e um cenário corrigido usando tempo limite.

## Compilar

Na raiz do projeto:

```bash
mkdir -p out
javac -d out src/br/ufg/scd/deadlock/*.java
```

## Executar a versão com deadlock

```bash
java -cp out br.ufg.scd.deadlock.DeadlockExample
```

Resultado esperado: o programa imprime que cada thread está esperando o lock da outra e fica travado. Pressione `Ctrl + C` para encerrar.

## Executar a versão corrigida

```bash
java -cp out br.ufg.scd.deadlock.DeadlockFixed
```

Resultado esperado: as duas threads executam e o programa termina normalmente.

## Executar o teste auxiliar

```bash
java -cp out br.ufg.scd.deadlock.DeadlockCheck
```

Resultado esperado: o primeiro cenário detecta deadlock por timeout; o segundo cenário termina sem deadlock.
